package com.junzixiehui.doraon.example.controller;

import com.junzixiehui.doraon.util.api.Resp;
import io.swagger.annotations.Api;
import org.dromara.soul.client.springmvc.annotation.SoulSpringMvcClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/10/23  11:18
 * @version: 1.0
 */
@Api(tags = {"测试"})
@RestController
@RequestMapping(value = "/api/soul")
@SoulSpringMvcClient(path = "/api/soul/**")
public class SoulController {

	@GetMapping("/test")
	public Resp test(HttpServletResponse response) throws Exception {
		return Resp.success("SoulController");
	}
}
