package com.junzixiehui.business.frame.templatemessage.alipay;


import com.alipay.api.AlipayApiException;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.alipay.api.response.AlipayOpenAppMiniTemplatemessageSendResponse;
import com.google.common.collect.Maps;
import com.junzixiehui.doraon.util.mapper.FastJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>Description: 访问支付宝 </p>
 * @author: by qulibin
 * @date: 2018/10/30  2:57 PM
 * @version: 1.0
 */
@Service
public class AliPayConsumer {

	private Logger log  = LoggerFactory.getLogger(AliPayConsumer.class);
	@Autowired
	private AliPayClientSingleton aliPayClientSingleton;

	/**
	 * @author: qulibin
	 * @description:
	 * @param toUserId    String	是	触达消息的支付宝user_id。	2088102122458832
	 * @param formId    String	是	用户发生的交易行为的交易号，或者用户在小程序产生表单提交的表单号，用于信息发送的校验。	2017010100000000580012345678
	 * @param userTemplateId    String	是	用户申请的模板 ID号，固定的模板 ID会发送固定的消息。	MDI4YzIxMDE2M2I5YTQzYjUxNWE4MjA4NmU1MTIyYmM=
	 * @param page    String	是	小程序的跳转页面，用于消息中心用户点击之后详细跳转的小程序页面。	page/component/index
	 * @param data    String	是	开发者需要发送模板消息中的自定义部分来替换模板的占位符。注意：占位符必须和申请模板时的关键词一一匹配。	{“keyword1”: {“value” : “12:00”}, “keyword2”: {“value” : “20180808”}, “keyword3”: {“value” : “支付宝”}}
	 *
	 * 调试地址 https://openhome.alipay.com/platform/demoManage.htm#/alipay.open.app.mini.templatemessage.send
	 * @date: 19:38 2019/9/17
	 * @return:
	 */
	public AlipayOpenAppMiniTemplatemessageSendResponse templateMessageSend(String toUserId, String formId,
			String userTemplateId, String page, Map<String, Object> data) throws AlipayApiException {
		AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
		Map<String, Object> dataMap = Maps.newHashMapWithExpectedSize(5);
		dataMap.put("to_user_id", toUserId);
		dataMap.put("form_id", formId);
		dataMap.put("user_template_id", userTemplateId);
		dataMap.put("page", page);
		dataMap.put("data", FastJson.object2JsonStr(data));

		request.setBizContent(FastJson.object2JsonStr(dataMap));

		final AlipayOpenAppMiniTemplatemessageSendResponse response = aliPayClientSingleton.getClient().execute(request);

		log.info("templateMessageSend:{}",response.getBody());
		return response;
	}
}
