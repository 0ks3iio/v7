package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;

public interface NewGkTimetableTeacherDao extends BaseJpaRepositoryDao<NewGkTimetableTeacher, String>{

	List<NewGkTimetableTeacher> findByTimetableIdIn(String[] timetableIds);

	@Query("from NewGkTimetableTeacher where teacherId=?1 and timetableId in (?2)")
	List<NewGkTimetableTeacher> findByTeacherIdTimetableIds(String teacherId,
			String[] timeTableIds);

	List<NewGkTimetableTeacher> findByTeacherId(String teacherId);

	NewGkTimetableTeacher findByTimetableId(String timetibleId);

	@Query("select tt from NewGkTimetable tb, NewGkTimetableTeacher tt "
			+ "where tb.id = tt.timetableId "
			+ "and tt.teacherId in (?1) and tb.arrayId =?2")
	List<NewGkTimetableTeacher> findByTeacherIds(String[] teacherIds, String arrayId);

	@Query("select ttt.teacherId from NewGkTimetableTeacher ttt,NewGkTimetable tt where ttt.timetableId = tt.id and tt.arrayId =?1")
	List<String> findTeachersByArrayId(String arrayId);

    // Basedata Sync Method
    void deleteByTeacherIdIn(String... teacherIds);
}
