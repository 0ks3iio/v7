package net.zdsoft.basedata.dao;

import net.zdsoft.basedata.entity.StudentSelectSubject;

import java.util.List;

public interface StudentSelectSubjectJdbcDao {
    void deleteAllByStudentId(String acadyear, Integer semester, String[] studentIds);

    void saveAll(List<StudentSelectSubject> insertList);

    void deleteAllByGradeId(String acadyear, Integer semester, String gradeId);
}
