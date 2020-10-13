package com.junzixiehui.doraon.util.number;

import java.math.BigDecimal;

/**
 * <p>Description: </p>
 * @author: by xxx
 * @date: 2020/10/13  17:04
 * @version: 1.0
 */
public class MoneyUtil {

	public static double changeF2Y(Object amount) {
		BigDecimal decimal = BigDecimal.valueOf(NumberUtil.toDouble(amount)).divide(new BigDecimal(100));
		return decimal.doubleValue();
	}

	public static String changeY2F(Object amount) {
		BigDecimal decimal = BigDecimal.valueOf(NumberUtil.toDouble(amount)).multiply(new BigDecimal(100));
		return NumberUtil.toString(decimal.intValue());
	}
}
