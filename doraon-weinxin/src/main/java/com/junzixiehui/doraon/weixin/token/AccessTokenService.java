package com.junzixiehui.doraon.weixin.token;

import com.google.common.collect.Maps;
import com.junzixiehui.doraon.business.cache.CacheService;
import com.junzixiehui.doraon.util.exception.ServiceException;
import com.junzixiehui.doraon.util.mapper.FastJson;
import com.junzixiehui.doraon.util.protocal.OkHttpUtil;
import com.junzixiehui.doraon.weixin.common.WinXinConstant;
import com.junzixiehui.doraon.weixin.token.resp.BaseAccessTokenResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

import static com.junzixiehui.doraon.weixin.common.WinXinConstant.ACCESS_TOKEN_NO_OPENID;

@Slf4j
@Service
public class AccessTokenService {

	@Value("${weixin.appId}")
	private String appId;
	@Value("${weixin.appSecret}")
	private String appSecret;
	@Resource
	private CacheService<String, Object> cacheService;

	/**
	 * 获取BaseAccessToken的方法
	 *
	 * @return 微信返回的AccessToken
	 * @throws
	 */
	public BaseAccessTokenResp getBaseAccessToken() {
		// 从缓存中获取AccessToken
		BaseAccessTokenResp bsBaseAccessTokenResp = (BaseAccessTokenResp) cacheService.getValue(ACCESS_TOKEN_NO_OPENID);
		log.info("[缓存中的基础AccessToken] {}" + bsBaseAccessTokenResp);
		if (bsBaseAccessTokenResp != null) {
			return bsBaseAccessTokenResp;
		}
		String urlGetAccessToken = WinXinConstant.WINXIN_HOST + "/cgi-bin/token";
		Map<String, String> paramMap = Maps.newHashMapWithExpectedSize(3);
		paramMap.put("grant_type", "client_credential");
		paramMap.put("appid", appId);
		paramMap.put("secret", appSecret);
		// 向微信服务器索取accessToken
		final String accessTokenJson = OkHttpUtil.get(urlGetAccessToken, paramMap);
		// 将AccessToken转换成对象
		BaseAccessTokenResp baseAccessTokenResp = FastJson.jsonStr2Object(accessTokenJson, BaseAccessTokenResp.class);
		if (baseAccessTokenResp.isSuccess()) {
			// 成功响应 将AccessToken存入缓存中
			cacheService
					.cacheValue(ACCESS_TOKEN_NO_OPENID, bsBaseAccessTokenResp, bsBaseAccessTokenResp.getExpiresIn());
			return baseAccessTokenResp;
		}
		log.error("[缓存中的基础AccessToken]失败" + baseAccessTokenResp.getErrmsg());
		final Integer errcode = baseAccessTokenResp.getErrcode();
		throw new ServiceException(String.valueOf(errcode), baseAccessTokenResp.getErrmsg());
	}
}