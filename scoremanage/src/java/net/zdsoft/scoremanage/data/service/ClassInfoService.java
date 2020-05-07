package net.zdsoft.scoremanage.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.ClassInfo;

public interface ClassInfoService extends BaseService<ClassInfo, String>{


	List<ClassInfo> findBySchoolIdAndSubjectInfoIdIn(String schoolId, String... subjectInfoIds);

	public List<ClassInfo> findList(String unitId, String classType,
			String classIdSearch, String[] courseInfoIds);

	public List<ClassInfo> findList(String unitId, String... courseInfoIds);


	public List<ClassInfo> findByCourseInfoIdIn(String... courseInfoIds);

	List<ClassInfo> findByExamInfoId(String examInfoId);

	void deleteBySchoolId(String schooolId, String... courseInfoIds);

	void deleteAllByIds(String... id);

	List<ClassInfo> saveAllEntitys(ClassInfo... classInfo);
	
	List<ClassInfo> findByAll(String unitId, String classType, String[] subjectInfoIds);
	ClassInfo findByAll(String unitId, String classType, String classId,String subjectInfoId);

	List<ClassInfo> findBySchoolIdAndClassIdIn(String unitId, String[] classIds);

}
