package com.junzixiehui.doraon.util.sql;

import java.util.List;

public final class SqlUtil {

    public static String buildInSql(List<String> strList) {

		StringBuilder sql = new StringBuilder("(");
		strList.forEach(productNo -> {
			sql.append("'").append(productNo).append("'").append(",");
		});

		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		return sql.toString();
    }
}
