package com.junzixiehui.doraon.weixin.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/9  18:27
 * @version: 1.0
 */
@Slf4j
@Component
public class DispatchHandler {


	private String dispach(HttpServletRequest request, HttpServletResponse response) {
		String result = null;
		try {

			InputStream inputStream = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";


			StringBuilder sb = new StringBuilder();
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			String xml = sb.toString();

			//扫码时间推送模板消息
			//result = handleEvent(xml);

			OutputStream os = response.getOutputStream();
			os.write(result.getBytes("UTF-8"));
			os.flush();
			os.close();

		} catch (Exception e) {
			log.error("dispatch", e);
		}
		return result;
	}
}
