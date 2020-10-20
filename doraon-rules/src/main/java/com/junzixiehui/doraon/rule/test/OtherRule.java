package com.junzixiehui.doraon.rule.test;

import org.jeasy.rules.annotation.*;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/10/20  10:33
 * @version: 1.0
 */
@Rule(name = "OtherRule", description = "")
public class OtherRule {

	@Condition
	public boolean isOther(@Fact("orderDisplayContext") OrderDisplayContext orderDisplayContext) {
		System.out.println(orderDisplayContext);
		return true;
	}

	@Action
	public void execute(@Fact("number") int number){
		System.out.println("执行:" + number);
	}

	@Priority
	public int getPriority() {
		return 99;
	}
}
