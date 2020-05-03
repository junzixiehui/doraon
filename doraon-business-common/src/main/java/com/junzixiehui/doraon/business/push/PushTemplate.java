package com.junzixiehui.doraon.business.push;


import com.junzixiehui.doraon.business.push.dto.Alert;
import com.junzixiehui.doraon.business.push.dto.AndroidMsgTemplate;
import com.junzixiehui.doraon.business.push.dto.AppleMsgTemplate;
import com.junzixiehui.doraon.business.push.dto.Aps;
import com.junzixiehui.doraon.util.mapper.FastJson;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/9/9  11:00
 * @version: 1.0
 */
public class PushTemplate {

	public static String buildAndroidPushMsg(String title, String content, String url, int type) {
		return buildAndroidPushMsg(title, content, url, type, null);
	}

	public static String buildAndroidPushMsg(String title, String content, String url, int type,
			Map<String, Object> extraMap) {

		final String activityId = MapUtils.getString(extraMap, "activityId", "0");
		final String level = MapUtils.getString(extraMap, "level", "");
		final long messageid = MapUtils.getLong(extraMap, "messageid", 1L);

		AndroidMsgTemplate androidMsgTemplate = new AndroidMsgTemplate();
		androidMsgTemplate.setTitle(title);
		androidMsgTemplate.setContent(content);
		androidMsgTemplate.setUrl(url);
		androidMsgTemplate.setType(type);
		androidMsgTemplate.setAct_id(activityId);
		androidMsgTemplate.setMessageid(messageid);
		androidMsgTemplate.setLevel(level);
		androidMsgTemplate.setExtraMap(extraMap);
		return FastJson.object2JsonStr(androidMsgTemplate);
	}

	public static String buildApplePushMsg(String title, String content, String url, int type) {
		return buildApplePushMsg(title, content, url, type, null);
	}

	public static String buildApplePushMsg(String title, String content, String url, int type,
			Map<String, Object> extraMap) {
		final String activityId = MapUtils.getString(extraMap, "activityId", "0");
		final long messageid = MapUtils.getLong(extraMap, "messageid", 1L);
		final String level = MapUtils.getString(extraMap, "level", "");
		AppleMsgTemplate msgTemplate = new AppleMsgTemplate();
		msgTemplate.setTitle(title);
		msgTemplate.setContent(content);
		msgTemplate.setUrl(url);
		msgTemplate.setType(type);
		msgTemplate.setAct_id(activityId);
		msgTemplate.setLevel(level);
		msgTemplate.setMessageid(messageid);
		msgTemplate.setExtraMap(extraMap);
		Aps aps = new Aps();
		Alert alert = new Alert();
		alert.setTitle(title);
		alert.setBody(content);
		aps.setAlert(alert);
		msgTemplate.setAps(aps);
		return FastJson.object2JsonStr(msgTemplate);
	}
}
