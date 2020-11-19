package com.junzixiehui.doraon.util.exception;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2020/5/3  7:39 PM
 * @version: 1.0
 */
public class HttpCallException extends RuntimeException {
	public HttpCallException(String message) {
		super(message);
	}

	public HttpCallException(String message, Throwable cause) {
		super(message, cause);
	}
}

