package com.junzixiehui.doraon.business.push.dto;
import lombok.Data;

import java.util.Map;

/**
 * <p>Description: android消息模板</p>
 * @author: by qulibin
 * @date: 2Da9/9/9  11:00
 * @version: 1.0
 */
@Data
public class AppleMsgTemplate {

	private int type;
	private String content;
	private String title;
	private String url;
	private String level;
	private String act_id;
	private long messageid;
	private long timestamp = System.currentTimeMillis() / 1000;
	private Aps aps;
	private Map<String,Object> extraMap;
}
