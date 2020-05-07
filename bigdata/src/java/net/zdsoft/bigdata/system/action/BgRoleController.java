package net.zdsoft.bigdata.system.action;

import java.util.*;

import net.zdsoft.bigdata.system.entity.BgModule;
import net.zdsoft.bigdata.system.entity.BgRole;
import net.zdsoft.bigdata.system.entity.BgRolePerm;
import net.zdsoft.bigdata.system.entity.BgUserRole;
import net.zdsoft.bigdata.system.service.BgModuleService;
import net.zdsoft.bigdata.system.service.BgRolePermService;
import net.zdsoft.bigdata.system.service.BgRoleService;
import net.zdsoft.bigdata.system.service.BgUserRoleService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.dto.ResultDto;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Json;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;

@Controller
@RequestMapping(value = "/bigdata/role")
public class BgRoleController extends BigdataBaseAction {

	@Autowired
	private BgRoleService bgRoleService;

	@Autowired
	private BgUserRoleService bgUserRoleService;

	@Autowired
	private BgRolePermService bgRolePermService;

	@Autowired
	private BgModuleService bgModuleService;

	@RequestMapping("/index")
	public String index(ModelMap map) {
		return "/bigdata/system/role/roleIndex.ftl";
	}

	@RequestMapping("/roleList")
	public String roleList(String roleId, ModelMap map) {
		List<BgRole> roleList = bgRoleService
				.findRoleListListByUnitId(Constant.GUID_ZERO);
		map.put("roleId", roleId);
		map.put("roleList", roleList);
		return "/bigdata/system/role/roleList.ftl";
	}

	@RequestMapping("/roleDetail")
	public String roleDetail(String roleId, ModelMap map) {
		map.put("roleId", roleId);
		return "/bigdata/system/role/roleDetail.ftl";
	}

	@RequestMapping("/roleModuleList")
	public String roleModuleList(String roleId, ModelMap map) {
		map.put("roleId", roleId);
		return "/bigdata/system/role/roleModuleList.ftl";
	}

	@RequestMapping("/roleUserList")
	public String roleUserList(String roleId, ModelMap map) {
		List<BgUserRole> userRoleList = bgUserRoleService
				.findUserRoleListByRoleId(roleId);
		map.put("roleId", roleId);
		map.put("userRoleList", userRoleList);
		return "/bigdata/system/role/roleUserList.ftl";
	}

	@RequestMapping("/roleUserSelect")
	public String roleUserSelect(String roleId, String username,
			String realname, ModelMap map) {
		map.put("isResult", 0);
		map.put("roleId", roleId);
		map.put("username", username);
		map.put("realname", realname);
		List<BgUserRole> userRoleList = new ArrayList<BgUserRole>();
		if (StringUtils.isBlank(username) && StringUtils.isBlank(realname)) {
			map.put("userList", userRoleList);
		} else {
			map.put("isResult", 1);
			userRoleList = bgUserRoleService.findUserListWithRole(roleId,
					username, realname);
		}
		map.put("userRoleList", userRoleList);
		return "/bigdata/system/role/roleUserSelect.ftl";
	}

	@RequestMapping("/edit")
	public String editRole(String id, ModelMap map) {
		BgRole role = new BgRole();
		if (StringUtils.isNotBlank(id)) {
			role = bgRoleService.findOne(id);
		} else {
			role.setUnitId(Constant.GUID_ZERO);
			Integer maxOrderId = bgRoleService
					.getMaxOrderIdByUnitId(Constant.GUID_ZERO);
			if (maxOrderId == null)
				maxOrderId = 0;
			if (maxOrderId >= 999)
				maxOrderId = 0;
			role.setOrderId(++maxOrderId);
		}
		map.put("role", role);
		return "/bigdata/system/role/roleEdit.ftl";
	}

	@ResponseBody
	@ControllerInfo("add user")
	@RequestMapping("/addUser")
	public String addUser(String roleId, String userId) {
		try {
			bgUserRoleService.saveUserRole(roleId, userId);
			return success("添加成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("delete user")
	@RequestMapping("/deleteUser")
	public String deleteUser(String roleId, String userId) {
		try {
			bgUserRoleService.deleteByRoleIdAndUserId(roleId, userId);
			return success("删除成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo(" 保存role")
	@RequestMapping("/save")
	public String saveRole(BgRole role) {
		try {
			BgRole queryRole = bgRoleService.findRoleByUnitIdAndName(
					role.getUnitId(), role.getName());
			if (StringUtils.isNotBlank(role.getId())) {
				if (queryRole != null
						&& !queryRole.getId().equals(role.getId())) {
					return error("用户组名称重复,请重新维护");
				}
				role.setModifyTime(new Date());
				bgRoleService.update(role, role.getId(), new String[] { "name",
						"belong", "orderId", "description", "modifyTime" });
			} else {
				if (queryRole != null) {
					return error("用户组名称重复,请重新维护");
				}
				role.setCreationTime(new Date());
				role.setModifyTime(new Date());
				role.setType(1);
				role.setId(UuidUtils.generateUuid());
				bgRoleService.save(role);
			}
			return Json.toJSONString(new ResultDto().setSuccess(true)
					.setCode("00").setMsg("保存成功")
					.setBusinessValue(role.getId()));

		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo(" 删除role")
	@RequestMapping("/delete")
	public String deleteRole(String id) {
		try {
			bgRoleService.deleteRole4All(id);
			return success("删除成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo(" 保存role")
	@RequestMapping("/saveRolePerm")
	public String saveRolePerm(String roleId, String moduleIds) {
		try {
			bgRolePermService.saveRolePerm(roleId, moduleIds);
			return success("保存成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("获取权限模块树")
	@RequestMapping("/moduleZtree")
	public String modelZtree(String roleId) {
		// 查找用户组授权模块
		List<BgRolePerm> modelList = bgRolePermService
				.findRolePermListByRoleId(roleId);

		BgRole role = bgRoleService.findOne(roleId);
		Set<String> moduleIds = new HashSet<String>();
		if (CollectionUtils.isNotEmpty(modelList)) {
			for (BgRolePerm rolePerm : modelList) {
				moduleIds.add(rolePerm.getModuleId());
			}
		}
		JSONArray array = new JSONArray();
		Json json = null;

		List<BgModule> allModelList = bgModuleService.findAllModuleList();

		List<BgModule> myValidModuleList = new ArrayList<BgModule>();
		Map<String, List<BgModule>> childModuleMap = new HashMap<String, List<BgModule>>();

		if (CollectionUtils.isNotEmpty(allModelList)) {
			for (BgModule model : allModelList) {
				if (model.getCommon() == 1 && !model.getType().equals("dir"))
					continue;
				if (!model.getUserType().contains(
						String.valueOf(role.getBelong())))
					continue;
				// 如果目录下没有模块就不显示目录
				if (model.getType().equals("item")) {
					List<BgModule> childModuleList = childModuleMap.get(model
							.getParentId());
					if (CollectionUtils.isEmpty(childModuleList))
						childModuleList = new ArrayList<BgModule>();
					childModuleList.add(model);
					childModuleMap.put(model.getParentId(), childModuleList);
				}
				myValidModuleList.add(model);
			}
		}

		for (BgModule model : myValidModuleList) {
			if (model.getType().equals("dir")) {
				if (childModuleMap.containsKey(model.getId())) {
					json = new Json();
					json.put("pId", model.getParentId());
					json.put("type", model.getType());
					json.put("id", model.getId());
					json.put("name", model.getName());
					json.put("title", model.getName());
					if (moduleIds.contains(model.getId())) {
						json.put("checked", true);
					} else {
						if (model.getCommon() == 1
								&& !model.getType().equals("dir")) {
							json.put("checked", true);
						}
					}
                    array.add(json);
				}
			} else {
				json = new Json();
				json.put("pId", model.getParentId());
				json.put("type", model.getType());
				json.put("id", model.getId());
				json.put("name", model.getName());
				json.put("title", model.getName());
				if (moduleIds.contains(model.getId())) {
					json.put("checked", true);
				} else {
					if (model.getCommon() == 1
							&& !model.getType().equals("dir")) {
						json.put("checked", true);
					}
				}
                array.add(json);
			}
		}
		return success(JSONUtils.toJSONString(array));
	}
}
