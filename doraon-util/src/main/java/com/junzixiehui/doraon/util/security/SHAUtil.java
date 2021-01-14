package com.junzixiehui.doraon.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @date 2018/10/8
 */
public class SHAUtil {
    private SHAUtil() {
    }

    public static String Encrypt(String strSrc, String encName) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = new byte[0];

        try {
            bt = strSrc.getBytes("UTF-8");
        } catch (UnsupportedEncodingException var6) {
            return null;
        }

        try {
            if (encName == null || encName.equals("")) {
                encName = "SHA-256";
            }

            md = MessageDigest.getInstance(encName);
            md.update(bt);
            strDes = bytes2Hex(md.digest());
            return strDes;
        } catch (NoSuchAlgorithmException var7) {
            return null;
        }
    }

    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp = null;

        for (int i = 0; i < bts.length; ++i) {
            tmp = Integer.toHexString(bts[i] & 255);
            if (tmp.length() == 1) {
                des = des + "0";
            }

            des = des + tmp;
        }

        return des;
    }
}
