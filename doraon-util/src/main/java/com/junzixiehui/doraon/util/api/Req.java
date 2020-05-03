package com.junzixiehui.doraon.util.api;

import java.io.Serializable;
import java.util.Map;

/**
 * 远程调用的请求参数
 * 定义了系统级参数和应用级参数(data)
 * <p/>
 * Created by lianjia on 2016/6/2.
 */
public class Req implements Serializable {
    private static final long serialVersionUID = 4755589234434528814L;
    /**
     * 系统级参数|必填|分配给调用方的appKey，用于区分调用方
     */
    private String appKey;
    /**
     * 系统级参数|非必填|分配给用户的令牌，通过授权获取，方法详见(授权说明TODO)
     */
    private String accessToken;
    /**
     * 系统级参数|非必填|签名，具体签名算法接口自行约定
     */
    private String sign;
    /**
     * 系统级参数|必填|时间戳，格式为yyyy-MM-dd HH:mm:ss.SSS，例如：2011-06-16 13:23:30.667
     */
    private String timestamp;
    /**
     * 系统级参数|必填|API协议版本，可选值:v1
     */
    private String version;
    /**
     * 系统级参数|必填|当前操作城市
     */
    private int cityCode;
    /**
     * 系统级参数|必填|当前操作人系统号
     */
    private String userCode;
    /**
     * 应用级参数|非必填|
     */

    private Map<String, Object> data;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Req{");
        sb.append("appKey='").append(appKey).append('\'');
        sb.append(", accessToken='").append(accessToken).append('\'');
        sb.append(", sign='").append(sign).append('\'');
        sb.append(", timestamp='").append(timestamp).append('\'');
        sb.append(", version='").append(version).append('\'');
        sb.append(", cityCode=").append(cityCode);
        sb.append(", userCode='").append(userCode).append('\'');
        sb.append(", data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
