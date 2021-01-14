package com.junzixiehui.doraon.weixin.qrcode.req;

import lombok.Data;


@Data
public class QRCodeWxReq {

    /**
     * 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
     */
    private long expire_seconds = 30L;

    /**
     * 二维码类型
     */
    private QRCodeTypeEnum action_name;
    /**
     * 二维码详细信息
     */
    private ActionInfoWxReq action_info;

    /**
     * 二维码类型
     */
    public enum QRCodeTypeEnum {
        /**
         * 临时的整型参数值
         */
        QR_SCENE,
        /**
         * 临时的字符串参数值
         */
        QR_STR_SCENE,
        /**
         * 永久的整型参数值
         */
        QR_LIMIT_SCENE,
        /**
         * 永久的字符串参数值
         */
        QR_LIMIT_STR_SCENE
    }
}
