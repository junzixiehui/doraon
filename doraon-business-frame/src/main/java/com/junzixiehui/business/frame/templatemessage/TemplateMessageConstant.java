package com.junzixiehui.business.frame.templatemessage;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2019/9/23  14:16
 * @version: 1.0
 */
public class TemplateMessageConstant {

	public static final String MESSAGE_TYPE_FORM = "form";
	public static final String MESSAGE_TYPE_TRADE = "trade";
	public static final String MESSAGE_TYPE_OPENID = "openid";
	public static final String STATUS_CANCEL_PAY_COMPLETE = "cancel_pay_complete";
	public static final String STATUS_PAY_COMPLETE = "pay_complete";
	public static final String STATUS_EVALUATION_ORDER = "evaluation_order";
	public static final String NO_INPUT_LOCATION = "未输入";

	public static final String TPIS_DIEVER_DRIVING = "司机已就位，将免费等候您至%s，超时将收取等候费（%s元/%s分钟)";
	public static final String TPIS_DIEVER_DRIVING_B = "司机已到达您的出发地，超时将收取等候费";
	public static final String TPIS_ORDER_FAIL = "很抱歉暂时未能为您找到司机，您可点击重新下单>>";
	public static final String TPIS_DRIVER_CANCLE_ORDER = "很遗憾本次司机未能为您服务，您可点击重新下单>>";
	public static final String TPIS_REPORT_ORDER = "感谢您使用e代驾，请您与司机确认订单支付方式并完成支付>>";
    public static final String TPIS_REPORT_ORDER_WEIXIN = "感谢您使用e代驾，请尽快完成支付";
    public static final String TPIS_ORDER_PAY_COMPLETE = "评价反馈您的代驾体验将帮助我们更好提升服务。点击评价>>";
	public static final String CANCEL_PAY_COMPLETE = "订单已取消，很遗憾司机师傅未能为您服务，您可点击重新下单>>";
    public static final String WX_PAY_COMPLETE_TIP = "评价反馈您的代驾体验，如有疑问请联系客服";
    public static final String WX_CANCEL_PAY_COMPLETE_TIP = "如有疑问请联系客服";

}
