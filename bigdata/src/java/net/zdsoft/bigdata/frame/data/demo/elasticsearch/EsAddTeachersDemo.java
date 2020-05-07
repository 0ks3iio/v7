package net.zdsoft.bigdata.frame.data.demo.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.zdsoft.framework.utils.UuidUtils;

import org.apache.log4j.Logger;
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
public class EsAddTeachersDemo {

	private Logger logger = Logger.getLogger(EsClientDemo.class);

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

	@Test
	public void saveData() {
		try {
			String INDEX = "es-teacher-tag";
			String TYPE = "teacher-tag";

			Map<String, Object> source = null;
			for (int i = 1; i < 871288; i++) {
				source = new HashMap<String, Object>();
				source.put("id", UuidUtils.generateUuid());
				source.put("teacher_name", "教师"+i);
				source.put("subject", getSubject(getRandom(1, 10)));
				source.put("age", getAge(getRandom(1, 13)));
				source.put("sex", getSex(getRandom(1, 2)));
				source.put("title", getTitle(getRandom(1, 5)));
				source.put("region", getRegion(getRandom(1, 31)));
				source.put("school_type", getSchoolType(getRandom(1, 4)));
				source.put("education", getEducation(getRandom(1, 5)));
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
	
	private String getSubject(int index){
		Map<Integer,String> subjectMap=new HashMap<Integer, String>();
		subjectMap.put(1, "语文");
		subjectMap.put(2, "数学");
		subjectMap.put(3, "英语");
		subjectMap.put(4, "物理");
		subjectMap.put(5, "化学");
		subjectMap.put(6, "生物");
		subjectMap.put(7, "政治");
		subjectMap.put(8, "地理");
		subjectMap.put(9, "历史");
		subjectMap.put(10, "信息技术");
		return subjectMap.get(index);
	}
	
	private String getAge(int index){
		Map<Integer,String> sexMap=new HashMap<Integer, String>();
		sexMap.put(1, "25岁以下");
		sexMap.put(2, "25-30");
		sexMap.put(3, "31-35");
		sexMap.put(4, "36-40");
		sexMap.put(5, "41-45");
		sexMap.put(6, "46-50");
		sexMap.put(7, "51-55");
		sexMap.put(8, "55-60");
		sexMap.put(9, "60岁以上");
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
	
	private String getTitle(int index){
		Map<Integer,String> titleMap=new HashMap<Integer, String>();
		titleMap.put(1, "高级教师");
		titleMap.put(2, "一级教师");
		titleMap.put(3, "二级教师");
		titleMap.put(4, "三级教师");
		titleMap.put(5, "无");
		return titleMap.get(index);
	}
	
	private String getEducation(int index){
		Map<Integer,String> educationMap=new HashMap<Integer, String>();
		educationMap.put(1, "大专");
		educationMap.put(2, "本科");
		educationMap.put(3, "硕士");
		educationMap.put(4, "博士");
		educationMap.put(5, "其他");
		return educationMap.get(index);
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

}