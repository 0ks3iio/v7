package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ClassTeachingEx;

public interface ClassTeachingExService extends BaseService<ClassTeachingEx, String>{

	List<ClassTeachingEx> findByClassTeachingIdIn(String[] classTeachingIds);

	void save(List<ClassTeachingEx> classTeachingExSaveList);

	void deleteByClassTeachingIdAndTeacherIdIn(String classTeachingId,
			String[] teacherIds);
	
	void deleteClassTeachingIdIn(String[] classTeachingIds);

	void deleteByTeacherIds(String... teacherIds);

	void deleteByClassIds(String... classIds);

	void deleteBySubjectIds(String... subjectIds);
}
