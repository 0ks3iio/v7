package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeacherSubject;

public interface TeacherSubjectRemoteService extends BaseRemoteService<TeacherSubject,String> {

	public String findListByTeacherIdIn(String[] teacherIds);
}
