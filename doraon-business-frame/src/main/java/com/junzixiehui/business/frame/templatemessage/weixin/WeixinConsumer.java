package com.junzixiehui.business.frame.templatemessage.weixin;

import com.google.common.collect.Maps;
import com.junzixiehui.doraon.business.util.EnvHelper;
import com.junzixiehui.doraon.util.api.Resp;
import com.junzixiehui.doraon.util.mapper.FastJson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.collections.MapUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * <p>Description: 访问 </p>
 *
 * @author: by jxll
 * @date: 2018/10/30  2:57 PM
 * @version: 1.0
 */
@Service
@Slf4j
public class WeixinConsumer {

    /**
     * @author: jxll  2020-03-31微信模板消息修改为订阅消息
     * @description: 属性    类型	默认值	必填	说明
     * access_token	string		是	接口调用凭证
     * touser	string		是	接收者（用户）的 openid
     * template_id	string		是	所需下发的模板消息的id
     * page	string		否	点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * form_id	string		是	表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     * data	Object		否	模板内容，不填则下发空模板。具体格式请参考示例。
     * emphasis_keyword	string		否	模板需要放大的关键词，不填则默认无放大
     * @date: 19:38 2019/9/17
     * @return:
     */
    public Resp templateMessageSend(String toUserId, String userTemplateId, String page, Map<String, Object> data) {
        Map<String, Object> dataMap = Maps.newHashMapWithExpectedSize(5);
        dataMap.put("touser", toUserId);
        dataMap.put("template_id", userTemplateId);
        dataMap.put("page", page);
        dataMap.put("data", data);
        String miniprogram_state;
        if (EnvHelper.isPro()) {
            miniprogram_state = "formal";
        } else {
            miniprogram_state = "trial";
        }
        dataMap.put("miniprogram_state", miniprogram_state);

        try {
            final String res = postWithAccessToken("cgi-bin/message/subscribe/send", dataMap,
                    WeixinUtils.getWxMiniAccessToken());
            final Map map = FastJson.jsonStr2Object(res, Map.class);
            final int errcode = MapUtils.getInteger(map, "errcode", -1);
            final String errMsg = MapUtils.getString(map, "errMsg", "");
            if (errcode != 0) {
                return Resp.error(String.valueOf(errcode), errMsg);
            }
            return Resp.success();
        } catch (Exception e) {
            log.error("templateMessageSend error", e);
            return Resp.error("-1", e.getMessage());
        }
    }

	public static String postWithAccessToken(final String api, final Object param, String accessToken)
			throws IOException {
		OkHttpClient client = new OkHttpClient();
		final String url = "https://api.weixin.qq.com/" + api + "?access_token=" + accessToken;
		final String postString = FastJson.object2JsonStr(param);
		log.info("post weixin:{},param:{},response:{}", url, postString);
		final String response = client.newCall(new Request.Builder().url(url).post(RequestBody
				.create(MediaType.parse(com.google.common.net.MediaType.PLAIN_TEXT_UTF_8.toString()), postString))
				.build()).execute().body().string();
		log.info("post weixin:response:{}", response);
		return response;
	}
}
