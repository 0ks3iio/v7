package net.zdsoft.bigdata.frame.data.demo.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.zdsoft.framework.entity.Json;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequest;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

/**
 * ES测试
 * 
 */
public class EsClientDemo {

	private Logger logger = Logger.getLogger(EsClientDemo.class);

	public final static String HOST = "172.16.10.113";

	public final static int PORT = 9300;// http请求的端口是9200，客户端是9300

	private final static String CLUSTER_NAME = "zdsoft-es-test";

	protected TransportClient client;

	/**
	 * 获取一个客户端
	 */
	@org.junit.Before
	public void getClient() {

		Settings settings = Settings.builder()
				.put("cluster.name", CLUSTER_NAME)
				.put("client.transport.ignore_cluster_name", false)
				.put("client.transport.sniff", false).build();

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

	/**
	 * 判断是否存在该索引
	 * 
	 * @param indexName
	 *            索引名称
	 * @return
	 */
	public boolean isIndexExists(String indexName) {
		IndicesExistsRequestBuilder builder = client.admin().indices()
				.prepareExists(indexName);
		IndicesExistsResponse res = builder.get();
		return res.isExists();
	}

	/**
	 * 把string字段设置为了过时字段，引入text，keyword字段 keyword：存储数据时候，不会分词建立索引
	 * text：存储数据时候，会自动分词，并生成索引（这是很智能的，但在有些字段里面是没用的，所以对于有些字段使用text则浪费了空间）。
	 *
	 * 如果在添加分词器的字段上，把type设置为keyword，则创建索引会失败
	 */
	public XContentBuilder getIndexSource() throws IOException {
		XContentBuilder source = XContentFactory.jsonBuilder()
				.startObject()
				.startObject("properties")
				.startObject("code")
				.field("type", "text")
				.field("index", true)
				.field("fielddata", true)
				.endObject()
				// 名称字段
				.startObject("name")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				// 信息字段
				.startObject("info").field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				// 主要内容字段
				.startObject("content").field("type", "text")
				.field("store", true).field("index", true)
				.field("analyzer", "ik_max_word").endObject()
				.startObject("date").field("type", "date") // 设置Date类型
				.field("format", "yyyy-MM-dd HH:mm:ss") // 设置Date的格式.
				.endObject().endObject().endObject();
		return source;
	}

	public XContentBuilder getStudentSource() throws IOException {

		XContentBuilder source = XContentFactory
				.jsonBuilder()
				.startObject()
				.startObject("properties")
				// id
				.startObject("id")
				.field("type", "text")
				.field("store", true)
				.field("fielddata", true)
				.endObject()
				//
				.startObject("unit_id")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("unit_name")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("student_name")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				.endObject().endObject();
		return source;
	}

	public XContentBuilder getTeacherSource() throws IOException {

		XContentBuilder source = XContentFactory
				.jsonBuilder()
				.startObject()
				.startObject("properties")
				// id
				.startObject("id")
				.field("type", "text")
				.field("store", true)
				.field("fielddata", true)
				.endObject()
				//
				.startObject("unit_id")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				//
				.startObject("unit_name")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("teacher_name")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("dept_name")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("sex_name")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("nation")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("age")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("sex")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("title")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("native_place")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("location")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("education")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("background").field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("work_time").field("type", "keyword")
				.field("store", true).endObject()
				//
				.startObject("country").field("type", "keyword")
				.field("store", true).endObject().endObject().endObject();
		return source;
	}


	public XContentBuilder getStudentDemoSource() throws IOException {

		XContentBuilder source = XContentFactory
				.jsonBuilder()
				.startObject()
				.startObject("properties")
				// id
				.startObject("id")
				.field("type", "text")
				.field("store", true)
				.field("fielddata", true)
				.endObject()
				//
				.startObject("name")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("age")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("sex")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.startObject("identitycard")
				.field("type", "keyword")
				.field("store", true)
				.endObject()
				//
				.endObject().endObject();
		return source;
	}

	/**
	 * 创建索引
	 */
	@Test
	public void createIndex() {
		try {
			getClient();
			if (isIndexExists("es-student-demo-1")) {
				logger.info("索引对象已经存在，无法创建！");
				return;
			}

			CreateIndexRequestBuilder builder = client.admin().indices()
					.prepareCreate("es-student-demo-1");
			// 直接创建Map结构的setting
			Map<String, Object> settings = new HashMap<>();
			settings.put("number_of_shards", 10); // 分片数
			settings.put("number_of_replicas", 0); // 副本数
			settings.put("refresh_interval", "10s"); // 刷新间隔
			builder.setSettings(settings);

			builder.addMapping("student_demo-1", getStudentDemoSource());
			CreateIndexResponse res = builder.get();
			logger.info(res.isAcknowledged() ? "索引创建成功！" : "索引创建失败！");
		} catch (Exception e) {
			logger.error("创建索引失败！", e);
		}
	}

	/**
	 * 删除索引
	 */
	@Test
	public void deleteIndex() {
		try {
			if (!isIndexExists("es-log")) {
				logger.info("索引对象已经不存在，无法删除！");
				return;
			}
			DeleteIndexRequestBuilder builder = client.admin().indices()
					.prepareDelete("es-log");
			DeleteIndexResponse res = builder.get();
			logger.info(res.isAcknowledged() ? "删除索引成功！" : "删除索引失败！");
		} catch (Exception e) {
			logger.error("删除索引失败！", e);
		}
	}

	@Test
	public void deleteDataByKey() throws Exception {
		// getClient();
		// // 这里可以忽略，组装一个我业务逻辑的ID
		// String prefix = "%sx_x%s";
		// String id = String.format(prefix, key, key);

		// DeleteResponse result
		// =client.prepareDelete().setIndex(MappingManager.INDEX)
		// .setType(MappingManager.B_TYPE)
		// .setId(id)//设置ID
		// .setRefresh(true)//刷新
		// .execute().actionGet();
		// //是否查找并删除
		// boolean isfound = result.isFound();
		// return isfound?1:0;
	}

	/**
	 * 使用bulk processor
	 * 
	 * @throws Exception
	 */
	@Test
	public void testBulkProcessor() throws Exception {
		// 创建BulkPorcessor对象
		BulkProcessor bulkProcessor = BulkProcessor
				.builder(client, new Listener() {
					public void beforeBulk(long paramLong,
							BulkRequest paramBulkRequest) {
						// TODO Auto-generated method stub
					}

					// 执行出错时执行
					public void afterBulk(long paramLong,
							BulkRequest paramBulkRequest,
							Throwable paramThrowable) {
						// TODO Auto-generated method stub
					}

					public void afterBulk(long paramLong,
							BulkRequest paramBulkRequest,
							BulkResponse paramBulkResponse) {
						// TODO Auto-generated method stub
					}
				})
				// 1w次请求执行一次bulk
				.setBulkActions(10000)
				// 1gb的数据刷新一次bulk
				.setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				// 固定5s必须刷新一次
				.setFlushInterval(TimeValue.timeValueSeconds(5))
				// 并发请求数量, 0不并发, 1并发允许执行
				.setConcurrentRequests(1)
				// 设置退避, 100ms后执行, 最大请求3次
				.setBackoffPolicy(
						BackoffPolicy.exponentialBackoff(
								TimeValue.timeValueMillis(100), 3)).build();

		// 添加单次请求
		// bulkProcessor.add(new IndexRequest("twitter", "tweet", "1"));
		// bulkProcessor.add(new DeleteRequest("twitter", "tweet", "2"));

		// 关闭
		bulkProcessor.awaitClose(10, TimeUnit.MINUTES);
		// 或者
		bulkProcessor.close();
	}

	@Test
	public void query() throws UnknownHostException {
		getClient();
		SearchRequestBuilder responsebuilder = client.prepareSearch("stusys")
				.setTypes("student");

		SearchResponse myresponse = null;

		QueryBuilder queryBuilder = null;
		// 基本查询
		queryBuilder = QueryBuilders.matchPhraseQuery("name", "科技");
		// 多条件查询
		queryBuilder = QueryBuilders.termsQuery("name", "科技", "age", "16");

		// 查询All
		queryBuilder = QueryBuilders.matchAllQuery();

		// 常用词查询
		queryBuilder = QueryBuilders.commonTermsQuery("name", "lishici");
		// 前缀查询
		queryBuilder = QueryBuilders.prefixQuery("name", "科技");
		// 通配符查询
		queryBuilder = QueryBuilders.wildcardQuery("name", "科技?频");

		// 多条件查询
		queryBuilder = QueryBuilders.multiMatchQuery("lishi", "subcat", "name");
		// 正则表达式查询
		// queryBuilder =QueryBuilders.regexpQuery(field, regexp)
		// rang查询
		queryBuilder = QueryBuilders.rangeQuery("age").gt(16).lt(18);

		myresponse = responsebuilder.setQuery(queryBuilder).setFrom(0)
				.setSize(10).setExplain(true).execute().actionGet();

		SearchHits hits = myresponse.getHits();
		for (int i = 0; i < hits.getHits().length; i++) {
			System.out.println(hits.getHits()[i].getSourceAsString());
		}

	}

	@Test
	public void ikTest() throws UnknownHostException {

		getClient();
		// ik_max_word ik_smart
		AnalyzeRequest analyzeRequest = new AnalyzeRequest("stusys").text(
				"中华人民共和国国歌").analyzer("ik_smart");

		List<AnalyzeResponse.AnalyzeToken> tokens = client.admin().indices()
				.analyze(analyzeRequest).actionGet().getTokens();

		for (AnalyzeResponse.AnalyzeToken token : tokens) {
			System.out.println(token.getTerm());
		}
	}

	@Test
	public void page() throws UnknownHostException {
		getClient();
		String INDEX = "stusys";
		String TYPE = "student";
		Date begin = new Date();
		System.out.println("scroll 模式启动！");
		begin = new Date();
		SearchResponse scrollResponse = client.prepareSearch(INDEX)
				.setTypes(TYPE).setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
				.addSort("_doc", SortOrder.ASC).setSize(100)
				.setScroll(TimeValue.timeValueMinutes(1)).execute().actionGet();
		long count = scrollResponse.getHits().getTotalHits();
		int runTimes = 1;
		while (true) {
			try {
				// 第一次查询，只返回数量和一个scrollId
				// 注意第一次运行没有结果
				for (SearchHit hit : scrollResponse.getHits().getHits()) {
					String name = (String) hit.getSourceAsMap().get("name");
					System.out.println("总量" + count + " 已经查到" + name);
				}
				// 使用上次的scrollId继续访问
				// 初始搜索请求和每个后续滚动请求返回一个新的滚动ID,只有最近的滚动ID才能被使用
				scrollResponse = client
						.prepareSearchScroll(scrollResponse.getScrollId())
						.setScroll(new TimeValue(10000)).execute().actionGet();

				if (scrollResponse.getHits().getHits().length == 0) {
					break;
				}
				runTimes++;
			} catch (Exception e) {

			}
		}
		System.out.println("------------运行" + runTimes + " 次数");
		Date end = new Date();
		System.out.println("耗时: " + (end.getTime() - begin.getTime()));
	}

	@Test
	public void searcher4IK() throws UnknownHostException {
		getClient();
		String indexId = "stusys";
		String type = "student";
		String key = "美丽大武汉";
		try {
			// 创建查询索引
			SearchRequestBuilder searchRequestBuilder = client
					.prepareSearch(indexId);

			// TODO
			// AnalyzeRequestBuilder analyzeBuilder =new AnalyzeRequestBuilder

			// 设置查询索引类型,setTypes("productType1", "productType2","productType3");
			// 用来设定在多个类型中搜索
			searchRequestBuilder.setTypes(type);
			// 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN
			// = 扫描查询,无序
			searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
			// 设置查询关键词
			// searchRequestBuilder
			// .setQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("title",
			// key))
			// .should(QueryBuilders.termQuery("content", key)));
			// key="\""+"key"+"\"";

			QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(
					key);
			// ik_max_word ik_smart
			// queryBuilder.analyzer("ik_max_word");
			queryBuilder.field("content");

			// QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
			searchRequestBuilder.setQuery(queryBuilder);

			// 分页应用
			searchRequestBuilder.setFrom(0).setSize(3000);//

			// 设置是否按查询匹配度排序
			searchRequestBuilder.setExplain(true);
			// 按照字段排序
			searchRequestBuilder.addSort("code", SortOrder.DESC);

			// 设置高亮显示
			String preTags = "<strong>";
			String postTags = "</strong>";
			HighlightBuilder highlightBuilder = new HighlightBuilder();
			highlightBuilder.preTags(preTags);// 设置前缀
			highlightBuilder.postTags(postTags);// 设置后缀
			highlightBuilder.field("content");// 设置高亮字段
			searchRequestBuilder.highlighter(highlightBuilder);// 设置高亮字段

			// 执行搜索,返回搜索响应信息
			SearchResponse response = searchRequestBuilder.execute()
					.actionGet();

			// 获取搜索的文档结果
			SearchHits searchHits = response.getHits();
			SearchHit[] hits = searchHits.getHits();
			System.out.println("-----------------------------------"
					+ hits.length);
			for (int i = 0; i < hits.length; i++) {
				Json result4print = new Json();
				SearchHit hit = hits[i];
				// 将文档中的每一个对象转换json串值
				// String json = hit.getSourceAsString();
				// 获取对应的高亮域
				Map<String, HighlightField> result = hit.getHighlightFields();
				// 从设定的高亮域中取得指定域
				HighlightField contentField = result.get("content");
				if (contentField != null) {
					// 取得定义的高亮标签
					Text[] contentTexts = contentField.fragments();
					// 为title串值增加自定义的高亮标签
					String content = "";
					for (Text text : contentTexts) {
						content += text;
					}
					// 将追加了高亮标签的串值重新填充到对应的对象
					result4print.put("content", content);
				}
				String name = (String) hit.getSourceAsMap().get("name");
				result4print.put("name", name);
				// 打印高亮标签追加完成后的实体对象
				System.out.println(result4print.toJSONString());
			}
			// 防止出现：远程主机强迫关闭了一个现有的连接
			// Thread.sleep(10000);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
