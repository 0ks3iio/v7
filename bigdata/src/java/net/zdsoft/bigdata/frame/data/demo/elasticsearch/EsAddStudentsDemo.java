package net.zdsoft.bigdata.frame.data.demo.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

/**
 * ES测试
 * 
 */
public class EsAddStudentsDemo {

	private Logger logger = Logger.getLogger(EsAddStudentsDemo.class);

	public final static String HOST = "10.255.238.16";

	public final static int PORT = 9300;// http请求的端口是9200，客户端是9300

	private final static String CLUSTER_NAME = "my-ELK";

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
				.field("index", true)
				.field("fielddata", true)
				.endObject()
				// 
				.startObject("student_name")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				// 
				.startObject("section")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				// 
				.startObject("age")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("sex")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("interest")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("region").field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("school_type").field("type", "keyword")
				.field("store", false).field("index", true)
				.endObject()
				//
				.startObject("family_background").field("type", "keyword")
				.field("store", false).field("index", true).endObject()
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
				.field("index", true)
				.field("fielddata", true)
				.endObject()
				// 
				.startObject("teacher_name")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				// 
				.startObject("subject")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				// 
				.startObject("age")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("sex")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("title")
				.field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("region").field("type", "keyword")
				.field("store", false)
				.field("index", true)
				.endObject()
				//
				.startObject("school_type").field("type", "keyword")
				.field("store", false).field("index", true)
				.endObject()
				//
				.startObject("education").field("type", "keyword")
				.field("store", false).field("index", true).endObject()
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
			if (isIndexExists("es-teacher-tag")) {
				logger.error("索引对象已经存在，无法创建！");
				return;
			}

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
			if (!isIndexExists("es-teacher-tag")) {
				logger.info("索引对象已经不存在，无法删除！");
				return;
			}
			DeleteIndexRequestBuilder builder = client.admin().indices()
					.prepareDelete("es-teacher-tag");
			DeleteIndexResponse res = builder.get();
			logger.info(res.isAcknowledged() ? "删除索引成功！" : "删除索引失败！");
		} catch (Exception e) {
			logger.error("删除索引失败！", e);
		}
	}

	@Test
	public void saveData() {
		try {
			String INDEX = "es-student-tag";
			String TYPE = "student-tag";

			Map<String, Object> source = null;
			for (int i = 1; i < 3000000; i++) {
				source = new HashMap<String, Object>();
				source.put("id", UuidUtils.generateUuid());
				source.put("student_name", "姓名"+i);
				source.put("section", getSection(getRandom(1, 3)));
				source.put("age", getAge(getRandom(1, 13)));
				source.put("sex", getSex(getRandom(1, 2)));
				source.put("interest", getInterests(getRandom(1, 5)));
				source.put("region", getRegion(getRandom(1, 31)));
				source.put("school_type", getSchoolType(getRandom(1, 4)));
				source.put("family_background", getFamily(getRandom(1, 10)));
				client.prepareIndex(INDEX, TYPE).setId("" + i)
						.setSource(source).get();
			}
		} catch (Exception e) {
			logger.error("保存数据失败！", e);
		}
	}
	
	
	
	private String getSex(int index){
		Map<Integer,String> sexMap=new HashMap<Integer, String>();
		sexMap.put(1, "男");
		sexMap.put(2, "女");
		return sexMap.get(index);
	}
	
	private String getSection(int index){
		Map<Integer,String> sexMap=new HashMap<Integer, String>();
		sexMap.put(1, "小学");
		sexMap.put(2, "初中");
		sexMap.put(3, "高中");
		return sexMap.get(index);
	}
	
	private String getAge(int index){
		Map<Integer,String> sexMap=new HashMap<Integer, String>();
		sexMap.put(1, "6岁以下");
		sexMap.put(2, "7");
		sexMap.put(3, "8");
		sexMap.put(4, "9");
		sexMap.put(5, "10");
		sexMap.put(6, "11");
		sexMap.put(7, "12");
		sexMap.put(8, "13");
		sexMap.put(9, "14");
		sexMap.put(10, "15");
		sexMap.put(11, "16");
		sexMap.put(12, "17");
		sexMap.put(13, "18岁以上");
		return sexMap.get(index);
	}
	
	private String getRegion(int index){
		Map<Integer,String> regionMap=new HashMap<Integer, String>();
		regionMap.put(1, "浙江市");
		regionMap.put(2, "上海市");
		regionMap.put(3, "江苏省");
		regionMap.put(4, "山东省");
		regionMap.put(5, "安徽省");
		regionMap.put(6, "北京市");
		regionMap.put(7, "天津市");
		regionMap.put(8, "重庆市");
		regionMap.put(9, "辽宁市");
		regionMap.put(10, "吉林省");
		regionMap.put(11, "陕西省");
		regionMap.put(12, "河北省");
		regionMap.put(13, "广东省");
		regionMap.put(14, "广西省");
		regionMap.put(15, "河南省");
		regionMap.put(16, "黑龙江省");
		regionMap.put(17, "吉林省");
		regionMap.put(18, "甘肃省");
		regionMap.put(19, "江西省");
		regionMap.put(20, "青海省");
		regionMap.put(21, "云南省");
		regionMap.put(22, "贵州省");
		regionMap.put(23, "新疆维吾尔自治区");
		regionMap.put(24, "广西壮族自治区");
		regionMap.put(25, "湖南省");
		regionMap.put(26, "湖北省");
		regionMap.put(27, "海南省");
		regionMap.put(28, "宁夏回族自治区");
		regionMap.put(29, "山西省");
		regionMap.put(30, "河北省");
		regionMap.put(31, "内蒙古自治区");
		return regionMap.get(index);
	}
	
	private String getInterests(int index){
		Map<Integer,String> interestsMap=new HashMap<Integer, String>();
		interestsMap.put(1, "音乐");
		interestsMap.put(2, "读书");
		interestsMap.put(3, "运动");
		interestsMap.put(4, "画画");
		interestsMap.put(5, "篮球");
		return interestsMap.get(index);
	}
	
	private String getFamily(int index){
		Map<Integer,String> familyMap=new HashMap<Integer, String>();
		familyMap.put(1, "教师");
		familyMap.put(2, "公务员");
		familyMap.put(3, "医生");
		familyMap.put(4, "律师");
		familyMap.put(5, "工人");
		familyMap.put(6, "私营业务");
		familyMap.put(7, "销售");
		familyMap.put(8, "IT工程师");
		familyMap.put(9, "农民");
		familyMap.put(10, "军人");
		return familyMap.get(index);
	}
	
	private String getSchoolType(int index){
		Map<Integer,String> schoolTypeMap=new HashMap<Integer, String>();
		schoolTypeMap.put(1, "民办");
		schoolTypeMap.put(2, "公办");
		schoolTypeMap.put(3, "私立");
		schoolTypeMap.put(4, "国际学校");
		return schoolTypeMap.get(index);
	}
	
	private int getRandom(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
public static void main(String[] args) {
	System.out.println(BooleanUtils.toBoolean("true"));
}
}
