package net.zdsoft.basedata.service;

import net.zdsoft.basedata.dto.StudentSelectSubjectDto;
import net.zdsoft.basedata.entity.StudentSelectSubject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StudentSelectSubjectService extends BaseService<StudentSelectSubject, String> {
	/**
	 * 用于组装
	 * @param studentSelectSubjectList
	 * @param classId
	 * @return
	 */
    List<StudentSelectSubjectDto> findStuSelectByClass(List<StudentSelectSubject> studentSelectSubjectList, String classId);

    Map<String, Set<String>> findStuSelectMapByGradeId(String acadyear, Integer semester, String gradeId);

    void updateStudentSelect(String acadyear, Integer semester, String gradeId, String studentId, String subjectIds);

    void updateStudentSelects(String unitId, String acadyear, Integer semester, String[] studentIds, List<StudentSelectSubject> insertList);

    void updateStudentSelectsByGradeId(String unitId, String acadyear, Integer semester, String gradeId, List<StudentSelectSubject> resultList);


	public Map<String, Set<String>> findStuBySubjectMap(String acadyear, Integer semester, String gradeId,boolean isXuankao,String[] subjectIds);

	void deleteByStudentIds(String... studentIds);
	
	void deleteBySubjectIds(String... subjectIds);
	
	List<StudentSelectSubject> findListBySchoolIdWithMaster(String acadyear, Integer semester, String schoolId);
}
