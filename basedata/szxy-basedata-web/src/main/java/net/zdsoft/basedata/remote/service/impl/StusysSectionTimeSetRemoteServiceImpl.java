package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.StusysSectionTimeSet;
import net.zdsoft.basedata.remote.service.StusysSectionTimeSetRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.StusysSectionTimeSetService;
import net.zdsoft.framework.utils.SUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("stusysSectionTimeSetRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class StusysSectionTimeSetRemoteServiceImpl extends BaseRemoteServiceImpl<StusysSectionTimeSet,String> implements StusysSectionTimeSetRemoteService {
    
	@Autowired
    private StusysSectionTimeSetService stusysSectionTimeSetService;
    
	@Override
	public String findByAcadyearAndSemesterAndUnitId(String acadyear,
			Integer semester, String unitId,String[] section,boolean upToFind) {
		return SUtils.s(stusysSectionTimeSetService.findByAcadyearAndSemesterAndUnitId(acadyear, semester, unitId, section, upToFind));
	}

	@Override
	protected BaseService<StusysSectionTimeSet, String> getBaseService() {
		return stusysSectionTimeSetService;
	}

	@Override
	public String findByUnitIdIn(String[] unitIds) {
		return SUtils.s(stusysSectionTimeSetService.findByUnitIdIn(unitIds));
	}
	
}
