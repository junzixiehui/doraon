package com.junzixiehui.doraon.example.rule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.support.ActivationRuleGroup;
import org.jeasy.rules.support.UnitRuleGroup;

/**
 * <p>Description:
 *
 * </p>
 * @author: by qulibin
 * @date: 2020/10/20  10:34
 * @version: 1.0
 */
@Rule(name = "或者",  description = "这是一个组合规则")
public class OrRuleUnitGroup extends ActivationRuleGroup {

	public OrRuleUnitGroup(Object... rules) {
		for (Object rule : rules) {
			addRule(rule);
		}
	}


	@Override
	public int getPriority() {
		return 3;
	}
}
