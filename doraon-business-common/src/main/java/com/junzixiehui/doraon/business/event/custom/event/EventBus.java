package com.junzixiehui.doraon.business.event.custom.event;

import com.junzixiehui.doraon.business.event.Event;
import com.junzixiehui.doraon.business.event.custom.exception.InfraException;
import com.junzixiehui.doraon.util.api.ErrorCode;
import com.junzixiehui.doraon.util.api.Resp;
import com.junzixiehui.doraon.util.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Event Bus
 *
 * @author shawnzhan.zxy
 * @date 2017/11/20
 */
@Component
public class EventBus implements EventBusI {

	private static final Logger logger = LoggerFactory.getLogger(EventBus.class);
	@Autowired
	private EventHub eventHub;
	/**
	 * 默认线程池
	 *     如果处理器无定制线程池，则使用此默认
	 */
	ExecutorService defaultExecutor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors() + 1,
			Runtime.getRuntime().availableProcessors() + 1, 0L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>(1000));

	@SuppressWarnings("unchecked")
	@Override
	public Resp fire(Event event) {
		Resp response = null;
		try {
			final List<EventHandlerI> eventHandler = eventHub.getEventHandler(event.getClass());
			final EventHandlerI eventHandlerI = eventHandler.get(0);
			response = eventHandlerI.execute(event);
		} catch (Exception exception) {
			response = handleException(event, exception);
		}
		return response;
	}

	@Override
	public Resp fireAll(Event event) {
		final List<EventHandlerI> eventHandlerList = eventHub.getEventHandler(event.getClass());
		final List<Resp> respList = eventHandlerList.stream().map(p -> {
			Resp response = null;
			try {
				response = p.execute(event);
			} catch (Exception exception) {
				response = handleException(p, response, exception);
			}
			return response;
		}).collect(Collectors.toList());
		return respList.get(0);
	}

	@Override
	public Resp asyncFire(Event event) {
		final List<EventHandlerI> eventHandlerList = eventHub.getEventHandler(event.getClass());
		eventHandlerList.parallelStream().map(p -> {
			Resp response = null;
			try {
				defaultExecutor.submit(() -> p.execute(event));
			} catch (Exception exception) {
				response = handleException(p, response, exception);
			}
			return response;
		}).collect(Collectors.toList());
		return Resp.success();
	}

	private Resp handleException(Event cmd, Exception exception) {
		Class responseClz = eventHub.getResponseRepository().get(cmd.getClass());
		Resp response;
		try {
			response = (Resp) responseClz.newInstance();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new InfraException(e.getMessage());
		}
		logger.error(exception.getMessage(), exception);
		return response;
	}

	private Resp handleException(EventHandlerI handler, Resp response, Exception exception) {
		logger.error(exception.getMessage(), exception);
		Class responseClz = eventHub.getResponseRepository().get(handler.getClass());
		try {
			response = (Resp) responseClz.newInstance();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		if (exception instanceof ServiceException) {
			String errCode = ((ServiceException) exception).getCode();
			response.setCode(errCode);
		} else {
			response.setCode(ErrorCode.SERVER.getCode());
		}
		response.setMessage(exception.getMessage());
		return response;
	}
}
