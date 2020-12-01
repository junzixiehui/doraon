package com.junzixiehui.doraon.design.pattern.filter;

/**
 *
 */
public interface Filter<T> {

	void doFilter(T context, FilterInvoker nextFilter);

}