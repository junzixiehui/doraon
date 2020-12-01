package com.junzixiehui.doraon.design.pattern.filter;

/**
 */
public interface FilterInvoker<T> {

    default public void invoke(T context){};
}
