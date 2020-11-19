package com.junzixiehui.doraon.business.curator;

import com.google.common.base.Charsets;
import com.junzixiehui.doraon.util.exception.ServiceException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>Description: zk访问客户端</p>
 * @author: by jxll
 * @date: 2019/4/12  10:13
 * @version: 1.0
 */
@Slf4j
public class ZkCuratorClient {

	@Setter
	private String zkHost;
	@Setter
	private int retryTimes = 3;
	@Setter
	private int sleepMsBetweenRetries = 3000;
	@Setter
	private int connectionTimeoutMs = 6000;
	@Setter
	private int sessionTimeoutMs = 60000;
	private final Map<String, TreeCache> caches = new HashMap<>();

	CuratorFramework client;
	public CuratorFramework init() {
		client = CuratorFrameworkFactory.builder().connectString(zkHost)
				.retryPolicy(new RetryNTimes(retryTimes, sleepMsBetweenRetries))
				.connectionTimeoutMs(connectionTimeoutMs).sessionTimeoutMs(sessionTimeoutMs).build();
		client.start();
		log.info("CuratorClient start");
		try {
			if (!client.blockUntilConnected(sleepMsBetweenRetries * retryTimes, TimeUnit.MILLISECONDS)) {
				client.close();
				throw new KeeperException.OperationTimeoutException();
			}
		} catch (final Exception ex) {
			log.error("CuratorClient register error", ex);
			throw new ServiceException("CuratorClient register error");
		}
		return client;
	}

	public void persist(final String key, final String value) {
		try {
			if (!isExisted(key)) {
				client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
						.forPath(key, value.getBytes(Charsets.UTF_8));
			} else {
				update(key, value);
			}
		} catch (final Exception ex) {
			ZkExceptionHandler.handleException(ex);
		}
	}

	public boolean isExisted(final String key) {
		try {
			return null != client.checkExists().forPath(key);
		} catch (final Exception ex) {
			ZkExceptionHandler.handleException(ex);
			return false;
		}
	}

	public void update(final String key, final String value) {
		try {
			client.inTransaction().check().forPath(key).and().setData().forPath(key, value.getBytes(Charsets.UTF_8))
					.and().commit();
		} catch (final Exception ex) {
			ZkExceptionHandler.handleException(ex);
		}
	}

	public String get(final String key) {
		TreeCache cache = findTreeCache(key);
		if (null == cache) {
			return getDirectly(key);
		}
		ChildData resultInCache = cache.getCurrentData(key);
		if (null != resultInCache) {
			return null == resultInCache.getData() ? null : new String(resultInCache.getData(), Charsets.UTF_8);
		}
		return getDirectly(key);
	}

	/**
	 * @author: jxll
	 * @description: 直接从zk获取data
	 * @date: 11:12 2019/4/12
	 * @return:
	 */
	public String getDirectly(final String key) {
		try {
			return new String(client.getData().forPath(key), Charsets.UTF_8);
		} catch (final Exception ex) {
			ZkExceptionHandler.handleException(ex);
			return null;
		}
	}

	/**
	 * @author: jxll
	 * @description: 获取子节点列表
	 * @date: 11:14 2019/4/12
	 * @return:
	 */
	public List<String> getChildrenKeys(final String key) {
		try {
			List<String> result = client.getChildren().forPath(key);
			Collections.sort(result, new Comparator<String>() {
				@Override
				public int compare(final String o1, final String o2) {
					return o2.compareTo(o1);
				}
			});
			return result;
		} catch (final Exception ex) {
			ZkExceptionHandler.handleException(ex);
			return Collections.emptyList();
		}
	}

	/**
	 * @author: jxll
	 * @description: 获取子节点数量
	 * @date: 11:13 2019/4/12
	 * @return:
	 */
	public int getNumChildren(final String key) {
		try {
			Stat stat = client.checkExists().forPath(key);
			if (null != stat) {
				return stat.getNumChildren();
			}
		} catch (final Exception ex) {
			ZkExceptionHandler.handleException(ex);
		}
		return 0;
	}

	public void remove(final String key) {
		try {
			client.delete().deletingChildrenIfNeeded().forPath(key);
		} catch (final Exception ex) {
			ZkExceptionHandler.handleException(ex);
		}
	}

	private TreeCache findTreeCache(final String key) {
		for (Map.Entry<String, TreeCache> entry : caches.entrySet()) {
			if (key.startsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	public void close() {
		waitForCacheClose();
		CloseableUtils.closeQuietly(client);
	}

	/* TODO 等待500ms, cache先关闭再关闭client, 否则会抛异常
	 * 因为异步处理, 可能会导致client先关闭而cache还未关闭结束.
	 * 等待Curator新版本解决这个bug.
	 * BUG地址：https://issues.apache.org/jira/browse/CURATOR-157
	 */
	private void waitForCacheClose() {
		try {
			Thread.sleep(500L);
		} catch (final InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
