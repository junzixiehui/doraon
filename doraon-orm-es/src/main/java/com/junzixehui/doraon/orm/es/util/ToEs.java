package com.junzixehui.doraon.orm.es.util;

import com.junzixiehui.doraon.design.pattern.chain.BaseChain;
import com.junzixiehui.doraon.design.pattern.chain.Chain;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * select sql 转 es 的 bool query
 * 支持： =、!=、in、not in、>、 >=、 <、 <=
 * 暂不支持： between and
 * <p/>
 */
public class ToEs {

	private static Logger logger = LoggerFactory.getLogger(ToEs.class);
	private static Chain chain = new BaseChain();

	static {
		chain.addCommand(new In());
		chain.addCommand(new Le());
		chain.addCommand(new Ge());
		chain.addCommand(new Ne());
		chain.addCommand(new Eq());
		chain.addCommand(new Other());
	}

	private static BoolQueryBuilder select(String sql) throws Exception {
		Assert.hasLength(sql, "sql is wrong!");
		String[] analyse = sql.trim().split("\\s");
		logger.debug("analyse is " + analyse.toString());
		Assert.notEmpty(analyse, "{} is wrong!");
		Assert.isTrue(analyse[0].toLowerCase().equals("select"), "not is select sql");
		BoolQueryBuilder queryBuilder = boolQuery();
		// sql 解析上下文信息
		SqlContext sqlContext = new SqlContext();
		logger.debug("input sql->{}", sql);
		sql = new Format(sql).go();
		logger.debug("format sql->{}", sql);
		sqlContext.put(SqlContextConst.SQL, sql);
		sqlContext.put(SqlContextConst.WORLDS, analyse);
		sqlContext.put(SqlContextConst.TABLE, analyse[3]);
		sqlContext.put(SqlContextConst.POINTER, 4);
		sqlContext.put(SqlContextConst.CURRENT_WOLD, analyse[4]);
		sqlContext.put(SqlContextConst.BOOLQUERY, queryBuilder);
		logger.debug("table: {}", sqlContext.get(SqlContextConst.TABLE));
		// 解析
		while ((Integer) sqlContext.get(SqlContextConst.POINTER) < analyse.length) {
			chain.execute(sqlContext);
		}
		logger.debug("sql query->{}", sql);
		logger.info("es bool query->{}", queryBuilder.toString());
		return queryBuilder;
	}

	public static BoolQueryBuilder convert(String sql) throws Exception {
		return select(sql);
	}

	public static void main(String[] args) throws Exception {
		String sql = "SELECT count(1) from t_user_20171021 where 1=1  and avg_income_all >= 10 and avg_income_all <= 30";
		//        String sqlbetween = "SELECT count(1) from t_user_20160315 where 1=1  and avg_income_all >= 20 and avg_income_all <=30";
		//        String sql = "SELECT count(1) from t_user_20171021 where 1=1  and city_id in (168,169,148,119,249,228,261,236,275,304,92,229,177)";
		final BoolQueryBuilder convert = convert(sql);
		System.out.println(convert);
	}
}
