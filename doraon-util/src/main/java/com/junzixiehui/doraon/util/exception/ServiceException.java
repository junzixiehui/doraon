package com.junzixiehui.doraon.util.exception;

import com.junzixiehui.doraon.util.api.ErrorCode;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2020/5/3  7:30 PM
 * @version: 1.0
 */
public class ServiceException extends RuntimeException {

	private String code;

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}

	public ServiceException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMessage(), cause);
		this.code = errorCode.getCode();
	}

	public ServiceException(ErrorCode errorCode, String appendMessage) {
		super(String.format(errorCode.getMessage(), appendMessage == null ? "" : appendMessage));
		this.code = errorCode.getCode();
	}

	public ServiceException(ErrorCode errorCode, String appendMessage, Throwable cause) {
		super(String.format(errorCode.getMessage(), appendMessage == null ? "" : appendMessage), cause);
		this.code = errorCode.getCode();
	}

	public String getCode() {
		return this.code;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
		sb.append("{");
		sb.append("code=").append(this.getCode());
		sb.append(",");
		sb.append("message=").append(this.getLocalizedMessage());
		sb.append('}');
		return sb.toString();
	}
}