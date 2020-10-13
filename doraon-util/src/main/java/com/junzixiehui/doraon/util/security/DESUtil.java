package com.junzixiehui.doraon.util.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 */
public class DESUtil {

	private static final Logger log = LoggerFactory.getLogger(DESUtil.class);
	private static final int MAX_MSG_LENGTH = 16384;
	private static final String Algorithm = "DESede";
	private static final String PADDING = "DESede/ECB/NoPadding";
	public static final byte[] DEFAULT_KEY = new byte[]{(byte) 49, (byte) 50, (byte) 51, (byte) 52, (byte) 53,
			(byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) 52,
			(byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 48, (byte) 49, (byte) 50, (byte) 51,
			(byte) 52};

	public DESUtil() {
	}

	public static byte[] encrypt(byte[] keybyte, byte[] src) {
		try {
			SecretKeySpec e3 = new SecretKeySpec(keybyte, "DESede");
			Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
			c1.init(1, e3);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException var4) {
			var4.printStackTrace();
		} catch (NoSuchPaddingException var5) {
			var5.printStackTrace();
		} catch (Exception var6) {
			var6.printStackTrace();
		}

		return null;
	}

	private static byte[] decrypt(byte[] keybyte, byte[] src) {
		try {
			SecretKeySpec e3 = new SecretKeySpec(keybyte, "DESede");
			Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
			c1.init(2, e3);
			return c1.doFinal(src);
		} catch (NoSuchAlgorithmException var4) {
			var4.printStackTrace();
		} catch (NoSuchPaddingException var5) {
			var5.printStackTrace();
		} catch (Exception var6) {
			var6.printStackTrace();
		}

		return null;
	}

	public static String encrypt2HexStr(byte[] keys, String sourceData) {
		byte[] source = new byte[0];

		try {
			source = sourceData.getBytes("UTF-8");
			int e = source.length;
			int x = (e + 4) % 8;
			int y = x == 0 ? 0 : 8 - x;
			byte[] sizeByte = intToByteArray(e);
			byte[] resultByte = new byte[e + 4 + y];
			resultByte[0] = sizeByte[0];
			resultByte[1] = sizeByte[1];
			resultByte[2] = sizeByte[2];
			resultByte[3] = sizeByte[3];

			int desdata;
			for (desdata = 0; desdata < e; ++desdata) {
				resultByte[4 + desdata] = source[desdata];
			}

			for (desdata = 0; desdata < y; ++desdata) {
				resultByte[e + 4 + desdata] = 0;
			}

			byte[] var10 = encrypt(keys, resultByte);
			return bytes2Hex(var10);
		} catch (UnsupportedEncodingException var9) {
			var9.printStackTrace();
			return null;
		}
	}

	public static String decrypt4HexStr(byte[] keys, String data) {
		byte[] hexSourceData = new byte[0];

		try {
			hexSourceData = hex2byte(data.getBytes("UTF-8"));
			byte[] e = decrypt(keys, hexSourceData);
			byte[] dataSizeByte = new byte[]{e[0], e[1], e[2], e[3]};
			int dsb = byteArrayToInt(dataSizeByte, 0);
			if (dsb > 16384) {
				throw new RuntimeException("msg over MAX_MSG_LENGTH or msg error");
			} else {
				byte[] tempData = new byte[dsb];

				for (int i = 0; i < dsb; ++i) {
					tempData[i] = e[4 + i];
				}

				return hex2bin(toHexString(tempData));
			}
		} catch (UnsupportedEncodingException var8) {
			var8.printStackTrace();
			return null;
		}
	}

	private static String hex2bin(String hex) throws UnsupportedEncodingException {
		String digital = "0123456789abcdef";
		char[] hex2char = hex.toCharArray();
		byte[] bytes = new byte[hex.length() / 2];

		for (int i = 0; i < bytes.length; ++i) {
			int temp = digital.indexOf(hex2char[2 * i]) * 16;
			temp += digital.indexOf(hex2char[2 * i + 1]);
			bytes[i] = (byte) (temp & 255);
		}

		return new String(bytes, "UTF-8");
	}

	private static String toHexString(byte[] ba) {
		StringBuilder str = new StringBuilder();

		for (int i = 0; i < ba.length; ++i) {
			str.append(String.format("%x", new Object[]{Byte.valueOf(ba[i])}));
		}

		return str.toString();
	}

	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp;

		for (int i = 0; i < bts.length; ++i) {
			tmp = Integer.toHexString(bts[i] & 255);
			if (tmp.length() == 1) {
				des = des + "0";
			}

			des = des + tmp;
		}

		return des;
	}

	public static byte[] hex2byte(byte[] b) {
		if (b.length % 2 != 0) {
			throw new IllegalArgumentException("长度不是偶数");
		} else {
			byte[] b2 = new byte[b.length / 2];

			for (int n = 0; n < b.length; n += 2) {
				String item = new String(b, n, 2);
				b2[n / 2] = (byte) Integer.parseInt(item, 16);
			}

			Object b1 = null;
			return b2;
		}
	}

	private static byte[] intToByteArray(int i) {
		byte[] result = new byte[]{(byte) (i >> 24 & 255), (byte) (i >> 16 & 255), (byte) (i >> 8 & 255),
				(byte) (i & 255)};
		return result;
	}

	private static int byteArrayToInt(byte[] b, int offset) {
		int value = 0;

		for (int i = 0; i < 4; ++i) {
			int shift = (3 - i) * 8;
			value += (b[i + offset] & 255) << shift;
		}

		return value;
	}

	public static void main(String[] args) {
		String szSrc = "This is a 3DES test. 测试abcdf";
		System.out.println("加密前的字符串:" + szSrc);
		byte[] encoded = new byte[0];

		try {
			System.out.println("加密前长度:" + szSrc.getBytes("UTF-8").length);
			System.out.println("加密前HEX:" + bytes2Hex(szSrc.getBytes("UTF-8")));
			encoded = encrypt(DEFAULT_KEY, szSrc.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException var5) {
			var5.printStackTrace();
		}

		try {
			System.out.println("加密后长度:" + encoded.length);
			System.out.println("加密后的字符串:" + new String(encoded, "UTF-8"));
		} catch (UnsupportedEncodingException var4) {
			var4.printStackTrace();
		}

		byte[] srcBytes = decrypt(DEFAULT_KEY, encoded);
		System.out.println("解密后HEX:" + bytes2Hex(srcBytes));
		System.out.println("解密后的字符串:" + new String(srcBytes));
	}
}
