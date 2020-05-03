package com.junzixiehui.doraon.util.servlet;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2018/9/10  下午2:52
 * @version: 1.0
 */
public class ServletUtils {

	public static HttpServletRequest getRequest() {
		return getAttributes().getRequest();
	}

	public static HttpServletResponse getResponse() {
		return getAttributes().getResponse();
	}

	public static ServletRequestAttributes getAttributes() {
		return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	}
}
