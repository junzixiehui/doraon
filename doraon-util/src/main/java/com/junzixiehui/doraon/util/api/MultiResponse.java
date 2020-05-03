package com.junzixiehui.doraon.util.api;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collection;

@Setter
@Getter
@ToString
public class MultiResponse<T> extends Response {

	private int total;
	private Collection<T> data;

	public MultiResponse() {

	}

	public static MultiResponse success(int code, String err_msg, Collection collection) {

		MultiResponse multiResponse = new MultiResponse();
		multiResponse.setCode(code);
		multiResponse.setMessage(err_msg);
		multiResponse.setData(collection);
		multiResponse.setTotal(collection != null ? collection.size() : 0);
		return multiResponse;
	}

	public static MultiResponse error(int code, String err_msg) {

		MultiResponse multiResponse = new MultiResponse();
		multiResponse.setCode(code);
		multiResponse.setMessage(err_msg);
		return multiResponse;
	}

	public static MultiResponse success(String err_msg) {
		return success(0, err_msg, null);
	}

	public static MultiResponse success(Collection collection) {
		return success(0, "success", collection);
	}

	public static MultiResponse success() {
		return success(0, "success", null);
	}
}
