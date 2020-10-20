package com.junzixiehui.doraon.business.util;

import com.junzixiehui.doraon.util.exception.ServiceException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: qulibin
 * @description:
 * @date: 下午7:40 2018/9/3
 * @modify：
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext; // Spring应用上下文环境

	/**
	 * 实现ApplicationContextAware接口的回调方法，设置上下文环境.
	 *
	 */
	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}

	public static void setApplicationContextV2(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	/**
	 * 获取对象.
	 *
	 */
	public static Object getBean(final String name) throws BeansException {
		return applicationContext.getBean(name);
	}

	/**
	 * 获取类型为requiredType的对象 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）.
	 *
	 * @param name bean注册名
	 * @param requiredType 返回对象类型
	 * @return Object 返回requiredType类型对象
	 */
	public static <T> T getBean(final String name, final Class<T> requiredType) throws BeansException {
		return applicationContext.getBean(name, requiredType);
	}

	/**
	 * 获取对象.
	 *
	 * @return Object 一个以所给名字注册的bean的实例
	 */
	public static <T> T getBean(final Class<T> clz) throws BeansException {
		if (applicationContext == null) {
			throw new ServiceException("SpringContextUtil applicationContext is null");
		}
		final T bean = applicationContext.getBean(clz);
		if (bean == null) {
			throw new ServiceException("bean is null,please register it");
		}
		return bean;
	}
}
