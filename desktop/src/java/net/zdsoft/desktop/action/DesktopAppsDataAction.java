package net.zdsoft.desktop.action;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.OperationLogRemoteService;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.desktop.constant.DeskTopConstant;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UuidUtils;

/**
 * @author shenke
 * @since 2017/2/16 8:52
 */
@RequestMapping("/desktop/app")
@Controller
public class DesktopAppsDataAction extends DeskTopBaseAction {

	protected static final Logger LOG = LoggerFactory.getLogger(DesktopAppsDataAction.class);
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private OperationLogRemoteService operationLogRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;

	@ControllerInfo(ignoreLog = 1)
	@RequestMapping("/showModuleUsage")
	@ResponseBody
	public String showModuleUsage(String userId, String unitId, ModelMap map, int showRows) {
		String textPrefix = "";
		List<String> userIds = new ArrayList<String>();
		if (StringUtils.isNotBlank(userId)) {
			userIds.add(userId);
			textPrefix = "个人";
		} else if (StringUtils.isNotBlank(unitId)) {
			List<User> users = User.dt(userRemoteService.findByUnitIds(unitId));
			userIds = EntityUtils.getList(users, User::getId);
			textPrefix = "本单位";
		}
		String text = genJsonTextForUsage(showRows, userIds.toArray(new String[0]));
		JSONObject json = JSONObject.parseObject(text);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonData", json);
		jsonObject.put("height", 300);
		jsonObject.put("title",
				textPrefix + "最常使用统计(" + DateFormatUtils.format(DateUtils.addMonths(new Date(), -6), "yyyy-MM-dd")
						+ " ~ " + DateFormatUtils.format(new Date(), "yyyy-MM-dd") + ")");
		return jsonObject.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1)
	@RequestMapping("/showTeacherStructure")
	@ResponseBody
	public String showTeacherStructure(String unitId, ModelMap map) {

		TeacherRemoteService teacherRemote = Evn.getBean("teacherRemoteService");
		DeptRemoteService deptRemote = Evn.getBean("deptRemoteService");

		List<Teacher> teachers = Teacher.dt(teacherRemote.findByUnitId(unitId)); 
		List<Dept> depts = SUtils.dt(deptRemote.findByParentId(SUtils.dc(deptRemote.findOneById(getLoginInfo().getDeptId()), Dept.class).getParentId()), Dept.class);
		SortUtils.ASC(depts, "deptName");
		List<String> deptNames = new ArrayList<>();
		deptNames.add("所有");
		deptNames.add("男");
		deptNames.add("女");
		deptNames.add("未知");
		Map<String, Integer> deptTeacherCount = new HashMap<>();
		
		int count = 0;
		
		
		for(Teacher t : teachers){
			String dep = t.getSex() + "";
			count ++;
			Integer teacherCount = deptTeacherCount.get(dep);
			if(teacherCount == null){
				teacherCount = 0;
			}
			deptTeacherCount.put(dep, ++teacherCount);
		}
		
		List<Integer> leftCount = new ArrayList<>();
		List<Integer> teacherCount = new ArrayList<>();
		leftCount.add(0);
		teacherCount.add(count);
		for(Integer d : new Integer[]{1,2,0}){
			Integer c = deptTeacherCount.get(d + "");
			if(c == null)
				c = 0;
			teacherCount.add(c);
			count -= c;
			leftCount.add(count);
		}

		String text = "{title:{text:'教师性别人数构成'},tooltip:{trigger:'axis',axisPointer:{type:'shadow'}},grid:{left:'3%',right:'4%',bottom:'3%',containLabel:true},"
				+ "xAxis:{type:'category',splitLine:{show:false}," + "data:" + JSONArray.toJSONString(deptNames) + "},"
				+ "yAxis:{type:'value'},series:[{name:'辅助',type:'bar',stack:'总量',itemStyle:{normal:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'},"
				+ "emphasis:{barBorderColor:'rgba(0,0,0,0)',color:'rgba(0,0,0,0)'}},data:" + JSONArray.toJSONString(leftCount) + "},"
				+ "{name:'人数',type:'bar',stack:'总量',label:{normal:{show:true,position:'inside'}},data:" + JSONArray.toJSONString(teacherCount) + "}]}";

		JSONObject json = JSONObject.parseObject(text);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("jsonData", json);
		jsonObject.put("height", 300);
		jsonObject.put("title", "本单位教师统计");
		return jsonObject.toJSONString();
	}

	private String genJsonTextForUsage(int showRows, String[] userIds) {
		JSONArray array = new JSONArray();
		JSONArray titles = new JSONArray();
		List<Object[]> urls = operationLogRemoteService.findUsageFunctions(userIds, showRows,
				DateUtils.addMonths(new Date(), -6));
		int min = 0;
		int max = 0;

		for (Object[] os : urls) {
			JSONObject json2 = new JSONObject();
			int value = NumberUtils.toInt("" + os[1]);
			if (min > value || min == 0) {
				min = value;
			}
			if (max == 0 || max < value) {
				max = value;
			}
			titles.add(os[0]);
			json2.put("name", os[0]);
			json2.put("value", value);
			array.add(json2);
		}

		JSONArray array2 = new JSONArray();
		List<Object[]> urls2 = operationLogRemoteService.findUsageServers(userIds, showRows,
				DateUtils.addMonths(new Date(), -6));
		int min2 = 0;
		int max2 = 0;
		for (Object[] os : urls2) {
			JSONObject json2 = new JSONObject();
			int value = NumberUtils.toInt("" + os[1]);
			if (min2 > value || min2 == 0) {
				min2 = value;
			}
			if (max2 == 0 || max2 < value) {
				max2 = value;
			}
			json2.put("name", os[0]);
			json2.put("value", value);
			array2.add(json2);
		}

		String text = "{tooltip: {},title: [{text: \"常用操作\",subtext: \"总计 " + urls.size()
				+ "\",x: \"25%\",textAlign: \"center\"}, {text: \"常用子系统\",subtext: \"总计 " + urls2.size()
				+ "\",x: \"80%\",textAlign: \"center\"}],grid: {top: 50,width: \"70%\",left: 10,containLabel: true},"
				+ "xAxis:{type: \"value\",max: " + (max + (max * 1.1 - max > 100 ? 100 : max * 0.1))
				+ ",splitLine: {show: false}},yAxis: {type: \"category\",data:" + titles.toJSONString()
				+ ",axisLabel: {interval: 0,rotate: 30}},"
				+ "series: [{itemStyle: {normal: {color:\"#104E8B\"}},type: \"bar\",stack: \"chart\",label: {normal: {position: \"right\",show: true}},"
				+ "data:" + array.toJSONString()
				+ "}, {type: \"pie\",radius: [0, \"40%\"],center: [\"80%\", \"50%\"],data:" + array2.toJSONString()
				+ "}]}";
		return text;
	}

	@ControllerInfo(ignoreLog = 1, value = "echarts复合图表展示")
	@ResponseBody
	@RequestMapping("/showComplex")
	public String showComplex(ModelMap map) {
		JSONObject chart1 = new JSONObject();
		chart1.put("type", DeskTopConstant.CHART_TYPE_LINE);
		JSONObject chart1Data = JSONObject.parseObject(showStudentNative2());
		chart1.put("col", 8);
		chart1.put("data", chart1Data);
		chart1.put("echartsDivId", UuidUtils.generateUuid());
		JSONObject chart2 = new JSONObject();
		chart2.put("type", DeskTopConstant.CHART_TYPE_RADAR);
		JSONObject chart2Data = JSONObject.parseObject(showStudentNative0());
		chart2.put("col", 4);
		chart2.put("data", chart2Data);
		chart2.put("echartsDivId", UuidUtils.generateUuid());

		List<JSONObject> datas = Lists.newArrayList(chart1, chart2);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("title", "复合图表");
		jsonObject.put("jsonData", datas);
		return jsonObject.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟报表数据")
	@RequestMapping("/showStudentNative4")
	public String showStudentNative4() {
		// 教师统计图--柱状图
		JSONObject jsonObject = new JSONObject();
		String[] xAxisDatas = null;
		Integer[][] loadingDataInt = null;
		String[] legendDatas = null;
		legendDatas = new String[] { "人数" };
		xAxisDatas = new String[] { "学前教育", "小学", "初中", "高中", "九年一贯制", "高职", "高等教育", "其他学校" };
		loadingDataInt = new Integer[1][xAxisDatas.length];
		int con = 0;
		for (int j = 0; j < xAxisDatas.length; j++) {
			int num = (int) (Math.random() * 100);
			loadingDataInt[0][j] = num;
			con += num;
		}
		jsonObject.put("text", "教师统计图");// 主标题 可不填写
		jsonObject.put("subtext", "共" + con + "人");// 副标题 可不填写
		jsonObject.put("xAxisData", xAxisDatas);
		jsonObject.put("legendData", legendDatas);
		// loadingData数据顺序和xAxisData相对
		jsonObject.put("loadingData", loadingDataInt);

		JSONObject map = new JSONObject();
		map.put("jsonData", jsonObject);

		map.put("title", "教师统计图");
		map.put("col", "8");
		map.put("differentColors", true);
		map.put("barW", 20);
		map.put("showLegend", true);
		map.put("showToolBox", true);
		map.put("titleSize", "10");
		map.put("colors",
				"['#b2d8fc','#bcdf69', '#deb968', '#e0796a', '#ee99c3','#a599ef',  '#ffa96e', '#cb92f3','#6791e7', '#89e7b2', '#efd87a']");
		map.put("type", "histogram");
		return map.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟报表数据")
	@ResponseBody
	@RequestMapping("/showStudentNative3")
	public String showStudentNative3() {
		// 教师年龄统计图--雷达
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArr22 = new JSONArray();
		JSONObject json22 = new JSONObject();
		String[] xAxisDatas = null;
		int con = 0;
		jsonObject.clear();
		xAxisDatas = new String[] { "20岁以下", "20岁至30岁", "30岁至40岁", "40岁至50岁", "50岁至55岁", "55岁以上" };
		jsonObject.put("legendData", new String[] { "人数" });
		jsonArr22 = new JSONArray();
		json22 = new JSONObject();
		for (int i = 0; i < xAxisDatas.length; i++) {
			json22 = new JSONObject();
			json22.put("text", xAxisDatas[i]);
			json22.put("max", 200);
			jsonArr22.add(json22);
		}
		jsonObject.put("polarIndicator", jsonArr22);
		jsonArr22 = new JSONArray();
		json22 = new JSONObject();
		json22.put("name", "人数");// 该值与legendData匹配
		Integer[] intLin = new Integer[xAxisDatas.length];
		for (int i = 0; i < xAxisDatas.length; i++) {
			int num = RandomUtils.nextInt(100, 200);
			intLin[i] = num;
			con += num;
		}
		jsonObject.put("text", "教师年龄阶段统计图");// 主标题 可不填写
		json22.put("value", intLin);
		jsonArr22.add(json22);
		jsonObject.put("loadingData", jsonArr22);

		JSONObject map = new JSONObject();
		map.put("jsonData", jsonObject);
		map.put("title", "教师年龄统计图");
		map.put("col", "6");
		map.put("differentColors", true);
		map.put("barW", 20);
		map.put("showLegend", false);
		map.put("showToolBox", false);
		map.put("titleSize", "10");
		map.put("type", "radar");
		return map.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟报表数据")
	@ResponseBody
	@RequestMapping("/showStudentNative0")
	public String showStudentNative0() {
		// 教师年龄统计图--雷达
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArr22 = new JSONArray();
		JSONObject json22 = new JSONObject();
		String[] xAxisDatas = null;
		jsonObject.clear();
		xAxisDatas = new String[] { "8岁", "9岁", "10岁", "11岁", "12岁", "13岁", "14岁", "15岁", "16岁", "16岁以上" };
		jsonObject.put("legendData", new String[] { "人数" });
		jsonArr22 = new JSONArray();
		json22 = new JSONObject();
		for (int i = 0; i < xAxisDatas.length; i++) {
			json22 = new JSONObject();
			json22.put("text", xAxisDatas[i]);
			json22.put("max", 2000);
			jsonArr22.add(json22);
		}
		jsonObject.put("polarIndicator", jsonArr22);
		jsonArr22 = new JSONArray();
		json22 = new JSONObject();
		json22.put("name", "人数");// 该值与legendData匹配
		Integer[] intLin = new Integer[xAxisDatas.length];
		for (int i = 0; i < xAxisDatas.length; i++) {
			int num = (int) (RandomUtils.nextInt(1000, 2000));
			intLin[i] = num;
		}
		jsonObject.put("text", "学生年龄阶段统计图");// 主标题 可不填写
		json22.put("value", intLin);
		jsonArr22.add(json22);
		jsonObject.put("loadingData", jsonArr22);

		JSONObject map = new JSONObject();
		map.put("jsonData", jsonObject);
		map.put("title", "学生年龄统计图");
		map.put("col", "6");
		map.put("differentColors", true);
		map.put("barW", 20);
		map.put("showLegend", false);
		map.put("showToolBox", false);
		map.put("titleSize", "10");
		map.put("type", "radar");
		return map.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟报表数据")
	@RequestMapping("/showStudentNative2")
	@ResponseBody
	public String showStudentNative2() {
		// 毕业生升学率变化情况--折线图
		JSONObject jsonObject = new JSONObject();
		String[] xAxisDatas = null;
		Double[][] loadingDataDou = null;
		String[] legendDatas = null;
		BigDecimal db = null;
		legendDatas = new String[] { "升学率" };
		xAxisDatas = new String[] { "2010", "2011", "2012", "2013", "2014", "2015", "2016" };
		loadingDataDou = new Double[1][xAxisDatas.length];
		for (int j = 0; j < xAxisDatas.length; j++) {
			db = new BigDecimal(RandomUtils.nextDouble(80, 100));
			loadingDataDou[0][j] = Double.valueOf(db.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
		}
		jsonObject.put("xAxisData", xAxisDatas);
		jsonObject.put("legendData", legendDatas);
		// loadingData数据顺序和xAxisData相对
		jsonObject.put("loadingData", loadingDataDou);
		jsonObject.put("text", "升学率");
		JSONObject map = new JSONObject();
		map.put("jsonData", jsonObject);
		map.put("title", "升学率");
		map.put("col", "12");
		map.put("differentColors", true);
		map.put("barW", 20);
		map.put("showLegend", false);
		map.put("showToolBox", false);
		map.put("titleSize", "10");
		map.put("colors",
				"['#bcdf69', '#deb968', '#e0796a', '#ee99c3','#a599ef',  '#ffa96e', '#cb92f3','#6791e7', '#89e7b2', '#efd87a']");
		map.put("type", "line");
		return map.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟报表数据")
	@RequestMapping("/showStudentNative")
	@ResponseBody
	public String showStudentNative() {
		// 学生综合统计--饼图
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArr22 = new JSONArray();
		JSONObject json22 = new JSONObject();
		String[] xAxisDatas = null;
		Integer[][] loadingDataInt = null;
		int con = 0;
		jsonObject.clear();
		xAxisDatas = new String[] { "农业家庭户口", "非农业家庭户口", "非农业集体户口" };
		loadingDataInt = new Integer[1][xAxisDatas.length];
		for (int i = 0; i < xAxisDatas.length; i++) {
			int num = (int) (Math.random() * 5000);
			loadingDataInt[0][i] = num;
			con += num;
		}
		jsonObject.put("text", "学生户口类别分布图" + "（共" + con + "人）");// 主标题 可不填写
		jsonObject.put("legendData", xAxisDatas);
		jsonArr22 = new JSONArray();
		json22 = new JSONObject();
		for (int i = 0; i < xAxisDatas.length; i++) {
			json22 = new JSONObject();
			json22.put("value", loadingDataInt[0][i]);
			json22.put("name", xAxisDatas[i]);
			jsonArr22.add(json22);
		}
		jsonObject.put("loadingData", jsonArr22);

		JSONObject data = new JSONObject();
		data.put("jsonData", jsonObject);
		data.put("title", "学生户口类别分布图");
		data.put("col", "6");
		data.put("differentColors", true);
		data.put("barW", 20);
		data.put("showLegend", false);
		data.put("showToolBox", false);
		data.put("titleSize", "10");
		data.put("colors", "['#b2d8fc','#bcdf69', '#deb968']");
		data.put("type", "pie");
		return data.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟学生人数分布地图数据")
	@RequestMapping("/showStudentNative5")
	@ResponseBody
	public String showStudentNative5(String unitId) {
		Unit unit = Unit.dc(unitRemoteService.findOneById(unitId, true));
		String regionCode = unit.getRegionCode();
		Region region = null;
		if (!StringUtils.endsWith(regionCode, "00")) {
			regionCode = StringUtils.substring(regionCode, 0, 4) + "00";
		}
		region = Region.dc(regionRemoteService.findByFullCode("1", regionCode));
		JSONObject json = new JSONObject();
		json.put("col", "6");
		json.put("title", "学生人数分布");
		json.put("city", regionCode);
		json.put("height", 500);
		JSONObject jsonStringData = new JSONObject();
		jsonStringData.put("title", region.getRegionName() + "学生数");
		jsonStringData.put("showLabelNromal", false);

		JSONArray datas = new JSONArray();
		List<Region> subRegions = Region.dt(regionRemoteService.findUnderlineRegions(regionCode));
		for (Region r : subRegions) {
			JSONObject data = new JSONObject();
			data.put("name", r.getRegionName());
			data.put("value", RandomUtils.nextInt(500, 2000));
			datas.add(data);
		}
		jsonStringData.put("rangeMin", 500);
		jsonStringData.put("rangeMax", 2000);
		jsonStringData.put("data", datas);
		json.put("jsonData", jsonStringData);

		return json.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟排行榜数据")
	@ResponseBody
	@RequestMapping("/showRankingList")
	public String showRankingList(Integer num, Boolean showImage) {
		num = num == null ? 3 : num;
		Map<String, User> rankingMap = new HashMap<String, User>();
		User user = new User();
		user.setRealName("小小");
		user.setBgImg("");
		for (int i = 0; i < num; i++) {
			rankingMap.put(String.valueOf(i + 1), user);
		}
		JSONObject map = new JSONObject();
		map.put("rankingMap", rankingMap);
		map.put("title", "排行榜");
		map.put("col", "6");
		map.put("titleSize", rankingMap.size());
		map.put("showImage", false);
		return map.toJSONString();
	}

	@ControllerInfo(ignoreLog = 1, value = "模拟生日提醒数据")
	@ResponseBody
	@RequestMapping("/showBirthdayReminder")
	public String showBirthdayReminder(Integer num, Boolean paging, Boolean showImage) {
		num = num == null ? 3 : num;
		Map<String, User> rankingMap = new HashMap<String, User>();
		User user = new User();
		user.setRealName("小小");
		user.setBgImg("");
		user.setAccountId("5");
		for (int i = 0; i < num; i++) {
			rankingMap.put(String.valueOf(i + 1), user);
		}
		JSONObject map = new JSONObject();
		map.put("rankingMap", rankingMap);
		map.put("title", "生日提醒");
		map.put("col", "6");
		map.put("showSize", rankingMap.size());
		map.put("showImage", showImage);
		map.put("isPagination", paging);
		return map.toJSONString();
	}

}
