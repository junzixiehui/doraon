package com.junzixiehui.business.frame.verificationcode.util;

import java.util.Random;

/**
 * <p>Description: 验证码工具类</p>
 *
 * @author: by jxll
 * @date: 2018/9/3  下午5:09
 * @version: 1.0
 */
public class VerifyCodeUtils {

    public static String getSmsCode() {
        int n = 6;
        StringBuilder code = new StringBuilder();
        Random ran = new Random();
        for (int i = 0; i < n; i++) {
            code.append(Integer.valueOf(ran.nextInt(10)).toString());
        }
        return code.toString();
    }

    public static String getPhoneTimes(String phone) {

        return phone + ":times";
    }

    public static String getPhoneExpire(String phone) {

        return phone + ":expire";
    }

    public static String getPhoneCodeKey(String phone) {

        return phone + ":code";
    }
}
