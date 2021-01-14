package com.junzixiehui.doraon.util.security;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

/**
 * @date 2018/10/8
 */
public class MD5Util {

    private MD5Util() {
    }

    public static String md5(String data) {
        return DigestUtils.md5Hex(data).toUpperCase();
    }

    public static String md5LowerCase(String text, String salt) throws Exception {
        byte[] bytes = (text + salt).getBytes();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(bytes);
        bytes = messageDigest.digest();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < bytes.length; ++i) {
            if ((bytes[i] & 255) < 16) {
                sb.append("0");
            }
            sb.append(Long.toString((long) (bytes[i] & 255), 16));
        }

        return sb.toString().toLowerCase();
    }
}
