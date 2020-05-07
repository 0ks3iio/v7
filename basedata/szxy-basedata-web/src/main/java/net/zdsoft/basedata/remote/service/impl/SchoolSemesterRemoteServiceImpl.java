package net.zdsoft.basedata.remote.service.impl;

import net.zdsoft.basedata.entity.SchoolSemester;
import net.zdsoft.basedata.remote.service.SchoolSemesterRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.SchoolSemesterService;
import net.zdsoft.framework.utils.SUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("schoolSemesterRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class SchoolSemesterRemoteServiceImpl extends BaseRemoteServiceImpl<SchoolSemester,String> implements
		SchoolSemesterRemoteService {

	@Autowired
    private SchoolSemesterService schoolSemesterService;
	
	@Override
	protected BaseService<SchoolSemester, String> getBaseService() {
		return schoolSemesterService;
	}

	@Override
	public String findBySchoolIdIn(String[] schoolIds) {
		 return SUtils.s(schoolSemesterService.findBySchoolIdIn(schoolIds));
	}

}
