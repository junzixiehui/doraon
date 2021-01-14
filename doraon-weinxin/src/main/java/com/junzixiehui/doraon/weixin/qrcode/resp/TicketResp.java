package com.junzixiehui.doraon.weixin.qrcode.resp;

import com.junzixiehui.doraon.weixin.common.WinXinBaseResp;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
public class TicketResp extends WinXinBaseResp {

    private static final long serialVersionUID = -488059214540676635L;
    /**
     * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    private String ticket;
    /**
     * 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天）。
     */
    private long expire_seconds;
    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    private String url;
}
