package com.junzixiehui.business.frame.templatemessage;


import com.google.common.base.Splitter;
import com.junzixiehui.business.frame.templatemessage.dto.PhoneFormDto;
import com.junzixiehui.doraon.util.constant.SymbolConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>Description: 访问支付宝 </p>
 *
 * @author: by qulibin
 * @date: 2018/10/30  2:57 PM
 * @version: 1.0
 */
@Service
public class FormIdService {

    private static final int expireTime = 6 * 86400;//60 * 60 * 24;
    private static final String SOURCE_WEIXIN = "weixin";
    private static final String SOURCE_ALIPAY = "alipay";
    private Logger log = LoggerFactory.getLogger(FormIdService.class);

    public void setFormId(String phone, String formId, String userId, String messageType) {
        if ("trade".equals(messageType)) {
            final PhoneFormDto phoneFormDto = getFormId(phone, "form");
            if (phoneFormDto == null) {
                return;
            }
            final String key = generateKey(phone, messageType, "");
            final String val = phoneFormDto.getUserId() + SymbolConstant.BAR + formId;
            //Redis.cmd().setex(key, expireTime, val);
            log.info("setFormId trade,key:{},val:{}", key, val);
        } else if ("form".equals(messageType)) {
            final String key = generateKey(phone, messageType, "");
            final String val = userId + SymbolConstant.BAR + formId;
            //Redis.cmd().setex(key, expireTime, val);
            log.info("setFormId form,key:{},val:{}", key, val);
        } else {
            log.error("setFormId messageType is null");
        }
    }

    public PhoneFormDto getFormId(String phone, String messageType) {
        final String key = generateKey(phone, messageType, "");
        final String value =  "";//Redis.cmd().get(key);
        log.info("getFormId,key:{},val:{}", key, value);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        final List<String> formIdList = Splitter.on(SymbolConstant.BAR).trimResults().splitToList(value);

        PhoneFormDto phoneFormDto = new PhoneFormDto();
        phoneFormDto.setPhone(phone);
        phoneFormDto.setUserId(formIdList.get(0));
        phoneFormDto.setFormId(formIdList.get(1));
        phoneFormDto.setMessageType(messageType);
        return phoneFormDto;
    }

    private String generateKey(String phone, String messageType, String source) {
        return "templateMsg:" + phone + messageType + source;
    }

    //微信小程序订阅消息，不再用formid,这里去查下openid
    public PhoneFormDto getFormIdWeiXin(String phone, String messageType) {
        String wxMiniOpenId = ""/*entity.getWxMiniOpenId()*/;
        log.info("获取phone:{},openid:{}", phone, wxMiniOpenId);
        PhoneFormDto phoneFormDto = new PhoneFormDto();
        phoneFormDto.setPhone(phone);
        phoneFormDto.setFormId("");
        phoneFormDto.setUserId(wxMiniOpenId);
        phoneFormDto.setMessageType(SOURCE_WEIXIN);
        return phoneFormDto;
    }
}
