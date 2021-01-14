package com.junzixiehui.doraon.example.rule;

import com.junzixiehui.doraon.rule.annotation.*;

/**
 * <p>Description: </p>
 * @author: by jxll
 * @date: 2020/10/19  20:15
 * @version: 1.0
 */
@Rule(name = "被3整除", description = "number如果被3整除，打印：number is three")
public class ThreeRule {

	@Condition //条件判断注解：如果return true， 执行Action
	public boolean isThree(@Fact("number") int number) {
		return number % 3 == 0;
	}


	@Action //执行方法注解
	public void threeAction(@Fact("number") int number) {
		//System.out.print("被3整除:" + number);
	}

	@Priority //优先级注解：return 数值越小，优先级越高
	public int getPriority() {
		return 1;
	}
}
