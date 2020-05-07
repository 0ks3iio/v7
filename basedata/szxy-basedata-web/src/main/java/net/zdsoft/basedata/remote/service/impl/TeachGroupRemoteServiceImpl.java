package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.TeachGroup;
import net.zdsoft.basedata.remote.service.TeachGroupRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeachGroupService;
import net.zdsoft.framework.utils.SUtils;

@Service("teachGroupRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class TeachGroupRemoteServiceImpl extends BaseRemoteServiceImpl<TeachGroup, String> implements TeachGroupRemoteService {

	@Autowired
    private TeachGroupService teachGroupService;
	@Override
	public String findBySchoolId(String unitId) {
		return SUtils.s(teachGroupService.findBySchoolId(unitId));
	}

	@Override
	public String findBySchoolId(String unitId, boolean isMakeTeacher) {
		return SUtils.s(teachGroupService.findBySchoolId(unitId,isMakeTeacher));
	}

	@Override
	public String findBySchoolIdAndType(String unitId, Integer type) {
		return SUtils.s(teachGroupService.findBySchoolIdAndType(unitId,type));
	}

	@Override
	public String findTeachers(String unitId) {
		return SUtils.s(teachGroupService.findTeachers(unitId));
	}

	@Override
	protected BaseService<TeachGroup, String> getBaseService() {
		return teachGroupService;
	}
	
	@Override
	public String findBySchoolIdAndSubjectIdIn(String unitId,String[] subids){
		return SUtils.s(teachGroupService.findBySchoolIdAndSubjectIdIn(unitId, subids));
	}
    
}
