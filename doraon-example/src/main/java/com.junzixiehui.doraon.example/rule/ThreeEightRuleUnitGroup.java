package com.junzixiehui.doraon.example.rule;

import com.junzixiehui.doraon.rule.annotation.Action;
import com.junzixiehui.doraon.rule.annotation.Fact;
import com.junzixiehui.doraon.rule.annotation.Rule;
import com.junzixiehui.doraon.rule.support.composite.UnitRuleGroup;

/**
 * <p>Description:
 *
 * UnitRuleGroup: 只要有一个规则不满足，就不执行
 * </p>
 * @author: by jxll
 * @date: 2020/10/20  10:34
 * @version: 1.0
 */
@Rule(name = "被3和8同时整除",  description = "这是一个组合规则")
public class ThreeEightRuleUnitGroup extends UnitRuleGroup {

	public ThreeEightRuleUnitGroup(Object... rules) {
		for (Object rule : rules) {
			addRule(rule);
		}
	}

	@Action
	public void execute(@Fact("number") int number){
		System.out.println("被3和8同时整除:" + number);
	}

	@Override
	public int getPriority() {
		return 0;
	}
}
