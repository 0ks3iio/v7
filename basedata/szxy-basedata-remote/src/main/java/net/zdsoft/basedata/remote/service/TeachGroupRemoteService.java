package net.zdsoft.basedata.remote.service;

import net.zdsoft.basedata.entity.TeachGroup;

public interface TeachGroupRemoteService extends BaseRemoteService<TeachGroup, String>{

	String findBySchoolId(String unitId);

	String findBySchoolId(String unitId,boolean isMakeTeacher);

	/**
	 * 根据类型或者教研组下负责人和成员老师信息
	 * @param unitId
	 * @param type TeachGroup.type_1 TeachGroup.type_0 可以为空 获取全部老师
	 * @return
	 */
	String findBySchoolIdAndType(String unitId,Integer type);

	String findBySchoolIdAndSubjectIdIn(String unitId,String[] subids);
	 /**
	  * 教研组教师数据
	  * @param unitId
	  * @return
	  */
	String findTeachers(String unitId);
}
