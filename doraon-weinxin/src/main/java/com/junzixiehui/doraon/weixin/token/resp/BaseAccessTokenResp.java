package com.junzixiehui.doraon.weixin.token.resp;

import com.junzixiehui.doraon.weixin.common.WinXinBaseResp;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
public class BaseAccessTokenResp extends WinXinBaseResp implements Serializable {

    /**
     * 序列化标识
     */
    private static final long serialVersionUID = -3098025684564653952L;
    /**
     * 获取到的凭证
     */
    private String access_token;

    /**
     * 凭证有效时间，单位：秒
     */
    private long expires_in;

    public long getExpiresIn() {
        return expires_in - 2000;
    }
}
