package com.junzixiehui.doraon.util.servlet;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/4/16  20:00
 * @version: 1.0
 */
public class RequestUtils {

	public static Map<String, String> requestToMap(ServletRequest servletRequest) {
		Map<String, String> params = new HashMap<>();
		Map requestParams = servletRequest.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		return params;
	}

	public static String requestToMap(ServletRequest servletRequest, String key) {
		final Map<String, String> requestToMap = requestToMap(servletRequest);
		return requestToMap.get(key);
	}
}
