package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;

public interface NewGkTimetableJdbcDao {
	public void insertBatch(List<NewGkTimetable> list);
	
	/**
	 * @param arrayId 可空
	 * @param timeTableIds 可空
	 */
	void deleteByArrayId(String arrayId,String[] timeTableIds);

	/**
	 * 查找在同一个时间上了多门课的 老师，以及这些老师的 上课班级 课程信息
	 * @param unitId
	 * @param arrayId
	 * @return
	 */
    List<CourseScheduleDto> findConflictTeaIds(String unitId, String arrayId);
}
