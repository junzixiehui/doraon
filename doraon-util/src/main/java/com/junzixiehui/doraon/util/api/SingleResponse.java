package com.junzixiehui.doraon.util.api;


public class SingleResponse<T> extends Response {

	private T data;

	public static <T> SingleResponse<T> of(T data) {
		SingleResponse<T> singleResponse = new SingleResponse<>();
		singleResponse.setData(data);
		return singleResponse;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
