package com.junzixiehui.doraon.example.states;

import com.junzixiehui.doraon.statemachine.Action;
import com.junzixiehui.doraon.statemachine.Condition;
import com.junzixiehui.doraon.statemachine.StateMachine;
import com.junzixiehui.doraon.statemachine.builder.StateMachineBuilder;
import com.junzixiehui.doraon.statemachine.builder.StateMachineBuilderFactory;
import org.junit.Assert;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/10/20  16:54
 * @version: 1.0
 */
public class OrderStatusService {

	private static final String MACHINE_ID = "ORDER_STATES";

	public static void main(String[] args) {

		OrderStatusService orderStatusService = new OrderStatusService();

		orderStatusService.testExternalNormal();
	}

	public void testExternalNormal() {
		StateMachineBuilder<OrderStatusEnum, OrderEventEnum, Object> builder = StateMachineBuilderFactory.create();

		//@formatter:off
		builder.externalTransition()
				.from(OrderStatusEnum.TO_ASSIGN)
				.to(OrderStatusEnum.TO_START)
				.on(OrderEventEnum.ASSIGN_DRIVER)
				.when(checkCondition())
				.perform(doAction());
		//@formatter:on

		StateMachine<OrderStatusEnum, OrderEventEnum, Object> stateMachine = builder.build(MACHINE_ID);

		DispatchOrderDTO dispatchOrderDTO = DispatchOrderDTO.builder().orderNo("OR001").driverId("123").build();

		OrderStatusEnum target = stateMachine.fireEvent(OrderStatusEnum.TO_ASSIGN, OrderEventEnum.ASSIGN_DRIVER, dispatchOrderDTO);
		Assert.assertEquals(OrderStatusEnum.TO_START, target);
	}


	/**
	 * @author: qulibin
	 * @description: 校验订单 校验司机
	 * @date: 17:10 2020/10/20
	 * @return:
	 */
	private Condition<Object> checkCondition() {
		return (ctx) -> {

			System.out.println(ctx);
			return true;
		};
	}

	/**
	 * @author: qulibin
	 * @description: 修改订单状态 记录等
	 * @date: 17:30 2020/10/20
	 * @return:
	 */
	private Action<OrderStatusEnum, OrderEventEnum, Object> doAction() {
		return (from, to, event, ctx) -> {
			System.out.println(ctx + " from:" + from + " to:" + to + " on:" + event);
		};
	}
}
