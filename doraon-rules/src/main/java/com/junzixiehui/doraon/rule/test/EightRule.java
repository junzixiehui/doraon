package com.junzixiehui.doraon.rule.test;

import org.jeasy.rules.annotation.*;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/10/19  20:15
 * @version: 1.0
 */
@Rule(name = "被8整除", description = "number如果被8整除，打印：number is three")
public class EightRule {

	@Condition //条件判断注解：如果return true， 执行Action
	public boolean isThree(@Fact("number") int number) {
		return number % 8 == 0;
	}

	@Action //执行方法注解
	public void threeAction(@Fact("number") int number) {
		//System.out.print("被3整除:" + number);
	}

	@Priority //优先级注解：return 数值越小，优先级越高
	public int getPriority() {
		return 2;
	}
}