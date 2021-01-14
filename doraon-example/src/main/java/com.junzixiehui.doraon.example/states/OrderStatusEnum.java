package com.junzixiehui.doraon.example.states;



/**
 * @author: jxll
 * @description: 活动状态
 * @date: 16:51 2020/10/20
 * @return:
 */
public enum OrderStatusEnum {

	/**
	 *
	 */
    TO_ASSIGN(1, "待调度"),
    TO_START(2, "待发车"),
    TRANSPORTING(3, "运输中"),
    ARRIVED(4, "已收车"),
    AUDIT(5, "已完结"),
    CANCELED(6, "已取消");

    private int code;
    private String desc;

    OrderStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

	public static String getNameByCode(int code) {
		OrderStatusEnum[] values = OrderStatusEnum.values();
		for (OrderStatusEnum enums : values) {
			if (enums.getCode() == code) {
				return enums.getDesc();
			}
		}
		return "";
	}

	public static OrderStatusEnum getByCode(int code){
        OrderStatusEnum[] values = OrderStatusEnum.values();
        for (OrderStatusEnum enums : values) {
            if (enums.getCode() == code) {
                return enums;
            }
        }
        return null;
    }
}
