package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.basedata.dto.TeacherDto;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.ControllerException;
import net.zdsoft.framework.echarts.entity.sub.Series;
import net.zdsoft.framework.entity.ColumnInfoEntity;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.ServletUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/basedata")
public class TeacherAction extends BaseAction {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private DeptService deptService;

	@Autowired
	private UserService userService;

	@Autowired
	private McodeRemoteService mcodeRemoteService;

	@ResponseBody
	@RequestMapping("/teacher/{id}/update")
	@ControllerInfo("更新教师，ID: {id}")
	public String doUpdate(@PathVariable String id, @RequestBody TeacherDto dto, ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {

		if (!StringUtils.equals(id, dto.getTeacher().getId())) {
			throw new ControllerException("数据主键不匹配！");
		}
		Teacher teacherO = teacherService.findOne(id);
		EntityUtils.copyProperties(dto.getTeacher(), teacherO, true);
		
		try {
			if (StringUtils.isNotBlank(dto.getUser().getUsername())) {
				User jcname=userService.findByUsername(dto.getUser().getUsername());
				if(jcname!=null && !id.equals(jcname.getOwnerId())){
					//throw new ControllerException("用户名已存在，请修改！");
					return returnError("","用户名已存在，请修改！");
				}
				User userO = userService.findByOwnerId(id);
				if(userO==null){
					userO=new User();
					userO.setIsDeleted(Constant.IS_DELETED_FALSE);
					userO.setOwnerId(teacherO.getId());
					userO.setOwnerType(User.OWNER_TYPE_TEACHER);
					userO.setUserRole(1);
					userO.setIconIndex(1);
					userO.setUnitId(teacherO.getUnitId());
				}
				EntityUtils.copyProperties(dto.getUser(), userO, true);
				teacherService.saveWithUser(teacherO, userO);
			}else{
				teacherService.saveAllEntitys(teacherO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("", "保存失败，请联系管理员！", e.getMessage());
		}
		return success("修改成功！");
	}

	@ResponseBody
	@RequestMapping("/teacher/save")
	@ControllerInfo("保存新增教师")
	public String doAddSave(@RequestBody TeacherDto dto, ModelMap map, HttpServletRequest request, HttpServletResponse response) {
		Teacher teacher = dto.getTeacher();
		try {
			User jcname=userService.findByUsername(dto.getUser().getUsername());
			if(jcname!=null){
				return returnError("","用户名已存在，请修改！");
			}
			teacher.setId(UuidUtils.generateUuid());
			teacher.setIsDeleted(Constant.IS_DELETED_FALSE);
			User user = dto.getUser();
			if (StringUtils.isNotBlank(user.getUsername())) {
				user.setIsDeleted(Constant.IS_DELETED_FALSE);
				user.setOwnerId(teacher.getId());
				user.setOwnerType(User.OWNER_TYPE_TEACHER);
				user.setUserRole(1);
				user.setIconIndex(1);
				user.setId(UuidUtils.generateUuid());
				user.setEnrollYear(0);
				teacherService.saveWithUser(teacher, dto.getUser());
			} else
				teacherService.saveAllEntitys(teacher);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("保存失败，请联系管理员！", e.getMessage());
		}

		return success("新增成功！");
	}

	@RequestMapping("/teacher/{id}/detail/page")
	@ControllerInfo("查看教师，ID：{id}")
	public String showTeacherDetail(@PathVariable String id, ModelMap map) {
		TeacherDto dto = new TeacherDto();
		Teacher teacher = teacherService.findOne(id);
		User user = userService.findByOwnerId(teacher.getId());
		if(user==null){
			user=new User();
		}
		dto.setUser(user);
		dto.setTeacher(teacher);
		map.put("dto", dto);
		map.put("fields", ColumnInfoUtils.getEntityFiledNames(Teacher.class));
		map.put("columnInfo", ColumnInfoUtils.getColumnInfos(Teacher.class));
		Map<String, ColumnInfoEntity> userColumnInfo = ColumnInfoUtils.getColumnInfos(User.class);
		userColumnInfo = ColumnInfoUtils.copyColumnInfo(userColumnInfo);
//		userColumnInfo.get("username").setReadonly(true);
//		userColumnInfo.get("realName").setHide(true);
		map.put("userFields", ColumnInfoUtils.getEntityFiledNames(User.class));
		map.put("userColumnInfo", userColumnInfo);
		return "/basedata/teacher/teacherDetail.ftl";
	}

	@RequestMapping("/teacher/add/page")
	@ControllerInfo("新增教师")
	public String showTeacherAdd(ModelMap map, HttpSession httpSession) {
		TeacherDto dto = new TeacherDto();
		Teacher teacher = new Teacher();
		User user = new User();
		dto.setUser(user);
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		String deptId = loginInfo.getDeptId();
		teacher.setUnitId(unitId);
		teacher.setDeptId(deptId);
		teacher.setSex(9);//默认设置未说明性别
		dto.setTeacher(teacher);
		map.put("dto", dto);
		map.put("fields", ColumnInfoUtils.getEntityFiledNames(Teacher.class));
		map.put("columnInfo", ColumnInfoUtils.getColumnInfos(Teacher.class));
		map.put("userFields", ColumnInfoUtils.getEntityFiledNames(User.class));
		map.put("userColumnInfo", ColumnInfoUtils.getColumnInfos(User.class));
		return "/basedata/teacher/teacherAdd.ftl";
	}

	@ResponseBody
	@RequestMapping("/unit/{unitId}/statTeachersByDept")
	@ControllerInfo("按部门统计教师情况（echarts）")
	public String doStatTeacherByDept(@PathVariable String unitId) {
		List<Dept> depts = deptService.findByUnitId(unitId);
		List<String> deptNames = new ArrayList<String>();
		List<McodeDetail> xbs = SUtils.dt(mcodeRemoteService.findByMcodeIds("DM-XB"),new TypeReference<List<McodeDetail>>(){});
		McodeDetail md = new McodeDetail();
		md.setMcodeContent("总数");
		md.setThisId("");
		xbs.add(md);
		List<String> xbnames = new ArrayList<String>();
		for (McodeDetail md2 : xbs) {
			xbnames.add(md2.getMcodeContent());
		}

		List<Teacher> teachers = teacherService.findByUnitId(unitId);
		Map<String, Integer> countMap = new HashMap<String, Integer>();
		for (Teacher t : teachers) {
			String key = t.getDeptId() + t.getSex();
			Integer count = countMap.get(t.getDeptId() + t.getSex());
			if (count == null)
				count = 0;
			countMap.put(key, ++count);
			key = t.getDeptId();
			count = countMap.get(key);
			if (count == null)
				count = 0;
			countMap.put(key, ++count);
		}
		for (Dept dept : depts) {
			deptNames.add(dept.getDeptName());
		}

		Json bar = new Json();

		Json tooltip = new Json();
		tooltip.put("trigger", "bar");
		tooltip.put("axisPointer", new Json().put("type", "shadow"));
		bar.put("tooltip", tooltip);

		Json title = new Json();
		title.put("text", "单位教师统计(按部门)");
		title.put("subtext", "点击图例刷新教师列表");
		bar.put("title", title);

		bar.put("grid", new Json().putEx("left", "0%").putEx("right", "0%").putEx("bottom", "0%").putEx("containLabel", true));

		Json dataZoom = new Json();
		dataZoom.put("realtime", false);
		dataZoom.put("show", true);
		dataZoom.put("start", 0);
		dataZoom.put("end", 30);
		bar.put("dataZoom", dataZoom);

		Json legend = new Json();
		legend.put("data", xbnames);
		bar.put("legend", legend);

		Json xAxis = new Json();
		xAxis.put("type", "category");
		xAxis.put("data", deptNames);
		bar.put("xAxis", xAxis);

		JSONArray yAxis = JSON.parseArray("[{'type':'value'}]");
		bar.put("yAxis", yAxis);

		List<Json> series = new ArrayList<Json>();
		for (McodeDetail md3 : xbs) {
			Json data = new Json();
			data.put("name", md3.getMcodeContent());
			data.put("type", "bar");

			Series s = new Series();
			s.setName(md3.getMcodeContent());
			s.setType("bar");
			List<Json> ls = new ArrayList<Json>();
			for (Dept dept : depts) {
				Json j = new Json();
				j.put("name", dept.getId() + "," + md3.getThisId());
				j.put("value", countMap.get(dept.getId() + md3.getThisId()) + "");
				ls.add(j);
			}
			data.put("data", ls);
			series.add(data);
		}
		bar.put("series", series);

		Json toolbox = new Json();
		Json feature = new Json();
		feature.put("magicType", "{show: true, type: ['line', 'bar']}");
		feature.put("restore", "{show: true}");
		feature.put("saveAsImage", "{show: true}");
		feature.put("dataView", "{show: true, readOnly: false}");
		feature.put("mark", "{show: true}");
		toolbox.put("feature", feature);
		bar.put("toolbox", toolbox);
		return JSON.toJSONString(bar);
	}

	@ResponseBody
	@RequestMapping("/unit/{unitId}/teacher")
	@ControllerInfo("查看单位内教师数据")
	public String showTeachers(@PathVariable String unitId, ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession sesion) {
		Pagination page = createPaginationJqGrid(request);
		List<Teacher> teachers = teacherService.findByUnitId(unitId, page);
		Set<String> teacherIds = new HashSet<String>();
		Set<String> deptIds = new HashSet<String>();

		for (Teacher t : teachers) {
			teacherIds.add(t.getId());
			deptIds.add(t.getDeptId());
		}
		List<User> users = new ArrayList<User>();
		if (CollectionUtils.isNotEmpty(teacherIds)) {
			users = userService.findListByIn("ownerId", teacherIds.toArray(new String[0]));
		}
		Map<String, User> userMap = new HashMap<String, User>();
		for (User u : users) {
			userMap.put(u.getOwnerId(), u);
		}
		List<Dept> depts = new ArrayList<Dept>();
		if (CollectionUtils.isNotEmpty(deptIds)) {
			depts = deptService.findListByIn("id", deptIds.toArray(new String[0]));
		}
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		for (Dept u : depts) {
			deptMap.put(u.getId(), u);
		}
		List<TeacherDto> dtos = new ArrayList<TeacherDto>(teachers.size());
		for (Teacher t : teachers) {
			TeacherDto dto = new TeacherDto();
			dto.setTeacher(t);
			User u = userMap.get(t.getId());
			dto.setUser(u);
			Dept d = deptMap.get(t.getDeptId());
			dto.setDept(d);
			dtos.add(dto);
		}
		return returnJqGridData(page, dtos);
	}

	@ResponseBody
	@RequestMapping("/teacher/{id}/delete")
	@ControllerInfo("删除教师")
	public String doDelete(@PathVariable String id, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		User user = userService.findByOwnerId(id);
		if (user != null) {
			return error("已经存在关联用户，不能删除！");
		}
		teacherService.deleteAllByIds(id);
		return success("删除成功！");
	}

	@RequestMapping("/teacher/index/page")
	@ControllerInfo("查看教师管理首页")
	public String index(String sex, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		LoginInfo loginInfo = ServletUtils.getLoginInfo(getSession());
		String unitId = loginInfo.getUnitId();
		map.put("unitId", unitId);
		return "/basedata/teacher/teacherIndex.ftl";
	}

	@RequestMapping("/dept/{deptId}/teacher/page")
	@ControllerInfo("查看部门教师列表")
	public String showDeptTeachers(@PathVariable String deptId, ModelMap map) {
		map.put("deptId", deptId);
		return "/basedata/teacher/teacherList.ftl";
	}

	@RequestMapping("/unit/{unitId}/teacher/page")
	@ControllerInfo("查看单位教师列表")
	public String showUnitTeachers(@PathVariable String unitId, ModelMap map) {
		map.put("unitId", unitId);
		return "/basedata/teacher/teacherList.ftl";
	}

	@ResponseBody
	@RequestMapping("/dept/{deptId}/teacher")
	@ControllerInfo("查看部门内教师数据")
	public String showTeachersByDeptId(@PathVariable String deptId, Integer sex, ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Pagination page = createPagination(request);
		List<Teacher> teachers = teacherService.findByDeptId(deptId, page);
		Set<String> teacherIds = new HashSet<String>();
		Set<String> deptIds = new HashSet<String>();

		for (Teacher t : teachers) {
			teacherIds.add(t.getId());
			deptIds.add(t.getDeptId());
		}
		List<User> users = new ArrayList<User>();
		if (CollectionUtils.isNotEmpty(teacherIds)) {
			users = userService.findListByIn("ownerId", teacherIds.toArray(new String[0]));
		}
		Map<String, User> userMap = new HashMap<String, User>();
		for (User u : users) {
			userMap.put(u.getOwnerId(), u);
		}
		List<Dept> depts = new ArrayList<Dept>();
		if (CollectionUtils.isNotEmpty(deptIds)) {
			depts = deptService.findListByIn("id", deptIds.toArray(new String[0]));
		}
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		for (Dept u : depts) {
			deptMap.put(u.getId(), u);
		}
		List<TeacherDto> dtos = new ArrayList<TeacherDto>(teachers.size());
		for (Teacher t : teachers) {
			TeacherDto dto = new TeacherDto();
			dto.setTeacher(t);
			User u = userMap.get(t.getId());
			dto.setUser(u);
			Dept d = deptMap.get(t.getDeptId());
			dto.setDept(d);
			dtos.add(dto);
		}
		return returnJqGridData(page, dtos);
	}
}
