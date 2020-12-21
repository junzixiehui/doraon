package com.junzixiehui.doraon.weixin.qrcode.req;

import lombok.Data;


@Data
public class ActionInfoWxReq {

    /**
     * 场景
     */
    private Scene scene;

    /**
     * 场景
     */
    @Data
    public class Scene {
        /**
         * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
         */
        private int scene_id;
        /**
         * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
         */
        private String scene_str;
    }
}
