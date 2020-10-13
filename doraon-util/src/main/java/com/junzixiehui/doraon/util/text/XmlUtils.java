package com.junzixiehui.doraon.util.text;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Description:
 * </p>
 * 
 * @date: 2018/11/27 4:24 PM
 * @version: 1.0
 */
@Slf4j
public class XmlUtils {

    public static final String XML_HEAD = "<?xml version=\"1.0\" encoding=\"%s\"?>";

    public static String head(String encoding) {
        return String.format(XML_HEAD, encoding);
    }

    public static String appendRoot(String rootNodeName, String body, String encoding) {
        String xmlStr = "";
        if (StringUtils.isBlank(rootNodeName)) {
            return xmlStr;
        }
        StringBuilder sb = new StringBuilder(head(encoding));
        sb.append("<").append(rootNodeName).append(">");
        sb.append(body);
        sb.append("</").append(rootNodeName).append(">");
        xmlStr = sb.toString();
        return xmlStr;
    }

    public static String mapToXml(String rootNodeName, Map<String, Object> map) {
        String xmlStr = "";
        if (StringUtils.isBlank(rootNodeName) || MapUtils.isEmpty(map)) {
            return xmlStr;
        }
        StringBuilder sb = new StringBuilder("");
        sb.append("<").append(rootNodeName).append(">");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">");
            sb.append(entry.getValue());
            sb.append("</").append(entry.getKey()).append(">");
        }
        sb.append("</").append(rootNodeName).append(">");
        xmlStr = sb.toString();
        return xmlStr;
    }

    public static String mapToXml(Map<String, Object> map) {
        String xmlStr = "";
        if (MapUtils.isEmpty(map)) {
            return xmlStr;
        }
        StringBuilder sb = new StringBuilder("");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append("<").append(entry.getKey()).append(">");
            sb.append(entry.getValue());
            sb.append("</").append(entry.getKey()).append(">");
        }
        xmlStr = sb.toString();
        return xmlStr;
    }

    public static String formatXml(String xmlStr, String encoding) {
    	if (StringUtils.isBlank(xmlStr)){
    		return "";
		}
        if (StringUtils.isBlank(encoding)) {
            encoding = "UTF-8";
        }
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlStr);
            // 格式化输出格式
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding(encoding);
            StringWriter writer = new StringWriter();
            // 格式化输出流
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            // 将document写入到输出流
            xmlWriter.write(document);
            xmlWriter.close();
            return writer.toString();
        } catch (Exception e) {
            log.error("formatXml ", e);
        }
        return xmlStr;
    }

    /**
     * @author: qulibin
     * @description: xml转map xml含有集合
     * @date: 5:49 PM 2018/11/28
     * @return:
     */
    public static Map<String, Object> xml2Map(String xmlStr) throws DocumentException {
        final JSONObject jsonObject = xml2Json(xmlStr);
        return jsonObject.getInnerMap();
    }

    public static JSONObject xml2Json(String xmlStr) throws DocumentException {
        Document doc = DocumentHelper.parseText(xmlStr);
        JSONObject json = new JSONObject();
        dom4j2Json(doc.getRootElement(), json);
        return json;
    }

    public static void dom4j2Json(Element element, JSONObject json) {
        for (Object o : element.attributes()) {
            Attribute attr = (Attribute)o;
            if (!StringUtils.isBlank(attr.getValue())) {
                json.put("@" + attr.getName(), attr.getValue());
            }
        }
        List<Element> chdEl = element.elements();

        if (chdEl.isEmpty() && !StringUtils.isBlank(element.getText())) {
            json.put(element.getName(), element.getText());
        }
        for (Element e : chdEl) {
            if (!e.elements().isEmpty()) {
                JSONObject chdjson = new JSONObject();
                dom4j2Json(e, chdjson);
                Object o = json.get(e.getName());
                if (o != null) {
                    JSONArray jsona = null;
                    if (o instanceof JSONObject) {
                        JSONObject jsono = (JSONObject)o;
                        json.remove(e.getName());
                        jsona = new JSONArray();
                        jsona.add(jsono);
                        jsona.add(chdjson);
                    }
                    if (o instanceof JSONArray) {
                        jsona = (JSONArray)o;
                        jsona.add(chdjson);
                    }
                    json.put(e.getName(), jsona);
                } else {
                    if (!chdjson.isEmpty()) {
                        json.put(e.getName(), chdjson);
                    }
                }
            } else {
                for (Object o : element.attributes()) {
                    Attribute attr = (Attribute)o;
                    if (!org.apache.commons.lang3.StringUtils.isBlank(attr.getValue())) {
                        json.put("@" + attr.getName(), attr.getValue());
                    }
                }
                if (!e.getText().isEmpty()) {
                    json.put(e.getName(), e.getText());
                }
            }
        }
    }
}
