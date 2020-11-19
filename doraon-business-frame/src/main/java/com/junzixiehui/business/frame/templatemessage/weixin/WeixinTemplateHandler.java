package com.junzixiehui.business.frame.templatemessage.weixin;


import com.junzixiehui.business.frame.templatemessage.TemplateMessageHandler;
import com.junzixiehui.business.frame.templatemessage.dto.TemplateMessageReq;
import com.junzixiehui.doraon.util.api.Resp;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class WeixinTemplateHandler implements TemplateMessageHandler {

	@Autowired
	private WeixinConsumer weixinConsumer;

	@Override
	public Resp sendTemplateMessage(TemplateMessageReq templateMessageReq) {
		final String userId = templateMessageReq.getUserId();
//		final String formId = templateMessageReq.getFormId();
		final String templateId = templateMessageReq.getTemplateId();
		final Map<String, Object> data = templateMessageReq.getData();
		final String page = templateMessageReq.getPage();

		try {
			return weixinConsumer.templateMessageSend(userId, templateId, page, data);
		} catch (Exception e) {
			return Resp.error("5000", e.getMessage());
		}
	}
}
