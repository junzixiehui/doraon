package com.junzixiehui.business.frame.templatemessage.dto;

import lombok.Getter;

public enum MiniProgramChannelEnum {
	GAODE("GaoDe", 7),
    ALIPAY("AliPay", 6),
	WEIXIN("WeiXin", 5);


    MiniProgramChannelEnum(String code, int... source) {
        this.code = code;
        this.source = source;
    }

    @Getter
    private String code;
    @Getter
    private int[] source;

    public static MiniProgramChannelEnum getMiniProgramChannel(int source) {
        for (MiniProgramChannelEnum miniProgramChannelEnum : MiniProgramChannelEnum.values()) {
            int[] sourceArr = miniProgramChannelEnum.getSource();
            for (int i = 0; i < sourceArr.length; i++) {
                if (sourceArr[i] == source) {
                    return miniProgramChannelEnum;
                }
            }
        }
        return null;
    }
}
