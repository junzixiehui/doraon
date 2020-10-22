package com.junzixiehui.doraon.example.rule;

import com.junzixiehui.doraon.rule.api.Facts;
import com.junzixiehui.doraon.rule.api.Rules;
import com.junzixiehui.doraon.rule.api.RulesEngine;
import com.junzixiehui.doraon.rule.api.RulesEngineParameters;
import com.junzixiehui.doraon.rule.core.DefaultRulesEngine;


/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/10/19  20:13
 * @version: 1.0
 */
public class RuleTest {

	public static void main(String[] args) {
		/**
		 * 创建规则执行引擎
		 * 注意: skipOnFirstAppliedRule意思是，只要匹配到第一条规则就跳过后面规则匹配
		 */
		RulesEngineParameters parameters = new RulesEngineParameters();
		//parameters.setSkipOnFirstAppliedRule(true);
		parameters.setSkipOnFirstFailedRule(true);
		parameters.setSkipOnFirstNonTriggeredRule(true);
		RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
		//创建规则
		Rules rules = new Rules();

		rules.register(new ThreeRule());
		rules.register(new EightRule());

		rules.register(new OrRuleUnitGroup(new SixRule(), new TenRule()));
		rules.register(new OtherRule());
		//rules.register(new OtherRule());
		Facts facts = new Facts();
		for (int i = 24; i <= 24; i++) {
			//规则因素，对应的name，要和规则里面的@Fact 一致
			facts.put("number", i);

			StringBuilder sb = new StringBuilder("订单显示规则:");
			OrderDisplayContext orderDisplayContext = new OrderDisplayContext();
			orderDisplayContext.setCombinationOrderNo("222");
			orderDisplayContext.setCombinationMethod("ss");
			orderDisplayContext.setOrderNo("22");
			orderDisplayContext.setDriverId("126");

			facts.put("orderDisplayContext", orderDisplayContext);
			//执行规则
			rulesEngine.fire(rules, facts);
			System.out.println();
		}
	}
}
