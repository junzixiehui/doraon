package com.junzixiehui.business.frame.templatemessage.weixin;

import com.junzixiehui.business.frame.templatemessage.TemplateMessageConstant;
import lombok.Getter;

public enum WeiXinTemplateIdEnum {

	DRIVER_IN_PLACE("302", "B_S8SluOy9_CjZJSxTa10hW9Ar9UsrMn67ky6u7MWCw", "pages/index/index?pages=../order/order"),
	//	ORDER_FAIL_405("405", "wyxJ-ngvtQa6nmjvRI2UNqV5t4X8fyj7k_ASHrlwUZY", "pages/index/index"),
//	ORDER_FAIL_406("406", "wyxJ-ngvtQa6nmjvRI2UNqV5t4X8fyj7k_ASHrlwUZY", "pages/index/index"),
//	ORDER_FAIL_506("506", "ZTT10WQV9sk8DUr04U-D6jtkosdklVJOeHmfMVYdfvk", "pages/index/index"),
//	DRIVER_CANCEL_ORDER_505("505", "wyxJ-ngvtQa6nmjvRI2UNqV5t4X8fyj7k_ASHrlwUZY", "pages/orderHistory/orderHistory"),
	REPORT_ORDER_501("501", "uX1AYC2OLrRyPE4vfYzW-q0TLutaTeW2QOEICbP0tXU", "pages/index/index?pages=../pay/pay"),
	//		EVALUATION_ORDER(AppConstant.STATUS_EVALUATION_ORDER, "Y2Jv6gaEvKMK1r1brJP0ytRd4RpULcm868LTI0zH4tU", "pages/index/index?pages=../orderDetail/orderDetail"),
	PAY_COMPLETE(TemplateMessageConstant.STATUS_PAY_COMPLETE, "zA_avVg48EBKlgRaC1zjk7XVX8wcyBCOiwULqnJk__4", "pages/index/index?pages=../orderDetail/orderDetail"),
	//取消费支付
	CANCEL_PAY_COMPLETE(TemplateMessageConstant.STATUS_CANCEL_PAY_COMPLETE, "zA_avVg48EBKlgRaC1zjk7XVX8wcyBCOiwULqnJk__4", "pages/index/index?pages=../orderDetail/orderDetail");

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
