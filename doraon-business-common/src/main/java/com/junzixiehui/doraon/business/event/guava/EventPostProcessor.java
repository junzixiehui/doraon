package com.junzixiehui.doraon.business.event.guava;

import com.google.common.eventbus.Subscribe;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2019/5/16  14:44
 * @version: 1.0
 */
@Component
public class EventPostProcessor implements BeanPostProcessor {

	@Autowired
	private EventBusFacade eventBusFacade;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		final Field[] fields = bean.getClass().getFields();

		final Method[] methods = bean.getClass().getMethods();
		if (methods == null || methods.length == 0) {
			return bean;
		}
		for (Method method : methods) {
			final Subscribe annotation = method.getAnnotation(Subscribe.class);
			if (annotation != null) {
				eventBusFacade.register(bean);
			}
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
