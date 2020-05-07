package net.zdsoft.tutor.remote.service;

public interface TutorRemoteService {

	/**
	 * 传unitId,获取学校未被删除的导师和对应的学生关系，返回Map<teacherId,Set<studentId>>
	 */
	public String getTutorTeaStuMapByUnitId(String unitId);
	
	/**
	 * 根据教师id，获取学生id集合Set<studentId>
	 * @param teacherId
	 * @return
	 */
	public String getTutorStuByTeacherId(String teacherId);
}
