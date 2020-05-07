package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.TeacherSubject;

public interface TeacherSubjectService extends BaseService<TeacherSubject, String> {

	List<TeacherSubject> findListByTeacherIdIn(String[] teacherIds);

}
