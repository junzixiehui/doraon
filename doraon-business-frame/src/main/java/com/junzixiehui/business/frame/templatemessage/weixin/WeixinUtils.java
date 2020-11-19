package com.junzixiehui.business.frame.templatemessage.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junzixiehui.doraon.business.redis.RedisUtil;
import com.junzixiehui.doraon.business.util.EnvHelper;
import com.junzixiehui.doraon.util.mapper.FastJson;
import com.junzixiehui.doraon.util.protocal.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 微信常用接口
 */
@Slf4j
public class WeixinUtils {

	public static final String WX_MINI_APPID = ""/*ApplicationContext.configContext.getString("wx_mini_appid")*/;
	public static final String WX_MINI_SECRET = ""/*ApplicationContext.configContext.getString("wx_mini_secret")*/;
	public static final String WEIXIN_MINI_PROGRAM_ACCESS_TOKEN_KEY = "WEIXIN_MINI_PROGRAM_ACCESS_TOKEN_KEY";

	public static String getWxMiniAccessToken() {
		String token = "";
		if (EnvHelper.isPro()) {
			token = String.valueOf(RedisUtil.hget(WEIXIN_MINI_PROGRAM_ACCESS_TOKEN_KEY, "accessToken"));
			log.debug("缓存获取微信线上基础token {}", token);
		} else {
			try {
				final String result = OkHttpUtil
						.get("http://weixin.xxx.cn/v1/api/weixin/miniProgramAccessToken", null);
				if (StringUtils.isNotBlank(result)) {
					final Map map = FastJson.jsonStr2Object(result, Map.class);
					final Integer code = MapUtils.getInteger(map, "code", -1);
					if (code == 0) {
						final Map<String, Object> dataMap = (Map<String, Object>) MapUtils.getObject(map, "data", null);
						if (MapUtils.isNotEmpty(dataMap)) {
							token = MapUtils.getString(dataMap, "access_token", "");
						}
					}
				}
			} catch (Exception e) {
				log.error("miniProgramAccessToken error", e);
			}

			if (StringUtils.isBlank(token)) {
				token = String.valueOf(RedisUtil.hget(WEIXIN_MINI_PROGRAM_ACCESS_TOKEN_KEY, "accessToken"));
			}
		}
		log.info("获取微信小程序基础token {}", token);
		if (StringUtils.isBlank(token)) {
			token = generateWxMiniAccessToken();
			log.info("去刷新微信token:{}", token);
		}
		return token;
	}

	/**
	 * @author: jxll
	 * @description: 生成微信小程序 access_token
	 * @date: 18:03 2019/10/10
	 * @return:
	 */
	public static String generateWxMiniAccessToken() {
		String getUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + WX_MINI_APPID
				+ "&secret=" + WX_MINI_SECRET;
		String res = OkHttpUtil.get(getUrl, null);
		if (res.contains("access_token")) {
			JSONObject object = JSON.parseObject(res);
			String token = (String) object.get("access_token");

			final boolean accessToken = RedisUtil.hset(WEIXIN_MINI_PROGRAM_ACCESS_TOKEN_KEY, "accessToken", token);
			RedisUtil.expire(WEIXIN_MINI_PROGRAM_ACCESS_TOKEN_KEY, 7100);
			log.info("微信小程序基础token suc {} redis hset {}", res, accessToken);
			return token;
		}
		return "";
	}
}
