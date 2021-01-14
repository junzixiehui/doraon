package com.junzixiehui.business.frame.templatemessage.weixin;

import com.junzixiehui.business.frame.templatemessage.TemplateMessageConstant;
import lombok.Getter;

public enum WeiXinTemplateIdEnum {

	DRIVER_IN_PLACE("302", "xx", "pages/index/index?pages=../order/order"),
	//	ORDER_FAIL_405("405", "wyxJ-xx", "pages/index/index"),
//	ORDER_FAIL_406("406", "wyxJ-xx", "pages/index/index"),
//	ORDER_FAIL_506("506", "xx-xx", "pages/index/index"),
//	DRIVER_CANCEL_ORDER_505("505", "wyxJ-xx", "pages/orderHistory/orderHistory"),
	REPORT_ORDER_501("501", "ss-ss", "pages/index/index?pages=../pay/pay"),
	//		EVALUATION_ORDER(AppConstant.STATUS_EVALUATION_ORDER, "xx", "pages/index/index?pages=../orderDetail/orderDetail"),
	PAY_COMPLETE(TemplateMessageConstant.STATUS_PAY_COMPLETE, "xx", "pages/index/index?pages=../orderDetail/orderDetail"),
	//取消费支付
	CANCEL_PAY_COMPLETE(TemplateMessageConstant.STATUS_CANCEL_PAY_COMPLETE, "xx", "pages/index/index?pages=../orderDetail/orderDetail");

	@Getter
	private String code;
	@Getter
	private String templateId;
	@Getter
	private String page;

	WeiXinTemplateIdEnum(String code, String templateId, String page) {
		this.code = code;
		this.templateId = templateId;
		this.page = page;
	}

	public static String getTemplateId(String code) {
		for (WeiXinTemplateIdEnum gaoDeTemplateIdEnum : WeiXinTemplateIdEnum.values()) {
			if (gaoDeTemplateIdEnum.getCode().equals(code)){
				return gaoDeTemplateIdEnum.getTemplateId();
			}
		}
		return "";
	}

	public static String getPage(String code) {
		for (WeiXinTemplateIdEnum gaoDeTemplateIdEnum : WeiXinTemplateIdEnum.values()) {
			if (gaoDeTemplateIdEnum.getCode().equals(code)){
				return gaoDeTemplateIdEnum.getPage();
			}
		}
		return "";
	}
}
