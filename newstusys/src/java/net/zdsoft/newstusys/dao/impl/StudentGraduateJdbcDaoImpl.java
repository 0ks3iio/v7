package net.zdsoft.newstusys.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newstusys.dao.StudentGraduateJdbcDao;
import net.zdsoft.newstusys.entity.StudentGraduate;
@Repository
public class StudentGraduateJdbcDaoImpl extends BaseDao<StudentGraduate> implements StudentGraduateJdbcDao {

	@Override
	public List<StudentGraduate> findByClassIds(String[] classIds) {
		String sql = "SELECT * FROM student_graduate WHERE class_id in";
		return queryForInSQL(sql, new Object[] {}, classIds, new MultiRow());
	}

	@Override
	public StudentGraduate setField(ResultSet rs) throws SQLException {
		StudentGraduate stuGradute = new StudentGraduate();
		stuGradute.setStuid(rs.getString("stuid"));
		stuGradute.setClassId(rs.getString("class_id"));
		return stuGradute;
	}

}
