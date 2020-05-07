package net.zdsoft.bigdata.extend.data.action;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.basedata.remote.service.StudentRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.data.manager.datasource.IHttpComponentsApiDataSource;
import net.zdsoft.bigdata.data.manager.datasource.IQueryException;
import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.extend.data.entity.*;
import net.zdsoft.bigdata.extend.data.service.*;
import net.zdsoft.bigdata.frame.data.elastic.EsClientService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.coords.radar.Radar;
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.element.inner.Indicator;
import net.zdsoft.echarts.enu.*;
import net.zdsoft.echarts.series.Line;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.DateUtils;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by wangdongdong on 2018/8/3 14:45.
 */
@Controller
@RequestMapping("/bigdata/personAnalysis")
public class PersonAnalysisController extends BigdataBaseAction {

	@Autowired
	private StudentTagResultService studentTagResultService;
	@Autowired
	private StatSocialCirclesService statSocialCirclesService;
	@Autowired
	private StatGrowthEventService statGrowthEventService;
	@Autowired
	private StatAppPreferenceService statAppPreferenceService;
	@Autowired
	private StatAppUsageService statAppUsageService;
	@Autowired
	private StatPlatformUsageService statPlatformUsageService;
	@Autowired
	private StatStuEvaluationService statStuEvaluationService;
	@Autowired
	private EsClientService esClientService;
	@Autowired
	private UserProfileService userProfileService;
	@Resource
	private IHttpComponentsApiDataSource httpComponentsApiDataSource;
	@Autowired
	private SemesterRemoteService semesterRemoteService;
	@Resource
	private UserRemoteService userRemoteService;
	@Resource
	private StudentRemoteService studentRemoteService;
	@Resource
	private SysOptionRemoteService sysOptionRemoteService;

	private static final Logger logger = LoggerFactory
			.getLogger(PersonAnalysisController.class);

	// 单次考试成绩
	private static final String ALL_SCORE_URL = "/exammanage/scoreRanking/getAllScoreByStudentId";
	// 所有考试成绩
	private static final String ALL_EXAM_SCORE_URL = "/exammanage/scoreRanking/getStuAllExamScore";
	// 所有考试成绩排名
	private static final String ALL_EXAM_RANK_SCORE_URL = "/exammanage/scoreRanking/getStuAllExamRank";

	private static final String ALL_EXAM_URL = "/exammanage/common/queryExamsByStudentId";

	@RequestMapping("/index")
	public String index(String userId, String profileCode, ModelMap model,String style,
			HttpServletRequest request) {
		boolean biStyle = false;
    	if(StringUtils.isNotBlank(style)&&"bi".equals(style)){
    		biStyle =true;
    	}
		List<UserProfile> userProfiles = userProfileService.findListBy("code",
				profileCode);
		UserProfile profile = userProfiles.get(0);
		Json queryParam = new Json();
		queryParam.put("type", "basicQuery");
		queryParam.put("field", "id");
		queryParam.put("value", userId);
		// 查询该学生或者老师信息
		if ("student".equals(profileCode)) {
			List<String> resultFieldList = new ArrayList<String>();
			resultFieldList.add("unit_name");
			resultFieldList.add("student_name");
			resultFieldList.add("class_name");
			resultFieldList.add("unit_id");
			resultFieldList.add("id");
			resultFieldList.add("grade_name");
			resultFieldList.add("sex");
			List<Json> list = esClientService.query(profile.getIndexName(),
					profile.getTypeName(), queryParam, resultFieldList, null,
					null);
			StudentTagResult studentTagResult = new StudentTagResult();
			for (Json json : list) {
				studentTagResult.setStudentId(userId);
				studentTagResult.setUnitName(json.getString("unit_name"));
				studentTagResult.setStudentName(json.getString("student_name"));
				studentTagResult.setUnitId(json.getString("unit_id"));
				studentTagResult.setClassName(json.getString("class_name"));
				studentTagResult.setGradeName(json.getString("grade_name"));
			}
			Student student = studentRemoteService.findOneObjectById(userId);
			if (student != null) {
				User user = User.dc(userRemoteService.findByOwnerId(student
						.getId()));
				final String fileUrl = sysOptionRemoteService
						.getFileUrl(getRequest().getServerName()) + "/store";
				studentTagResult
						.setAvatarUrl(fileUrl
								+ (user != null ? user.getAvatarUrl()
										: ((student.getSex() != null && 1 == student
												.getSex()) ? "/store/portrait/user-male.png"
												: "/store/portrait/user-female.png")));
			}
			studentTagResult
					.setSex(student != null ? ((student.getSex() != null && 1 == student
							.getSex()) ? "男" : "女") : "男");
			model.addAttribute("user", studentTagResult);
		} else {
			List<String> resultFieldList = new ArrayList<String>();
			resultFieldList.add("unit_name");
			resultFieldList.add("teacher_name");
			resultFieldList.add("dept_name");
			resultFieldList.add("id");
			List<Json> list = esClientService.query(profile.getIndexName(),
					profile.getTypeName(), queryParam, resultFieldList, null,
					null);
			TeacherTagResult teacherTagResult = new TeacherTagResult();
			for (Json json : list) {
				teacherTagResult.setTeacherId(userId);
				teacherTagResult.setUnitName(json.getString("unit_name"));
				teacherTagResult.setTeacherName(json.getString("teacher_name"));
				teacherTagResult.setDeptName(json.getString("dept_name"));
			}
			model.addAttribute("user", teacherTagResult);
		}
		model.addAttribute("profileCode", profileCode);
		model.addAttribute("userId", userId);
		model.addAttribute("biStyle", biStyle);
		return "/bigdata/extend/profileAnalysis/personAnalysis.ftl";
	}

	@RequestMapping("/baseInfo")
	public String baseInfo(String userId, ModelMap model) {
		// 成长历程
		List<StatGrowthEvent> growthEvents = statGrowthEventService
				.findByOwnerId(userId);
		// 应用偏好
		List<StatAppPreference> appPreferences = statAppPreferenceService
				.findByOwnerId(userId);
		int sum = appPreferences.stream()
				.mapToInt(StatAppPreference::getAppUsage).sum();
		appPreferences.forEach(e -> {
			float percent = (float) e.getAppUsage() / (float) sum * 100;
			e.setAppUsage((int) percent);
		});
		// 在线社交圈
		List<StatSocialCircles> socialCirclesList = statSocialCirclesService
				.findByOwnerId(userId);
		// 学生综合素质评价
		List<StatStuEvaluation> statStuEvaluations = statStuEvaluationService
				.findByStudentId(userId);
		model.addAttribute("growthEvents", growthEvents);
		model.addAttribute("appPreferences", appPreferences);
		model.addAttribute("socialCircles",
				socialCirclesList.size() > 0 ? socialCirclesList.get(0)
						: new StatSocialCircles());
		model.addAttribute("statStuEvaluations", statStuEvaluations);
		return "/bigdata/extend/profileAnalysis/baseInfo.ftl";
	}

	@RequestMapping("/scoreInfo")
	public String scoreInfo(String userId, String unitId, ModelMap model,
			HttpServletRequest request) {
		// 查询考试
		Semester currentSemester = Semester.dc(semesterRemoteService
				.getCurrentSemester(0, unitId));
		Map<String, String> params = Maps.newHashMap();
		params.put("unitId", unitId);
		params.put("acadyear", currentSemester == null ? "" : currentSemester.getAcadyear());
		params.put("semester", currentSemester == null ? "" : currentSemester.getSemester().toString());
		params.put("studentId", userId);
		try {
			String result = getScoreInfoResult(request, ALL_EXAM_URL, params);
			model.addAttribute("examList", result);
		} catch (Exception e) {
			model.addAttribute("examList", "{\"infolist\":[]}");
			logger.error(e.getMessage(), e);
		}
		// 查询学年学期
		model.addAttribute("currentAcadyear", currentSemester == null ? "" : currentSemester.getAcadyear());
		model.addAttribute("currentSemester", currentSemester == null ? "" : currentSemester.getSemester()
				.toString());
		return "/bigdata/extend/profileAnalysis/scoreInfo.ftl";
	}

	@RequestMapping("/getScoreInfo")
	@ResponseBody
	public Response getScoreInfo(String userId, String unitId, String examId,
			HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();
		params.put("unitId", unitId);
		params.put("examId", examId);
		params.put("studentId", userId);
		try {
			String result = getScoreInfoResult(request, ALL_SCORE_URL, params);
			return Response.ok().data(result).build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	@RequestMapping("/getAppUsageOption")
	@ResponseBody
	public Response getAppUsageOption(String userId) {
		List<JData.Entry> entryList = Lists.newArrayList();
		List<StatAppUsage> statAppUsages = statAppUsageService
				.findByOwnerId(userId);
		if (statAppUsages.size() == 0) {
			return Response.error().build();
		}
		statAppUsages.forEach(e -> {
			JData.Entry entry = new JData.Entry();
			entry.setX(DateUtils.date2String(e.getUsageDate(), "yyyy-MM"));
			entry.setName(e.getAppName());
			entry.setY(e.getAppUsage());
			entryList.add(entry);
		});
		// 获取option
		Option option = getLineOption(entryList);
		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

	@RequestMapping("/getPlatformUsageOption")
	@ResponseBody
	public Response getPlatformUsageOption(String userId) {
		List<JData.Entry> entryList = Lists.newArrayList();
		List<StatPlatformUsage> statPlatformUsages = statPlatformUsageService
				.findByOwnerId(userId);
		if (statPlatformUsages.size() == 0) {
			return Response.error().build();
		}
		statPlatformUsages.forEach(e -> {
			JData.Entry entry = new JData.Entry();
			entry.setX(DateUtils.date2String(e.getLoginDate(), "yyyy-MM"));
			entry.setY(e.getLoginNum());
			entry.setName("平台使用次数");
			entryList.add(entry);
		});
		// 获取option
		Option option = getLineOption(entryList);
		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

	@RequestMapping("/getPersonTag")
	@ResponseBody
	public Response getPersonTag(String userId) {
		List<StudentTagResult> list = studentTagResultService.findListBy(
				"studentId", userId);
		if (list.size() == 0) {
			return Response.error().build();
		}
		List<JData.Entry> entryList = Lists.newArrayList();
		list.forEach(e -> {
			JData.Entry entry = new JData.Entry();
			entry.setY(0);
			entry.setX(e.getTagName());
			entryList.add(entry);
		});
		// 获取option
		Option option = getWordCloudOption(entryList);
		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

	/**
	 * 折线图
	 *
	 * @param entryList
	 * @return
	 */
	private Option getLineOption(List<JData.Entry> entryList) {
		JData data = new JData();
		data.setType(SeriesEnum.line);
		data.setCoordSys(CoordinateSystem.cartesian2d);
		data.setEntryList(entryList);
		data.setSelfCoordSys(true);
		data.setSelfXAxis(true);
		data.setSelfYAxis(true);
		JData.JCoordSysPosition position = new JData.JCoordSysPosition();
		position.setTop(TopEx.create("15%"));
		position.setBottom(BottomEx.create("13%"));
		data.setCoordSysPosition(position);
		data.setXAxisType(AxisType.category);
		JData.JAxisPosition xp = new JData.JAxisPosition();
		JData.JAxisPosition yp = new JData.JAxisPosition();
		data.setXJAxisPosition(xp);
		data.setYJAxisPosition(yp);
		Option option = new Option();
		JConverter.convert(data, option);
		for (Cartesian2dAxis axis : option.getXAxis()) {
			axis.axisLabel().interval(0);
			axis.axisTick().interval(0);
			axis.interval(0);
			axis.boundaryGap(false);
		}
		option.series().forEach(e -> {
			((Line) e).areaStyle().origin(Origin.start);
		});
		Tooltip tooltip = new Tooltip().option(option);
		tooltip.trigger(Trigger.axis);
		option.tooltip(tooltip);
		return option;
	}

	/**
	 * 字符云
	 *
	 * @param entryList
	 * @return
	 */
	private Option getWordCloudOption(List<JData.Entry> entryList) {
		JData data = new JData();
		data.setSelfCoordSys(true);
		data.setType(SeriesEnum.wordCloud);
		data.setEntryList(entryList);
		data.setSelfCoordSys(true);
		JData.JCoordSysPosition position = new JData.JCoordSysPosition();
		data.setCoordSysPosition(position);

		Option option = new Option();
		JConverter.convert(data, option);

		// 设置标题，提示框等
		Tooltip tooltip = new Tooltip().option(option);
		option.tooltip(tooltip);
		tooltip.trigger(Trigger.item);
		option.textStyle()
				.color("function(){ var rd = parseInt(Math.random()*9); "
						+ "var array = ['#9949d7', '#ee913a', '#1ebcd3', '#cb309a','#1f83f5', '#93f51f', '#3bb7f0', '#2fe3c7', '#ee3a71', '#d142a4']; "
						+ " return array[rd];" + "}");
		return option;
	}

	/**
	 * 根据考试获取科目成绩
	 *
	 * @return
	 */
	@RequestMapping("/getSubjectScore")
	@ResponseBody
	public Response getSubjectScore(String userId, String unitId,
			String examId, HttpServletRequest request) {

		Map<String, String> params = Maps.newHashMap();
		params.put("unitId", unitId);
		params.put("examId", examId);
		params.put("studentId", userId);
		try {
			String result = getScoreInfoResult(request, ALL_SCORE_URL, params);
			List<JData.Entry> entryList = Lists.newArrayList();
			JSONObject jsonObject = JSON.parseObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("infolist");
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject info = jsonArray.getJSONObject(i);
				JData.Entry entry = new JData.Entry();
				entry.setX(info.getString("subjectName"));
				entry.setY(info.getString("subjectScore"));
				entryList.add(entry);
			}

			JData data = new JData();
			data.setType(SeriesEnum.bar);
			data.setCoordSys(CoordinateSystem.cartesian2d);
			data.setEntryList(entryList);
			data.setSelfCoordSys(true);
			data.setSelfXAxis(true);
			data.setSelfYAxis(true);
			JData.JCoordSysPosition position = new JData.JCoordSysPosition();
			position.setTop(TopEx.create("4%"));
			position.setBottom(BottomEx.create("12%"));
			data.setCoordSysPosition(position);
			data.setXAxisType(AxisType.category);
			JData.JAxisPosition xp = new JData.JAxisPosition();
			JData.JAxisPosition yp = new JData.JAxisPosition();
			data.setXJAxisPosition(xp);
			data.setYJAxisPosition(yp);

			Option option = new Option();

			JConverter.convert(data, option);
			for (Cartesian2dAxis dAxis : option.xAxis()) {
				dAxis.axisLabel().interval(0);
			}
			// 设置标题，提示框等
			option.title().show(true).left(LeftEnum.left);
			Tooltip tooltip = new Tooltip().option(option);
			tooltip.axisPointer().type(AxisPointerType.shadow);
			option.tooltip(tooltip);
			option.legend().show(true).right(RightEnum.center)
					.type(LegendEnum.scroll).top(TopEnum.top)
					.orient(Orient.horizontal);

			tooltip.trigger(Trigger.axis);
			String[] color = new String[] { "#37a2da", "#34c4e9", "#96bfff",
					"#317deb", "#fedb5b", "#96beb8", "#ae89f3", "#ff9f7f",
					"#ae89f3", "#46c6a3" };

			for (Series e : option.series()) {
				int i = 0;
				for (Object b : e.getData()) {
					BarData bd = (BarData) b;
					ItemStyle itemStyle = new ItemStyle();
					itemStyle.color(color[i++ % color.length]);
					bd.itemStyle(itemStyle);
				}
			}
			for (Cartesian2dAxis axis : option.yAxis()) {
				axis.axisLabel().interval(3);
			}
			return Response.ok().data(JSONObject.toJSONString(option)).build();
		} catch (Exception e) {
			return Response.error().message(e.getMessage()).build();
		}
	}

	/**
	 * @return
	 */
	@RequestMapping("/getSubjectRendar")
	@ResponseBody
	public Response getSubjectRendar(String userId, String unitId,
			String examId, HttpServletRequest request) {

		Map<String, String> params = Maps.newHashMap();
		params.put("unitId", unitId);
		params.put("examId", examId);
		params.put("studentId", userId);
		List<JData.Entry> entryList = Lists.newArrayList();
		try {
			String result = getScoreInfoResult(request, ALL_SCORE_URL, params);
			JSONObject jsonObject = JSON.parseObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("infolist");
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject info = jsonArray.getJSONObject(i);
					JData.Entry entry = new JData.Entry();
					entry.setX(info.getString("subjectName"));
					entry.setY(info.getString("subjectScore"));
					entry.setName("学科");
					entryList.add(entry);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		JData data = new JData();
		data.setType(SeriesEnum.radar);
		data.setSelfCoordSys(true);
		data.setCoordSys(CoordinateSystem.radar);
		data.setEntryList(entryList);
		data.setSelfCoordSys(true);
		JData.JCoordSysPosition position = new JData.JCoordSysPosition();
		data.setCoordSysPosition(position);
		Option option = new Option();
		JConverter.convert(data, option);

		for (Radar radar : option.getRadar()) {
			radar.setRadius("60%");
			for (Indicator indicator : radar.getIndicator()) {
				indicator.max(Double.valueOf("150"));
			}
		}
		option.legend().top(TopEnum.top);
		option.color(Lists.newArrayList("#00aeef"));
		// 设置标题，提示框等
		Tooltip tooltip = new Tooltip().option(option);
		option.tooltip(tooltip);
		tooltip.trigger(Trigger.item);

		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

	/**
	 * @return
	 */
	@RequestMapping("/getHistoryExamInfo")
	@ResponseBody
	public Response getHistoryExamInfo(String userId, String unitId,
			String acadyear, String semester, HttpServletRequest request) {
		Map<String, String> params = Maps.newHashMap();
		params.put("unitId", unitId);
		params.put("acadyear", acadyear);
		params.put("semester", semester);
		params.put("studentId", userId);
		List<JData.Entry> entryList = Lists.newArrayList();
		try {
			String result = getScoreInfoResult(request, ALL_EXAM_SCORE_URL,
					params);
			JSONObject jsonObject = JSON.parseObject(result);
			if (jsonObject == null) {
				return Response.error().build();
			}
			JSONArray jsonArray = jsonObject.getJSONArray("infolist");
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject info = jsonArray.getJSONObject(i);
					JData.Entry entry = new JData.Entry();
					entry.setX(info.getString("examName"));
					entry.setY(info.getString("classRank"));
					entry.setName("班级排名");
					entryList.add(entry);
					entry = new JData.Entry();
					entry.setX(info.getString("examName"));
					entry.setY(info.getString("gradeRank"));
					entry.setName("年级排名");
					entryList.add(entry);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (entryList.size() == 0) {
			return Response.error().build();
		}
		JData data = new JData();
		data.setType(SeriesEnum.bar);
		data.setCoordSys(CoordinateSystem.cartesian2d);
		data.setEntryList(entryList);
		data.setSelfCoordSys(true);
		data.setSelfXAxis(true);
		data.setSelfYAxis(true);
		JData.JCoordSysPosition position = new JData.JCoordSysPosition();
		position.setTop(TopEx.create("15%"));
		position.setBottom(BottomEx.create("13%"));
		data.setCoordSysPosition(position);
		data.setXAxisType(AxisType.category);
		JData.JAxisPosition xp = new JData.JAxisPosition();
		JData.JAxisPosition yp = new JData.JAxisPosition();
		data.setXJAxisPosition(xp);
		data.setYJAxisPosition(yp);

		Option option = new Option();
		JConverter.convert(data, option);

		// 设置标题，提示框等
		Tooltip tooltip = new Tooltip().option(option);
		tooltip.axisPointer().type(AxisPointerType.shadow);
		option.tooltip(tooltip);
		option.legend().show(true).right(RightEnum.center)
				.type(LegendEnum.scroll).top(TopEnum.top)
				.orient(Orient.horizontal);
		tooltip.trigger(Trigger.axis);// .formatter("{a} <br/> {b}: {c0}");

		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

	@RequestMapping("/getHistoryExamRank")
	@ResponseBody
	public Response getHistoryExamRank(String userId, String unitId,
			String acadyear, String semester, HttpServletRequest request) {
		List<JData.Entry> entryList = Lists.newArrayList();

		Map<String, String> params = Maps.newHashMap();
		params.put("unitId", unitId);
		params.put("acadyear", acadyear);
		params.put("semester", semester);
		params.put("studentId", userId);
		try {
			String result = getScoreInfoResult(request,
					ALL_EXAM_RANK_SCORE_URL, params);
			JSONObject jsonObject = JSON.parseObject(result);
			if (jsonObject == null) {
				return Response.error().build();
			}
			JSONArray jsonArray = jsonObject.getJSONArray("infolist");
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject info = jsonArray.getJSONObject(i);
					String examName = info.getString("examName");
					JSONArray infolistIn = info.getJSONArray("infolistIn");
					if (infolistIn != null) {
						for (int j = 0; j < infolistIn.size(); j++) {
							JSONObject subject = infolistIn.getJSONObject(j);
							JData.Entry entry = new JData.Entry();
							entry.setX(examName);
							entry.setName(subject.getString("subjectName"));
							String rank = subject.getString("subjecGradeRank");
							entry.setY(StringUtils.isNotBlank(rank) ? rank : 0);
							entryList.add(entry);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (entryList.size() == 0) {
			return Response.error().build();
		}
		// 获取option
		Option option = getLineOption(entryList);
		for (Series e : option.series()) {
			((Line) e).areaStyle().opacity(0.0);
		}
		option.setColor(Arrays.asList("#1f83f5", "#d042a4", "#1ebcd3",
				"#9949d7", "#ee913a", "#3bb7f0", "#cdb112", "#b9396d"));
		return Response.ok().data(JSON.toJSON(option).toString()).build();
	}

	private synchronized String getScoreInfoResult(HttpServletRequest request,
			String url, Map<String, String> params) throws IQueryException {
		// String key = "scoreInfo." + url;
		// String result = RedisUtils.get(key);
		String result = null;
		if (result == null) {
			StringBuilder apiUrl = new StringBuilder(request.getScheme()
					+ "://" + request.getServerName() + ":"
					+ request.getServerPort()).append(url);
			int i = 0;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				apiUrl.append(i == 0 ? "?" : "&");
				apiUrl.append(entry.getKey()).append("=")
						.append(entry.getValue());
				i++;
			}
			result = (String) httpComponentsApiDataSource.executeQuery(
					apiUrl.toString(), 30 * 1000);
			// RedisUtils.set(key, result, 60 * 60 * 2);
		}
		return result;
	}
}
