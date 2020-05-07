package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.dao.TipsayJdbcDao;
import net.zdsoft.basedata.entity.Tipsay;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.SQLUtils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class TipsayDaoImpl extends BaseDao<Tipsay> implements TipsayJdbcDao {

	@Override
	public Tipsay setField(ResultSet rs) throws SQLException {
		Tipsay tipsay = new Tipsay();
		tipsay.setId(rs.getString("id"));
		tipsay.setSchoolId(rs.getString("school_id"));
		tipsay.setAcadyear(rs.getString("acadyear"));
		tipsay.setSemester(rs.getInt("semester"));
		tipsay.setCourseScheduleId(rs.getString("course_schedule_id"));
		tipsay.setWeekOfWorktime(rs.getInt("week_of_worktime"));
		tipsay.setDayOfWeek(rs.getInt("day_of_week"));
		tipsay.setPeriod(rs.getInt("period"));
		tipsay.setNewTeacherId(rs.getString("new_teacher_id"));
		tipsay.setPeriodInterval(rs.getString("period_interval"));
		tipsay.setClassId(rs.getString("class_id"));
		tipsay.setClassType(rs.getInt("class_type"));
		tipsay.setSubjectId(rs.getString("subject_id"));
		tipsay.setTeacherId(rs.getString("teacher_id"));
		tipsay.setTeacherExIds(rs.getString("teacher_ex_ids"));
		tipsay.setCreationTime(rs.getDate("creation_time"));
		tipsay.setModifyTime(rs.getDate("modify_time"));
		tipsay.setIsDeleted(rs.getInt("is_deleted"));
		tipsay.setRemark(rs.getString("remark"));
		tipsay.setState(rs.getString("state"));
		tipsay.setOperator(rs.getString("operator"));
		tipsay.setTipsayType(rs.getString("tipsay_type"));
		tipsay.setType(rs.getString("type"));
		return tipsay;
	}

	@Override
	public List<Tipsay> findByCondition(String schoolId, String acadyear, Integer semester, String[] dates, String type, String[] teacherIds) {
		StringBuilder sb = new StringBuilder("select * from base_tipsay where school_id=? and acadyear=? and semester=? and state=1 and is_deleted=0");
		List<Object> listOfArgs = new ArrayList<Object>();
		listOfArgs.add(schoolId);
		listOfArgs.add(acadyear);
		listOfArgs.add(semester);
		if(StringUtils.isNotBlank(type)){
			sb.append(" and type=?");
			listOfArgs.add(type);
		}
		if(ArrayUtils.isNotEmpty(teacherIds)){
			sb.append(" and (new_teacher_id in "+SQLUtils.toSQLInString(teacherIds)+" or teacher_id in "+SQLUtils.toSQLInString(teacherIds));
			for (String tid : teacherIds) {
				sb.append(" or teacher_ex_ids like '%"+tid+"%'");
			}
			sb.append(")");
		}
		sb.append(" and week_of_worktime||'_'||day_of_week in ");
		return queryForInSQL(sb.toString(), listOfArgs.toArray(new Object[listOfArgs.size()]), dates, new MultiRow()," order by type,week_of_worktime desc,day_of_week desc,period_interval,period");
	}

}
