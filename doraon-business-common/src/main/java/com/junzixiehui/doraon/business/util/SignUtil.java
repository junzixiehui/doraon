package com.junzixiehui.doraon.business.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
public class SignUtil {

	public static String generateSignNew(Map<String, String> params, String keySecret, String appkey) {
		List<Map.Entry<String, String>> paramsList = sort(params);
		String queryStr = createQueryStr(paramsList);
		String mdStr = null;
		String sig = null;
		boolean useNewSig = false;

		if (useNewSig) {
			mdStr = keySecret + queryStr + keySecret;
			sig = md5(mdStr).substring(0, 30);
		} else {
			mdStr = queryStr + keySecret;
			sig = md5(mdStr);
		}
		return sig;
	}

	/**
	 * @param plainText 明文
	 * @return 32位密文
	 */
	public static String md5(String plainText) {
		String re_md5 = new String();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}

			re_md5 = buf.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return re_md5;
	}

	private static List<Map.Entry<String, String>> sort(Map<String, String> params) {
		List<Map.Entry<String, String>> paramsList = new ArrayList<Map.Entry<String, String>>(params.entrySet());
		Collections.sort(paramsList, new Comparator<Map.Entry<String, String>>() {
			public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
				return (o1.getKey()).toString().compareTo(o2.getKey());
			}
		});
		return paramsList;
	}

	private static String createQueryStr(List<Map.Entry<String, String>> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < params.size(); i++) {
			String key = params.get(i).getKey();
			String value = params.get(i).getValue();
			if (!key.equals("gpsstring") && !key.equals("callback") && !key.equals("_") && !key.equals("sig")) {
				sb.append(key);
				sb.append(value);
			}
		}
		return sb.toString();
	}
}
