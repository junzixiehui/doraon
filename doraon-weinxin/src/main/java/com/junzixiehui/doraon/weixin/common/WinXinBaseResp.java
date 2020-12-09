package com.junzixiehui.doraon.weixin.common;

import lombok.Data;

import java.io.Serializable;


@Data
public class WinXinBaseResp implements Serializable {

    private static final long serialVersionUID = 1998773449643319969L;
    /**
     * 响应信息
     */
    private String errmsg;
    /**
     * 响应码 默认为0 表示成功
     */
    private Integer errcode = 0;

    public boolean isSuccess(){
    	return WinXinErrorCode.isSuccess(errcode);
	}
}
