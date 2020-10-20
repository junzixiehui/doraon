package com.junzixiehui.doraon.business.filter;

/**
 * 拦截器链
 */
public class FilterChain<T>{

    private FilterInvoker header;

    public void doFilter(T context){
        header.invoke(context);
    }

    public void setHeader(FilterInvoker header) {
        this.header = header;
    }
}
