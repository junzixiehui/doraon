package com.junzixiehui.doraon.business.util;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.junzixiehui.doraon.util.protocal.HttpClientUtils;
import com.junzixiehui.doraon.util.time.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author: qulibin
 * @description: edaijia API请求
 * @date: 下午7:57 2018/9/6
 * @modify：
 */
@Component
@Slf4j
public class ApiHttpUtils {

	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private HttpClientUtils httpClientUtils;

	/**
	 * @author: qulibin
	 * @description: 调用api接口
	 * @date: 下午3:23 2018/9/28
	 * @return:
	 */
	public String getApi(String appKey, String secret, String host, Map<String, String> params) {

		Map<String, String> phpParams = Maps.newHashMap();
		phpParams.put("appkey", appKey);
		if (null != params) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				phpParams.put(key, entry.getValue());
			}
		}
		if (!phpParams.containsKey("ver")) {
			phpParams.put("ver", "3");
		}
		if (!phpParams.containsKey("timestamp")) {
			phpParams.put("timestamp", getCurrentTimeStamp());
		}

		String sig = SignUtil.generateSignNew(phpParams, secret, appKey);
		phpParams.put("sig", sig);

		return getApi(host, phpParams);
	}

	public String getApi(String host, Map<String, String> params) {
		String urlParam = "";
		if (MapUtils.isNotEmpty(params)) {
			urlParam = "?" + Joiner.on("&").withKeyValueSeparator("=").join(params);
		}

		return restTemplate.getForObject(host + urlParam, String.class);
	}

	/**
	 * 发起post请求
	 *
	 * @param params        需要传的参数
	 * @return
	 */
	public String postApi(String appKey, String secret, String host, Map<String, String> params) {

		Map<String, String> phpParams = Maps.newHashMap();
		phpParams.put("appkey", appKey);
		if (null != params) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String key = entry.getKey();
				phpParams.put(key, entry.getValue());
			}
		}
		if (!phpParams.containsKey("ver")) {
			phpParams.put("ver", "3");
		}
		if (!phpParams.containsKey("timestamp")) {
			phpParams.put("timestamp", getCurrentTimeStamp());
		}
		String sig = SignUtil.generateSignNew(phpParams, secret, appKey);
		phpParams.put("sig", sig);

		return postApi(host, phpParams);
	}

	public String postApi(String host, Map<String, String> params) {
		final String result = httpClientUtils.httpPostForm(host, params);
		log.info("=========httpCallPostForm request, url:{}, param:{},result:{}", host, params.toString(), result);
		return result;
		/*MultiValueMap<String, Object> requestParam = new LinkedMultiValueMap<>();
		if (MapUtils.isNotEmpty(params)) {
			params.forEach((k, v) -> {
				requestParam.set(k, v);
			});
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAcceptCharset(Lists.newArrayList(Charsets.UTF_8));
		HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(requestParam, headers);
		ResponseEntity<String> response = restTemplate.postForEntity(host, request, String.class);
		return response.getStatusCode() == HttpStatus.OK ? response.getBody() : "";*/
	}

	private static String getCurrentTimeStamp() {
		return DateUtil.getCurrentTime();
	}
}
