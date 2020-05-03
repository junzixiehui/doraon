package com.junzixiehui.doraon.business.curator;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/4/12  11:03
 * @version: 1.0
 */
@Slf4j
public class ZkExceptionHandler {

	/**
	 * 处理异常.
	 *
	 * <p>处理掉中断和连接失效异常并继续抛注册中心.</p>
	 *
	 * @param cause 待处理异常.
	 */
	public static void handleException(final Exception cause) {
		if (null == cause) {
			return;
		}
		log.error("ZkExceptionHandler handleException", cause);
		if (isIgnoredException(cause) || null != cause.getCause() && isIgnoredException(cause.getCause())) {
			log.debug("configCenter: ignored exception for: {}", cause.getMessage());
		} else if (cause instanceof InterruptedException) {
			Thread.currentThread().interrupt();
		} else {
			throw new RuntimeException(cause);
		}
	}

	private static boolean isIgnoredException(final Throwable cause) {
		return cause instanceof KeeperException.ConnectionLossException
				|| cause instanceof KeeperException.NoNodeException
				|| cause instanceof KeeperException.NodeExistsException;
	}
}
