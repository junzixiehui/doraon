package com.junzixiehui.doraon.weixin.qrcode.service;

import com.junzixiehui.doraon.weixin.qrcode.req.QRCodeWxReq;
import com.junzixiehui.doraon.weixin.qrcode.resp.TicketResp;

public interface IQRCodeService {
    /**
     * 创建整型永久的二维码Ticket
     *
     * @param qrCodeWxRequest 微信二维码请求实体
     * @return Ticket
     */
	TicketResp createIntPermanentTicket(QRCodeWxReq qrCodeWxRequest);

    /**
     * 获取整型临时二维码Ticket
     *
     * @param qrCodeWxRequest 微信二维码请求实体
     * @return Ticket
     */
	TicketResp createIntTempTicket(QRCodeWxReq qrCodeWxRequest);

    /**
     * 创建字符串形式永久的二维码Ticket
     *
     * @param qrCodeWxRequest 微信二维码请求实体
     * @return Ticket
     */
	TicketResp createStrPermanentTicket(QRCodeWxReq qrCodeWxRequest);

    /**
     * 创建字符串形式的临时二维码Ticket
     *
     * @param qrCodeWxRequest 微信二维码请求实体
     * @return Ticket
     */
	TicketResp createStrTempTicket(QRCodeWxReq qrCodeWxRequest);
}
