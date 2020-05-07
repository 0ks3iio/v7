package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.dto.DeptDto;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.DeptService;
import net.zdsoft.basedata.service.UnitService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.ColumnInfoEntity;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.ColumnInfoUtils;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.UuidUtils;

@Controller
@RequestMapping("/basedata")
public class DeptAction extends BaseAction {

	private static Logger log = Logger.getLogger(DeptAction.class);

	@Autowired
	private UserService userService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private UnitService unitService;

	@RequestMapping("/dept/index/page")
	@ControllerInfo("部门首页")
	public String index(String sex, ModelMap map, HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession) {
		LoginInfo loginInfo = getLoginInfo(httpSession);
		String unitId = loginInfo.getUnitId();
		map.put("unitId", unitId);
		return "/basedata/dept/deptIndex.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "/dept/{id}/delete", method = RequestMethod.DELETE)
	@ControllerInfo("删除部门")
	public String doDelete(ModelMap map, HttpServletRequest request, HttpServletResponse response,
			@PathVariable String id) {

		List<Dept> depts = deptService.findByParentId(id);
		if (CollectionUtils.isNotEmpty(depts))
			return error("此部门存在下级部门，不能删除！");
		List<User> users = userService.findByDeptId(id);
		if (CollectionUtils.isNotEmpty(users)) {
			return error("此部门下存在用户信息，不能删除！");
		}
		deptService.deleteAllByIds(id);
		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/unit/{unitId}/dept/ztree")
	@ControllerInfo("显示部门树")
	public String showDeptZtree(@PathVariable String unitId, ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession sesion) {
		List<Dept> depts = deptService.findByUnitId(unitId);
		JSONArray array = new JSONArray();
		Unit unit = unitService.findOne(unitId);
		if (unit != null) {
			Json json = new Json();
			json.putEx("id", unit.getId()).putEx("pId", "").putEx("name", unit.getUnitName()).putEx("type", "unit");
			json.put("title", unit.getUnitName());
			json.put("open", true);
			array.add(json);
		}
		for (Dept dept : depts) {
			Json json = new Json();
			if (StringUtils.equals(Constant.GUID_ZERO, dept.getParentId())) {
				json.put("pId", unitId);
			} else {
				json.put("pId", dept.getParentId());
			}
			json.put("type", "dept");
			json.putEx("id", dept.getId()).putEx("name", dept.getDeptName()).putEx("title", dept.getDeptName());
			array.add(json);
		}
		return success(JSONUtils.toJSONString(array));
	}

	@ResponseBody
	@RequestMapping("/unit/{unitId}/dept")
	@ControllerInfo("列出单位内部门")
	public String depts(@PathVariable String unitId, ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession sesion) {
		List<Dept> depts = deptService.findByUnitId(unitId);
		return Json.toJSONString(depts);
	}

	@ResponseBody
	@RequestMapping("/dept/{parentId}/dept")
	@ControllerInfo("列出{parentId}下级部门")
	public String showSubdepts(@PathVariable String parentId, ModelMap map, HttpServletRequest request,
			HttpServletResponse response, HttpSession sesion) {
		List<Dept> depts = deptService.findByParentId(parentId);
		// 列表显示的时候，将本部门也显示出来
		depts.add(0, deptService.findOne(parentId));
		return Json.toJSONString(depts);
	}

	@ResponseBody
	@RequestMapping("/dept/update")
	@ControllerInfo("更新部门信息")
	public String doUpdate(@RequestBody DeptDto deptDto, ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Dept dept = deptDto.getDept();
			String id = dept.getId();
			Dept deptOri = deptService.findOne(id);
			EntityUtils.copyProperties(dept, deptOri, true);
			deptService.saveAllEntitys(deptOri);
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("", "保存部门失败，请联系管理员！", e.getMessage());
		}
		return returnSuccess();
	}

	@ResponseBody
	@RequestMapping("/dept/save")
	@ControllerInfo("保存新增部门")
	public String doSave(@RequestBody DeptDto deptDto, ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Dept dept = deptDto.getDept();
			dept.setId(UuidUtils.generateUuid());
			dept.setInstituteId(Constant.GUID_ONE);
			dept.setIsDeleted(Constant.IS_DELETED_FALSE);
			deptService.saveAllEntitys(dept);
			log.info(Json.toJSONString(dept));
		} catch (Exception e) {
			e.printStackTrace();
			return returnError("", "保存部门失败，请联系管理员！", e.getMessage());
		}
		return returnSuccess();
	}

	@RequestMapping("/dept/{id}/detail/page")
	@ControllerInfo("显示{id}部门明细")
	public String showDept(@PathVariable String id, ModelMap map, HttpServletRequest request,
			HttpServletResponse response) {
		Dept dept = deptService.findOne(id);
		DeptDto dto = new DeptDto();
		dto.setDept(dept);
		map.put("dto", dto);
//		School school = schoolService.findOne(dept.getUnitId());
		List<String> fields = ColumnInfoUtils.getEntityFiledNames(Dept.class);
		Map<String, ColumnInfoEntity> columnInfo = ColumnInfoUtils.getColumnInfos(Dept.class);
		map.put("fields", fields);
		map.put("columnInfo", columnInfo);
		LoginInfo info = getLoginInfo(request.getSession());
		String unitId = info.getUnitId();
		map.put("unitId", unitId);
		return "/basedata/dept/deptDetail.ftl";
	}

	@RequestMapping("/dept/add/page")
	@ControllerInfo("新增部门")
	public String showDeptAdd(ModelMap map, HttpServletRequest request, HttpServletResponse response) {
		Dept dept = new Dept();
		DeptDto dto = new DeptDto();
		dto.setDept(dept);
		LoginInfo info = getLoginInfo(request.getSession());
		String unitId = info.getUnitId();
		dept.setParentId(info.getDeptId());
		dept.setUnitId(unitId);
		dept.setIsDeleted(Constant.IS_DELETED_FALSE);
		map.put("dto", dto);
		map.put("fields", ColumnInfoUtils.getEntityFiledNames(Dept.class));
		Map<String, ColumnInfoEntity> columnInfo = ColumnInfoUtils.getColumnInfos(Dept.class);
		columnInfo.get("parentId");
		map.put("columnInfo", columnInfo);
		map.put("unitId", unitId);
		return "/basedata/dept/deptAdd.ftl";
	}

	@ResponseBody
	@RequestMapping("/unit/{unitId}/depts")
	@ControllerInfo("查看部门下数据")
	public String showTeachersByDeptId(@PathVariable String unitId, String deptId, ModelMap map,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Pagination page = createPaginationJqGrid(request);
		List<Dept> dtos = new ArrayList<Dept>();
		if (StringUtils.isNotBlank(deptId)) {
			// 选中某个部门
			dtos = deptService.findByParentId(deptId, page);
		} else {
			// 单位内所有
			dtos = deptService.findByUnitId(unitId, page);
		}

		return returnJqGridData(page, dtos);
	}
}
