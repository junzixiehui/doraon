package com.junzixiehui.doraon.business.push.dto;

import lombok.Data;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/9/9  11:30
 * @version: 1.0
 */
@Data
public class Aps {

	private Alert alert;
	private int badge = 1;//表示应用程序图标显示的数字
	private String sound = "ping1";//表示收到push的提示音
}
