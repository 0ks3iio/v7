package net.zdsoft.basedata.remote.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.TeacherSubject;
import net.zdsoft.basedata.remote.service.TeacherSubjectRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.TeacherSubjectService;
import net.zdsoft.framework.utils.SUtils;

@Service("teacherSubjectRemoteService")
public class TeacherSubjectRemoteServiceImpl extends BaseRemoteServiceImpl<TeacherSubject, String> implements TeacherSubjectRemoteService {

	@Autowired
	private TeacherSubjectService teacherSubjectService;
	@Override
	protected BaseService<TeacherSubject, String> getBaseService() {
		return teacherSubjectService;
	}
	@Override
	public String findListByTeacherIdIn(String[] teacherIds) {
		return SUtils.s(teacherSubjectService.findListByTeacherIdIn(teacherIds));
	}

	

}
