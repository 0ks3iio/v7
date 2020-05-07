package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkTimetableTeacher;

public interface NewGkTimetableTeacherJdbcDao {
	
	public void insertBatch(List<NewGkTimetableTeacher> newGkTimetableTeacherList);
	
	/**
	 * @param ids 可为空
	 * @param timetableIds 可为空
	 */
	public void deleteByIdInOrTimetableIdIn(String[] ids,String[] timetableIds);

}
