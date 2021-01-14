package com.junzixiehui.doraon.design.pattern.filter;

import com.junzixiehui.doraon.business.util.SpringContextUtil;

/**
 * 责任链模式工厂
 */
public class FilterChainFactory {

	public static <T> FilterChain<T> buildFilterChain(Class... filterClsList) {
		FilterInvoker last = new FilterInvoker() {};
		FilterChain filterChain = new FilterChain();
		for (int i = filterClsList.length - 1; i >= 0; i--) {
			FilterInvoker next = last;
			Filter filter = (Filter) SpringContextUtil.getBean(filterClsList[i]);
			last = new FilterInvoker<T>() {
				@Override
				public void invoke(T context) {
					filter.doFilter(context, next);
				}
			};
		}
		filterChain.setHeader(last);
		return filterChain;
	}
}
