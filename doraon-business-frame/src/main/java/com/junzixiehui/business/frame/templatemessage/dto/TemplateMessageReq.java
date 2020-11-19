package com.junzixiehui.business.frame.templatemessage.dto;

import lombok.Data;

import java.util.Map;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2019/9/20  15:10
 * @version: 1.0
 */
@Data
public class TemplateMessageReq {

	private String userId;
	private String formId;
	private String templateId;
	private String page;
	private Map<String, Object> data;
}
