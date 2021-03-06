package com.junzixiehui.doraon.util.api;

import com.junzixiehui.doraon.util.exception.ServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
@NoArgsConstructor
public class Resp<T> implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String CODE_SUCCESS = "0";
	public static final String MESSAGE_SUCCESS = "success";
	@ApiModelProperty("状态码,0成功非0失败")
	private String code;
	@ApiModelProperty("状态码描述,0success非0失败原因")
	private String message;
	@ApiModelProperty("返回数据")
	private T data;

	private Resp(String code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static Resp success() {
		return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, (Object)null);
	}

	public static <T> Resp success(T data) {
		return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, data);
	}

	public static <T> Resp success(T data, String message) {
		return new Resp(CODE_SUCCESS, message, data);
	}

	public static Resp error(Throwable e) {

		if (e instanceof ServiceException) {
			ServiceException se = (ServiceException)e;
			String code = StringUtils.isBlank(se.getCode()) ? ErrorCode.OPEN_FAIL.getCode() : se.getCode();
			String msg = StringUtils.isBlank(se.getMessage()) ? ErrorCode.OPEN_FAIL.getMessage() : se.getMessage();
			return new Resp(code, msg, (Object)null);
		} else {
			return new Resp(ErrorCode.SERVER.getCode(), String.format(ErrorCode.SERVER.getMessage(), e.getMessage()), (Object)null);
		}
	}

	public static Resp error(ErrorCode errorCode, String appendMessage) {
		String message = String.format(errorCode.getMessage(), appendMessage == null ? "" : appendMessage);
		return new Resp(errorCode.getCode(), message, (Object)null);
	}

	public static Resp error(ErrorCode errorCode) {
		return new Resp(errorCode.getCode(), errorCode.getMessage(), (Object)null);
	}

	public static Resp error(String code, String message) {
		return new Resp(code, message, (Object)null);
	}

	public static Resp error(String message) {
		return new Resp(ErrorCode.OPEN_FAIL.getCode(), message, (Object)null);
	}


	public boolean isSuccess() {
		return CODE_SUCCESS.equals(code);
	}

	/**
	 * 判断返回是否为成功
	 *
	 * @return 是否成功
	 */
	public boolean isNotSuccess() {
		return !isSuccess();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Resp{");
		sb.append("code='").append(this.code).append('\'');
		sb.append(", message='").append(this.message).append('\'');
		sb.append(", data=").append(this.data);
		sb.append('}');
		return sb.toString();
	}

	public String toSimpleString() {
		StringBuilder sb = new StringBuilder("Resp{");
		sb.append("code='").append(this.code).append('\'');
		sb.append(", message='").append(this.message).append('\'');
		sb.append('}');
		return sb.toString();
	}

	public String getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}

	public T getData() {
		return this.data;
	}
}
