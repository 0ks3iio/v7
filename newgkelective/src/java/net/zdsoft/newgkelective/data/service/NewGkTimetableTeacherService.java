package net.zdsoft.newgkelective.data.service;

import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;

public interface NewGkTimetableTeacherService extends BaseService<NewGkTimetableTeacher, String>{

	void deleteByTimetableIdIn(String[] timeTableIds);

	void saveAllEntity(List<NewGkTimetableTeacher> insertTeacherList);

	List<NewGkTimetableTeacher> findByTimetableIds(String[] array);

	void saveOrDel(String arrayId,List<NewGkTimetableTeacher> insertList, String[] timetableIds);

	List<NewGkTimetableTeacher> findByTeacherIdTimetableIds(String teacherId,
			String[] timeTableIds);

	List<NewGkTimetableTeacher> findByTeacherId(String teacherId);

	NewGkTimetableTeacher findByTimetableId(String timetibleId);

	void deleteAndsave(List<String> timetableTeachIdList,
			List<NewGkTimetableTeacher> newGkTimetableTeacherList);
	
	List<NewGkTimetableTeacher> findByTeacherIds(Set<String> teacherIds, String arrayId);
	
	List<String> findTeachersByArrayId(String arrayId);

    // Basedata Sync Method
    void deleteByTeacherIds(String... teacherIds);
}
