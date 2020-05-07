package net.zdsoft.system.action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Region;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.remote.service.RegionRemoteService;
import net.zdsoft.basedata.remote.service.SysProductParamRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.system.dto.common.DomainListDto;
import net.zdsoft.system.entity.LoginDomain;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.SystemIniRemoteService;
import net.zdsoft.system.service.LoginDomainService;
import net.zdsoft.system.service.ServerRegionService;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录的多域名配置
 */
@Controller
@RequestMapping(value = "/system/ops/loginDomain")
public class LoginDomainAction extends BaseAction {

	private Logger logger = LoggerFactory.getLogger(LoginDomainAction.class);

	@Autowired
	private SysOptionRemoteService sysOptionRemoteService;

	@Autowired
	private SystemIniRemoteService systemIniRemoteService;
	@Autowired
	private SysProductParamRemoteService sysProductParamRemoteService;
	@Autowired
	private ServerRegionService serverRegionService;
	@Autowired
	private RegionRemoteService regionRemoteService;
	@Autowired
	private UnitRemoteService unitRemoteService;
	@Autowired
	private LoginDomainService loginDomainService;
	
	@RequestMapping(value = "/index/page", method = RequestMethod.GET)
	public String loginRegionIndex(String regionCode) {
		return "/system/ops/loginRegionIndex.ftl";
	}
	
	@RequestMapping(value = "/List/page", method = RequestMethod.GET)
	public String showDomainList(String unitName,ModelMap map) {
		List<LoginDomain> loginDomains = new ArrayList<>();
		if(StringUtils.isNotBlank(unitName)){
			Set<String> uidList = new HashSet<>();
			List<Unit> unitList = Unit.dt(unitRemoteService.findByUnitName("%"+unitName+"%"));
			if(CollectionUtils.isNotEmpty(unitList)){
				uidList = EntityUtils.getSet(unitList, Unit::getId);
				loginDomains = loginDomainService.findByUnitIdIn(uidList.toArray(new String[uidList.size()]));
			}
		}else{
			loginDomains = loginDomainService.findAll();
			loginDomains = EntityUtils.filter2(loginDomains, t->{
        		return t.getIsDeleted() == LoginDomain.DEFAULT_IS_DELETED_VALUE;
        	});
		}
		List<DomainListDto> domainListDtos = new ArrayList<>();
		if(CollectionUtils.isNotEmpty(loginDomains)){
			Set<String> regionCodes = EntityUtils.getSet(loginDomains, LoginDomain::getRegion);
			Set<String> unitIds = EntityUtils.getSet(loginDomains, LoginDomain::getUnitId);
			List<Region> regionList = SUtils.dt(regionRemoteService.findByFullCodes(Region.TYPE_1,
					regionCodes.toArray(new String[regionCodes.size()])),Region.class);
			Map<String, String> regionNameMap = EntityUtils.getMap(regionList, Region::getFullCode, Region::getRegionName);
			List<Unit> unList = Unit.dt(unitRemoteService.findListByIds(unitIds.toArray(new String[unitIds.size()])));
			Map<String, String> unitNameMap  = EntityUtils.getMap(unList, Unit::getId, Unit::getUnitName);
			for (LoginDomain loginDomain : loginDomains) {
				DomainListDto domainListDto = new DomainListDto();
				domainListDto.setUnitId(loginDomain.getUnitId());
				domainListDto.setRegion(loginDomain.getRegion());
				domainListDto.setRegionAdmin(loginDomain.getRegionAdmin());
				domainListDto.setRegionName(regionNameMap.get(loginDomain.getRegion()));
				domainListDto.setUnitName(unitNameMap.get(loginDomain.getUnitId()));
				domainListDtos.add(domainListDto);
			}
		}
		map.put("domainDtos", domainListDtos);
		return "/system/ops/loginRegionList.ftl";
	}

	@ResponseBody
	@RequestMapping("/delete")
	@ControllerInfo("删除记录")
	public String deleteRecord(String unitId) {
		try {
			if(StringUtils.isNotBlank(unitId))
			  loginDomainService.deleteByUnitId(unitId);
		} catch (Exception e) {
			e.printStackTrace();
			return error("删除失败");
		}
		return success("删除成功");
	}
	
	
}
