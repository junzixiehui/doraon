package com.junzixiehui.doraon.business.filter;

/**
 *
 */
public interface Filter<T> {

	void doFilter(T context, FilterInvoker nextFilter);

}