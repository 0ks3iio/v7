package net.zdsoft.basedata.dao.impl;

import net.zdsoft.basedata.dao.StudentSelectSubjectJdbcDao;
import net.zdsoft.basedata.entity.StudentSelectSubject;
import net.zdsoft.framework.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentSekectSubjectJdbcDaoImpl extends BaseDao<StudentSelectSubject> implements StudentSelectSubjectJdbcDao {
    @Override
    public void deleteAllByStudentId(String acadyear, Integer semester, String[] studentIds) {
        if (studentIds.length == 0) {
            return;
        }
        String sql = new String("update base_student_selsub set is_deleted=1 where acadyear=? and semester=? and student_id in ");
        this.updateForInSQL(sql, new Object[]{acadyear, semester}, studentIds);
    }

    @Override
    public void saveAll(List<StudentSelectSubject> insertList) {
        this.saveAll(insertList.toArray(new StudentSelectSubject[] {}));
    }

    @Override
    public void deleteAllByGradeId(String acadyear, Integer semester, String gradeId) {
        String sql = new String("delete from base_student_selsub where acadyear=? and semester=? and grade_id=?");
        this.update(sql, new Object[]{acadyear, semester, gradeId});
    }

    @Override
    public StudentSelectSubject setField(ResultSet rs) throws SQLException {
        StudentSelectSubject studentSelectSubject = new StudentSelectSubject();
        studentSelectSubject.setId(rs.getString("ID"));
        studentSelectSubject.setAcadyear(rs.getString("ACADYEAR"));
        studentSelectSubject.setSemester(rs.getInt("SEMESTER"));
        studentSelectSubject.setSchoolId(rs.getString("SCHOOL_ID"));
        studentSelectSubject.setGradeId(rs.getString("GRADE_ID"));
        studentSelectSubject.setStudentId(rs.getString("STUDENT_ID"));
        studentSelectSubject.setSubjectId(rs.getString("SUBJECT_ID"));
        studentSelectSubject.setCreationTime(rs.getTimestamp("CREATION_TIME"));
        studentSelectSubject.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
        studentSelectSubject.setIsDeleted(rs.getInt("IS_DELETED"));
        return studentSelectSubject;
    }
}
