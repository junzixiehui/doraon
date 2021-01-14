package com.junzixiehui.doraon.util.text;

import com.alibaba.fastjson.support.spring.FastjsonSockJsMessageCodec;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.junzixiehui.doraon.util.mapper.FastJson;
import org.junit.Test;

import java.util.Map;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/21  13:27
 * @version: 1.0
 */

public class XmlUtilsTest {


	String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><stream><action>DLUPRSUB</action><userName>qulibin</userName><clientID>123456</clientID><list name=\"userDataList\"><row><ID>1</ID><recAccountNo>222</recAccountNo><recAccountName>qulibin</recAccountName></row></list></stream>";


	@Test
	public void test(){


	}

	@Test
	public void xml2Map() {
	}

	@Test
	public void xml2Json() throws Exception{
		final String json = XmlAndJsonUtils.xmlToJson(xml);

		System.out.println(json);

	}

	@Test
	public void jsonToXml(){
		Map<String,Object> dataMap = Maps.newHashMapWithExpectedSize(2);
		dataMap.put("stream","sss");
		dataMap.put("list",Lists.newArrayList("1","2","3"));

		//final String jsonStr = FastJson.object2JsonStr(dataMap);


		String jsonStr = "{\"action\":\"DLUPRSUB\",\"userName\":\"qulibin\",\"clientID\":\"123456\",\"list\":{\"row\":{\"ID\":\"1\",\"recAccountNo\":\"222\",\"recAccountName\":\"qulibin\"},\"@name\":\"userDataList\"}}";

		System.out.println(jsonStr);
		final String xml = XmlAndJsonUtils.jsonToXml(jsonStr);

		System.out.println(xml);
	}

}
