package net.zdsoft.diathesis.data.service;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.diathesis.data.dto.DiathesisGradeDto;
import net.zdsoft.diathesis.data.entity.DiathesisSetSubject;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/22 16:23
 */
public interface DiathesisSetSubjectService {

    //只有必修和学业,没有选修
    List<DiathesisSetSubject> findByGradeIdAndGradeCodeAndSemesterAndType(String gradeId, String gradeCode, Integer semester, String type);

    List<Course> getTeachingCourses(String gradeId, String gradeCode, Integer semester, String schoolId);

    void updateSubjectList(DiathesisGradeDto subjectSet);

    List<DiathesisSetSubject> findByGradeIdAndType(String gradeId, String type);
}
