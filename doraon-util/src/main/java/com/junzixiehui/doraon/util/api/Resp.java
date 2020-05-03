package com.junzixiehui.doraon.util.api;

import com.google.common.collect.Maps;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用远程调用的响应对象
 */
public class Resp implements Serializable {

    private static final String CODE_SUCCESS = "20000";
    private static final String MESSAGE_SUCCESS = "success";

    /**
     * 状态码
     */
    private String code;
    /**
     * 状态信息
     */
    private String message;

    /**
     * 数据对象
     */
    private Map<String, Object> data;

    private Resp(String code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data == null ? new HashMap<String, Object>() : data;
    }

    public boolean isSuccess() {
        return CODE_SUCCESS.equals(this.getCode());
    }

    public static Resp success(String key, Object value) {
        Map<String, Object> data = Maps.newHashMap();
        data.put(key, value);
        return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, data);
    }

    public static Resp success(String msg) {
        return new Resp(CODE_SUCCESS, msg, null);
    }


    public static Resp success(Map<String, Object> data) {
        return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, data);
    }

    public static Resp success() {
        return new Resp(CODE_SUCCESS, MESSAGE_SUCCESS, null);
    }

    public Resp append(String key, Object value) {
        if (data == null) {
            data = Maps.newHashMap();
        }
        data.put(key, value);
        return this;
    }

    public static Resp error(ErrorCode errorCode, String... message) {
        return new Resp(errorCode.getCode(), String.format(errorCode.getMessage(), message), null);
    }

    public static Resp error(String code, String message) {
        return new Resp(code, message, null);
    }

    public static Resp error(ErrorCode errorCode, Map<String, Object> data) {
        return new Resp(errorCode.getCode(), errorCode.getMessage(), data);
    }

    public static Resp error(ErrorCode code) {
        return new Resp(code.getCode(), code.getMessage(), null);
    }


    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resp{");
        sb.append("code='").append(code).append('\'');
        sb.append(", message='").append(message).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
