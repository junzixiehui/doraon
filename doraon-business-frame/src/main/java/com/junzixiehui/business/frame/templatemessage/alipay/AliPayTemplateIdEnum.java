package com.junzixiehui.business.frame.templatemessage.alipay;


import com.junzixiehui.business.frame.templatemessage.TemplateMessageConstant;
import lombok.Getter;

public enum AliPayTemplateIdEnum {

	DRIVER_IN_PLACE("302", "NWY2MmIxNDkwZTViYzRlMzkwZTNlNTJjYTU4MmFkYmQ=", "pages/index/index"),
	ORDER_FAIL_405("405", "ZTA4YTkxZDE3OWRhNzU0YjNjODlmMTA2YzdkZWY0MjE=", "pages/index/index"),
	ORDER_FAIL_406("406", "ZTA4YTkxZDE3OWRhNzU0YjNjODlmMTA2YzdkZWY0MjE=", "pages/index/index"),
	ORDER_FAIL_506("506", "ZTA4YTkxZDE3OWRhNzU0YjNjODlmMTA2YzdkZWY0MjE=", "pages/index/index"),
	DRIVER_CANCEL_ORDER_505("505", "ZmU2OTA1ZjM0NDZiYmU0MTQ0M2MyOTE0MTViNzUyZjQ=", "pages/index/index"),
	REPORT_ORDER_501("501", "YTcwZWE5NDFmN2FlMDQ3NTVmMjcyZWUyYjBhMmM3MTA=", "pages/index/index"),
	EVALUATION_ORDER(TemplateMessageConstant.STATUS_EVALUATION_ORDER, "NDhiM2IyYTVjYzBiMWRiNjY3YmQxYmE5YjdiOWM3OGY=", "pages/index/index"),
	PAY_COMPLETE(TemplateMessageConstant.STATUS_PAY_COMPLETE, "NDY1OTU1OWM0ZThkMWFmZDAxYjYxN2VjMzNlNTVjZjM=", "pages/index/index"),
	CANCEL_PAY_COMPLETE(TemplateMessageConstant.STATUS_CANCEL_PAY_COMPLETE, "NDY1OTU1OWM0ZThkMWFmZDAxYjYxN2VjMzNlNTVjZjM=", "pages/index/index");

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
