package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.CourseType;
import net.zdsoft.basedata.remote.service.CourseTypeRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.CourseTypeService;
@Service("courseTypeRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class CourseTypeRemoteServiceImpl extends BaseRemoteServiceImpl<CourseType, String> implements CourseTypeRemoteService{
	@Autowired
	private CourseTypeService courseTypeService;
	@Override
	protected BaseService<CourseType, String> getBaseService() {
		return courseTypeService;
	}
	
}
