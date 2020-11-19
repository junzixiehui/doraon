package com.junzixiehui.doraon.business.event.custom.enums;

import lombok.Getter;

/**
 * @author: jxll
 * @description:
 * @date: 16:50 2018/4/16
 * @modify：
 */
public enum EventType {
    CREATE("CREATE","CREATE"),
    UPDATE("UPDATE","UPDATE"),
    DELETE("DELETE","DELETE");


    EventType(String code, String name) {
        this.code = code;
        this.name = name;
    }


    @Getter
    private String code;
    @Getter
    private String name;
}
