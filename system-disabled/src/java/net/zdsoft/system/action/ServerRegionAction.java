package net.zdsoft.system.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.system.constant.Constant;
import net.zdsoft.system.entity.LoginDomain;
import net.zdsoft.system.entity.ServerRegion;
import net.zdsoft.system.entity.server.Server;
import net.zdsoft.system.enums.server.AppStatusEnum;
import net.zdsoft.system.enums.server.ServerClassEnum;
import net.zdsoft.system.service.LoginDomainService;
import net.zdsoft.system.service.ServerRegionService;
import net.zdsoft.system.service.config.SysOptionService;
import net.zdsoft.system.service.server.ServerService;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 多域名指定设置
 * @author ke_shen@126.com
 * @since 2018/2/5 上午11:44
 */
@Controller
@RequestMapping("system/ops/serverRegion")
public class ServerRegionAction extends BaseAction {

	@Autowired
	private ServerService serverService;
	@Autowired
	private ServerRegionService serverRegionService;
	@Autowired
	private SysOptionService sysOptionService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private LoginDomainService loginDomainService;

	@RequestMapping("index")
	public ModelAndView index() {
		User topAdmin = SUtils.dc(userRemoteService.findTopAdmin(), User.class);
		Map<String, Object> map = new HashMap<>();
		map.put("initLicense", topAdmin != null);
		String regionCode = sysOptionService.findValueByOptionCode(Constant.REGION_CODE);
		Region region = SUtils.dc(regionRemoteService.findByFullCode(regionCode), Region.class);
		map.put("regionCode", regionCode);
		map.put("regionName", region != null ? region.getFullName() : "");
		return new ModelAndView("/system/ops/serverRegion.ftl").addObject(map);
	}

	@RequestMapping("list")
	public String serverRegionList(String regionCode, ModelMap map, String unitId) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(regionCode)) {
			if (org.apache.commons.lang3.StringUtils.length(regionCode) < 6) {
				regionCode = org.apache.commons.lang3.StringUtils.rightPad(regionCode, 6, "0");
			}
			List<ServerRegion> serverRegions ;
			if(StringUtils.isNotBlank(unitId)){
				serverRegions = serverRegionService.findByRegionAndUnitId(regionCode,unitId);
				if(CollectionUtils.isEmpty(serverRegions)){
					Unit unit = unitRemoteService.findOneObjectById(unitId);
					if(unit != null){
						Integer unitClass = unit.getUnitClass();
						String  parentId  = unit.getParentId();
						if(unitClass != null && !( unitClass == Unit.UNIT_CLASS_EDU && Unit.TOP_UNIT_GUID.equals(parentId))){
							serverRegions = serverRegionService.findByRegionAndUnitId(regionCode,parentId);
						}
					}
				}
			}else{
				serverRegions = serverRegionService.findByRegion(regionCode);
				serverRegions = EntityUtils.filter2(serverRegions, t->{
	        		return t.getUnitId() == null;
	        	});
			}
			Map<Integer,ServerRegion> serverRegionmap = serverRegions.stream()
					.collect(Collectors.toMap(ServerRegion::getServerId, s-> s));
			List<Server> serverList = serverService.findListBy(new String[]{"status", "serverClass"},
					new Integer[]{AppStatusEnum.ONLINE.getValue(), ServerClassEnum.INNER_PRODUCT.getValue()});
			List<Server> outServerList = serverService.findListBy(new String[]{"status", "serverClass"},
					new Integer[]{AppStatusEnum.ONLINE.getValue(), ServerClassEnum.AP_PRODUCT.getValue()});
			serverList.addAll(outServerList);
			String fileUrl = sysOptionService.getFileUrl(getRequest().getServerName());
			ServerRegion temp;
			for (Server server : serverList) {
				if (StringUtils.isNotBlank(server.getIconUrl())) {
					server.setIconUrl(UrlUtils.ignoreLastRightSlash(fileUrl) + "/" + server.getIconUrl()); //得到应用图标引用地址
				}
				temp = serverRegionmap.get(server.getId());
				if (temp == null) {
					server.setServerRegionId(UuidUtils.generateUuid());
					server.setDomain("");
				} else {
					server.setServerRegionId(temp.getId());
					server.setDomain(temp.getDomain());
					server.setIndexUrl(temp.getIndexUrl());
					server.setPort(temp.getPort());
					server.setProtocol(temp.getProtocol());
					server.setContext(temp.getContextPath());
				}
			}
            map.put("unitId", unitId);
			map.put("servers", serverList);
		}
		return "/system/ops/serverRegionList.ftl";
	}

	@ResponseBody
	@RequestMapping(value = "update")
	public String updateRegionServer(ServerRegion serverRegion) {
		String regionCode = serverRegion.getRegion();
		serverRegion.setRegion(org.apache.commons.lang3.StringUtils.rightPad(regionCode, 6, "0"));
		serverRegionService.save(serverRegion);
		return success("更新成功");
	}
	
	@ResponseBody
	@RequestMapping("unitList")
	public String unitRegionList(String regionCode, String isDomain ,ModelMap map) {
		List<Unit> unitList = new ArrayList<>();
		if (org.apache.commons.lang3.StringUtils.isNotBlank(regionCode)) {
			unitList = Unit.dt(unitRemoteService.findByRegionCode(regionCode+"%"));
			if(StringUtils.isNotBlank(isDomain) && "true".equals(isDomain)){
				Map<String, LoginDomain> unitMap = new HashMap<>();
				List<LoginDomain> loginDomains = loginDomainService.findAll();
				if(CollectionUtils.isNotEmpty(loginDomains)){
					loginDomains = EntityUtils.filter2(loginDomains, t->{
						return t.getIsDeleted() == LoginDomain.DEFAULT_IS_DELETED_VALUE;
					});
					unitMap = EntityUtils.getMap(loginDomains, LoginDomain::getUnitId);
				}
				List<Unit> removeUnits = new ArrayList<>();
				for (Unit unit : unitList) {
					String uid = unit.getId();
					if(unitMap!= null && unitMap.get(uid)!= null){
						removeUnits.add(unit);
					}
				}
				unitList.removeAll(removeUnits);
			}
	    }
		map.put("unitList", unitList);
		return SUtils.s(map);
	}
}
