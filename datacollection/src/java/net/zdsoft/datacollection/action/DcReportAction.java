package net.zdsoft.datacollection.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.SemesterRemoteService;
import net.zdsoft.datacollection.entity.DcDataModel;
import net.zdsoft.datacollection.entity.DcOperationData;
import net.zdsoft.datacollection.entity.DcReport;
import net.zdsoft.datacollection.service.DcReportService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.annotation.ControllerParam;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.JsonUtils;
import net.zdsoft.framework.utils.RedisUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.SortUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Controller
@RequestMapping("/dc/report")
public class DcReportAction extends BaseAction {

	@Autowired
	private DcReportService dcReportService;

	@Autowired
	private SemesterRemoteService semesterRemoteService;
	
	@Autowired
	private DeptRemoteService deptRemoteService;

	@RequestMapping("/add")
	public String add() {
		return "/datacollection/report/add.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/saveTemplate/{reportId}")
	@ControllerInfo("保存模板数据,report:{reportId}")
	public String saveTemplate(@RequestBody String content, ModelMap map, @PathVariable String reportId,
			HttpServletRequest req) {
		DcReport report = dcReportService.findOne(reportId);
		if (report == null) {
			return error("找不到填报项目！");
		}
		return saveReportAndTemplate(content, req, report);
	}

	private String saveReportAndTemplate(String content, HttpServletRequest req, DcReport report) {
		String finalViewPath;
		String templateType = req.getParameter("_templateType");
		String convered = req.getParameter("_convered");
		finalViewPath = getTemplatePath(content, report, templateType, convered);
		File file = new File(req.getSession().getServletContext().getRealPath("/") + finalViewPath);
		try {
			FileUtils.write(file, content, "utf8");
			dcReportService.save(report);
			return success("保存成功！");
		} catch (IOException e) {
			return error(e.getMessage());
		}
	}

	private String getTemplatePath(String content, DcReport report, String templateType, String convered) {
		String viewPath = StringUtils.equals(templateType, "detail") ? report.getTemplatePath()
				: report.getListTemplatePath();
		String finalViewPath;
		if (BooleanUtils.toBoolean(convered)) {
			finalViewPath = viewPath;
		} else {
			finalViewPath = "/datacollection/report/" + report.getReportCode() + "_" + templateType + "_"
					+ System.nanoTime() + RandomStringUtils.randomAlphabetic(3) + ".ftl";
		}
		if (StringUtils.equals(templateType, "detail"))
			report.setTemplatePath(finalViewPath);
		else
			report.setListTemplatePath(finalViewPath);
		return finalViewPath;
	}

	@RequestMapping("/templateEdit")
	@ControllerInfo(ignoreLog=1)
	public String templateEdit(ModelMap map, String viewPath, HttpServletRequest req) {
		String path = req.getSession().getServletContext().getRealPath("/");
		String reportId = req.getParameter("_reportId");
		String templateType = req.getParameter("_templateType");
		map.put("_reportId", reportId);
		map.put("_condition", req.getParameter("_condition"));
		DcReport report = dcReportService.findOne(reportId);
		String templateContent = "";
		map.put("_templateType", templateType);
		if (report != null) {
			if (StringUtils.equals(templateType, "list")) {
				viewPath = report.getListTemplatePath();
				try {
					map.put("_url", "/dc/report/listReportData/" + report.getId() + "?_condition="
							+ URLEncoder.encode(req.getParameter("_condition"), "utf8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (StringUtils.equals(templateType, "detail")) {
				viewPath = report.getTemplatePath();
				try {
					map.put("_url", "/dc/report/detailByDataId/" + report.getId() + "/" + req.getParameter("_dataId")
							+ "?_condition=" + URLEncoder.encode(req.getParameter("_condition"), "utf8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			map.put("_viewPath", viewPath);
			File file = new File(path + viewPath);
			try {
				templateContent = FileUtils.readFileToString(file, "utf-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			map.put("_report", report);
		}
		map.put("_templateContent", templateContent);
		map.put("_dataId", req.getParameter("_dataId"));

		return "/datacollection/report/reportTemplateEditor.ftl";
	}

	/**
	 * 列出所有的填报任务
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping("/listReports")
	@ControllerInfo(ignoreLog=1)
	public String listByUnitId(ModelMap map, String unitId) {
		if (StringUtils.isBlank(unitId))
			unitId = getLoginInfo().getUnitId();

		List<DcReport> projects = dcReportService.findListBy("unitId", unitId);
		if (CollectionUtils.isEmpty(projects)) {
			dcReportService.save(initDcReport(projects));
		}
		SortUtils.ASC(projects, "reportName");
		map.put("projects", projects);
		return "/datacollection/report/index.ftl";
	}

	private DcReport initDcReport(List<DcReport> projects) {
		DcReport dr = new DcReport();
		dr.setId(UuidUtils.generateUuid());
		dr.setUnitId(getLoginInfo().getUnitId());
		dr.setCreateUserId(getLoginInfo().getUserId());
		dr.setCreationTime(new Date());
		dr.setListTemplatePath("/datacollection/report/sjtb_list.ftl");
		dr.setReportCode("dc_report");
		dr.setReportName("数据填报项目");
		dr.setReportType(3);
		dr.setTemplatePath("/datacollection/report/sjtb.ftl");
		projects.add(dr);
		return dr;
	}

	@RequestMapping("/listReportDataSum/{reportId}")
	@ControllerInfo(ignoreLog=1)
	public String listReportDataSum(ModelMap map, @PathVariable String reportId, String unitId, HttpServletRequest req,
			HttpServletResponse res) {
		replaceSpecialReqParameter(map, req);
		setReqParameter2Map(map, req);
		req.setAttribute("_map", map);
		DcReport report = dcReportService.findOne(reportId);
		if (report == null)
			return "/datacollection/report/indexList.ftl";

		String path = req.getParameter("_viewPath");

		if (StringUtils.isBlank(path) && StringUtils.isBlank(report.getListTemplatePath())) {
			initListTemplateFile(req, report);
			dcReportService.save(report);
		}
		JSONArray arrayRtn = new JSONArray();
		String condition = req.getParameter("_condition");

		String cookieKey = "dc.report." + getLoginInfo().getUserName() + ".sum." + reportId;
		condition = dealConditionWithCookie(req, res, condition, cookieKey);

		DcDataModel dcm = new DcDataModel();
		analyzeCondition(map, condition, arrayRtn, dcm);
		List<String> orderBys = new ArrayList<String>();
		analyzeOrderBy(map, condition, orderBys);
		for(int index = 0; index < orderBys.size(); index ++){
			if(StringUtils.contains(orderBys.get(index), "creation_time desc")){
				orderBys.remove(index);
				orderBys.add(index, "wo_date desc");
				break;
			}
		} 
		String parameter = report.getParameter();
		analyzeParameter(map, condition, arrayRtn, parameter, dcm);

		List<JSONObject> jsons;
		
		String key = EntityUtils.getCode(SUtils.s(dcm));
		String data = RedisUtils.get(key);
		data = null;
		if(StringUtils.isNotBlank(data)){
			jsons = SUtils.dt(data, new TypeReference<List<JSONObject>>(){});
		}
		else{
			jsons = dcReportService.findDatas(report.getReportCode(), orderBys.toArray(new String[0]), dcm, null);
			try{
				 RedisUtils.set(key, SUtils.s(jsons), 3600);
				 RedisUtils.set(key + "_cache_time", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			}
			catch(Exception e){
				//如果缓存错误，不做任何处理，允许出错
			}
		}

		Map<String, Float> scoreMap = new HashMap<String, Float>();
		Map<String, Float> scoreCountMap = new HashMap<String, Float>();
		Map<String, JSONObject> teacherMap = new HashMap<String, JSONObject>();
		for (JSONObject json : jsons) {
			float score = NumberUtils.toFloat(json.getString("score"));
			String teacherId = json.getString("teacher_id");
			Float totalScore = scoreMap.get(teacherId);
			if (totalScore == null) {
				scoreMap.put(teacherId, score);
				scoreCountMap.put(teacherId, 1.0f);
			} else {
				scoreCountMap.put(teacherId, scoreCountMap.get(teacherId) + 1);
				scoreMap.put(teacherId, totalScore + score);
			}

			JSONObject teacher = teacherMap.get(teacherId);
			if (teacher == null) {
				teacherMap.put(teacherId, json);
			}
		}

		List<JSONObject> teachers = new ArrayList<>();
		dealScoreSum(getLoginInfo().getDeptId(), scoreMap, scoreCountMap, teacherMap, teachers);
		map.put("_condition", condition);
		map.put("_datas", teachers);
		map.put("_report", report);
		map.put("_templateType", "list");
		map.put("_cacheTime", RedisUtils.get(key + "_cache_time"));
		return StringUtils.isBlank(path) ? report.getListTemplatePath() : path;
	}

	/**
	 * 处理积分制的报表数据，这个属于特殊的定制
	 * @param scoreMap
	 * @param scoreCountMap
	 * @param teacherMap
	 * @param teachers
	 */
	private void dealScoreSum(String deptId, Map<String, Float> scoreMap, Map<String, Float> scoreCountMap,
			Map<String, JSONObject> teacherMap, List<JSONObject> teachers) {
		for (String teacherId : teacherMap.keySet()) {
			Float sumScore = scoreMap.get(teacherId);
			JSONObject teacher = teacherMap.get(teacherId);
			if(StringUtils.equals(deptId, teacher.getString("deptId"))){
				teacher.put("isMyDept", true);
			}
			else{
				teacher.put("isMyDept", false);
			}
			teacher.put("sumScore", sumScore);
			teacher.put("countScore", scoreCountMap.get(teacherId));
			teachers.add(teacher);
		}
		SortUtils.DESC(teachers, "sumScore");
		float preSumScore = 0;
		int preIndex = 0;
		for(JSONObject j : teachers) {
			float sumScore = j.getFloatValue("sumScore");
			if(preSumScore == sumScore) {
				j.put("index", preIndex++);
			}
			else {
				j.put("index", ++preIndex);
			}
			preSumScore = sumScore;
		}
	}

	/**
	 * 填报数据列表
	 * 
	 * @param map
	 * @param reportId
	 *            报表id
	 * @param unitId
	 *            如果为空，则直接取当前登录用户所在单位id
	 * @param path
	 *            可以为空，不传，那么会默认取填报任务的listTemplatePath
	 * @return
	 */
	@RequestMapping("/listReportData/{reportId}")
	@ControllerInfo(ignoreLog=1)
	public String listReportData(ModelMap map, @PathVariable String reportId, String unitId, HttpServletRequest req,
			HttpServletResponse res) {
		replaceSpecialReqParameter(map, req);
		setReqParameter2Map(map, req);
		req.setAttribute("_map", map);

		DcReport report = dcReportService.findOne(reportId);
		if (report == null)
			return "/datacollection/report/indexList.ftl";

		String path = req.getParameter("_viewPath");

		if (StringUtils.isBlank(path) && StringUtils.isBlank(report.getListTemplatePath())) {
			initListTemplateFile(req, report);
			dcReportService.save(report);
		}
		JSONArray arrayRtn = new JSONArray();
		String condition = req.getParameter("_condition");
		
		String cookieKye = "dc.report." + getLoginInfo().getUserName() + "." + reportId;

		condition = dealConditionWithCookie(req, res, condition, cookieKye);

		DcDataModel dcm = new DcDataModel();
		analyzeCondition(map, condition, arrayRtn, dcm);
		List<String> orderBys = new ArrayList<String>();
		analyzeOrderBy(map, condition, orderBys);
		
		for(int index = 0; index < orderBys.size(); index ++){
			if(StringUtils.contains(orderBys.get(index), "creation_time desc")){
				orderBys.remove(index);
				orderBys.add(index, "wo_date desc");
				break;
			}
		} 
		
		String parameter = report.getParameter();
		analyzeParameter(map, condition, arrayRtn, parameter, dcm);

		List<JSONObject> jsons;
		jsons = dcReportService.findDatas(report.getReportCode(), orderBys.toArray(new String[0]), dcm, 300);

		showAddButton(map, report, jsons);

		map.put("_condition", condition);
		map.put("_datas", jsons);
		map.put("_report", report);
		map.put("_templateType", "list");
		
		//特殊处理下
		if(StringUtils.equals(reportId, "12758205882496011523882331922547")){
			 String deptId = getLoginInfo().getDeptId();
			 Dept dept = SUtils.dc(deptRemoteService.findOneById(deptId), Dept.class);
			 if(dept != null){
				 String parentId = dept.getParentId();
				 if(ArrayUtils.contains(new String[]{"FF8080813A3EEE1C013A3F631D6F0019"}, parentId)){
					 map.put("isDev", true);
				 }
				 else{
					 map.put("isDev", false);
				 }
			 }
		}
				
		return StringUtils.isBlank(path) ? report.getListTemplatePath() : path;
	}

	/**
	 * 将条件保存到cookie中，下次默认从cookie中获取
	 * @param req
	 * @param res
	 * @param condition
	 * @param cookieKye
	 * @return
	 */
	private String dealConditionWithCookie(HttpServletRequest req, HttpServletResponse res, String condition,
			String cookieKye) {
		if (StringUtils.isBlank(condition)) {
			Cookie[] cookies = req.getCookies();
			for (Cookie cookie : cookies) {
				String cookieName = cookie.getName();
				if (StringUtils.equals(cookieKye, cookieName)) {
					try { 
						condition = URLDecoder.decode(cookie.getValue(), "utf8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					break;
				}
			}
		} else {
			JSONArray array = JSONArray.parseArray(condition);
			JSONArray array2 = new JSONArray();
			for (int i = 0; i < array.size(); i++) {
				JSONObject json = array.getJSONObject(i);
				String name = json.getString("name");
				if(StringUtils.equalsIgnoreCase(name, "creation_time@orderBy")){
					json.put("name", "wo_date@orderBy");
				}
				else if(StringUtils.equalsIgnoreCase(name, "creation_time")){
					json.put("name", "wo_date");
				}
				String value = req.getParameter(name);
				if(value != null){
					json.put("value", value);
				}
				else{
					if("NONE".equals(json.getString("operation"))){
						continue;
					}
				}
				array2.add(json);
			}
			condition = array2.toJSONString();
			Cookie cookie;
			try {
				cookie = new Cookie(cookieKye, URLEncoder.encode(condition, "utf8"));
				cookie.setPath("/");
				cookie.setMaxAge(60 * 60 * 5);
				res.addCookie(cookie);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return condition;
	}

	private void showAddButton(ModelMap map, DcReport report, List<JSONObject> jsons) {
		// 如果类型是单位级别的，则只要没有记录，则可新增
		if (report.getReportType() == 1) {
			map.put("_showAdd", jsons.size() <= 0);
			// 类型是个人的，则当前用户还没有填报的，可新增
		} else if (report.getReportType() == 2) {
			do4ReportTypeIs2(map, jsons);
			// 类型是随意新增的，则可以新增
		} else if (report.getReportType() == 3)
			map.put("_showAdd", true);
	}

	private void do4ReportTypeIs2(ModelMap map, List<JSONObject> jsons) {
		boolean showAdd = true;
		for (JSONObject json : jsons) {
			String createUserId = json.getString("create_user_id");
			if (StringUtils.equals(createUserId, getLoginInfo().getUserId())) {
				showAdd = false;
				break;
			}
		}
		map.put("_showAdd", showAdd);
	}

	private void analyzeParameter(ModelMap map, String condition, JSONArray arrayRtn, String parameter,
			DcDataModel dcm) {
		if (StringUtils.isNotBlank(parameter)) {
			String[] ps = parameter.split("&");
			for (String p : ps) {
				String[] operations = new String[] { "=", " in ", ">=", "<=", "<>", " like ", " notin " };
				doParameter2Json(map, arrayRtn, dcm, p, operations);
			}
			condition = arrayRtn.toJSONString();
		}
	}

	private void doParameter2Json(ModelMap map, JSONArray arrayRtn, DcDataModel dcm, String p, String[] operations) {
		for (String operation : operations) {
			if (StringUtils.contains(p, operation)) {
				String[] pas = p.split(operation);
				if (pas.length == 2) {
					String o = StringUtils.trimToEmpty(pas[1]);
					String key = StringUtils.trimToEmpty(pas[0]);
					if (dcm.getKeySet().contains(key))
						continue;
					fillValue2Json(map, arrayRtn, dcm, operation, o, key);
				}
			}
		}
	}

	private JSONObject fillValue2Json(ModelMap map, JSONArray arrayRtn, DcDataModel dcm, String operation, String o,
			String key) {
		dcm.getKeySet().add(key);
		o = dealAssignedParameter(o);
		DcOperationData dod = new DcOperationData();
		dod.setColumnName(key);
		dod.setOperation(operation);
		dod.setDataValue(o);
		dcm.getDcOperationDatas().add(dod);
		map.put(key, o);
		JSONObject json = new JSONObject();
		json.put("name", key);
		json.put("value", o);
		json.put("operation", operation);
		arrayRtn.add(json);
		return json;
	}

	private void analyzeCondition(ModelMap map, String condition, JSONArray arrayRtn, DcDataModel dcm) {
		JSONArray array;
		if (StringUtils.isNotBlank(condition)) {
			array = JSONArray.parseArray(condition);
			for (int i = 0; i < array.size(); i++) {
				JSONObject object = array.getJSONObject(i);
				String operation = StringUtils.trimToEmpty(object.getString("operation"));
				if(StringUtils.trimToEmpty(operation).equals("NONE"))
					operation = "=";
				String value = StringUtils.trimToEmpty(object.getString("value"));
				String name = StringUtils.trimToEmpty(object.getString("name"));
				if (StringUtils.contains(name, "@"))
					continue;
				dcm.getKeySet().add(name);
				if (StringUtils.isBlank(value) || StringUtils.equals(value, "''")) {
					continue;
				}
				DcOperationData dod = new DcOperationData();
				if (StringUtils.equalsIgnoreCase(operation, "like")) {
					dod.setDataValue("%" + value + "%");
				} else {
					dod.setDataValue(value);
				}
				dod.setOperation(operation);
				dod.setColumnName(object.getString("name").trim());
				dod.setDataType(object.getString("dataType"));
				map.put(name, value);
				if (object.containsKey("alias")) {
					map.put(object.getString("alias"), value);
				}
				dcm.getDcOperationDatas().add(dod);
				arrayRtn.add(object);
			}
		}
	}

	private void analyzeOrderBy(ModelMap map, String condition, List<String> orderBys) {
		JSONArray array;
		if (StringUtils.isNotBlank(condition)) {
			array = JSONArray.parseArray(condition);
			for (int i = 0; i < array.size(); i++) {
				JSONObject object = array.getJSONObject(i);
				String value = StringUtils.trimToEmpty(object.getString("value"));
				String name = StringUtils.trimToEmpty(object.getString("name"));
				if (!StringUtils.contains(name, "@"))
					continue;
				name = StringUtils.substringBefore(name, "@orderBy");
				orderBys.add(name + " " + value);
			}
		}
	}

	private void initListTemplateFile(HttpServletRequest req, DcReport report) {
		String realPath = req.getSession().getServletContext().getRealPath("/");
		String initViewPath = "/datacollection/report/" + report.getReportCode() + "_list_" + System.nanoTime()
				+ RandomStringUtils.randomAlphabetic(3) + ".ftl";
		File file = new File(realPath + initViewPath);
		String content = "<@w.reportData  reportCode=_report.reportCode>";
		content += "<@w.dcRdButton value=\"新增数据\" url=\"/dc/report/detailByDataId/\" + _report.id + \"/0\" reportId=_report.id />";
		content += "<@w.dcEditTemplate value=\"编辑本模板\" reportId=_report.id templateType=\"list\" />";
		content += "<@w.dcRdButton value=\"返回填报列表\" url=\"/dc/report/listReports\" />";
		content += "</@w.reportData>";
		try {
			FileUtils.write(file, content, "utf8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		report.setListTemplatePath(initViewPath);
	}

	private void replaceSpecialReqParameter(ModelMap map, HttpServletRequest req) {
		if (req.getParameter("_userId") == null)
			map.put("_userId", getLoginInfo().getUserId());
		if (req.getParameter("_unitId") == null) {
			map.put("_unitId", getLoginInfo().getUnitId());
		}
		if (req.getParameter("_teacherId") == null) {
			map.put("_teacherId", getLoginInfo().getOwnerId());
		}
		if (req.getParameter("_familyId") == null) {
			map.put("_familyId", getLoginInfo().getOwnerId());
		}
		if (req.getParameter("_studentId") == null) {
			map.put("_studentId", getLoginInfo().getOwnerId());
		}
		if (req.getParameter("_ownerId") == null) {
			map.put("_ownerId", getLoginInfo().getOwnerId());
		}
		if (req.getParameter("_deptId") == null) {
			map.put("_deptId", getLoginInfo().getDeptId());
		}
		if (req.getParameter("_today") == null) {
			map.put("_today", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		}
		if (req.getParameter("_username") == null)
			map.put("_username", getLoginInfo().getUserName());
		map.put("editMode", BooleanUtils.toBoolean(Evn.getString("fw.devModel")));
	}

	private String dealAssignedParameter(String o) {
		o = StringUtils.trimToEmpty(o);
		if (ArrayUtils.contains(new String[] { "{teacherId}", "{familyId}", "{studentId}", "{ownerId}" }, o)) {
			o = getLoginInfo().getOwnerId();
		} else if (StringUtils.equals("{unitId}", o))
			o = getLoginInfo().getUnitId();
		else if (StringUtils.equals("{userId}", o))
			o = getLoginInfo().getUserId();
		else if (StringUtils.equals("{deptId}", o))
			o = getLoginInfo().getDeptId();
		else if (StringUtils.equals("{today}", o))
			o = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
		return o;
	}

	/**
	 * 具体填报的某条数据内容
	 * 
	 * @param map
	 * @param dataId
	 *            数据id
	 * @param reportId
	 *            报表id
	 * @param viewPath
	 *            模板路径，如果为空，则取报表默认的明细模板
	 * @param req
	 * @return
	 */
	@RequestMapping("/detailByDataId/{reportId}/{dataId}")
	@ControllerInfo(ignoreLog=1)
	public String detailByDataId(ModelMap map, @PathVariable String dataId, @PathVariable String reportId,
			HttpServletRequest req) {
		replaceSpecialReqParameter(map, req);
		setReqParameter2Map(map, req);
		dataId = initIsNewMark(map, dataId);
		map.put("_condition", req.getParameter("_condition"));
		map.put("_dataId", dataId);
		req.setAttribute("_map", map);
		DcReport report = dcReportService.findOne(reportId);
		String viewPath = req.getParameter("_viewPath");
		if (report != null) {
			if (StringUtils.isBlank(viewPath) && StringUtils.isBlank(report.getTemplatePath())) {
				initDetailTemplateFile(req, report);
				dcReportService.save(report);
			}

			if (StringUtils.isBlank(viewPath))
				viewPath = report.getTemplatePath();
			map.put("_viewPath", viewPath);
			boolean updatable = setUpdatableMark(map, dataId, report);
			if (req.getParameter("_updatable") == null) {
				map.put("_updatable", updatable);
			} else {
				map.put("_updatable", BooleanUtils.toBoolean(req.getParameter("_updatable")));
			}
		}
		map.put("_reportCode", report.getReportCode());
		map.put("_report", report);
		map.put("_templateType", "detail");
		
		//特殊处理下
		if(StringUtils.equals(reportId, "26349202137943115927811909988307")){
			 String deptId = getLoginInfo().getDeptId();
			 Dept dept = SUtils.dc(deptRemoteService.findOneById(deptId), Dept.class);
			 if(dept != null){
				 String parentId = dept.getParentId();
				 if(StringUtils.equals(parentId, "FF8080813A3EEE1C013A3F631D6F0019")){
					 map.put("_finalUserId", "FF8080813A3EEE1C013A43152ACB09F0");
				 }
				 else if(StringUtils.equals(parentId, "4028819A53A913600153AD01FE251CD8")){
					 map.put("_finalUserId", "FF8080813A3EEE1C013A43152ACB09F0");
				 }
				 else{
					 map.put("_finalUserId", "");
				 }
			 }
		}
		
		map.put("username", getLoginInfo().getUserName());

		return viewPath;
	}

	private boolean setUpdatableMark(ModelMap map, String dataId, DcReport report) {
		boolean updatable;
		if (dcReportService.checkReportTable(report.getReportCode())) {
			JSONObject json = dcReportService.findData(report.getReportCode(), dataId);
			map.put("_data", null != json ? json : new JSONObject());
			int type = report.getReportType();
			if (json == null || (type == 1
					&& StringUtils.equals(getLoginInfo().getUnitId(), JsonUtils.getString(json, "unit_id")))) {
				updatable = true;
			} else if (type == 2
					&& StringUtils.equals(getLoginInfo().getUserId(), JsonUtils.getString(json, "create_user_id"))) {
				updatable = true;
			} else
				updatable = true;
		} else {
			map.put("_data", new JSONObject());
			updatable = true;
		}
		return updatable;
	}

	private void initDetailTemplateFile(HttpServletRequest req, DcReport report) {
		String content = "<@w.reportData  reportCode=_report.reportCode>";
		content += "<@w.dcEditTemplate value=\"编辑本模板\" reportId=_report.id dataId=_dataId templateType=\"detail\" />";
		content += "<@w.saveButton />";
		content += "<@w.dcRdButton value=\"返回\" reportId=_report.id />";
		content += "</@w.reportData>";

		String realPath = req.getSession().getServletContext().getRealPath("/");
		String initViewPath = "/datacollection/report/" + report.getReportCode() + "_detail_" + System.nanoTime()
				+ RandomStringUtils.randomAlphabetic(3) + ".ftl";
		File file = new File(realPath + initViewPath);
		try {
			FileUtils.write(file, content, "utf8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		report.setTemplatePath(initViewPath);
	}

	private String initIsNewMark(ModelMap map, String dataId) {
		if (StringUtils.isBlank(dataId) || StringUtils.equals("0", dataId)) {
			dataId = UuidUtils.generateUuid();
			map.put("_isNew", "1");
			map.put("_dataId", dataId);
		} else {
			map.put("_isNew", "0");
		}
		return dataId;
	}

	private void setReqParameter2Map(ModelMap map, HttpServletRequest req) {
		List<String> list = new ArrayList<String>();
		Enumeration<String> es = req.getParameterNames();
		while (es.hasMoreElements()) {
			String key = es.nextElement();
			String value = req.getParameter(key);
			if (!StringUtils.startsWith(key, "_"))
				list.add(key);
			value = dealAssignedParameter(value);
			map.put(key, value);
		}
		map.put("_keys", list);
	}

	@ResponseBody
	@RequestMapping("/removeByDataId/{reportId}/{dataId}")
	public String remoteByDataId(@PathVariable String reportId, @PathVariable String dataId) {
		DcReport report = dcReportService.findOne(reportId);
		if (report == null)
			return error("找不到记录！");
		dcReportService.removeByDataId(report.getReportCode(), dataId);
		JSONObject json = new JSONObject();
		json.put("dataId", dataId);
		json.put("msg", "删除成功！");
		return json.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/vsql")
	@ControllerInfo(ignoreLog=1)
	public String vsql(@RequestBody String vsql) {
		try {
			vsql = URLDecoder.decode(vsql, "utf8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		List<Object[]> os = dcReportService.findBySql(vsql, null);
		return SUtils.s(os);
	}

	@ResponseBody
	@RequestMapping("/data/{reportCode}/{dataId}")
	@ControllerInfo(ignoreLog=1)
	public String data(@PathVariable String reportCode, @PathVariable String dataId) {
		JSONObject json = dcReportService.findData(reportCode, dataId);
		if (json != null)
			return json.toJSONString();
		return new JSONObject().toJSONString();
	}

	@ResponseBody
	@RequestMapping("/maxReportCode")
	@ControllerInfo(ignoreLog=1)
	public String maxReportCode() {
		String codePrefix = DateFormatUtils.format(new Date(), "yyyyMMdd");
		int max = dcReportService.findMaxCode(codePrefix);
		return codePrefix + StringUtils.leftPad(String.valueOf((max + 1)), 4, "0");
	}

	@ResponseBody
	@RequestMapping("/datas/{reportCode}/{dataId}")
	@ControllerInfo(ignoreLog=1)
	public String datas(@PathVariable String reportCode, @PathVariable String dataId) {
		JSONArray array = dcReportService.findMData(reportCode, dataId);
		if (array != null && array.size() > 0)
			return array.toJSONString();
		return new JSONArray().toJSONString();
	}

	@ResponseBody
	@RequestMapping("/saveReportData")
	@ControllerInfo("保存填报数据，reportCode={_reportCode},dataId={_dataId}")
	public String saveReportData(ModelMap map, @ControllerParam @RequestBody JSONObject data) {

		boolean multi = false;
		for (String key : data.keySet()) {
			if (StringUtils.contains(key, "@")) {
				multi = true;
				break;
			}
		}
		String reportCode = JsonUtils.getString(data, "_reportCode");

		if (multi) {
			if (!dcReportService.checkReportTable("dc_data_m_" + reportCode)) {
				synchronized (this) {
					if (!dcReportService.checkReportTable("dc_data_m_" + reportCode)) {
						dcReportService.createReportMTable(data);
					}
				}
			}
		}

		if (!dcReportService.checkReportTable(reportCode)) {
			synchronized (this) {
				if (!dcReportService.checkReportTable(reportCode)) {
					dcReportService.createReportTable(data);
				}
			}
		}

		if (dcReportService.checkReportTable(reportCode)) {
			if (null == dcReportService.findData(reportCode, data.getString("_dataId"))) {
				initReportData(data);
			} else
				data.put("id", data.getString("_dataId"));
			dcReportService.saveReportData(data);
		}

		return success("保存成功！");
	}

	private void initReportData(JSONObject data) {
		Semester seme = Semester.dc(semesterRemoteService.getCurrentSemester(1));
		String acadyear;
		Integer semester;
		if (seme == null) {
			acadyear = "0000-0000";
			semester = 0;
		} else {
			acadyear = seme.getAcadyear();
			semester = seme.getSemester();
		}
		data.put("id", data.getString("_dataId"));
		data.put("unit_id", getLoginInfo().getUnitId());
		data.put("acadyear", acadyear);
		data.put("semester", semester);
		data.put("create_user_id", getLoginInfo().getUserId());
		data.put("creation_time", new Date());
	}
}
