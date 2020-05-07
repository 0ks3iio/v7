package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.TeachBuilding;
import net.zdsoft.basedata.remote.service.TeachBuildingRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachBuildingService;
import net.zdsoft.framework.utils.SUtils;

@Service("teachBuildingRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachBuildingRemoteServiceImpl extends BaseRemoteServiceImpl<TeachBuilding,String> implements TeachBuildingRemoteService {

	@Autowired
	private TeachBuildingService teachBuildingService;
	@Override
	protected BaseService<TeachBuilding, String> getBaseService() {
		 return teachBuildingService;
	}
	@Override
	public String findTeachBuildMap(String[] ids) {
		return SUtils.s(teachBuildingService.findTeachBuildMap(ids));
	}
	@Override
	public String findTeachBuildListByUnitId(String unitId) {
		return SUtils.s(teachBuildingService.findByUnitId(unitId));
	}
	@Override
	public String findByUnitIdIn(String[] uidList) {
		return SUtils.s(teachBuildingService.findByUnitIdIn(uidList));
	}

}
