package com.junzixiehui.business.frame.templatemessage;


import com.junzixiehui.business.frame.templatemessage.alipay.AliPayTemplateHandler;
import com.junzixiehui.business.frame.templatemessage.alipay.AliPayTemplateIdEnum;
import com.junzixiehui.business.frame.templatemessage.dto.MiniProgramChannelEnum;
import com.junzixiehui.business.frame.templatemessage.gaode.GaoDeTemplateIdEnum;
import com.junzixiehui.business.frame.templatemessage.gaode.GaodeTemplateHandler;
import com.junzixiehui.business.frame.templatemessage.weixin.WeiXinTemplateIdEnum;
import com.junzixiehui.business.frame.templatemessage.weixin.WeixinTemplateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChannelRouter {

	@Autowired
	private AliPayTemplateHandler aliPayTemplateHandler;
    @Autowired
	private GaodeTemplateHandler gaodeTemplateHandler;
    @Autowired
	private WeixinTemplateHandler weixinTemplateHandler;

	public TemplateMessageHandler route(int source) {
		MiniProgramChannelEnum channelHandler = MiniProgramChannelEnum.getMiniProgramChannel(source);
		if (channelHandler == null) {
			return null;
		}
		switch (channelHandler){
			case GAODE:
				return gaodeTemplateHandler;
			case ALIPAY:
				return aliPayTemplateHandler;
			case WEIXIN:
				return weixinTemplateHandler;
			default:
				break;
		}
		return null;
	}

	public String getTemplateId(String orderStatus,int source) {
		MiniProgramChannelEnum channelHandler = MiniProgramChannelEnum.getMiniProgramChannel(source);
		if (channelHandler == null) {
			return null;
		}
		switch (channelHandler){
			case GAODE:
				return GaoDeTemplateIdEnum.getTemplateId(orderStatus);
			case ALIPAY:
				return AliPayTemplateIdEnum.getTemplateId(orderStatus);
			case WEIXIN:
				return WeiXinTemplateIdEnum.getTemplateId(orderStatus);
			default:
				break;
		}
		return null;
	}
}
