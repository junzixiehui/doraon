package com.junzixiehui.doraon.business.filter;

/**
 */
public interface FilterInvoker<T> {

    default public void invoke(T context){};
}
