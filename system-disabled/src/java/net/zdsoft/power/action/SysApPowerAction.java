package net.zdsoft.power.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.LoginInfo;
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
import net.zdsoft.system.remote.service.ServerRemoteService;

/**
 * @author yangsj  2018年6月7日下午2:12:48
 */
@Controller
@RequestMapping("/system/ap/power")
public class SysApPowerAction extends BaseAction {

	@Autowired
	private SysApPowerService sysApPowerService;
	@Autowired
	private SysPowerService sysPowerService;
	@Autowired
	private ServerRemoteService serverRemoteService;
	@Autowired
	private SysUserPowerService sysUserPowerService;
	
	@ResponseBody
    @ControllerInfo("保存权限信息")
    @RequestMapping("/savePower")
    public String savePower(String powerId,@RequestBody String power) {
		try {
			//判断角色名称是否已经存在 
        	JSONObject jsonObject = SUtils.dc(power, JSONObject.class);
        	String value = jsonObject.getString("value");
        	String serverId = jsonObject.getString("serverId");
        	String source = jsonObject.getString("source");
        	SysPower sysPower = null;
        	if(PowerConstant.OTHER_SOURCE_VALUE == Integer.valueOf(source) &&  StringUtils.isNotBlank(serverId)) {
        		List<SysApPower> sysApPowers = sysApPowerService.findByServerId(Integer.valueOf(serverId));
        		Stream<String> powerIdStr = sysApPowers.stream().map(SysApPower::getPowerId);
        		Set<String> powerIdList = powerIdStr.collect(Collectors.toSet());
        		if(CollectionUtils.isNotEmpty(powerIdList)) {
        			sysPower = sysPowerService.findByValueAndIdIn(value,powerIdList.toArray(new String[powerIdList.size()]));
        		}
        	}else {
        		sysPower = sysPowerService.findByValueAndSource(value,PowerConstant.DEFAULT_SOURCE_VALUE);
        	}
        	if(sysPower != null) {
        		if(StringUtils.isNotBlank(powerId) ){
        			if(!(powerId.equals(sysPower.getId()))){
        				return error("该权限的特征值重复，请重新输入！");
        			}
        		}else {
        			return error("该权限的特征值重复，请重新输入！");
        		}
        	}
        	sysPower = getSysPower(powerId, power);
        	//添加默认权限
        	LoginInfo info = getLoginInfo();
        	if(PowerConstant.DEFAULT_SOURCE_VALUE == sysPower.getSource()) {
        		sysPower.setSource(PowerConstant.DEFAULT_SOURCE_VALUE);
        	}else {
        		//保存关联关系
        		sysPower.setSource(PowerConstant.OTHER_SOURCE_VALUE);
        		sysApPowerService.deleteBypowerIdAndServerId(powerId,null);
        		SysApPower sysApPower = new SysApPower();
        		sysApPower.setId(UuidUtils.generateUuid());
        		sysApPower.setPowerId(sysPower.getId());
        		sysApPower.setServerId(sysPower.getServerId());
        		sysApPower.setUnitId(info.getUnitId());
        		sysApPowerService.save(sysApPower);
        	}
        	sysPower.setUnitId(info.getUnitId());
        	sysPowerService.save(sysPower);
		} catch (Exception e) {
			return error("保存失败！"+e.getMessage());
		}
        return success("保存成功");
    }
	
	@ResponseBody
    @ControllerInfo("删除权限信息")
    @RequestMapping("/deletePower")
    public String deletePower(String powerId) {
		try {
			SysPower sysPower = sysPowerService.findOne(powerId);
			if(sysPower == null) {
				return error("权限信息不存在！");
			}
			if(PowerConstant.OTHER_SOURCE_VALUE == sysPower.getSource()) {
				//删除关联关系
				sysApPowerService.deleteBypowerIdAndServerId(powerId,null);
			}
			sysPowerService.delete(sysPower);
		} catch (Exception e) {
			return error("删除失败! "+e.getMessage());
		}
        return success("删除成功");
    }
	
    @ControllerInfo("查找权限信息")
    @RequestMapping("/findPowerList/page")
    public String findPowerList(String source,String serverId,String userId,ModelMap map) {
		List<ApPowerDto> apPowerDtos = new ArrayList<>();
		List<SysPower> sysPowers = new ArrayList<>();
		Map<Integer,Server> idServerMap = new HashMap<>();
		Map<String, Integer> pidMap = new HashMap<>();
		//查看默认的权限
		LoginInfo info = getLoginInfo();
		try {
			if(StringUtils.isNotBlank(source)) {
				if(PowerConstant.DEFAULT_SOURCE_VALUE == Integer.valueOf(source)) {
					sysPowers = sysPowerService.findBySourceAndUnitId(Integer.valueOf(source),info.getUnitId());
				}else {
					sysPowers = getAllSysPowerByUser(sysPowers, idServerMap, pidMap, serverId);
				}
			}else if (StringUtils.isNotBlank(serverId)) {
				List<SysApPower> sysApPowers = sysApPowerService.findByServerIdAndUnitId(Integer.valueOf(serverId),info.getUnitId());
				if(CollectionUtils.isNotEmpty(sysApPowers)) {
					Set<String> powerIds = getSetPowerIds(sysApPowers);
					sysPowers = sysPowerService.findListByIdIn(powerIds.toArray(new String[powerIds.size()]));
					pidMap.putAll(sysApPowers.stream().collect(Collectors.toMap(SysApPower::getPowerId, SysApPower::getServerId)));
				}
				Server server = SUtils.dc(serverRemoteService.findOneById(Integer.valueOf(serverId)), Server.class);
				idServerMap.put(Integer.valueOf(serverId), server);
			}else {
				List<SysPower> defaultPowers = sysPowerService.findBySourceAndUnitId(PowerConstant.DEFAULT_SOURCE_VALUE,info.getUnitId());
				sysPowers.addAll(defaultPowers);
				List<SysPower> allServerPower = new ArrayList<>();
				allServerPower = getAllSysPowerByUser(allServerPower, idServerMap, pidMap, serverId);
				sysPowers.addAll(allServerPower);
			}
			//判断用户是已经授权 usetId
			Map<String, SysUserPower> supMap = new HashMap<>();
			if(StringUtils.isNotBlank(userId)) {
				List<SysUserPower>  SUPList = sysUserPowerService.findByTargetIdAndType(userId,SysUserPower.TYPE_USER_VALUE);
				if(CollectionUtils.isNotEmpty(SUPList)) {
					supMap = SUPList.stream().collect(Collectors.toMap(SysUserPower::getPowerId, Function.identity()));
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
					//判断用户是否授权
					if(!supMap.isEmpty() && supMap.get(sysPower.getId()) != null) {
						apPowerDto.setIsEmpower("true");
					}else {
						apPowerDto.setIsEmpower("false");
					}
					apPowerDtos.add(apPowerDto);
				}
			}
		    map.put("apPowerDtos", apPowerDtos);
		    if(StringUtils.isNotBlank(userId)) {
		    	map.put("isEmpower", Boolean.TRUE);
		    	map.put("userId", userId);
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
			Map<String, Integer> pidMap, String serverId) {
		LoginInfo info = getLoginInfo();
		List<Server> serverList = new ArrayList<>();
		if (StringUtils.isNotBlank(serverId)) {
			serverList.add(SUtils.dc(serverRemoteService.findOneById(Integer.valueOf(serverId)), Server.class));
		}else {
			serverList.addAll(SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class));
		}
		if(CollectionUtils.isNotEmpty(serverList)) {
			idServerMap.putAll(serverList.stream().collect(Collectors.toMap(Server::getId, Function.identity())));
			Stream<Integer> sidStr = serverList.stream().map(Server::getId);
    		Set<Integer> serverIds = sidStr.collect(Collectors.toSet());
			List<SysApPower> sysApPowers = sysApPowerService.findByServerIdInAndUnitId(serverIds.toArray(new Integer[serverIds.size()]),info.getUnitId());
			if(CollectionUtils.isNotEmpty(sysApPowers)) {
				pidMap.putAll(sysApPowers.stream().collect(Collectors.toMap(SysApPower::getPowerId, SysApPower::getServerId)));
				Set<String> powerIds = getSetPowerIds(sysApPowers);
				sysPowers = sysPowerService.findListByIdIn(powerIds.toArray(new String[powerIds.size()]));
			}
		}
		return sysPowers;
	}

	
	@RequestMapping("/register/power/page")
    @ControllerInfo("编辑权限列表")
	public String showPowerList(String powerId,String isSee,ModelMap map){
		if(StringUtils.isNotBlank(powerId)) {
			SysPower sysPower = sysPowerService.findOne(powerId);
			if(PowerConstant.OTHER_SOURCE_VALUE == sysPower.getSource()) {
				SysApPower sysApPower = sysApPowerService.findByPowerId(sysPower.getId());
				map.put("serverId", sysApPower.getServerId());
			}
			map.put("sysPower", sysPower);
			map.put("isSee", "true".equals(isSee)?Boolean.TRUE : Boolean.FALSE);
		}
		LoginInfo info = getLoginInfo();
		List<Server> serverList = SUtils.dt(serverRemoteService.findByOwnerTypeAndUnitIdAndUnitClass(info.getOwnerType(), info.getUnitId(), info.getUnitClass()), Server.class);
		map.put("serverList", serverList);
    	return "/system/power/pw/powerRegister.ftl";
	}
	
	/**
	 * 解析页面，得到syspower
	 * @param powerId
	 * @param power
	 * @return 
	 */
	private SysPower getSysPower(String powerId, String power) {
		SysPower sysPower;
		JSONObject jsonObject = SUtils.dc(power, JSONObject.class);
		String description = jsonObject.getString("description");
		String powerName = jsonObject.getString("powerName");
		String value = jsonObject.getString("value");
		String source = jsonObject.getString("source");
		String serverId = jsonObject.getString("serverId");
		String isActive = jsonObject.getString("isActive");
		if(StringUtils.isNotBlank(powerId)) {
			sysPower = sysPowerService.findOne(powerId);
		}else {
			sysPower = new SysPower();
			sysPower.setId(UuidUtils.generateUuid());
		}
		sysPower.setDescription(description);
    	sysPower.setPowerName(powerName);
    	sysPower.setValue(value);
    	sysPower.setServerId(Integer.valueOf(serverId));
    	sysPower.setSource(StringUtils.isBlank(source)?PowerConstant.DEFAULT_SOURCE_VALUE : Integer.valueOf(source));
    	if(StringUtils.isBlank(isActive)) {
    		isActive = PowerConstant.IS_ACTIVE_TRUE_VALUE;
    	}
    	sysPower.setIsActive(isActive);
		return sysPower;
	}
	
	private Set<String> getSetPowerIds(List<SysApPower> sysApPowers) {
		Stream<String> pidStr = sysApPowers.stream().map(SysApPower::getPowerId);
		return pidStr.collect(Collectors.toSet());
	}
	
}
