package net.zdsoft.power.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.power.constant.PowerConstant;
import net.zdsoft.power.dto.ApRoleDto;
import net.zdsoft.power.dto.UserRoleDto;
import net.zdsoft.power.entity.SysApRole;
import net.zdsoft.power.entity.SysUserPower;
import net.zdsoft.power.service.SysApRoleService;
import net.zdsoft.power.service.SysUserPowerService;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.enums.YesNoEnum;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.UserRoleRemoteService;
import net.zdsoft.system.service.user.RoleService;

/**
 * @author yangsj  2018年6月7日下午2:14:51
 */
@Controller
@RequestMapping("/system/ap/role")
public class SysApRoleAction extends BaseAction {

	@Autowired
	private SysApRoleService sysApRoleService;
	@Autowired
	private ServerRemoteService serverRemoteService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private UserRoleRemoteService userRoleRemoteService;
	@Autowired
	private SysUserPowerService sysUserPowerService;

	@RequestMapping("/showIndex/page")
    @ControllerInfo("进入包装页面")
	public String showIndex(ModelMap map){
    	return "/system/power/role/roleShowIndex.ftl";
	}
	
	
	@RequestMapping("/showRoleIndex/page")
    @ControllerInfo("查看角色列表")
	public String showRoleIndex(ModelMap map){
		LoginInfo info = getLoginInfo();
		List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
		map.put("serverList", serverList);
    	return "/system/power/role/roleIndex.ftl";
	}
	
	@RequestMapping("/roleUserIndex/page")
    @ControllerInfo("查看角色列表")
	public String showRoleUserIndex(String roleId,ModelMap map){
		map.put("roleId", roleId);
    	return "/system/power/role/roleUserIndex.ftl";
	}
	
	@ResponseBody
    @ControllerInfo("保存角色信息")
    @RequestMapping("/saveRole")
    public String doSaveRole(String roleId,@RequestBody String body) {
        try {
        	//判断角色名称是否已经存在 
        	JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
    		String roleName = jsonObject.getString("roleName");
    		LoginInfo info = getLoginInfo();
    		Role role = roleService.findByNameAndUnitId(roleName,info.getUnitId());
    		if(StringUtils.isNotBlank(roleId)) {
    			if(role != null && !(roleId.equals(role.getId())) ) {
    				return error("角色名称重复，请重新输入！");
    			}
    		}else {
    			if(role != null) {
    				return error("角色名称重复，请重新输入！");
    			}
    		}
        	role = getRole(roleId, body);
        	roleService.save(role);
			//保存关联关系
			if(role.getServerId() != null && StringUtils.isEmpty(roleId)) {
				SysApRole sysApRole = new SysApRole();
				sysApRole.setRoleId(role.getId());
				sysApRole.setServerId(role.getServerId());
				sysApRole.setId(UuidUtils.generateUuid());
				sysApRole.setUnitId(role.getUnitId());
				sysApRoleService.save(sysApRole);
			}
		} catch (Exception e) {
			return error("保存失败！"+e.getMessage());
		}
        return success("保存成功");
    }

	@ResponseBody
    @ControllerInfo("删除角色信息")
    @RequestMapping("/deleteRole")
    public String doDeleteRole(String roleId) {
		try {
			Role role = roleService.findById(roleId);
			if(role == null) {
				return error("角色信息不存在！");
			}
			roleService.delete(role);  //考虑是否要删除  roleDao.deleteRole(roleIds);rolePermDao.deleteRolePerm(roleIds);userRoleRelationService.deleteUserRole(idsString);
			//根据roleid 来删除对应关系表数据
			sysApRoleService.deleteByRoleId(roleId);
			sysUserPowerService.deleteByTargetIdAndType(roleId,SysUserPower.TYPE_ROLE_VALUE);
		} catch (Exception e) {
			return error("删除失败! "+e.getMessage());
		}
        return success("删除成功");
    }
	
    @ControllerInfo("查找角色信息")
    @RequestMapping("/findRoleList/page")
    public String showRoleList(String source, String serverId,ModelMap map) {
		Map<Integer,Server> serverMap = new HashMap<>();
		List<Role> roleList = new ArrayList<>();
		List<SysApRole> sysApRoleList = new ArrayList<>();
		try {
			LoginInfo info = getLoginInfo();
			boolean isNotDefault = true;
			//查找默认角色
			if(StringUtils.isNotBlank(source)) {
				if( PowerConstant.DEFAULT_SOURCE_VALUE == Integer.valueOf(source)) {
					 roleList = roleService.findByUnitIdAndRoleType(info.getUnitId(),
			 				PowerConstant.ROLE_TYPE_OPER);
					 isNotDefault = false;
				}
			}else if(StringUtils.isBlank(serverId)){
				roleList = roleService.findByUnitIdAndRoleType(info.getUnitId(),
						PowerConstant.ROLE_TYPE_OPER);
			}
			if(isNotDefault) {
				if(StringUtils.isNotBlank(serverId)) {
					sysApRoleList = sysApRoleService.findByServerIdAndUnitId(Integer.valueOf(serverId),info.getUnitId());
					Server server = SUtils.dc(serverRemoteService.findOneById(Integer.valueOf(serverId)), Server.class);
					serverMap.put(Integer.valueOf(serverId), server);
				}else {
					sysApRoleList = getRoleListByUser(serverMap, sysApRoleList, info);
				}
			}
			List<ApRoleDto> apRoleDtos = new ArrayList<>();
			//开始封装数据
			if(CollectionUtils.isNotEmpty(sysApRoleList)) {
				Set<String> roleIds = sysApRoleList.stream().map(SysApRole::getRoleId).collect(Collectors.toSet());
				List<Role> roleList1 = roleService.findListByIds(roleIds.toArray(new String[roleIds.size()]));
				Map<String,Role> roleMap = roleList1.stream().collect(Collectors.toMap(Role::getId, Function.identity()));
                sysApRoleList.forEach(sysApRole->{
                	ApRoleDto apRoleDto = new ApRoleDto();
					Server server = serverMap.get(sysApRole.getServerId());
					Role role = roleMap.get(sysApRole.getRoleId());
					//截取过长的Description
					apRoleDto.setRoelDescription(StringUtils.length(role.getDescription()) > 10 ? role.getDescription().substring(0, 10)+"..." : role.getDescription());
					apRoleDto.setRole(role);
					apRoleDto.setServer(server);
					apRoleDto.setType(String.valueOf(PowerConstant.OTHER_SOURCE_VALUE));
					apRoleDtos.add(apRoleDto);
                });
			}
			if(CollectionUtils.isNotEmpty(roleList)) {
				roleList.forEach(role->{
					ApRoleDto apRoleDto = new ApRoleDto();
					apRoleDto.setRole(role);
					apRoleDto.setType(String.valueOf(PowerConstant.DEFAULT_SOURCE_VALUE));
					apRoleDtos.add(apRoleDto);
				});
			}	
			map.put("apRoleDtos", apRoleDtos);
		} catch (Exception e) {
			return errorFtl(map, "查找角色数据异常！！");
		}
		return "/system/power/role/roleList.ftl";
    }
	
	@RequestMapping("/register/role/page")
    @ControllerInfo("编辑角色")
	public String showRegister(String roleId,String isSee,String isEditor,ModelMap map){
		if(StringUtils.isNotBlank(roleId)) {
			Role role = roleService.findOne(roleId);
			if(PowerConstant.ROLE_TYPE_OTHERAP == role.getRoleType()) {
				SysApRole sysApRole = sysApRoleService.findByRoleId(roleId);
				map.put("serverId", sysApRole.getServerId());
				map.put("source", PowerConstant.OTHER_SOURCE_VALUE);
			}else {
				map.put("source", PowerConstant.DEFAULT_SOURCE_VALUE);
			}
			map.put("role", role);
			map.put("isSee", "true".equals(isSee)?Boolean.TRUE : Boolean.FALSE);
			map.put("isEditor", "true".equals(isEditor)?Boolean.TRUE : Boolean.FALSE);
		}
		LoginInfo info = getLoginInfo();
		List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
		map.put("serverList", serverList);
    	return "/system/power/role/roleRegister.ftl";
	}
	
	@RequestMapping("/appoint/role/page")
    @ControllerInfo("角色委派")
	public String showRoleUserList(HttpServletRequest request,String roleId,String ownerType,String userName,String realName,ModelMap map){
		Pagination page=createPagination();
		Map<String, String> paramMap = syncParameters(request);
		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
		if(row<=0){
			page.setPageSize(20);
		}
		List<User> userList = User.dt(userRemoteService.findByUnitIdAndRealNameAndOwnerTypeAndUserTypeAndUserName(getLoginInfo()
                .getUnitId(), realName, StringUtils.isNotBlank(ownerType)?Integer.valueOf(ownerType):null, new Integer[] { User.USER_TYPE_COMMON_USER },userName,SUtils.s(page)), page);
//		Map<String, User> userNameMap = userList.stream().collect(Collectors.toMap(User::getUsername, Function.identity()));
//		if(StringUtils.isNotBlank(userName) && CollectionUtils.isNotEmpty(userList)) {
//			if(userNameMap.get(userName) != null) {
//				User user = userNameMap.get(userName);
//				userList.clear();
//				userList.add(user);
//				page.setMaxRowCount(1);
//			}else {
//				userList.clear();
//			}
//		}
		Set<String> userIdList = EntityUtils.getSet(userList, User::getId);
		if(CollectionUtils.isNotEmpty(userList)) {
			List<UserRole> userRoleList = SUtils.dt(userRoleRemoteService.findByRoleIdAndUserIdIn
					(roleId,userIdList.toArray(new String[userIdList.size()])),UserRole.class);
			Map<String, UserRole> userIdMap = userRoleList.stream().collect(Collectors.toMap(UserRole::getUserId, Function.identity()));
			//封装数据
			List<UserRoleDto> userRoleDtoList = new ArrayList<>();
			userList.forEach(user->{
				UserRoleDto userRoleDto = new UserRoleDto();
				userRoleDto.setRoleId(roleId);
				userRoleDto.setUser(user);
				if(userIdMap.get(user.getId()) != null) {
					userRoleDto.setIsAppoint("true");
				}else {
					userRoleDto.setIsAppoint("false");
				}
				userRoleDtoList.add(userRoleDto);
			});
			map.put("userRoleDtoList", userRoleDtoList);
			map.put("roleId", roleId);
			map.put("userIdList", userIdList);
		}
		sendPagination(request, map, page);
    	return "/system/power/role/roleUserList.ftl";
	}
	
	@ResponseBody
    @ControllerInfo("委派用户角色")
    @RequestMapping("/saveUserRole")
    public String doSaveUserRole(String roleId,@RequestBody String params) {
        try {
        	//判断角色名称是否已经存在 
        	JSONObject jsonObject = SUtils.dc(params, JSONObject.class);
    		List<String>  addUserIdList = JSONObject.parseArray(jsonObject.getString("ids"), String.class);
    		List<String>  allIdList = JSONObject.parseArray(jsonObject.getString("allIds"), String.class);
    		//先删除再新增
    		allIdList.removeAll(addUserIdList);
    		if(CollectionUtils.isNotEmpty(allIdList)) {
    			userRoleRemoteService.deleteByRoleIdAndUserIdIn(roleId,allIdList.toArray(new String[allIdList.size()]));
    		}
    		//添加没有的用户角色
    		if(CollectionUtils.isNotEmpty(addUserIdList)) {
    			List<UserRole> userRoles = SUtils.dt(userRoleRemoteService.findByRoleIdAndUserIdIn
    					(roleId,addUserIdList.toArray(new String[addUserIdList.size()])),UserRole.class);
    			Set<String> userIds = userRoles.stream().map(UserRole::getUserId).collect(Collectors.toSet());
    			addUserIdList.removeAll(userIds);
    			//开始保存
    			if(CollectionUtils.isNotEmpty(addUserIdList)) {
    				List<UserRole> saveUR = new ArrayList<>();
    				addUserIdList.forEach(userId->{
    					UserRole userRole = new UserRole();
    					userRole.setId(UuidUtils.generateUuid());
    					userRole.setRoleId(roleId);
    					userRole.setUserId(userId);
    					saveUR.add(userRole);
    				});
    				userRoleRemoteService.saveAll(SUtils.s(saveUR.toArray(new UserRole[0])));
    			}
    		}
		} catch (Exception e) {
			return error("委派失败！"+e.getMessage());
		}
        return success("委派成功");
    }
	
	
	
	/**
	 * 得到当前用户的所有第三方ap的角色
	 * @param serverMap
	 * @param sysApRoleList
	 * @param info
	 * @return
	 */
	private List<SysApRole> getRoleListByUser(Map<Integer, Server> serverMap, List<SysApRole> sysApRoleList,
			LoginInfo info) {
		List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
		if(CollectionUtils.isNotEmpty(serverList)) {
			Set<Integer> serverIds = serverList.stream().map(Server::getId).collect(Collectors.toSet());
			sysApRoleList.addAll(sysApRoleService.findByServerIdInAndUnitId(serverIds.toArray(new Integer[serverIds.size()]),info.getUnitId()));
			serverMap.putAll(serverList.stream().collect(Collectors.toMap(Server::getId, Function.identity())));
		}
		return sysApRoleList;
	}
	
	/**
	 * 得到角色信息
	 * @param roleId
	 * @param body
	 * @return
	 */
	private Role getRole(String roleId, String body) {
		JSONObject jsonObject = SUtils.dc(body, JSONObject.class);
		String description = jsonObject.getString("description");
		String roleName = jsonObject.getString("roleName");
		String source = jsonObject.getString("roleSource");
		String serverId = jsonObject.getString("serverId");
		String isActive = jsonObject.getString("isActive");
		Role role;
		if(StringUtils.isNotBlank(roleId)) {
			role = roleService.findOne(roleId);
		}else {
			role = new Role();
			role.setId(UuidUtils.generateUuid());
			role.setUnitId(getLoginInfo().getUnitId());
			role.setIsSystem(YesNoEnum.NO.getValue());
		}
		role.setDescription(description);
		role.setName(roleName);
		if(StringUtils.isBlank(isActive)) {
			isActive = String.valueOf(YesNoEnum.YES.getValue());
		}
		role.setIsActive(isActive);
		if(PowerConstant.OTHER_SOURCE_VALUE == Integer.valueOf(source)) {
			role.setRoleType(PowerConstant.ROLE_TYPE_OTHERAP);
			role.setServerId(Integer.valueOf(serverId));
		}else {
			role.setRoleType(PowerConstant.ROLE_TYPE_OPER);
		}
		role.setModifyTime(new Date());
		return role;
	}
	
	
	
}
