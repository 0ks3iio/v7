package net.zdsoft.bigdata.system.action;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.entity.BgUserRole;
import net.zdsoft.bigdata.system.service.BgModuleService;
import net.zdsoft.bigdata.system.service.BgUserRoleService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/bigdata/module")
public class BgModuleController extends BigdataBaseAction {

	@Autowired
	private BgModuleService bgModuleService;

	@Autowired
	private BgUserRoleService bgUserRoleService;

	@RequestMapping("/index")
	public String index(ModelMap map) {
		List<BgModule> parentModuleList = bgModuleService
				.findModuleListByParentId(Constant.GUID_ZERO);
		map.put("moduleList", parentModuleList);
		return "/bigdata/system/module/moduleIndex.ftl";
	}

	@RequestMapping("/list")
	public String list(String parentId, String userType, Integer mark,
			Integer common, ModelMap map) {
		List<BgModule> moduleList = new ArrayList<BgModule>();
		List<BgModule> resultList = new ArrayList<BgModule>();
		moduleList = bgModuleService.findAllModuleList();

		for (BgModule module : moduleList) {
			if (StringUtils.isNotBlank(parentId)) {
				if (!parentId.equals(module.getParentId())) {
					continue;
				}
			}
			if (StringUtils.isNotBlank(userType)) {
				if (!module.getUserType().contains(userType)) {
					continue;
				}
			}
			if (mark != null) {
				if (module.getMark() != mark) {
					continue;
				}
			}
			if (common != null) {
				if (module.getCommon() != common) {
					continue;
				}
			}
			resultList.add(module);
		}
		map.put("moduleList", resultList);
		return "/bigdata/system/module/moduleList.ftl";
	}

	@RequestMapping("/moduleUserList")
	public String moduleUserList(String moduleId, ModelMap map) {
		List<BgUserRole> userRoleList = bgUserRoleService
				.findUserRoleListByModuleId(moduleId);
		map.put("moduleId", moduleId);
		map.put("userRoleList", userRoleList);
		return "/bigdata/system/module/moduleUserList.ftl";
	}

	@RequestMapping("/moduleUserSelect")
	public String roleUserSelect(String moduleId, String username,
			String realname, ModelMap map) {
		map.put("isResult", 0);
		map.put("moduleId", moduleId);
		map.put("username", username);
		map.put("realname", realname);
		List<BgUserRole> userRoleList = new ArrayList<BgUserRole>();
		if (StringUtils.isBlank(username) && StringUtils.isBlank(realname)) {
			map.put("userList", userRoleList);
		} else {
			map.put("isResult", 1);
			userRoleList = bgUserRoleService.findUserListWithModule(moduleId,
					username, realname);
			map.put("userRoleList", userRoleList);
		}
		return "/bigdata/system/module/moduleUserSelect.ftl";
	}

	@ResponseBody
	@ControllerInfo("更新mark")
	@RequestMapping("/updateMark")
	public String updateMark(String id, Integer mark) {
		try {
			bgModuleService.updateMarkById(id, mark);
			return success("更新成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("add user")
	@RequestMapping("/addUser")
	public String addUser(String moduleId, String userId) {
		try {
			bgUserRoleService.saveUserModuleRole(moduleId, userId);
			return success("添加成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("delete user")
	@RequestMapping("/deleteUser")
	public String deleteUser(String moduleId, String userId) {
		try {
			bgUserRoleService.deleteByModuleIdAndUserId(moduleId, userId);
			return success("删除成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}
}
