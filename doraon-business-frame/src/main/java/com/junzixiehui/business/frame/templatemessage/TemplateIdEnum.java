package com.junzixiehui.business.frame.templatemessage;

import com.junzixiehui.business.frame.templatemessage.alipay.AliPayTemplateIdEnum;
import com.junzixiehui.business.frame.templatemessage.dto.MiniProgramChannelEnum;
import com.junzixiehui.business.frame.templatemessage.gaode.GaoDeTemplateIdEnum;
import com.junzixiehui.business.frame.templatemessage.weixin.WeiXinTemplateIdEnum;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/9/26  15:51
 * @version: 1.0
 */
public class TemplateIdEnum {

	public static String getPage(String orderStatus,int source) {
		final MiniProgramChannelEnum miniProgramChannel = MiniProgramChannelEnum.getMiniProgramChannel(source);
		if (miniProgramChannel == null){
			return null;
		}
		switch (miniProgramChannel){
			case GAODE:
				return GaoDeTemplateIdEnum.getPage(orderStatus);
			case WEIXIN:
				return WeiXinTemplateIdEnum.getPage(orderStatus);
			case ALIPAY:
				return AliPayTemplateIdEnum.getPage(orderStatus);
			default:
				return null;
		}
	}
}
