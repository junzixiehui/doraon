package com.junzixiehui.doraon.util.api;

import com.junzixiehui.doraon.util.exception.ServiceException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@ToString
@ApiModel(description = "返回信息")
@NoArgsConstructor
public class MultiResp<T> implements Serializable {

	private static final long serialVersionUID = 2L;
	public static final String CODE_SUCCESS = "0";
	public static final String MESSAGE_SUCCESS = "success";
	@ApiModelProperty("状态码,0成功非0失败")
	private String code;
	@ApiModelProperty("状态码描述,0success非0失败原因")
	private String message;
	@ApiModelProperty("返回数据")
	private Collection<T> data;

	private MultiResp(String code, String message, Collection<T> data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static MultiResp success() {
		return new MultiResp(CODE_SUCCESS, MESSAGE_SUCCESS, CollectionUtils.EMPTY_COLLECTION);
	}

	public static <T> MultiResp success(Collection<T> data) {
		return new MultiResp(CODE_SUCCESS, MESSAGE_SUCCESS, data);
	}

	public static <T> MultiResp success(Collection<T> data, String message) {
		return new MultiResp(CODE_SUCCESS, message, data);
	}

	public static MultiResp error(Throwable e) {

		if (e instanceof ServiceException) {
			ServiceException se = (ServiceException) e;
			String code = StringUtils.isBlank(se.getCode()) ? ErrorCode.OPEN_FAIL.getCode() : se.getCode();
			String msg = StringUtils.isBlank(se.getMessage()) ? ErrorCode.OPEN_FAIL.getMessage() : se.getMessage();
			return new MultiResp(code, msg, CollectionUtils.EMPTY_COLLECTION);
		} else {
			return new MultiResp(ErrorCode.SERVER.getCode(),
					String.format(ErrorCode.SERVER.getMessage(), e.getMessage()), CollectionUtils.EMPTY_COLLECTION);
		}
	}

	public static MultiResp error(ErrorCode errorCode, String appendMessage) {
		String message = String.format(errorCode.getMessage(), appendMessage == null ? "" : appendMessage);
		return new MultiResp(errorCode.getCode(), message, CollectionUtils.EMPTY_COLLECTION);
	}

	public static MultiResp error(ErrorCode errorCode) {
		return new MultiResp(errorCode.getCode(), errorCode.getMessage(), CollectionUtils.EMPTY_COLLECTION);
	}

	public static MultiResp error(String code, String message) {
		return new MultiResp(code, message, CollectionUtils.EMPTY_COLLECTION);
	}

	public static MultiResp error(String message) {
		return new MultiResp(ErrorCode.OPEN_FAIL.getCode(), message, CollectionUtils.EMPTY_COLLECTION);
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("MultiResp{");
		sb.append("code='").append(this.code).append('\'');
		sb.append(", message='").append(this.message).append('\'');
		sb.append(", data=").append(this.data);
		sb.append('}');
		return sb.toString();
	}

	public String toSimpleString() {
		StringBuilder sb = new StringBuilder("MultiResp{");
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

	public Collection<T> getData() {
		return this.data;
	}
}
