/*
* Project: v7
* Author : shenke
* @(#) CustomRoleAction.java Created on 2016-10-18
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.zdsoft.basedata.dto.CustomRoleDto;
import net.zdsoft.basedata.dto.CustomRoleUserDto;
import net.zdsoft.basedata.entity.CustomRole;
import net.zdsoft.basedata.entity.CustomRoleUser;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.CustomRoleService;
import net.zdsoft.basedata.service.CustomRoleUserService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;

/**
 * @description: 人员权限设置
 * @author: shenke
 * @version: 1.0
 * @date: 2016-10-18下午3:08:32
 */
@Controller
public class CustomRoleAction extends BaseAction{
	
	@Autowired
	private CustomRoleUserService customRoleUserService;
	@Autowired
	private CustomRoleService customRoleService;
	@Autowired
	private UserService userService;
	
	/**
	 * 兼容6.0的人员权限
	 * @param subsystem
	 * @param map
	 * @return
	 */
	@RequestMapping(value={"/basedata/customrole/customRole/page","system/customrole/customRole.action"})
	@ControllerInfo("子系统下的人员权限列表")
	public String showRoleBySubsystemIndex(String subsystemId, ModelMap map){
		if(StringUtils.isBlank(subsystemId)) {
			return errorFtl(map, "subsystemId 不能为空");
		}
		map.put("subsystem", subsystemId);
		return "/basedata/role/subsystemRole/subsystemRole.ftl";
	}
	/**
	 * 子系统下的人员权限列表:只能修改用户数据
	 * @param subsystem 子系统code
	 * @param map
	 * @return
	 */
	@RequestMapping(value={"/basedata/customrole/customRoleList/page"})
	@ControllerInfo("子系统下的人员权限列表")
	public String showRoleBySubsystem(String subsystem, ModelMap map){
		if(StringUtils.isBlank(subsystem)) {
			return errorFtl(map, "subsystem 不能为空");
		}
		String unitId=getLoginInfo().getUnitId();
		List<CustomRole> customRoleList=customRoleService.findByUnitIdAndSubsystem(unitId,subsystem,true);
		map.put("customRoleList", customRoleList);
		map.put("subsystem", subsystem);
		return "/basedata/role/subsystemRole/subsystemRoleList.ftl";
	}
	@ResponseBody
	@RequestMapping("/basedata/customrole/saveUsers")
	@ControllerInfo("保存人员")
	public String saveUsers(String customRoleId,String userIds,ModelMap map){
		List<String> uIds=new ArrayList<>();
		if(StringUtils.isNotBlank(userIds)) {
			String[] ids=userIds.split(",");
			uIds = EntityUtils.<String>removeEmptyElement(ids);
		}
		
		try {
			if(CollectionUtils.isEmpty(uIds)) {
				customRoleUserService.saveCustomRoleUsers(getLoginInfo().getUnitId(),new String[]{}, customRoleId);
			}else{
				customRoleUserService.saveCustomRoleUsers(getLoginInfo().getUnitId(),uIds.toArray(new String[0]), customRoleId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功！");

	}
	
	/***以上是子系统中人员权限模块操作***/
	
	/***以下暂时不起作用***/
	
	
	@RequestMapping(value={"/{subsystem}/customrole/customRole/"})
	public String execute(@PathVariable("subsystem") String subsystem, ModelMap map){
		try {
			List<CustomRole> roles = customRoleService.findByUnitIdAndSubsystem(getLoginInfo().getUnitId(), subsystem, false);
			List<String> ids = EntityUtils.getList(roles, "id");
			List<CustomRoleUser> roleUsers = customRoleUserService.findListByIn("roleId", ids.toArray(new String[0]));
			List<User> realUsers = userService.findListByIdIn(EntityUtils.getList(roleUsers, "userId").toArray(new String[0])); 
			Map<String,User> realUserMap = EntityUtils.getMap(realUsers, "id", StringUtils.EMPTY);
			
			Map<String,Set<CustomRoleUserDto>> ruMap = Maps.newHashMap();
			for(CustomRoleUser customRoleUser : roleUsers){
				Set<CustomRoleUserDto> customRoleUsers = ruMap.get(customRoleUser.getRoleId());
				
				CustomRoleUserDto dto = new CustomRoleUserDto();
				dto.setCustomRoleUser(customRoleUser);
				User u = realUserMap.get(customRoleUser.getUserId());
				dto.setUserName(u.getRealName());
				
				if(CollectionUtils.isEmpty(customRoleUsers)){
					customRoleUsers = Sets.newHashSet();
					customRoleUsers.add(dto);
					ruMap.put(customRoleUser.getRoleId(), customRoleUsers);
				}else{
					customRoleUsers.add(dto);
				}
				u=null;
			}
			
			//当前单位
			List<User> allUsers = userService.findListByIn("unitId", ArrayUtils.toArray(getLoginInfo().getUnitId()));
			Map<String,User> allUserMap = EntityUtils.getMap(allUsers, "id", StringUtils.EMPTY); 
			
			List<CustomRoleDto> dtos = Lists.newArrayList();
			for(CustomRole role : roles){
				CustomRoleDto roleDto = new CustomRoleDto();
				roleDto.setCustomRole(role);
				
				Set<CustomRoleUserDto> dtoss = ruMap.get(role.getId());
				if(CollectionUtils.isNotEmpty(dtoss)){
					Map<String,User> userMap = Maps.newHashMap();
					for(CustomRoleUserDto roleUserDto : dtoss){
						userMap.put(roleUserDto.getCustomRoleUser().getUserId(), allUserMap.get(roleUserDto.getCustomRoleUser().getUserId()));
					}
					roleDto.setRoleUserMap(userMap);
				}
				dtos.add(roleDto);
			}
			
			
			map.put("users", allUsers);
			map.put("customRoles", dtos);
			map.put("subsystem",subsystem);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/basedata/role/customrole/customRoleIndex.ftl";
	}
	
	@ResponseBody
	@RequestMapping("/{subsystem}/customrole/save")
	public String save(@PathVariable("subsystem") String subsystem,String userIds,String customRoleId,ModelMap map){
		try {

				JSONArray array = JSONArray.parseArray(userIds);
				if(!array.isEmpty()){
					String[] ids = array.toArray(new String[0]);
					List<String> uIds = EntityUtils.<String>removeEmptyElement(ids);
					if(CollectionUtils.isEmpty(uIds)) {
						customRoleUserService.saveCustomRoleUsers(getLoginInfo().getUnitId(),new String[]{}, customRoleId);
					}else{
						customRoleUserService.saveCustomRoleUsers(getLoginInfo().getUnitId(),uIds.toArray(new String[0]), customRoleId);
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			return error("保存失败！"+e.getMessage());
		}
		return success("保存成功！");
	}
}
