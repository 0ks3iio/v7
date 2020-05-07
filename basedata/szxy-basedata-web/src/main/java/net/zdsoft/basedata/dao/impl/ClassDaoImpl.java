package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.zdsoft.basedata.dao.ClassJdbcDao;
import net.zdsoft.basedata.entity.Clazz;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;

@Repository
public class ClassDaoImpl extends BaseDao<Clazz> implements ClassJdbcDao {

	@Override
	public Map<String, Integer> countByGradeIds(String[] gradeIds) {
		String sql = "select grade_id, count(*)  from base_class where is_deleted = 0 and is_graduate <> 2 and grade_id in";
		return queryForInSQL(sql, null, gradeIds, new MapRowMapper<String, Integer>() {
			@Override
			public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
			@Override
			public Integer mapRowValue(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(2);
			}
		}, " group by grade_id");
	}

	@Override
	public Clazz setField(ResultSet rs) throws SQLException {
		return null;
	}

}
