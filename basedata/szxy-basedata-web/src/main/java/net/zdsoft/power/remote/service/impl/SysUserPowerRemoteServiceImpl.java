package net.zdsoft.power.remote.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.power.entity.SysPower;
import net.zdsoft.power.entity.SysUserPower;
import net.zdsoft.power.remote.dto.UserPowerRemoteDto;
import net.zdsoft.power.remote.service.SysUserPowerRemoteService;
import net.zdsoft.power.service.SysPowerService;
import net.zdsoft.power.service.SysUserPowerService;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.remote.service.UserRoleRemoteService;
import net.zdsoft.system.service.user.RoleService;

/**
 * @author yangsj  2018年6月20日上午10:39:24
 */
@Service("sysUserPowerRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SysUserPowerRemoteServiceImpl extends BaseRemoteServiceImpl<SysUserPower, String> implements SysUserPowerRemoteService{

	@Autowired
	private UserRoleRemoteService userRoleRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private SysUserPowerService sysUserPowerService;
	@Autowired
	private RoleService roleService;
	
	@Override
	protected BaseService<SysUserPower, String> getBaseService() {
		return sysUserPowerService;
	}
	
	@Override
	public String findPowerByUserName(String userName) {
		JSONObject json = new JSONObject();
		User  user = SUtils.dc(userRemoteService.findByUsername(userName), User.class);
		List<UserPowerRemoteDto> uprDtos = new ArrayList<>(); 
		if(user != null) {
			String userId = user.getId();
			//先得到用户的角色类型
			Map<String,Role> roleMap = new HashMap<>();
			List<SysUserPower>  supList = new ArrayList<>();
			List<UserRole> userRoleList = SUtils.dt(userRoleRemoteService.findByUserId(userId),UserRole.class);
			Set<String> roleIds = EntityUtils.getSet(userRoleList, "roleId");
			if(CollectionUtils.isNotEmpty(roleIds)) {
				List<Role> roleList = roleService.findListByIdIn(roleIds.toArray(new String[roleIds.size()]));
				roleMap = EntityUtils.getMap(roleList, "id");
				List<SysUserPower>  roleSUP = sysUserPowerService.findByTypeAndTargetIdIn
						(SysUserPower.TYPE_ROLE_VALUE,roleIds.toArray(new String[roleIds.size()]));
				supList.addAll(roleSUP);
			}
			//得到用户权限的对应关系
			List<SysUserPower>  userSUP = sysUserPowerService.findByTargetIdAndType(userId,SysUserPower.TYPE_USER_VALUE);
			supList.addAll(userSUP);
			
			Set<String> powerIdList = EntityUtils.getSet(supList, "powerId");
			List<SysPower> spList = sysPowerService.findListByIdIn(powerIdList.toArray(new String[powerIdList.size()]));
			Map<String, SysPower> spMap = EntityUtils.getMap(spList, "id");
			//封装数据
            for (SysUserPower sysUserPower : supList) {
            	UserPowerRemoteDto dto = new UserPowerRemoteDto();
                dto.setType(sysUserPower.getType());
            	dto.setTargetId(sysUserPower.getTargetId());
            	SysPower sysPower = spMap.get(sysUserPower.getPowerId());
            	dto.setDescription(sysPower.getDescription());
            	dto.setIsActive(sysPower.getIsActive());
            	dto.setPowerName(sysPower.getPowerName());
            	dto.setSource(sysPower.getSource());
            	dto.setValue(sysPower.getValue());
            	String name;
            	if(sysUserPower.getType() == SysUserPower.TYPE_ROLE_VALUE) {
            		name = roleMap.get(sysUserPower.getTargetId()).getName();
            	}else {
            		name = "用户";
            	}
            	dto.setTypeName(name);
            	uprDtos.add(dto);
			}
		}
		json.put("data", uprDtos);
		json.put("dataCount", CollectionUtils.isEmpty(uprDtos)? 0: uprDtos.size());
		return json.toJSONString();
	}
	

}
