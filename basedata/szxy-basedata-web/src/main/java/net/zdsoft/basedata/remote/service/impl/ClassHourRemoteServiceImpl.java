package net.zdsoft.basedata.remote.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.ClassHour;
import net.zdsoft.basedata.remote.service.ClassHourRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.ClassHourService;
import net.zdsoft.framework.utils.SUtils;

@com.alibaba.dubbo.config.annotation.Service
@Service("classHourRemoteService")
public class ClassHourRemoteServiceImpl extends BaseRemoteServiceImpl<ClassHour,String>
		implements ClassHourRemoteService {
	@Autowired
    private ClassHourService classHourService;
	@Override
	protected BaseService<ClassHour, String> getBaseService() {
		return classHourService;
	}
	
	@Override
	public String findListByUnitId(String acadyear, String semester, String unitId,String gradeId) {
		return SUtils.s(classHourService.findListByUnitId(acadyear, semester, unitId, gradeId,false));
	}
	
}
