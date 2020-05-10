package com.junzixiehui.business.frame.verificationcode;

import com.junzixiehui.business.frame.verificationcode.util.VerifyCodeUtils;
import com.junzixiehui.doraon.business.redis.RedisTemplateWarpper;
import com.junzixiehui.doraon.business.util.EnvHelper;
import com.junzixiehui.doraon.util.api.ErrorCode;
import com.junzixiehui.doraon.util.api.Resp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author: qulibin
 * @description: 验证码服务 - 校验一个独立服务的单一职责原则 就是看他引用外部的引用
 * @date: 上午11:18 2018/8/31
 * @modify：
 */
@Service
@Slf4j
public class VerifyCodeService {

    private static final int MAX_PHONE_TIMES = 6;
    private static final Long SMS_CODE_EXPIRE = 10 * 60L;
    private static final Long PHONE_TIMES_KEY_EXPIRE = 1 * 60 * 60L;
    public static final String SMS_CODE_CONTENT = "您的短信验证码为 %s,有效期为10分钟.";
    @Autowired
    private RedisTemplateWarpper redisTemplateWarpper;

    /**
     * @author: qulibin
     * @description: 生成短信验证码
     * @date: 下午5:16 2018/9/3
     * @return:
     */
    public Resp generateSmsCode(String phone) {

        StringBuilder sb = new StringBuilder("生成短信验证码:");
        sb.append(phone).append("|");
        /**
         * 校验验证码次数
         */
        String phoneTimesKey = VerifyCodeUtils.getPhoneTimes(phone);
        String phoneTimes = redisTemplateWarpper.vGet(phoneTimesKey);

        if (StringUtils.isNotBlank(phoneTimes)) {
            int phoneTimesInt = Integer.parseInt(phoneTimes);
            if (phoneTimesInt > MAX_PHONE_TIMES) {
                return Resp.error("获取验证码次数过多,请1小时之后再试");
            }
        }

        /**
         * 60秒之内校验
         */
        String phoneExpireKey = VerifyCodeUtils.getPhoneExpire(phone);
        if (redisTemplateWarpper.kExists(phoneExpireKey)) {
            return Resp.error("获取验证码过于频繁,请稍后再试");
        } else {
            redisTemplateWarpper.vSet(phoneExpireKey, "0", 60L, TimeUnit.SECONDS);
        }

        /**
         * 生成验证码
         */
        String phoneCodeKey = VerifyCodeUtils.getPhoneCodeKey(phone);
        String phoneSmsCode = redisTemplateWarpper.vGet(phoneCodeKey);

        if (StringUtils.isBlank(phoneSmsCode)) {
            phoneSmsCode = VerifyCodeUtils.getSmsCode();
            redisTemplateWarpper.vSet(phoneCodeKey, phoneSmsCode, SMS_CODE_EXPIRE, TimeUnit.SECONDS);
            redisTemplateWarpper.vIncr(phoneTimesKey);
            redisTemplateWarpper.kExpire(phoneTimesKey, PHONE_TIMES_KEY_EXPIRE, TimeUnit.SECONDS);
        }
        //发短信
        //final String send = SmsTools.send(phone, String.format(SMS_CODE_CONTENT, phoneSmsCode));
        if (EnvHelper.isTest()) {
            sb.append("phoneSmsCode:").append(phoneSmsCode);
        }
        //sb.append(send);
        log.info(sb.toString());
        return Resp.success();
    }

    private String getSmsCode(String phone) {
        String phoneCodeKey = VerifyCodeUtils.getPhoneCodeKey(phone);
        String phoneSmsCode = redisTemplateWarpper.vGet(phoneCodeKey);
        return StringUtils.isBlank(phoneSmsCode) ? "" : phoneSmsCode;
    }

    /**
     * @author: qulibin
     * @description: 校验验证码
     * @date: 9:25 PM 2020/3/5
     * @return:
     */
    public boolean checkSmsCode(String phone, String smsCode) {
        return getSmsCode(phone).equals(smsCode);
    }
}
