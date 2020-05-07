package net.zdsoft.power.action;

import java.util.ArrayList;
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
import net.zdsoft.power.dto.ApPowerDto;
import net.zdsoft.power.entity.SysApPower;
import net.zdsoft.power.entity.SysPower;
import net.zdsoft.power.entity.SysUserPower;
import net.zdsoft.power.service.SysApPowerService;
import net.zdsoft.power.service.SysPowerService;
import net.zdsoft.power.service.SysUserPowerService;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.remote.service.ServerRemoteService;
import net.zdsoft.system.remote.service.UserRoleRemoteService;
import net.zdsoft.system.service.user.RoleService;

/**
 * @author yangsj  2018年6月7日下午2:17:07
 */
@Controller
@RequestMapping("/system/user/power")
public class SysUserPowerAction extends BaseAction {

	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private SysUserPowerService sysUserPowerService;
	@Autowired
	private ServerRemoteService serverRemoteService;
	@Autowired
	private SysApPowerService sysApPowerService;
	@Autowired
	private UserRoleRemoteService userRoleRemoteService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private RoleService roleService;
	
	@RequestMapping("/showIndex/page")
    @ControllerInfo("进入包装页面")
	public String showIndex(ModelMap map){
    	return "/system/power/empower/empwShowIndex.ftl";
	}
	
	@RequestMapping("/index/page")
    @ControllerInfo("查看用户授权页面")
	public String showUserIndex(String type,ModelMap map){
		map.put("type", type);
		if(type.equals("3")) {
			return "/system/power/user/userIndex.ftl";
		}
    	return "/system/power/empower/empwIndex.ftl";
	}
	
	@ControllerInfo("查找权限信息")
    @RequestMapping("/findPowerList/page")
    public String findPowerList(String targetId,String type,String pid,ModelMap map) {
		List<ApPowerDto> apPowerDtos = new ArrayList<>();
		List<SysPower> sysPowers = new ArrayList<>();
		Map<Integer,Server> idServerMap = new HashMap<>();
		Map<String, Integer> pidMap = new HashMap<>();
		//查看默认的权限
		LoginInfo info = getLoginInfo();
		try {
			//默认权限
			List<SysPower> defaultPowers = sysPowerService.findBySourceAndUnitId(PowerConstant.DEFAULT_SOURCE_VALUE,info.getUnitId());
			sysPowers.addAll(defaultPowers);
			if(StringUtils.isNotBlank(pid)) {
				if(Integer.valueOf(pid) != PowerConstant.DEFAULT_SOURCE_VALUE) {
					List<SysApPower> sysApPowers = sysApPowerService.findByServerId(Integer.valueOf(pid));
					if(CollectionUtils.isNotEmpty(sysApPowers)) {
						Set<String> powerIds = sysApPowers.stream().map(SysApPower::getPowerId).collect(Collectors.toSet());
						List<SysPower> sPowers = sysPowerService.findListByIdIn(powerIds.toArray(new String[powerIds.size()]));
						sysPowers.addAll(sPowers);
						pidMap.putAll(sysApPowers.stream().collect(Collectors.toMap(SysApPower::getPowerId, SysApPower::getServerId)));
					}
					Server server = SUtils.dc(serverRemoteService.findOneById(Integer.valueOf(pid)), Server.class);
					idServerMap.put(Integer.valueOf(pid), server);
				}
			}else {
				List<SysPower> allServerPower = new ArrayList<>();
				allServerPower = getAllSysPowerByUser(allServerPower, idServerMap, pidMap);
				sysPowers.addAll(allServerPower);
			}
			Map<String, SysUserPower> supMap = new HashMap<>();
			Map<String, SysUserPower> supMapByUser = new HashMap<>();
			if(StringUtils.isNotBlank(type)) {
				//判断用户是已经授权 userId
				if(StringUtils.isNotBlank(targetId)) {
					List<SysUserPower>  SUPList = sysUserPowerService.findByTargetIdAndType(targetId,Integer.valueOf(type));
					if(CollectionUtils.isNotEmpty(SUPList)) {
						supMap = EntityUtils.getMap(SUPList, SysUserPower::getPowerId);
					}
				}
				//用户授权时， 先查找用户的角色，再查找角色的权限，得到用户的权限；  注意 角色的权限，用户不能修改
				if(SysUserPower.TYPE_USER_VALUE == Integer.valueOf(type)) {
					List<UserRole> userRoleList = SUtils.dt(userRoleRemoteService.findByUserId(targetId),UserRole.class);
					Set<String> roleIds = EntityUtils.getSet(userRoleList, UserRole::getRoleId);
					if(CollectionUtils.isNotEmpty(roleIds)) {
						List<SysUserPower>  SUPList = sysUserPowerService.findByTypeAndTargetIdIn
								(SysUserPower.TYPE_ROLE_VALUE,roleIds.toArray(new String[roleIds.size()]));
						supMapByUser = EntityUtils.getMap(SUPList, SysUserPower::getPowerId);
					}
				}
			}
			//封装数据
			if(CollectionUtils.isNotEmpty(sysPowers)) {
				for (SysPower sysPower : sysPowers) {
					ApPowerDto apPowerDto = new ApPowerDto();
					apPowerDto.setSysPower(sysPower);
					String sourceName;
					if(sysPower.getSource() == PowerConstant.DEFAULT_SOURCE_VALUE) {
						sourceName = "默认权限";
					}else {
						Server server = idServerMap.get(pidMap.get(sysPower.getId()));
						sourceName = server.getName();
					}
					apPowerDto.setSourceName(sourceName);
					//判断是否是用户角色授权
					if(!supMapByUser.isEmpty() && supMapByUser.get(sysPower.getId()) != null) {
						apPowerDto.setIsEmpower("true");
//						apPowerDto.setDescription("角色授权");
						continue;
					}
					//判断用户是否授权
					if(!supMap.isEmpty() && supMap.get(sysPower.getId()) != null) {
						apPowerDto.setIsEmpower("true");
//						if(SysUserPower.TYPE_USER_VALUE == Integer.valueOf(type)) {
//							apPowerDto.setDescription("用户授权");
//						}
					}
					apPowerDtos.add(apPowerDto);
				}
			}
		    map.put("apPowerDtos", apPowerDtos);
		    map.put("targetId", targetId);
		    map.put("type", type);
		    map.put("isEmpower", Boolean.TRUE);
		    if(StringUtils.isBlank(targetId)) {
		    	map.put("isFirst", Boolean.FALSE);
		    }
		} catch (NumberFormatException e) {
			return errorFtl(map, "查找权限数据异常！！");
		}
		return "/system/power/pw/powerList.ftl";
	}
	
	/**
	 * @param sysPowers
	 * @param idServerMap
	 * @param pidMap
	 * @return
	 */
	private List<SysPower> getAllSysPowerByUser(List<SysPower> sysPowers, Map<Integer, Server> idServerMap,
			Map<String, Integer> pidMap) {
		LoginInfo info = getLoginInfo();
		List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
		if(CollectionUtils.isNotEmpty(serverList)) {
			idServerMap.putAll(serverList.stream().collect(Collectors.toMap(Server::getId, Function.identity())));
    		Set<Integer> serverIds = serverList.stream().map(Server::getId).collect(Collectors.toSet());
			List<SysApPower> sysApPowers = sysApPowerService.findByServerIdInAndUnitId(serverIds.toArray(new Integer[serverIds.size()]),info.getUnitId());
			if(CollectionUtils.isNotEmpty(sysApPowers)) {
				pidMap.putAll(sysApPowers.stream().collect(Collectors.toMap(SysApPower::getPowerId, SysApPower::getServerId)));
				Set<String> powerIds = sysApPowers.stream().map(SysApPower::getPowerId).collect(Collectors.toSet());
				sysPowers = sysPowerService.findListByIdIn(powerIds.toArray(new String[powerIds.size()]));
			}
		}
		return sysPowers;
	}
	
	@ResponseBody
    @ControllerInfo("授权用户权限")
    @RequestMapping("/saveUserPower")
    public String doSaveUserPower(String targetId,String type,@RequestBody String params) {
        try {
        	//判断角色名称是否已经存在 
        	JSONObject jsonObject = SUtils.dc(params, JSONObject.class);
    		List<String>  addPIdList = JSONObject.parseArray(jsonObject.getString("ids"), String.class);
    		List<String>  allIdList = JSONObject.parseArray(jsonObject.getString("allIds"), String.class);
    		//先删除再新增
    		if(StringUtils.isNotBlank(targetId)) {
    			if(CollectionUtils.isNotEmpty(allIdList)) {
    				sysUserPowerService.deleteByTargetIdAndPowerIdInAndType(targetId,allIdList.toArray(new String[allIdList.size()]),Integer.valueOf(type));
    			}
    			//添加没有的用户角色
    			if(CollectionUtils.isNotEmpty(addPIdList)) {
    				//开始保存
    				if(CollectionUtils.isNotEmpty(addPIdList)) {
    					List<SysUserPower> saveSUP = new ArrayList<>();
    					addPIdList.forEach(pid->{
    						SysUserPower sysUserPower = new SysUserPower();
    						sysUserPower.setId(UuidUtils.generateUuid());
    						sysUserPower.setTargetId(targetId);
    						sysUserPower.setType(Integer.valueOf(type));
    						sysUserPower.setPowerId(pid);
    						saveSUP.add(sysUserPower);
    					});
    					sysUserPowerService.saveAll(saveSUP.toArray(new SysUserPower[0]));
    				}
    			}
    		}
		} catch (Exception e) {
			return error("授权失败！"+e.getMessage());
		}
        return success("授权成功");
    }
	
	@RequestMapping("/findUser/page")
    @ControllerInfo("查看用户")
	public String showUserList(HttpServletRequest request,String roleId,String ownerType,String userName,String realName,ModelMap map){
		Pagination page=createPagination();
		Map<String, String> paramMap = syncParameters(request);
		int row = NumberUtils.toInt(paramMap.get("_pageSize"));
		if(row<=0){
			page.setPageSize(20);
		}
		List<User> userList = User.dt(userRemoteService.findByUnitIdAndRealNameAndOwnerTypeAndUserTypeAndUserName(getLoginInfo()
	                .getUnitId(), realName, StringUtils.isNotBlank(ownerType)?Integer.valueOf(ownerType):null, new Integer[] { User.USER_TYPE_COMMON_USER },userName,SUtils.s(page)), page);
//		Map<String, User> userNameMap = userList.stream().collect(Collectors.toMap(User::getUsername, Function.identity(),(key1, key2) -> key2));
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
		map.put("userList", userList);
		sendPagination(request, map, page);
    	return "/system/power/empower/empwUserList.ftl";
	}
	
	@RequestMapping("/findUserPower/page")
    @ControllerInfo("查看用户权限")
	public String showUserPowerList(String userId,ModelMap map){
		Map<String, List<SysPower>> showMap = new HashMap<>();
		//先得到用户的角色类型
		Map<String,Role> roleMap = new HashMap<>();
		List<SysUserPower>  SUPList = new ArrayList<>();
		
		List<UserRole> userRoleList = SUtils.dt(userRoleRemoteService.findByUserId(userId),UserRole.class);
		Set<String> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toSet());
		if(CollectionUtils.isNotEmpty(roleIds)) {
			List<Role> roleList = roleService.findListByIdIn(roleIds.toArray(new String[roleIds.size()]));
			roleMap = roleList.stream().collect(Collectors.toMap(Role::getId, Function.identity()));
			List<SysUserPower>  roleSUP = sysUserPowerService.findByTypeAndTargetIdIn
					(SysUserPower.TYPE_ROLE_VALUE,roleIds.toArray(new String[roleIds.size()]));
			SUPList.addAll(roleSUP);
		}
		//得到用户权限的对应关系
		List<SysUserPower>  userSUP = sysUserPowerService.findByTargetIdAndType(userId,SysUserPower.TYPE_USER_VALUE);
		SUPList.addAll(userSUP);
		
		Map<String,List<SysUserPower>> roleIdMap = SUPList.stream().collect(Collectors.groupingBy(SysUserPower::getTargetId));
		
		Set<String> powerIdList = EntityUtils.getSet(SUPList, SysUserPower::getPowerId);
		List<SysPower> SPList = sysPowerService.findListByIdIn(powerIdList.toArray(new String[powerIdList.size()]));
		Map<String, SysPower> spMap = SPList.stream().collect(Collectors.toMap(SysPower::getId, Function.identity()));
		//封装数据
		for (Map.Entry<String, List<SysUserPower>> entry : roleIdMap.entrySet()) {
        	String key =entry.getKey();
        	List<SysUserPower> value = entry.getValue();
        	List<SysPower> sysPowers = new ArrayList<>();
        	for (SysUserPower sysUserPower : value) {
				SysPower sysPower = spMap.get(sysUserPower.getPowerId());
				sysPowers.add(sysPower);
			}
        	String name;
        	if(value.get(0).getType() == SysUserPower.TYPE_ROLE_VALUE) {
        		name = roleMap.get(key).getName();
        		name = "角色("+name+")";
        	}else {
        		name = "用户";
        	}
        	showMap.put(name, sysPowers);
        }
		map.put("showMap", showMap);
    	return "/system/power/empower/empwPowerList.ftl";
	}
}
