package com.junzixiehui.doraon.util.ip;

import com.vip.vjtools.vjkit.number.NumberUtil;
import com.vip.vjtools.vjkit.text.MoreStringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

@Slf4j
public final class IPUtils {

	private IPUtils() {
	}

	public static long toLong(String ip) {
		String[] strings = ip.split("\\.");
		StringBuilder builder = new StringBuilder();
		for (String s : strings) {
			String b = Integer.toBinaryString(Integer.parseInt(s));
			if (b.length() < 8) {
				for (int i = 0; i < 8 - b.length(); i++) {
					builder.append("0");
				}
			}
			builder.append(b);
		}
		return Long.valueOf(builder.toString(), 2);
	}

	public static String toStringIp(long ip) {
		StringBuilder builder = new StringBuilder(Long.toBinaryString(ip));
		int l = builder.length();
		if (l < 32) {
			for (int i = 0; i < 32 - l; i++) {
				builder.insert(0, "0");
			}
		}
		StringBuilder ipBuilder = new StringBuilder();
		for (int i = 0; i < 32; i += 8) {
			if (i != 0) {
				ipBuilder.append(".");
			}
			String s = builder.substring(i, i + 8);
			ipBuilder.append(Integer.valueOf(s, 2));
		}
		return ipBuilder.toString();
	}

	/**
	 * int转换到IPV4 String, from Netty NetUtil
	 */
	public static String intToIpv4String(int i) {
		return new StringBuilder(15).append((i >> 24) & 0xff).append('.').append((i >> 16) & 0xff).append('.')
				.append((i >> 8) & 0xff).append('.').append(i & 0xff).toString();
	}

	/**
	 * Ipv4 String 转换到int
	 */
	public static int ipv4StringToInt(String ipv4Str) {
		byte[] byteAddress = ip4StringToBytes(ipv4Str);
		if (byteAddress == null) {
			return 0;
		} else {
			return NumberUtil.toInt(byteAddress);
		}
	}

	/**
	 * Ipv4 String 转换到byte[]
	 */
	private static byte[] ip4StringToBytes(String ipv4Str) {
		if (ipv4Str == null) {
			return null;
		}

		List<String> it = MoreStringUtil.split(ipv4Str, '.', 4);
		if (it.size() != 4) {
			return null;
		}

		byte[] byteAddress = new byte[4];
		for (int i = 0; i < 4; i++) {
			int tempInt = Integer.parseInt(it.get(i));
			if (tempInt > 255) {
				return null;
			}
			byteAddress[i] = (byte) tempInt;
		}
		return byteAddress;
	}

	public static final String getLocalIpv4() {
		try {
			InetAddress candidateAddress = null;
			// 遍历所有的网络接口
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				// 在所有的接口下再遍历IP
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress()) {
							// 如果是site-local地址，就是它了
							return inetAddr.getHostAddress();
						} else if (candidateAddress == null) {
							// site-local类型的地址未被发现，先记录候选地址
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress.getHostAddress();
			}
			// 如果没有发现 non-loopback地址.只能用最次选的方案
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			return jdkSuppliedAddress.getHostAddress();
		} catch (Exception e) {
		}
		return "";
	}

	public static final String getRemoteIpv4(HttpServletRequest request) {
		if (request == null){
			return "";
		}
		try {
			String ip = request.getHeader("x-forwarded-for");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("http_client_ip");
			}
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			// 如果是多级代理，那么取第一个ip为客户ip
			if (ip != null && ip.indexOf(",") != -1) {
				ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
			}
			return ip;
		} catch (Exception e) {
			log.error("getRemoteIpv4", e);
		}
		return "";
	}
}
