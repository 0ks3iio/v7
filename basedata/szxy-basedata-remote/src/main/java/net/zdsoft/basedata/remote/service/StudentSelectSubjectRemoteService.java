package net.zdsoft.basedata.remote.service;

import java.util.List;

import net.zdsoft.basedata.entity.StudentSelectSubject;

public interface StudentSelectSubjectRemoteService {
    String findStuSelectMapByGradeId(String acadyear, String semester, String gradeId);
    
    void updateStudentSelectsByGradeId(String unitId, String acadyear, Integer semester, String gradeId, List<StudentSelectSubject> resultList);

    /**
	 * 查询某个年级选择某个科目的学生
	 * @param acadyear
	 * @param semester
	 * @param gradeId
	 * @param isXuankao 是：查询某个年级选择某个科目的学生 否：查询某个年级没有选择某个科目的学生
	 * @param subjectIds
	 * @return Map<String,Set<String>> 备注：返回null代表没有确定的选课方案
	 */
	public String getStuSelectByGradeId(String acadyear, String semester, String gradeId,boolean isXuankao,String[] subjectIds);
}
