package com.junzixiehui.doraon.weixin.qrcode.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.junzixiehui.doraon.business.cache.CacheService;
import com.junzixiehui.doraon.util.protocal.OkHttpUtil;
import com.junzixiehui.doraon.weixin.Apis;
import com.junzixiehui.doraon.weixin.common.WinXinConstant;
import com.junzixiehui.doraon.weixin.qrcode.req.QRCodeWxReq;
import com.junzixiehui.doraon.weixin.qrcode.resp.TicketResp;
import com.junzixiehui.doraon.weixin.qrcode.service.IQRCodeService;
import com.junzixiehui.doraon.weixin.token.resp.BaseAccessTokenResp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.junzixiehui.doraon.weixin.common.WinXinConstant.ACCESS_TOKEN_NO_OPENID;

@Service
public class QRCodeServiceImpl implements IQRCodeService {

	@Resource
	private CacheService<String, BaseAccessTokenResp> cacheService;

	/**
	 * 创建整型永久的二维码Ticket
	 *
	 * @param qrCodeWxRequest 微信二维码请求实体
	 * @return Ticket
	 */
	@Override
	public TicketResp createIntPermanentTicket(QRCodeWxReq qrCodeWxRequest) {
		// QR_SCENE为临时的整型参数值
		qrCodeWxRequest.setAction_name(QRCodeWxReq.QRCodeTypeEnum.QR_LIMIT_SCENE);
		return createTicket(qrCodeWxRequest);
	}

	/**
	 * 获取整型临时二维码Ticket
	 *
	 * @param qrCodeWxRequest 微信二维码请求实体
	 * @return Ticket
	 */
	@Override
	public TicketResp createIntTempTicket(QRCodeWxReq qrCodeWxRequest) {
		// QR_SCENE为临时的整型参数值
		qrCodeWxRequest.setAction_name(QRCodeWxReq.QRCodeTypeEnum.QR_SCENE);
		return createTicket(qrCodeWxRequest);
	}

	/**
	 * 创建字符串形式永久的二维码Ticket
	 *
	 * @param qrCodeWxRequest 微信二维码请求实体
	 * @return Ticket
	 */
	@Override
	public TicketResp createStrPermanentTicket(QRCodeWxReq qrCodeWxRequest) {
		// QR_SCENE为临时的整型参数值
		qrCodeWxRequest.setAction_name(QRCodeWxReq.QRCodeTypeEnum.QR_LIMIT_STR_SCENE);
		return createTicket(qrCodeWxRequest);
	}

	/**
	 * 创建字符串形式的临时二维码Ticket
	 *
	 * @param qrCodeWxRequest 微信二维码请求实体
	 * @return Ticket
	 */
	@Override
	public TicketResp createStrTempTicket(QRCodeWxReq qrCodeWxRequest) {
		// QR_SCENE为临时的整型参数值
		qrCodeWxRequest.setAction_name(QRCodeWxReq.QRCodeTypeEnum.QR_STR_SCENE);
		return createTicket(qrCodeWxRequest);
	}

	/**
	 * 获取Ticket方法
	 *
	 * @param qrCodeWxRequest 微信二维码请求实体
	 * @return Ticket
	 */
	private TicketResp createTicket(QRCodeWxReq qrCodeWxRequest) {
		// 获取 BaseAccessTokenRequest
		BaseAccessTokenResp baseAccessTokenRequest = cacheService.getValue(ACCESS_TOKEN_NO_OPENID);
		// 拼接请求创建Ticket的URL
		String urlCreateTicket = WinXinConstant.WINXIN_HOST + Apis.CREATE_TICKET + "?access_token=" + baseAccessTokenRequest.getAccess_token();
		String qrCodeRequestBody = JSONObject.toJSONString(qrCodeWxRequest);
		String ticketResponseStr = OkHttpUtil.postJson(urlCreateTicket, qrCodeRequestBody);
		return JSONObject.parseObject(ticketResponseStr, TicketResp.class);
	}
}
