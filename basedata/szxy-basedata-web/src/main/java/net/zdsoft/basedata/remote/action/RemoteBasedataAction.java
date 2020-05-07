package net.zdsoft.basedata.remote.action;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.basedata.entity.Semester;
import net.zdsoft.basedata.service.*;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author weixh
 * 2018年8月16日	
 */
@Controller
@RequestMapping("/remote/common/basedata")
public class RemoteBasedataAction extends BaseAction {
	@Autowired
	private SemesterService semesterService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private ClassService classService;
	@Autowired
	private StudentService studentService;

	@ResponseBody
	@RequestMapping("/remoteClazzList")
	@ControllerInfo("获取行政班列表")
	public String remoteClazzList(HttpServletRequest request) {
		String unitId= request.getParameter("unitId");
		String acadyear = request.getParameter("acadyear");
		String gradeCode = request.getParameter("gradeCode");
		List<Grade> glist = gradeService.findByUnitIdAndCurrentAcadyear(unitId, acadyear, null, true);
		String gradeId = "";
		String gradeName="";
		for (Grade grade : glist) {
			if(StringUtils.equals(gradeCode, grade.getGradeCode())){
				gradeId = grade.getId();
				gradeName = grade.getGradeName();
			}
		}
		List<Clazz> clslist = classService.findBySchoolIdAndGradeIds(unitId, gradeId);
		JSONObject json = new JSONObject();
		List<Json> result = new ArrayList<>();
		for (Clazz e : clslist) {
			if(e.getIsGraduate() == 2) {
				continue;
			}
			Json data = new Json();
			data.put("id", e.getId());
			data.put("className", gradeName + e.getClassName());
			result.add(data);
		}
		json.put("infolist", result);
		return json.toJSONString();
	}
	@ResponseBody
	@RequestMapping("/remoteAcadyearList")
	@ControllerInfo("获取学年列表")
	public String remoteAcadyearList(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		List<Json> result = new ArrayList<>();
		semesterService.findAcadeyearList().forEach(e -> {
			Json data = new Json();
			data.put("id", e);
			data.put("name", e + "学年");
			result.add(data);
		});
		json.put("infolist", result);
		return json.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/remoteSemesterList")
	@ControllerInfo("获取学期列表")
	public String remoteSemesterList(HttpServletRequest request) {
		JSONObject json = new JSONObject();

		Json json1 = new Json();
		json1.put("id", "1");
		json1.put("name", "第1学期");
		Json json2 = new Json();
		json2.put("id", "2");
		json2.put("name", "第2学期");
		json.put("infolist", Lists.newArrayList(json1, json2));
		return json.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/remoteCurrentSemester")
	@ControllerInfo("获取当前学年学期")
	public String remoteCurrentSemester(HttpServletRequest request) {
		int type = NumberUtils.toInt(request.getParameter("type"));
		String unitId = StringUtils.trimToEmpty(request.getParameter("unitId"));
		Semester cs = semesterService.findCurrentSemester(type, unitId);
		return SUtils.s(cs);
	}
	
	@ResponseBody
	@RequestMapping("/remoteSemester")
	@ControllerInfo("获取学年学期")
	public String remoteSemester(HttpServletRequest request) {
		String unitId = StringUtils.trimToEmpty(request.getParameter("unitId"));
		String acadyear = request.getParameter("acadyear");
		int semester = NumberUtils.toInt(request.getParameter("semester"));
		Semester cs = semesterService.findByAcadyearAndSemester(acadyear, semester, unitId);
		return SUtils.s(cs);
	}
	
	@ResponseBody
	@RequestMapping("/remoteGradeForSch")
	@ControllerInfo("获取学校年级")
	public String remoteGradeForSch(HttpServletRequest request) {
		String unitId = StringUtils.trimToEmpty(request.getParameter("unitId"));
		List<Json> result = new ArrayList<>();
		if (StringUtils.isNotBlank(unitId)) {
			gradeService.findByUnitId(unitId).forEach(e -> {
				Json data = new Json();
				data.put("id", e.getGradeCode());
				data.put("name", e.getGradeName());
				result.add(data);
			});
		}
		JSONObject json = new JSONObject();
		json.put("infolist", result);
		return json.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/remoteUnit")
	@ControllerInfo("获取学校年级")
	public String remoteUnit(HttpServletRequest request) {
		String unitId = StringUtils.trimToEmpty(request.getParameter("unitId"));
		int unitClass = NumberUtils.toInt(request.getParameter("unitId"));
		int unitType = NumberUtils.toInt(request.getParameter("unitType"));
		JSONArray array = new JSONArray();
		if (StringUtils.isEmpty(unitId)) {
			
		}
		return array.toJSONString();
	}

	@ResponseBody
	@RequestMapping("/remoteStudentList")
	@ControllerInfo("根据行政班级获取学生")
	public String remoteStudentList(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String classId = StringUtils.trimToEmpty(request.getParameter("classId"));
		List<Json> result = new ArrayList<>();
		studentService.findByClassIdIn(null, new String[]{classId}).forEach(e -> {
			Json data = new Json();
			data.put("id", e.getId());
			data.put("name", e.getStudentName());
			result.add(data);
		});
		json.put("infolist", result);
		return json.toJSONString();
	}
}
