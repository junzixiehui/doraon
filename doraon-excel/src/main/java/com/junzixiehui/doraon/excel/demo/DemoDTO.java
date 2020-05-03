package com.junzixiehui.doraon.excel.demo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;

/**
 * @author: qulibin
 * @description:
 * @date: 11:14 PM 2020/3/31
 * @modify：
 */
@ApiModel
public class DemoDTO {

	@ExcelProperty(value = "运输作业单号" )
	private String yszydh;
	@ExcelProperty(value = "VIN" )
	private String vin;

}
