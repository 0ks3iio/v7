package net.zdsoft.eclasscard.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.zdsoft.eclasscard.data.dao.EccClassAttenceJdbcDao;
import net.zdsoft.eclasscard.data.entity.EccClassAttence;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.dao.BaseDao.MultiRow;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.SQLUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

/**
 * eclasscard_class_attance 
 * @author 
 * 
 */
@Repository
public class EccClassAttenceDaoImpl extends BaseDao<EccClassAttence> implements EccClassAttenceJdbcDao{
	@Override
	public EccClassAttence setField(ResultSet rs) throws SQLException{
		EccClassAttence EccClassAttence = new EccClassAttence();
		EccClassAttence.setId(rs.getString("id"));
		EccClassAttence.setInfoName(rs.getString("info_name"));
		EccClassAttence.setUnitId(rs.getString("unit_id"));
		EccClassAttence.setClassId(rs.getString("class_id"));
		EccClassAttence.setPlaceId(rs.getString("place_id"));
		EccClassAttence.setTeacherId(rs.getString("teacher_id"));
		EccClassAttence.setSubjectId(rs.getString("subject_id"));
		EccClassAttence.setSubjectName(rs.getString("subject_name"));
		EccClassAttence.setPeriod(rs.getInt("period"));
		EccClassAttence.setPeriodInterval(rs.getString("period_interval"));
		EccClassAttence.setClockDate(rs.getTimestamp("clock_date"));
		EccClassAttence.setOver(rs.getBoolean("is_over"));
		EccClassAttence.setSectionNumber(rs.getInt("section_number"));
		EccClassAttence.setBeginTime(rs.getString("begin_time"));
		EccClassAttence.setEndTime(rs.getString("end_time"));
		EccClassAttence.setClassType(rs.getInt("class_type"));
		EccClassAttence.setCourseScheduleId(rs.getString("course_schedule_id"));
		return EccClassAttence;
	}

	@Override
	public List<EccClassAttence> findByClassIdsSection(String unitId,String date,
			int section, String[] classIds,Pagination page) {
		StringBuffer sql = new StringBuffer("select * from eclasscard_class_attance where unit_id = ? and section_number = ? and to_char(clock_date,'yyyy-MM-dd') = ?");
		
		List<Object> args = Lists.newArrayList();
		args.add(unitId);
		args.add(section);
		args.add(date);
		if(classIds!=null&&classIds.length>0){
			if (page != null) {
        		page.setUseCursor(true);
        	}
			sql.append(" and class_id in ");
			if(page != null)
				return queryForInSQL(sql.toString(), args.toArray(new Object[0]), classIds, new MultiRow(),"", page);
			else
				return queryForInSQL(sql.toString(), args.toArray(new Object[0]), classIds,  new MultiRow());
		}else{
			
			if(page != null)
				return query(sql.toString(), args.toArray(new Object[0]), new MultiRow(), page);
			else
				return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
		}
		
	}

	@Override
	public List<EccClassAttence> findByIdsSort(String[] caIds,
			String beginDate, String endDate, Pagination page) {
			StringBuffer sql = new StringBuffer("select * from eclasscard_class_attance where to_char(clock_date,'yyyy-MM-dd') >=? and to_char(clock_date,'yyyy-MM-dd') <=?");
		
		List<Object> args = Lists.newArrayList();
		args.add(beginDate);
		args.add(endDate);
		if(caIds!=null&&caIds.length>0){
			if (page != null) {
        		page.setUseCursor(true);
        	}
			sql.append(" and id in ");
			if(page != null)
				return queryForInSQL(sql.toString(), args.toArray(new Object[0]), caIds, new MultiRow(),"", page);
			else
				return queryForInSQL(sql.toString(), args.toArray(new Object[0]), caIds,  new MultiRow());
		}else{
			if(page != null)
				return query(sql.toString(), args.toArray(new Object[0]), new MultiRow(), page);
			else
				return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
		}
		
	}
	
}


