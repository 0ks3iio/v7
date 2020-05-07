package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.CourseScheduleRevert;

public interface CourseScheduleRevertService extends
		BaseService<CourseScheduleRevert, String> {

	/**
	 * 备份课表，如果存在 gradeId 表示备份的是年级课表，如果gradeId不存在则备份的是 全校课表
	 * @param unitId
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param savedList
	 */
	void saveBackup(String unitId, String acadyear, Integer semester, String gradeId, List<CourseScheduleRevert> savedList);

	List<CourseScheduleRevert> findByClassIds(String schoolId,String acadyear, Integer semester, String[] classIds);

	boolean checkBackupExists(String unitId, String acadyear, Integer semester, String[] gradeIds);
	
}
