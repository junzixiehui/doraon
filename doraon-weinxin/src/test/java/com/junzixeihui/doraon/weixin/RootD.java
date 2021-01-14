package com.junzixeihui.doraon.weixin;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;

import java.io.ByteArrayInputStream;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/9  18:38
 * @version: 1.0
 */
public class RootD {

	@Test
	public void test() throws Exception{



		SAXReader saxReader = new SAXReader();

		// 解析到一个Document中
		Document document = saxReader.read(RootD.class.getResourceAsStream("/file.xml"));
		// 获取xml中根节点
		Element rootElement = document.getRootElement();

		System.out.println(rootElement.getText());
		System.out.println(rootElement.getData());
		System.out.println(rootElement.getTextTrim());
		System.out.println(rootElement.getName());

	}
}
