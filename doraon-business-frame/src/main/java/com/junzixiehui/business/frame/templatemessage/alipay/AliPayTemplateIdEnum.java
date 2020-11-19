package com.junzixiehui.business.frame.templatemessage.alipay;


import com.junzixiehui.business.frame.templatemessage.TemplateMessageConstant;
import lombok.Getter;

public enum AliPayTemplateIdEnum {

	DRIVER_IN_PLACE("302", "xxx=", "pages/index/index"),
	ORDER_FAIL_405("405", "xxx=", "pages/index/index"),
	ORDER_FAIL_406("406", "xx=", "pages/index/index"),
	ORDER_FAIL_506("506", "xx=", "pages/index/index"),
	DRIVER_CANCEL_ORDER_505("505", "xx=", "pages/index/index"),
	REPORT_ORDER_501("501", "xx=", "pages/index/index"),
	EVALUATION_ORDER(TemplateMessageConstant.STATUS_EVALUATION_ORDER, "xx=", "pages/index/index"),
	PAY_COMPLETE(TemplateMessageConstant.STATUS_PAY_COMPLETE, "xx=", "pages/index/index"),
	CANCEL_PAY_COMPLETE(TemplateMessageConstant.STATUS_CANCEL_PAY_COMPLETE, "xx=", "pages/index/index");

	AliPayTemplateIdEnum(String code, String templateId, String page) {
		this.code = code;
		this.templateId = templateId;
		this.page = page;
	}

	@Getter
	private String code;
	@Getter
	private String templateId;
	@Getter
	private String page;

	public static String getTemplateId(String code) {
		for (AliPayTemplateIdEnum gaoDeTemplateIdEnum : AliPayTemplateIdEnum.values()) {
			if (gaoDeTemplateIdEnum.getCode().equals(code)){
				return gaoDeTemplateIdEnum.getTemplateId();
			}
		}
		return "";
	}

	public static String getPage(String code) {
		for (AliPayTemplateIdEnum gaoDeTemplateIdEnum : AliPayTemplateIdEnum.values()) {
			if (gaoDeTemplateIdEnum.getCode().equals(code)){
				return gaoDeTemplateIdEnum.getPage();
			}
		}
		return "";
	}
}
