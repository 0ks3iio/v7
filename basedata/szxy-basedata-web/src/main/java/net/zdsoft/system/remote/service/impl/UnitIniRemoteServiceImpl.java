package net.zdsoft.system.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.remote.service.impl.BaseRemoteServiceImpl;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.system.entity.config.UnitIni;
import net.zdsoft.system.remote.service.UnitIniRemoteService;
import net.zdsoft.system.service.config.UnitIniService;

@Service("unitIniRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class UnitIniRemoteServiceImpl extends BaseRemoteServiceImpl<UnitIni,String> implements UnitIniRemoteService {

	@Autowired
	private UnitIniService unitIniService;
	
	@Override
	public String getUnitIni(String unitId, String iniId) {
		return SUtils.s(unitIniService.getUnitIni(unitId, iniId));
	}

	@Override
	public void updateNowvalue(String nowValue, String iniid, String unitId) {
		unitIniService.updateNowvalue(nowValue, iniid, unitId);
	}

	@Override
	protected BaseService<UnitIni, String> getBaseService() {
		return unitIniService;
	}

	@Override
	public String getIniList(String iniId) {
		return SUtils.s(unitIniService.getIniList(iniId));
	}

}
