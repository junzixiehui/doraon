package com.junzixiehui.doraon.example.states;


public enum OrderEventEnum {

	/**
	 *
	 */
    ASSIGN_DRIVER(1, "分配司机");

    private int code;
    private String desc;

    OrderEventEnum(int code, String desc) {
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
		OrderEventEnum[] values = OrderEventEnum.values();
		for (OrderEventEnum enums : values) {
			if (enums.getCode() == code) {
				return enums.getDesc();
			}
		}
		return "";
	}

	public static OrderEventEnum getByCode(int code){
        OrderEventEnum[] values = OrderEventEnum.values();
        for (OrderEventEnum enums : values) {
            if (enums.getCode() == code) {
                return enums;
            }
        }
        return null;
    }
}
