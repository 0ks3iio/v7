package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.zdsoft.basedata.dto.CourseScheduleDto;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.newgkelective.data.dao.NewGkTimetableJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkTimetable;

@Repository
public class NewGkTimetableJdbcDaoImpl extends BaseDao<NewGkTimetable> implements NewGkTimetableJdbcDao{

	@Override
	public void insertBatch(List<NewGkTimetable> list) {
		saveAll(list.toArray(new NewGkTimetable[] {}));
	}

	@Override
	public NewGkTimetable setField(ResultSet rs) throws SQLException {
		return null;
	}

	@Override
	public void deleteByArrayId(String arrayId, String[] timeTableIds) {
		String sql= "delete newgkelective_timetable where array_id =?";
		if(StringUtils.isNotBlank(arrayId)){
			this.update(sql,arrayId);
		}
		if(timeTableIds!=null){
			sql= "delete newgkelective_timetable where id in ";
			this.updateForInSQL(sql,null, timeTableIds);
		}
	}

	@Override
	public List<CourseScheduleDto> findConflictTeaIds(String unitId, String arrayId) {
		String sql =
				"select ttt.teacher_id,tt.class_id,tt.class_type,tt.subject_id,tt.subject_type,tto.day_of_week,tto.period_interval,tto.period,tto.firstsd_week " +
				"	from newgkelective_timetable tt,newgkelective_timetable_other tto, newgkelective_timetable_teach ttt " +
				"	where tt.unit_id=? and tt.array_id=? and tto.timetable_id=tt.id and ttt.timetable_id=tt.id " +
				"	and ttt.teacher_id in(" +
				"			select ttt.teacher_id from newgkelective_timetable tt,newgkelective_timetable_other tto,newgkelective_timetable_teach ttt " +
				"			where tt.unit_id=? and tt.array_id=? and tto.timetable_id=tt.id and ttt.timetable_id=tt.id " +
				"			group by ttt.teacher_id,tto.day_of_week||tto.period_interval||tto.period " +
				"			having count(ttt.teacher_id)>1)";

		return query(sql, new String[]{unitId, arrayId,unitId, arrayId}, (rs, rowNum) -> setDtoField(rs));
	}

	private CourseScheduleDto setDtoField(ResultSet rs) throws SQLException {
		CourseScheduleDto dto = new CourseScheduleDto();
		dto.setTeacherId(rs.getString("teacher_id"));
		dto.setClassId(rs.getString("class_id"));
		dto.setClassType(rs.getInt("class_type"));
		dto.setSubjectId(rs.getString("subject_id"));
		dto.setSubjectType(rs.getString("subject_type"));
		dto.setDayOfWeek(rs.getInt("day_of_week"));
		dto.setPeriodInterval(rs.getString("period_interval"));
		dto.setPeriod(rs.getInt("period"));
		String firstsd_week = rs.getString("firstsd_week");
		if(NumberUtils.isNumber(firstsd_week)){
			dto.setWeekType(Integer.parseInt(firstsd_week));
		}
		return dto;
	}

}
