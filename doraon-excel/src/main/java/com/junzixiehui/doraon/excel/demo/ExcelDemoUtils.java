package com.junzixiehui.doraon.excel.demo;

import com.alibaba.excel.EasyExcel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/4/30  5:59 PM
 * @version: 1.0
 */
public class ExcelDemoUtils {

	public static void main(String[] args) throws Exception{
		MultipartFile file = null;
		final List<DemoDTO> DemoDTOList = EasyExcel.read(file.getInputStream()).autoTrim(true).head(DemoDTO.class).sheet()
				.doReadSync();
	}

}
