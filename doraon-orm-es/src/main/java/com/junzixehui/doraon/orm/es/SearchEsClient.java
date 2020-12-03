package com.junzixehui.doraon.orm.es;

import com.google.common.collect.Maps;
import com.junzixehui.doraon.orm.es.util.ToEs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class SearchEsClient {

	private static final String TYPE = "user_model";
	private static final Map<String, RestHighLevelClient> CLIENT_MAP = Maps.newHashMap();

	public static synchronized RestHighLevelClient getClient() {
		String serverAddressList = "127.0.0.1:9200,127.0.0.1:9200";
		RestHighLevelClient client = CLIENT_MAP.get(serverAddressList);
		if (client == null) {
			try {
				String[] serverAddrArr = serverAddressList.split(",");
				HttpHost[] httpHost = new HttpHost[serverAddrArr.length];

				for (int i = 0; i < serverAddrArr.length; ++i) {
					String[] ipAndPort = serverAddrArr[i].split(":");
					httpHost[i] = new HttpHost(ipAndPort[0], Integer.parseInt(ipAndPort[1]), "http");
				}

				RestClientBuilder builder = RestClient.builder(httpHost);
				client = new RestHighLevelClient(builder);
				CLIENT_MAP.put(serverAddressList, client);
			} catch (Exception var6) {
				throw new RuntimeException("无法生产Client[" + serverAddressList + "]", var6);
			}
		}

		return client;
	}

	public static long getTargetCount(String sql, String index) throws Exception {

		log.info("=========getTargetCount sql:" + sql + "|index:" + index);
		BoolQueryBuilder boolQueryBuilder = ToEs.convert(sql);
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(new String[]{index});
		searchRequest.types(new String[]{TYPE});
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(boolQueryBuilder).size(0);
		searchRequest.source(searchSourceBuilder);

		long count = 0L;
		try {
			log.info("=========getTargetCount request:" + searchRequest.toString());
			SearchResponse searchResponse = getClient().search(searchRequest, RequestOptions.DEFAULT);
			log.info("=========countBySql response:" + searchResponse.toString());

			count = searchResponse.getHits().getTotalHits();
		} catch (IOException e) {
			log.error("getTargetCount", e);
		}
		return count;
	}

	public static long getTargetData(String sql, String tableName, String index) throws Exception {
		log.info("=========getTargetData sql:" + sql + "|index:" + index + "|:tableName:" + tableName);
		int pageSize = 5000;
		long count = 0;

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		BoolQueryBuilder boolQueryBuilder = ToEs.convert(sql);
		searchSourceBuilder.query(boolQueryBuilder).size(pageSize).fetchSource("phone", null);
		// 初始化scroll
		// 设定滚动时间间隔
		// 这个时间并不需要长到可以处理所有的数据，仅仅需要足够长来处理前一批次的结果。每个 scroll 请求（包含 scroll 参数）设置了一个新的失效时间。
		final Scroll scroll = new Scroll(TimeValue.timeValueSeconds(10L));
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(new String[]{index});
		searchRequest.types(new String[]{TYPE});
		searchRequest.scroll(scroll);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse;
		try {
			searchResponse = getClient().search(searchRequest, RequestOptions.DEFAULT);
		} catch (Exception e) {
			log.error("es search", e);
			return 0;
		}

		String scrollId = searchResponse.getScrollId();
		final SearchHits searchHits = searchResponse.getHits();
		SearchHit[] hits = searchHits.getHits();
		long totalHits = searchHits.getTotalHits();
		if (totalHits == 0) {
			return 0;
		}
		count = count + executeInsertSql(tableName, hits);

		while (true) {

			SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
			scrollRequest.scroll(scroll);
			try {
				searchResponse = getClient().scroll(scrollRequest, RequestOptions.DEFAULT);
			} catch (IOException e) {
				log.error("es search scroll", e);
				break;
			}

			scrollId = searchResponse.getScrollId();
			hits = searchResponse.getHits().getHits();
			if (hits == null || hits.length == 0) {
				break;
			}
			count = count + executeInsertSql(tableName, hits);
		}

		ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
		clearScrollRequest.addScrollId(scrollId);
		ClearScrollResponse clearScrollResponse = null;
		try {
			clearScrollResponse = getClient().clearScroll(clearScrollRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.error("clear-scroll-error:", e);
		}
		if (clearScrollResponse != null) {
			boolean succeeded = clearScrollResponse.isSucceeded();
			log.info("clear-scroll-succeeded:" + succeeded);
		}

		return count;
	}

	private static long executeInsertSql(String tableName, SearchHit[] hits) {
		if (hits == null || hits.length == 0) {
			return 0L;
		}

		long n = 0L;
		StringBuilder buffer = new StringBuilder();
		buffer.append("insert into ").append(tableName).append(" (phone) values ");
		for (SearchHit hit : hits) {
			final Map<String, Object> sourceAsMap = hit.getSourceAsMap();
			String phone = String.valueOf(sourceAsMap.get("phone"));
			if (StringUtils.isNotBlank(phone)) {
				buffer.append("('").append(phone).append("'),");
				n++;
			}
		}
		String insert = buffer.substring(0, buffer.length() - 1);
		log.info("getTargetData insert sql -> {}", insert);
		return n;
	}
}

