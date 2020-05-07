package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.dao.AdjustedDetailJdbcDao;
import net.zdsoft.basedata.entity.AdjustedDetail;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.SQLUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;

@Repository
public class AdjustedDetailDaoImpl extends BaseDao<AdjustedDetail> implements AdjustedDetailJdbcDao {

	@Override
	public AdjustedDetail setField(ResultSet rs) throws SQLException {
		AdjustedDetail adjustedDetail = new AdjustedDetail();
		adjustedDetail.setId(rs.getString("id"));
		adjustedDetail.setSchoolId(rs.getString("school_id"));
		adjustedDetail.setAdjustedId(rs.getString("adjusted_id"));
		adjustedDetail.setWeekOfWorktime(rs.getInt("week_of_worktime"));
		adjustedDetail.setDayOfWeek(rs.getInt("day_of_week"));
		adjustedDetail.setPeriod(rs.getInt("period"));
		adjustedDetail.setPeriodInterval(rs.getString("period_interval"));
		adjustedDetail.setClassId(rs.getString("class_id"));
		adjustedDetail.setSubjectId(rs.getString("subject_id"));
		adjustedDetail.setTeacherId(rs.getString("teacher_id"));
		adjustedDetail.setTeacherExIds(rs.getString("teacher_ex_ids"));
		adjustedDetail.setCreationTime(rs.getDate("creation_time"));
		adjustedDetail.setModifyTime(rs.getDate("modify_time"));
		adjustedDetail.setAdjustedType(rs.getString("adjusted_type"));
		return adjustedDetail;
	}

	@Override
	public List<AdjustedDetail> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String[] teacherIds) {
		StringBuilder sb = new StringBuilder("select bad.* from base_adjusted_detail bad, base_adjusted ba where bad.adjusted_id=ba.id and ba.school_id=? and ba.acadyear=? and ba.semester=? and ba.state=1");
		List<Object> listOfArgs = new ArrayList<Object>();
		listOfArgs.add(schoolId);
		listOfArgs.add(acadyear);
		listOfArgs.add(semester);
		
		if(ArrayUtils.isNotEmpty(teacherIds)){
			sb.append(" and (teacher_id in "+SQLUtils.toSQLInString(teacherIds));
			for (String tid : teacherIds) {
				sb.append(" or teacher_ex_ids like '%"+tid+"%'");
			}
			sb.append(")");
		}
		
		sb.append(" and bad.week_of_worktime||'_'|| bad.day_of_week in ");
		return queryForInSQL(sb.toString(), listOfArgs.toArray(new Object[listOfArgs.size()]), dates, new MultiRow(), " order by bad.week_of_worktime desc,bad.day_of_week desc,bad.period_interval,bad.period");
	}

	@Override
	public Map<String, String> findMapByAdjustedIdIn(String[] adjustedIds) {
		String sql = "select bad1.id id1,bad2.id id2 from base_adjusted_detail bad1,base_adjusted_detail bad2 where bad1.adjusted_id=bad2.adjusted_id and bad1.id<>bad2.id and bad1.adjusted_id in";
		return queryForInSQL(sql, new Object[]{}, adjustedIds, new MapRowMapper<String, String>() {

			@Override
			public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("id1");
			}

			@Override
			public String mapRowValue(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("id2");
			}
		});
	}

    @Override
    public Map<String, String> findCourseScheduleIdMapByAdjustedIdIn(String[] adjustedIds) {
        String sql = "select bad1.course_schedule_id id1,bad2.course_schedule_id id2 from base_adjusted_detail bad1,base_adjusted_detail bad2 where bad1.adjusted_id=bad2.adjusted_id and bad1.id<>bad2.id and bad1.adjusted_type='01' and bad1.adjusted_id in";
        return queryForInSQL(sql, new Object[]{}, adjustedIds, new MapRowMapper<String, String>() {

            @Override
            public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("id1");
            }

            @Override
            public String mapRowValue(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("id2");
            }
        });
    }

}
