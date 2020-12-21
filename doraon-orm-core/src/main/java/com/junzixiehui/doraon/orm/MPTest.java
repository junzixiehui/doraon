package com.junzixiehui.doraon.orm;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.segments.NormalSegmentList;

import java.util.Iterator;
import java.util.Map;

/**
 * <p>Description: </p>
 * @author: by qulibin
 * @date: 2020/12/15  19:19
 * @version: 1.0
 */
public class MPTest {

	public static void main(String[] args) {
		LambdaQueryWrapper v1 = new LambdaQueryWrapper();
		v1.eq("name","姓名");
		v1.in("phone","123");
		final MergeSegments expression = v1.getExpression();

		final NormalSegmentList normal = expression.getNormal();

		final Iterator<ISqlSegment> iterator = normal.iterator();

		while (iterator.hasNext()){
			final ISqlSegment iSqlSegment = iterator.next();

			final String sqlSegment = iSqlSegment.getSqlSegment();
			System.out.println(sqlSegment);
		}
	}
}
