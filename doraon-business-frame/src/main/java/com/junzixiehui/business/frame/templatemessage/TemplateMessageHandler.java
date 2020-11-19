package com.junzixiehui.business.frame.templatemessage;

import com.junzixiehui.business.frame.templatemessage.dto.TemplateMessageReq;
import com.junzixiehui.doraon.util.api.Resp;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2019/9/20  15:07
 * @version: 1.0
 */
public interface TemplateMessageHandler {

	Resp sendTemplateMessage(TemplateMessageReq templateMessageReq);
}
