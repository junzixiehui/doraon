package com.junzixiehui.doraon.util.security;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @date 2018/10/2
 */
public class RSAUtil {
    private static String hexString = "0123456789ABCDEF";

    public RSAUtil() {
    }

    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.getDecoder().decode(key);
    }

    public static String bytesToString(byte[] src) throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src != null && src.length > 0) {
            for (int bytes = 0; bytes < src.length; ++bytes) {
                int baos = src[bytes] & 255;
                String i = Integer.toHexString(baos);
                if (i.length() < 2) {
                    stringBuilder.append(0);
                }

                stringBuilder.append(i);
            }

            String var5 = stringBuilder.toString();
            ByteArrayOutputStream var6 = new ByteArrayOutputStream(var5.length() / 2);

            for (int var7 = 0; var7 < var5.length(); var7 += 2) {
                var6.write(hexString.indexOf(var5.charAt(var7)) << 4 | hexString.indexOf(var5.charAt(var7 + 1)));
            }

            return new String(var6.toByteArray());
        } else {
            return null;
        }
    }

	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(2, privateKey);
		return cipher.doFinal(data);
	}

	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(2, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(1, publicKey);
		return cipher.doFinal(data);
	}

	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		byte[] keyBytes = decryptBASE64(key);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(1, privateKey);
		return cipher.doFinal(data);
	}
}
