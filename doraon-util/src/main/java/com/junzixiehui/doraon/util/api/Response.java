package com.junzixiehui.doraon.util.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

@Setter
@Getter
@ToString
public class Response {

	private static final long serialVersionUID = 1L;
	public static final int STATUS_SUCCESS = 0;
	public static final String MESSAGE_SUCCESS = "success";
	private int code;
	private String message;

	public Response() {
	}

	public Response(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public boolean isSuccess() {
		return STATUS_SUCCESS == this.code;
	}

	public static boolean isSuccess(Map<String,Object> map) {
		final Integer code = MapUtils.getInteger(map, "code", -1);
		return STATUS_SUCCESS == code;
	}

	public boolean isNotSuccess() {
		return STATUS_SUCCESS != (this.code);
	}
}
