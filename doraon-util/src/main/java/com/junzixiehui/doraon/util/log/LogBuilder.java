package com.junzixiehui.doraon.util.log;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Description: 业务日志统一</p>
 * @author: by jxll
 * @date: 2019/5/29  18:05
 * @version: 1.0
 */
public class LogBuilder {

	private final String startDesc;
	private StringBuilder sbLog = new StringBuilder();
	private long startTime;
	private Object busiCode;

	private LogBuilder(String startDesc) {
		startTime = System.currentTimeMillis();
		this.startDesc = Preconditions.checkNotNull(startDesc);
	}

	public static LogBuilder start(String startDesc) {
		return new LogBuilder(startDesc);
	}

	/**
	 * 业务单号
	 * @param busiCode
	 * @return
	 */
	public final LogBuilder busiCode(Object busiCode) {
		this.busiCode = busiCode;
		return this;
	}

	/**
	 * 自定义
	 */
	public final LogBuilder append(Object append) {
		if (append != null && StringUtils.isNotBlank(String.valueOf(append))) {
			sbLog.append(append);
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Log Start[");
		sb.append(startDesc);
		String traceId = "";
		if (StringUtils.isNotBlank(traceId)) {
			sb.append("TraceId:[").append(traceId).append("]");
		}
		sb = busiCode != null ? sb.append("|业务号:[").append(busiCode).append("]") : sb;

		sb = StringUtils.isNotBlank(sbLog.toString()) ? sb.append("操作记录:").append(sbLog.toString()) : sb;
		sb.append("|耗时:").append(System.currentTimeMillis() - startTime);
		sb.append("]Log End");
		return sb.toString();
	}
}
