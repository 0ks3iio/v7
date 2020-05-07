package net.zdsoft.bigdata.frame.data.demo.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

/**
 * ES测试
 * 
 */
public class EsAggDemo {

	private Logger logger = Logger.getLogger(EsAggDemo.class);

	public final static String HOST = "192.168.0.202";

	public final static int PORT = 9300;// http请求的端口是9200，客户端是9300

	private final static String CLUSTER_NAME = "zdsoft-es";

	protected TransportClient client;

	/**
	 * 获取一个客户端
	 */
	@org.junit.Before
	public void getClient() {

		Settings settings = Settings.builder()
				.put("cluster.name", CLUSTER_NAME)
				.put("client.transport.ignore_cluster_name", false)
				.put("client.transport.sniff", true).build();

		try {
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress
							.getByName(HOST), PORT));
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		logger.debug("Elasticsearch connect info:" + client.toString());
	}

	/**
	 * 关闭连接
	 */
	@org.junit.After
	public void close() {
		client.close();
	}

	@Test
	public void EsAggregation1() {
		getClient();

		// 时间范围的设定
		RangeQueryBuilder rangequerybuilder = QueryBuilders
				.rangeQuery("operation_date").from("2018-09-15")
				.to("2018-09-25");

		AggregationBuilder teamAgg = AggregationBuilders.terms("os_name")
				.field("os_name");
		AggregationBuilder posAgg = AggregationBuilders.terms("browser_name")
				.field("browser_name");

		// teamAgg.subAggregation(posAgg);

		SearchResponse response = client.prepareSearch("es_login_log")
				.setTypes("login_log").setQuery(rangequerybuilder)
				.addAggregation(posAgg).addAggregation(teamAgg).execute().actionGet();

		StringTerms os = response.getAggregations().get("os_name");

		Iterator<Bucket> osBucketIt = os.getBuckets().iterator();
		while (osBucketIt.hasNext()) {
			Bucket osBucket = osBucketIt.next();
			System.out.println(osBucket.getKey() + "系统"
					+ osBucket.getDocCount() + "个记录。");
		}
		
		StringTerms browser = response.getAggregations().get("browser_name");

		Iterator<Bucket> browserBucketIt = browser.getBuckets().iterator();
		while (browserBucketIt.hasNext()) {
			Bucket browserBucket = browserBucketIt.next();
			System.out.println(browserBucket.getKey() + "系统"
					+ browserBucket.getDocCount() + "个记录。");
		}

	}

	@Test
	public void EsAggregation() {
		getClient();

		// 时间范围的设定
		RangeQueryBuilder rangequerybuilder = QueryBuilders
				.rangeQuery("operation_date").from("2018-09-15")
				.to("2018-09-25");

		AggregationBuilder dateAgg = AggregationBuilders.dateHistogram("agg")
				.field("operation_date")
				.dateHistogramInterval(DateHistogramInterval.DAY)
				.format("yyyy-MM-dd");

		AggregationBuilder teamAgg = AggregationBuilders.terms("os_name")
				.field("os_name");
		AggregationBuilder posAgg = AggregationBuilders.terms("browser_name")
				.field("browser_name");

		// teamAgg.subAggregation(posAgg);
		dateAgg.subAggregation(teamAgg);
		dateAgg.subAggregation(posAgg);
		SearchResponse response = client.prepareSearch("es_login_log")
				.setTypes("login_log").setQuery(rangequerybuilder)
				.addAggregation(dateAgg).execute().actionGet();

		Map<String, Aggregation> aggMap = response.getAggregations()
				.asMap();
		InternalDateHistogram idh = (InternalDateHistogram) aggMap
				.get("dateAgg");
		List<org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket> buckets = idh
				.getBuckets();
		for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket b : buckets) {
			System.out.println(b.getKeyAsString() + "时间有" + b.getDocCount() + "个记录。");
			Aggregations sub = b.getAggregations();
			StringTerms osNameTerms = sub.get("os_name");

			Iterator<Bucket> osBucketIt = osNameTerms.getBuckets().iterator();
			while (osBucketIt.hasNext()) {
				Bucket osBucket = osBucketIt.next();
				System.out.println(osBucket.getKey() + "系统"
						+ osBucket.getDocCount() + "个记录。");
			}

			StringTerms broswserTerms = sub.get("browser_name");

			Iterator<Bucket> broswserTermtt = broswserTerms.getBuckets()
					.iterator();
			while (broswserTermtt.hasNext()) {
				Bucket browserBucket = broswserTermtt.next();
				System.out.println(browserBucket.getKey() + "浏览器"
						+ browserBucket.getDocCount() + "个记录。");
			}
			System.out.println();
		}
	}

}
