/**
 * @author: jxll
 * @description:
 *
 * 使用前注入:
 * @Configuration
 * public class EventBootConfig {
 *
 * 	@Bean(name = "bootstrap", initMethod = "init")
 * 	public Bootstrap bootstrap() {
 * 		Bootstrap bootstrap = new Bootstrap();
 * 		bootstrap.setPackages(Lists.newArrayList("com.xxx.*******"));
 * 		return bootstrap;
 * 	}
 * }
 *
 * @date: 19:35 2019/4/10
 * @modify：
 */
package com.junzixiehui.doraon.business.event.custom;