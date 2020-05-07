package net.zdsoft.newstusys.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.SQLUtils;
import net.zdsoft.newstusys.dao.StudentReportJdbcDao;
import net.zdsoft.newstusys.entity.StudentReport;
@Repository
public class StudentReportJdbcDaoImpl extends BaseDao<StudentReport> implements StudentReportJdbcDao{
	
	@Override
	public Map<String, Integer> stuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}
	
	@Override
	public Map<String, Integer> inCityStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and source=1 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, Integer> notInCityStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and source not in (1,4) and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public StudentReport setField(ResultSet rs) throws SQLException {
		// TODO
		return null;
	}

	@Override
	public Map<String, Integer> migrationStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and is_migration=1 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}
	
	@Override
	public Map<String, Integer> sYmigrationStuCountByClass(String... classIds){
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and is_migration=1 and source=3 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, Integer> stayinStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and stayin=1 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, Integer> regularClassStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and regular_class<>1 and regular_class is not null and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, Integer> boardingStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and is_boarding=1 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}
	
	@Override
	public Map<String, Integer> notHkStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and source=4 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, Integer> normalStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and country<>156 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}
	
	@Override
	public Map<String, Integer> compatriotsCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and compatriots<>0 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, Integer> nowStateStuCountByClass(String... classIds) {
		String sql = "select count(*), class_id from base_student where is_deleted = 0 and is_leave_school=0 and now_state=24 and class_id IN";
		return queryForInSQL(sql, null, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By class_id");
	}

	@Override
	public Map<String, Integer> classCountByGrade(String... gradeIds) {
		String sql = "select count(*), grade_id from base_class where is_deleted = 0 and is_graduate = 0 and grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By grade_id");
	}
	
	@Override
	public Map<String, Integer> stuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> inCityCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.source=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> notCityCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.source not in(1,4) and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> notHkCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.source=4 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> migrationStuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.is_migration=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.is_migration=1 and a.source=3 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> stayinStuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.stayin=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> regularClassStuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.regular_class<>1 and  a.regular_class is not null and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> normalStuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.country<>156 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> compatriotsCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.compatriots<>0 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> nowStateStuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.now_state=24 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> boardingStuCountByGrade(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.is_boarding=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> classCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_class where is_deleted = 0 and is_graduate = 0 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}
	
	@Override
	public Map<String, Integer> stuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> inCityCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and source=1 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> notCityCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and source not in(1,4) and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> notHkCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and source=4 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	
	@Override
	public Map<String, Integer> migrationStuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and is_migration=1 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and is_migration=1 and source=3 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> stayinStuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and stayin=1 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> regularClassStuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and regular_class<>1 and regular_class is not null and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> normalStuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and country<>156 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> compatriotsCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and compatriots<>0 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> nowStateStuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and now_state=24 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}

	@Override
	public Map<String, Integer> boardingStuCountBySchool(String... schoolIds) {
		String sql = "select count(*), school_id from base_student where is_deleted = 0 and is_leave_school=0 and is_boarding=1 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}
	
	

	
	@Override
	public Map<String, Integer> classCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), school_id from base_class where is_deleted = 0 and is_graduate = 0 and section=1 and school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By school_id");
	}
	
	@Override
	public Map<String, Integer> stuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> inCityCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.source=1 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> notCityCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.source not in(1,4) and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> notHkCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.source=4 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	
	@Override
	public Map<String, Integer> migrationStuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.is_migration=1 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.is_migration=1 and a.source=3 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> stayinStuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.stayin=1 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> regularClassStuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.regular_class<>1 and a.regular_class is not null and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> normalStuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.country<>156 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> compatriotsCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.compatriots<>0 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> nowStateStuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.now_state=24 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}

	@Override
	public Map<String, Integer> boardingStuCountBySchoolPrimary(String... schoolIds) {
		String sql = "select count(*), a.school_id from base_student a,base_class b where a.class_id=b.id and b.section=1 and a.is_deleted = 0 and a.is_leave_school=0 and a.is_boarding=1 and a.school_id IN";
		return queryForInSQL(sql, null, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.school_id");
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public Map<String, Integer> classCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), grade_id from base_class where is_deleted = 0 and is_graduate = 0 and section=2 or senction=3 and grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By grade_id");
	}
	
	@Override
	public Map<String, Integer> stuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> inCityCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.source=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> notCityCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.source not in(1,4) and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> notHkCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.source=4 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> migrationStuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.is_migration=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> sYmigrationStuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.is_migration=1 and a.source=3 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> stayinStuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.stayin=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> regularClassStuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and a.is_deleted = 0 and a.is_leave_school=0 and a.regular_class<>1 and a.regular_class is not null and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> normalStuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.country<>156 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> compatriotsCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.compatriots<>0 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> nowStateStuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.now_state=24 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> boardingStuCountByGradeHigh(String... gradeIds) {
		String sql = "select count(*), b.grade_id from base_student a,base_class b where a.class_id=b.id and b.section=2 or b.section=3 and a.is_deleted = 0 and a.is_leave_school=0 and a.is_boarding=1 and b.grade_id IN";
		return queryForInSQL(sql, null, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> ydByClassId(String acadyear, String semester,
			String flowtype, String... classIds) {
		String sql = "select count(*), currentclassid from student_abnormalflow where acadyear=? and semester=? and flowtype=? and currentclassid IN";
		return queryForInSQL(sql, new Object[]{acadyear, semester,flowtype}, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By currentclassid");
	}
	
	public Map<String, Integer> ydByClassIdsTypes(String acadyear, String semester, String[] flowTypes, String[] classIds){
		String sql = "select count(*), currentclassid,flowtype from student_abnormalflow where acadyear=? and semester=? and flowtype in "+SQLUtils.toSQLInString(flowTypes)+" and currentclassid IN";
		final Map<String, Integer> sumMap = new HashMap<String, Integer>();
		Map<String, Integer> countMap = queryForInSQL(sql, new Object[]{acadyear, semester}, classIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						Integer cn = sumMap.get(BaseConstants.ZERO_GUID+rs.getString("flowtype"));
						if(cn == null) {
							cn = 0;
						}
						cn = cn + rs.getInt(1);
						sumMap.put(BaseConstants.ZERO_GUID+rs.getString("flowtype"), cn);
						return rs.getString("currentclassid")+rs.getString("flowtype");
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By currentclassid,flowtype");
		sumMap.putAll(countMap);
		return sumMap;
	}

	@Override
	public Map<String, Integer> ydByGradeId(String acadyear, String semester,
			String flowtype, String... gradeIds) {
		String sql = "select count(*), b.grade_id from student_abnormalflow a, base_class b where a.currentclassid=b.id and a.acadyear=? and a.semester=? and a.flowtype=? and b.grade_id IN";
		return queryForInSQL(sql, new Object[]{acadyear, semester,flowtype}, gradeIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By b.grade_id");
	}

	@Override
	public Map<String, Integer> ydBySchIdPrimary(String acadyear,
			String semester, String flowtype, String... schoolIds) {
		String sql = "select count(*), a.schid from student_abnormalflow a, base_class b where a.currentclassid=b.id and b.section=1 and a.acadyear=? and a.semester=? and a.flowtype=? and a.schid IN";
		return queryForInSQL(sql, new Object[]{acadyear, semester,flowtype}, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.schid");
	}

	@Override
	public Map<String, Integer> ydBySchIdHign(String acadyear, String semester,
			String flowtype, String... schoolIds) {
		String sql = "select count(*), a.schid from student_abnormalflow a, base_class b where a.currentclassid=b.id and b.section=2 or b.section=3 and a.acadyear=? and a.semester=? and a.flowtype=? and a.schid IN";
		return queryForInSQL(sql, new Object[]{acadyear, semester,flowtype}, schoolIds,
				new MapRowMapper<String, Integer>() {
					@Override
					public String mapRowKey(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getString(2);
					}

					@Override
					public Integer mapRowValue(ResultSet rs, int rowNum)
							throws SQLException {
						return rs.getInt(1);
					}
				}, " Group By a.schid");
	}

	


}
