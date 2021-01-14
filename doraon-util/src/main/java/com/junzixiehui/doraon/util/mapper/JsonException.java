package com.junzixiehui.doraon.util.mapper;

/**
 * <p>Description: </p>
 *
 * @author: by jxll
 * @date: 2018/5/12  17:48
 * @version: 1.0
 */
public class JsonException extends RuntimeException {
    public JsonException(String message) {
        super(message);
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
