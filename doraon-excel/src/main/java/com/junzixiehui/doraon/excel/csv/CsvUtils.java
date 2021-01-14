package com.junzixiehui.doraon.excel.csv;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: Csv工具</p>
 * @author: by jxll
 * @date: 2018/9/5  下午6:57
 * @version: 1.0
 */
@Slf4j
public class CsvUtils {

	/**
	 * 导出csv文件
	 *
	 * @param headers    内容标题
	 *                   注意：headers类型是LinkedHashMap，保证遍历输出顺序和添加顺序一致。
	 *                   而HashMap的话不保证添加数据的顺序和遍历出来的数据顺序一致，这样就出现
	 *                   数据的标题不搭的情况的
	 * @param exportData 要导出的数据集合
	 * @return
	 */
	public static byte[] exportCSV(LinkedHashMap<String, String> headers, List<LinkedHashMap<String, Object>> exportData) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedWriter buffCvsWriter = null;

		try {
			// 编码gb2312，处理excel打开csv的时候会出现的标题中文乱码
			buffCvsWriter = new BufferedWriter(new OutputStreamWriter(baos, "gb2312"));
			// 写入cvs文件的头部
			Map.Entry propertyEntry = null;
			for (Iterator<Map.Entry<String, String>> propertyIterator = headers.entrySet().iterator(); propertyIterator.hasNext(); ) {
				propertyEntry = propertyIterator.next();
				String val = String.valueOf(propertyEntry.getValue());
				buffCvsWriter.write("\"" + val + "\"");
				if (propertyIterator.hasNext()) {
					buffCvsWriter.write(",");
				}
			}
			buffCvsWriter.newLine();
			// 写入文件内容
			LinkedHashMap row = null;
			for (Iterator<LinkedHashMap<String, Object>> iterator = exportData.iterator(); iterator.hasNext(); ) {
				row = iterator.next();
				for (Iterator<Map.Entry> propertyIterator = row.entrySet().iterator(); propertyIterator.hasNext(); ) {
					propertyEntry = propertyIterator.next();
					String val = String.valueOf(propertyEntry.getValue());
					buffCvsWriter.write("\"" + val + "\"");
					if (propertyIterator.hasNext()) {
						buffCvsWriter.write(",");
					}
				}
				if (iterator.hasNext()) {
					buffCvsWriter.newLine();
				}
			}
			// 记得刷新缓冲区，不然数可能会不全的，当然close的话也会flush的，不加也没问题
			buffCvsWriter.flush();
		} catch (IOException e) {
			log.error("exportCSV IOException",e);
		} finally {
			// 释放资源
			if (buffCvsWriter != null) {
				try {
					buffCvsWriter.close();
				} catch (IOException e) {
					log.error("exportCSV IOException error",e);
				}
			}
		}
		return baos.toByteArray();
	}
}
