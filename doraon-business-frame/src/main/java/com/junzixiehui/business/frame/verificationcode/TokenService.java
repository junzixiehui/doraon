package com.junzixiehui.business.frame.verificationcode;


import com.junzixiehui.doraon.business.redis.RedisTemplateWarpper;
import com.junzixiehui.doraon.util.api.ErrorCode;
import com.junzixiehui.doraon.util.constant.SymbolConstant;
import com.junzixiehui.doraon.util.exception.ServiceException;
import com.junzixiehui.doraon.util.security.DigestUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.security.provider.MD5;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author: jxll
 * @description: token服务
 * @date: 上午11:18 2018/8/31
 * @modify：
 */
@Service
@Slf4j
public class TokenService {

    @Autowired
    private RedisTemplateWarpper redisTemplateWarpper;

    /**
     * @param phone          手机号
     * @param clientUniqueId 业务客户端唯一标识
     * @author: jxll
     * @description: 生成token
     * @date: 下午5:22 2018/8/31
     * @return:
     */
    public String generateToken(String phone, String clientUniqueId) {
        /**
         * clear old token
         */
		clearToken(phone);
		/**
		 * set new token
		 */
		final String key = getPhoneKey(phone);
        String token = getToken(phone, clientUniqueId);
        try {
            redisTemplateWarpper.vSet(key, token, 24, TimeUnit.HOURS);
            redisTemplateWarpper.vSet(token, phone + SymbolConstant.UNDERLINE + clientUniqueId, 24, TimeUnit.HOURS);
        } catch (Exception e) {
            log.error("redis vSet error ", e);
            throw new ServiceException( "生成token出错");
        }
        return token;
    }

    private String getPhoneKey(String phone) {
        return "token:" + phone;
    }

    public void clearToken(String phone) {
        try {
			final String token = getTokenByPhone(phone);
			if (StringUtils.isNotBlank(token)){
				redisTemplateWarpper.kDelete(token);
			}
        } catch (Exception e) {
            log.error("redis clearToken kDelete error ", e);
            throw new ServiceException("清除token出错");
        }
    }

    public String getPhoneByToken(String token) {
        String tokenVal = redisTemplateWarpper.vGet(token);
        if (StringUtils.isBlank(tokenVal)) {
            return "";
        }
        int i = tokenVal.indexOf(SymbolConstant.UNDERLINE);
        return tokenVal.substring(0, i);
    }

    public String getTokenByPhone(String phone) {
		final String key = getPhoneKey(phone);
		return redisTemplateWarpper.vGet(key);
    }

    private static String getToken(String phone, String un) {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return DigestUtil.md5Hex(phone + uuidStr + un);
    }
}
