package net.zdsoft.bigdata.system.action;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Json;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/bigdata/common/tree")
public class BgCommonTreeAction extends BigdataBaseAction {

	@Autowired
	private UnitRemoteService unitRemoteService;

	@Autowired
	private DeptRemoteService deptRemoteService;

	@Autowired
	private UserRemoteService userRemoteService;

	@RequestMapping("/demo")
	public String demo(ModelMap map) {
		return "/bigdata/demo/treeDemo.ftl";
	}
	
	@RequestMapping("/userTreeIndex")
	public String userTreeIndex(String users, ModelMap map, boolean isChart) {
		if(StringUtils.isBlank(users))
			users="[]";
		map.put("users", users);
		if (isChart) {
			return "/bigdata/system/tree/chartUserTree.ftl";
		}
		return "/bigdata/system/tree/userTree.ftl";
	}

	@RequestMapping("/unitTreeIndex")
	public String unitTreeIndex(String units, ModelMap map, boolean isChart) {
		if(StringUtils.isBlank(units))
			units="[]";
		map.put("units", units);
		if (isChart) {
			return "/bigdata/system/tree/chartUnitTree.ftl";
		}
		return "/bigdata/system/tree/unitTree.ftl";
	}

	@RequestMapping("/shareUserTreeIndex")
	public String shareUserTreeIndex(String users, ModelMap map) {
		if(StringUtils.isBlank(users))
			users="[]";
		map.put("users", users);
		map.put("unitId", getLoginInfo().getUnitId());
		return "/bigdata/system/tree/shareUserTree.ftl";
	}

	@RequestMapping("/shareUserTreeIndex4Bi")
	public String shareUserTreeIndex4Bi(String users, ModelMap map) {
		if(StringUtils.isBlank(users))
			users="[]";
		map.put("users", users);
		map.put("unitId", getLoginInfo().getUnitId());
		return "/bigdata/v3/templates/bi/tree/userSelectTree.ftl";
	}

	@ResponseBody
	@ControllerInfo("获取单位树")
	@RequestMapping("/unitTree")
	public String unitTree(String unitId) {
		if (StringUtils.isBlank(unitId))
			unitId = getLoginInfo().getUnitId();
		List<Unit> unitList = Unit.dt(unitRemoteService
				.getAllSubUnitByParentId(unitId));

		JSONArray array = new JSONArray();
		Json json = null;
		if (CollectionUtils.isNotEmpty(unitList)) {
			for (Unit unit : unitList) {
				json = new Json();
				json.put("pId", unit.getParentId());
				json.put("type", unit.getUnitClass());
				json.put("id", unit.getId());
				json.put("name", unit.getUnitName());
				json.put("title", unit.getUnitName());
				array.add(json);
			}
		}
		return success(JSONUtils.toJSONString(array));
	}

	@ResponseBody
	@ControllerInfo("获取部门树")
	@RequestMapping("/deptTree")
	public String deptTree(String unitId) {
		if (StringUtils.isBlank(unitId))
			unitId = getLoginInfo().getUnitId();
		List<Dept> deptList = Dept.dt(deptRemoteService.findByUnitId(unitId));
		JSONArray array = new JSONArray();
		Json json = null;
		if (CollectionUtils.isNotEmpty(deptList)) {
			for (Dept dept : deptList) {
				json = new Json();
				json.put("pId", dept.getParentId());
				// json.put("type", dept.getUnitClass());
				json.put("id", dept.getId());
				json.put("name", dept.getDeptName());
				json.put("title", dept.getDeptName());
				array.add(json);
			}
		}
		return success(JSONUtils.toJSONString(array));
	}

	@ResponseBody
	@ControllerInfo("根据单位获取用户树")
	@RequestMapping("/userTreeByUnit")
	public String userTreeByUnit(String unitId) {
		JSONArray array = new JSONArray();
		Json json = null;
		List<Dept> deptList = Dept.dt(deptRemoteService.findByUnitId(unitId));
		if (CollectionUtils.isNotEmpty(deptList)) {
			for (Dept dept : deptList) {
				json = new Json();
				json.put("pId", dept.getParentId());
				json.put("type", "dept");
				json.put("id", dept.getId());
				json.put("name", dept.getDeptName());
				json.put("title", dept.getDeptName());
				array.add(json);
			}
		}

		List<User> userList = User.dt(userRemoteService.findByUnitIds(unitId));
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				json = new Json();
				json.put("pId", user.getDeptId());
				json.put("type", "user");
				json.put("id", user.getId());
				json.put("name", user.getRealName());
				json.put("title", user.getRealName());
				array.add(json);
			}
		}
		return success(JSONUtils.toJSONString(array));
	}

	@ResponseBody
	@ControllerInfo("根据部门获取用户树")
	@RequestMapping("/userTreeByDept")
	public String userTreeByDept(String deptId) {
		List<User> userList = User.dt(userRemoteService.findByDeptId(deptId,
				null));
		JSONArray array = new JSONArray();
		Json json = null;
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				json = new Json();
				json.put("pId", user.getDeptId());
				// json.put("type", dept.getUnitClass());
				json.put("id", user.getId());
				json.put("name", user.getRealName());
				json.put("title", user.getRealName());
				array.add(json);
			}
		}
		return success(JSONUtils.toJSONString(array));
	}

}
