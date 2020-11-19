package com.junzixiehui.business.frame.templatemessage.gaode;

import com.junzixiehui.business.frame.templatemessage.TemplateMessageConstant;
import lombok.Getter;

public enum GaoDeTemplateIdEnum {

	DRIVER_IN_PLACE("302", "OGI1YmFiMTJkNzk2MmM0ZGY1ZGVjYzZjNWNhZDdlYWY=", "pages/index/index"),
	ORDER_FAIL_405("405", "YWRjNjhkNzgwMDIwYjNjOTgzNWY0N2YwYjFmNjFlNzg=", "pages/index/index"),
	ORDER_FAIL_406("406", "YWRjNjhkNzgwMDIwYjNjOTgzNWY0N2YwYjFmNjFlNzg=", "pages/index/index"),
	ORDER_FAIL_506("506", "YWRjNjhkNzgwMDIwYjNjOTgzNWY0N2YwYjFmNjFlNzg=", "pages/index/index"),
	DRIVER_CANCEL_ORDER_505("505", "MmMzM2M3MzFjMmVkMzVmYzRhZWJmMjMxOTJmYmE4Mjc=", "pages/index/index"),
	REPORT_ORDER_501("501", "NmYxYTUzNWI5YmNhMGUxMWRkYWZhNDllNWU5MTMwNjg=", "pages/index/index"),
	EVALUATION_ORDER(TemplateMessageConstant.STATUS_EVALUATION_ORDER, "YjhlODcyZDNmZGNmYTY4YTdiZDE2MzM2OGU5ZWUwNGQ=", "pages/index/index"),
	PAY_COMPLETE(TemplateMessageConstant.STATUS_PAY_COMPLETE, "OTc4ZTMwODlkMmY3OGI0MmEyODYxYTJjZDljOWQzNjc=", "pages/index/index"),
	CANCEL_PAY_COMPLETE(TemplateMessageConstant.STATUS_CANCEL_PAY_COMPLETE, "OTc4ZTMwODlkMmY3OGI0MmEyODYxYTJjZDljOWQzNjc=", "pages/index/index");

	GaoDeTemplateIdEnum(String code, String templateId, String page) {
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
		for (GaoDeTemplateIdEnum gaoDeTemplateIdEnum : GaoDeTemplateIdEnum.values()) {
			if (gaoDeTemplateIdEnum.getCode().equals(code)){
				return gaoDeTemplateIdEnum.getTemplateId();
			}
		}
		return "";
	}

	public static String getPage(String code) {
		for (GaoDeTemplateIdEnum gaoDeTemplateIdEnum : GaoDeTemplateIdEnum.values()) {
			if (gaoDeTemplateIdEnum.getCode().equals(code)){
				return gaoDeTemplateIdEnum.getPage();
			}
		}
		return "";
	}
}
