package net.zdsoft.basedata.remote.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.RhKeyUnit;
import net.zdsoft.basedata.remote.service.RhKeyUnitRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.RhKeyUnitService;
import net.zdsoft.framework.utils.SUtils;

@Service("rhKeyUnitRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class RhKeyUnitRemoteServiceImpl extends BaseRemoteServiceImpl<RhKeyUnit, String> implements RhKeyUnitRemoteService{
	
	@Autowired
	private RhKeyUnitService rhKeyUnitService;


	@Override
	public String findByUnitIdAnduKeyId(String unitId, String uKeyId) {
		if(StringUtils.isBlank(uKeyId)) {
			return SUtils.s(rhKeyUnitService.findByUnitId(unitId));
		}
		if(StringUtils.isBlank(unitId)) {
			return SUtils.s(rhKeyUnitService.findByUkey(uKeyId));
		}
		return SUtils.s(rhKeyUnitService.findByUnitIdAnduKeyId(unitId,uKeyId));
	}


	@Override
	public String findByUkey(String uKeyId) {
		return SUtils.s(rhKeyUnitService.findByUkey(uKeyId));
	}


	@Override
	protected BaseService<RhKeyUnit, String> getBaseService() {
		return rhKeyUnitService;
	}
	
}
