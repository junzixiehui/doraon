package com.junzixiehui.business.frame.templatemessage.alipay;


import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2018/10/30  3:05 PM
 * @version: 1.0
 */
@Component
public class AliPayClientSingleton {

	private static final String appId = ""/*EdaijiaContext.configContext.getString("AliPay.appId")*/;
	private static final String aliPayUrl = ""/*EdaijiaContext.configContext.getString("AliPay.aliPayUrl")*/;
	private static final String appPrivateKey = ""/*EdaijiaContext.configContext.getString("AliPay.appPrivateKey")*/;
	private static final String aliPayPublicKey = ""/*EdaijiaContext.configContext.getString("AliPay.aliPayPublicKey")*/;

	private static String DEFAULT = "default";

	private static final Map<String, AlipayClient> ALIPAY_CLIENT_MAP = new ConcurrentHashMap<String, AlipayClient>();


	public AlipayClient getClient(){

		if (ALIPAY_CLIENT_MAP != null && !ALIPAY_CLIENT_MAP.isEmpty()){
			return ALIPAY_CLIENT_MAP.get(DEFAULT);
		}
		AlipayClient alipayClient = new DefaultAlipayClient(
				aliPayUrl,
				appId,
				appPrivateKey,
				AlipayConstants.FORMAT_JSON,
				AlipayConstants.CHARSET_UTF8,
				aliPayPublicKey,
				AlipayConstants.SIGN_TYPE_RSA2
		);

		ALIPAY_CLIENT_MAP.put(DEFAULT,alipayClient);
		return alipayClient;
	}
}
