package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.dao.CourseScheduleJdbcDao;
import net.zdsoft.basedata.dto.CourseScheduleDto;
import net.zdsoft.basedata.entity.CourseSchedule;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.SQLUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
public class CourseScheduleDaoImpl extends BaseDao<CourseSchedule> implements CourseScheduleJdbcDao {

	@Override
	public CourseSchedule setField(ResultSet rs) throws SQLException {
		CourseSchedule ent = new CourseSchedule();
		ent.setId(rs.getString("id"));
		ent.setSchoolId(rs.getString("school_id"));
		ent.setClassId(rs.getString("class_id"));
		ent.setAcadyear(rs.getString("acadyear")); // 学年
		ent.setSemester(rs.getInt("semester")); // 学期
		ent.setSubjectId(rs.getString("subject_id")); // 课程Id
		ent.setTeacherId(rs.getString("teacher_id")); // 教师Id
		ent.setPlaceId(rs.getString("place_id"));//场地Id
		ent.setWeekOfWorktime(rs.getInt("week_of_worktime")); //开学时间的周次
		ent.setDayOfWeek(rs.getInt("day_of_week")); // 星期一:0; 星期二:1;以此类推
		ent.setPeriodInterval(rs.getString("period_interval"));//早上，上午，下午，晚上
		ent.setPeriod(rs.getInt("period")); // 节课
		ent.setClassType(rs.getInt("class_type"));//班级类型  
		ent.setSubjectType(rs.getString("subject_type"));
		ent.setSubjectName(rs.getString("subject_name"));
		ent.setWeekType(rs.getInt("week_type"));//单周，双周，正常
		ent.setPunchCard(rs.getInt("punch_card"));
		ent.setModifyTime(rs.getDate("modify_time"));
		ent.setCreationTime(rs.getDate("creation_time"));
		ent.setIsDeleted(rs.getInt("is_deleted"));
		return ent;
	}

	@Override
	public List<CourseSchedule> getByCourseScheduleDto(CourseScheduleDto dto) {
		StringBuffer sql = new StringBuffer("select * from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.school_id=? and c.acadyear=? and c.semester=? ");
		List<Object> args = Lists.newArrayList();
		args.add(dto.getSchoolId());
		args.add(dto.getAcadyear());
		args.add(dto.getSemester());
		
		if(dto.getWeekOfWorktime1()!=0){
			args.add(dto.getWeekOfWorktime1());
			sql.append(" and c.week_of_worktime >= ? ");
		}
		if(dto.getWeekOfWorktime2()!=0){
			args.add(dto.getWeekOfWorktime2());
			sql.append(" and c.week_of_worktime <= ?");
		}
		
		if(dto.getWeekOfWorktime()!=0){
			args.add(dto.getWeekOfWorktime());
			sql.append(" and c.week_of_worktime = ? ");
		}
		
		if(dto.getDayOfWeek()!=null){
			args.add(dto.getDayOfWeek());
			sql.append(" and c.day_of_week = ?");
		}
		
		if(dto.getPeriod()!=0){
			args.add(dto.getPeriod());
			sql.append(" and c.period = ?");
		}
		if(StringUtils.isNotBlank(dto.getPeriodInterval())){
			args.add(dto.getPeriodInterval());
			sql.append(" and c.period_interval = ?");
		}
		if(!dto.isXN()) {
			//不要虚拟
			sql.append(" and (c.class_type<>1 or (c.class_type=1 and c.subject_type<>'3' ))");
		}

		if(StringUtils.isNotBlank(dto.getGradeId())){
			args.add(dto.getGradeId());
			sql.append(" and exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.grade_id=?)");
		}
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	}
   
	@Override
	public List<CourseSchedule> findCourseScheduleListByTeacherId(
			String searchAcadyear, Integer searchSemester, String teacherId,
			Integer week) {
		StringBuffer sql = new StringBuffer("select * from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.acadyear=? and c.semester=? and c.teacher_id=? ");
		List<Object> args = Lists.newArrayList();
		args.add(searchAcadyear);
		args.add(searchSemester);
		args.add(teacherId);
		if(week != null){
			sql.append(" and week_of_worktime=?");
			args.add(week);
		}
		sql.append(" and (exists(select 1 from base_teach_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.is_using=1) " +
				" or exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0)" +
				" or c.class_type ="+CourseSchedule.CLASS_TYPE_NORMAL+")");
		
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	
	}

	@Override
	public List<CourseSchedule> findCourseScheduleListByFuTeacherId(
			String searchAcadyear, Integer searchSemester, String teacherId,
			Integer week) {
		StringBuffer sql = new StringBuffer("select * from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.acadyear=? and c.semester=? and c.teacher_id<>? ");
		List<Object> args = Lists.newArrayList();
		args.add(searchAcadyear);
		args.add(searchSemester);
		args.add(teacherId);
		if(week != null){
			sql.append(" and week_of_worktime=?");
			args.add(week);
		}
		sql.append(" and (exists(select 1 from base_teach_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.is_using=1) " +
				" or exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0)" +
				" or c.class_type ="+CourseSchedule.CLASS_TYPE_TEACH+")");
		
		sql.append(" and exists(select 1 from timetable_teach_plan_ex bc where bc.primary_table_id=c.id and bc.teacher_id=? and bc.type=2) ");
		args.add(teacherId);
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	}
	
	@Override
	public List<CourseSchedule> findCourseScheduleListByFuTeacherId(
			String searchAcadyear, Integer searchSemester, String teacherId, String startDate, String endStart) {
		StringBuffer sql = new StringBuffer("select c.* from BASE_COURSE_SCHEDULE c, sys_date_info b where c.is_deleted=0 and c.school_id = b.sch_id and c.day_of_week = (b.weekday -1) and c.week_of_worktime = b.week "
				+ "and to_char(b.info_date, 'yyyyMMdd') >= ? and to_char(b.info_date, 'yyyyMMdd') <=  ? and c.acadyear=? and c.semester=? and c.teacher_id<>? ");
		List<Object> args = Lists.newArrayList();
		args.add(startDate);
		args.add(endStart);
		args.add(searchAcadyear);
		args.add(searchSemester);
		args.add(teacherId);
		sql.append(" and (exists(select 1 from base_teach_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.is_using=1) " +
				" or exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0)" +
				" or c.class_type ="+CourseSchedule.CLASS_TYPE_TEACH+")");
		
		sql.append(" and exists(select 1 from timetable_teach_plan_ex bc where bc.primary_table_id=c.id and bc.teacher_id=? and bc.type=2) ");
		args.add(teacherId);
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	}
	
	

	@Override
	public List<CourseSchedule> findByTimes(CourseScheduleDto dto,
			String[] timeStr) {
		StringBuffer sql = new StringBuffer("select * from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.school_id=? and c.acadyear=? and c.semester=? ");
		List<Object> args = Lists.newArrayList();
		args.add(dto.getSchoolId());
		args.add(dto.getAcadyear());
		args.add(dto.getSemester());
		
		if(dto.getWeekOfWorktime1()!=0){
//			args.add(dto.getWeekOfWorktime1());
//			sql.append(" and c.week_of_worktime >= ? ");
			if(dto.getDayOfWeek1()==0){
				args.add(dto.getWeekOfWorktime1());
				sql.append(" and c.week_of_worktime >= ? ");
			}else{
				sql.append(" and ( c.week_of_worktime > ? or ( c.week_of_worktime= ? and c.day_of_week >= ? ) )");
				args.add(dto.getWeekOfWorktime1());
				args.add(dto.getWeekOfWorktime1());
				args.add(dto.getDayOfWeek1());
			}
		}
		if(dto.getWeekOfWorktime2()!=0){
//			args.add(dto.getWeekOfWorktime2());
//			sql.append(" and c.week_of_worktime <= ?");
			if(dto.getDayOfWeek2()==6){
				args.add(dto.getWeekOfWorktime2());
				sql.append(" and c.week_of_worktime <= ?");
			}else{
				sql.append(" and ( c.week_of_worktime < ? or ( c.week_of_worktime= ? and c.day_of_week <= ? ) )");
				args.add(dto.getWeekOfWorktime2());
				args.add(dto.getWeekOfWorktime2());
				args.add(dto.getDayOfWeek2());
			}
		}
		if(!dto.isXN()) {
			//不要虚拟
			sql.append(" and (c.class_type<>1 or (c.class_type=1 and c.subject_type<>'3' ))");
		}
		
		sql.append(" and  day_of_week || '_' || period_interval ||  '_' || period  in  " + SQLUtils.toSQLInString(timeStr));
		if(dto.getClassIds()!=null && dto.getClassIds().length>0){
			sql.append(" and c.class_id in ");
			return queryForInSQL(sql.toString(), args.toArray(new Object[]{}), dto.getClassIds(), new MultiRow());
		}
		return query(sql.toString(), args.toArray(new Object[]{}),new MultiRow());
		
	}

	@Override
	public Map<String, Set<String>> findTeacherIds(String[] courseScheduleId) {
		StringBuffer sql = new StringBuffer("select primary_table_id,teacher_id from timetable_teach_plan_ex where type=2 and primary_table_id in ");
		Map<String, Set<String>> map=new HashMap<String, Set<String>>();
		
		 List<String[]> list =queryForInSQL(sql.toString(), null, courseScheduleId,new MultiRowMapper<String[]>(){

			@Override
			public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				String[] str =new String[2];
				str[0]=rs.getString("primary_table_id");
				str[1]=rs.getString("teacher_id");
				return str;
			}
			 
		 });
		 if(CollectionUtils.isNotEmpty(list)){
			 for(String[] s:list){
				 if(!map.containsKey(s[0])){
					 map.put(s[0], new HashSet<String>()); 
				 }
				 map.get(s[0]).add(s[1]);
			 }
		 }
		 return map;
	}

	@Override
	public void deleteByClassId(CourseScheduleDto deleteDto) {
		StringBuffer sql = new StringBuffer("update BASE_COURSE_SCHEDULE c set c.is_deleted=1, c.modify_time =? where c.school_id=? and c.acadyear=? and c.semester=? "
				+ "and c.is_deleted=0");
		List<Object> args = Lists.newArrayList();
		args.add(new Date());
		args.add(deleteDto.getSchoolId());
		args.add(deleteDto.getAcadyear());
		args.add(deleteDto.getSemester());
		
		if(deleteDto.getWeekOfWorktime1()!=0){
			if(deleteDto.getDayOfWeek1()==0){
				args.add(deleteDto.getWeekOfWorktime1());
				sql.append(" and c.week_of_worktime >= ? ");
			}else{
				sql.append(" and ( c.week_of_worktime > ? or ( c.week_of_worktime= ? and c.day_of_week >= ? ) )");
				args.add(deleteDto.getWeekOfWorktime1());
				args.add(deleteDto.getWeekOfWorktime1());
				args.add(deleteDto.getDayOfWeek1());
			}
			
			
		}
		if(deleteDto.getWeekOfWorktime2()!=0){
			if(deleteDto.getDayOfWeek2()==6){
				args.add(deleteDto.getWeekOfWorktime2());
				sql.append(" and c.week_of_worktime <= ?");
			}else{
				sql.append(" and ( c.week_of_worktime < ? or ( c.week_of_worktime= ? and c.day_of_week <= ? ) )");
				args.add(deleteDto.getWeekOfWorktime2());
				args.add(deleteDto.getWeekOfWorktime2());
				args.add(deleteDto.getDayOfWeek2());
			}
			
		}
		if(StringUtils.isNotBlank(deleteDto.getSubjectType())) {
			args.add(deleteDto.getSubjectType());
			sql.append(" and c.subject_type = ? ");
		}
		if(StringUtils.isNotBlank(deleteDto.getClassId())){
			args.add(deleteDto.getClassId());
			sql.append(" and c.class_id = ? ");
		}
		
		if(deleteDto.isXN()) {
			//增加虚拟课程type
			//删除特殊处理 如果isXN=true 那么行政班删除时需要使用subjectId+时间点删除
			if(StringUtils.isNotBlank(deleteDto.getXnSubjectId())) {
				sql.append(" and (class_type <> 1 or (class_type=1 and subject_type='3' and subject_id = ?))");
				args.add(deleteDto.getXnSubjectId());
			}else {
				sql.append(" and (class_type <> 1 or (class_type=1 and subject_type='3' and subject_id = ?))");
			}
		}
		
		if(deleteDto.getTimeArr()!=null && deleteDto.getTimeArr().length>0) {
			sql.append(" and  day_of_week || '_' || period_interval ||  '_' || period  in  " + SQLUtils.toSQLInString(deleteDto.getTimeArr()));
		}
		
		if(ArrayUtils.isNotEmpty(deleteDto.getClassIds())){
			sql.append(" and c.class_id in ");
			updateForInSQL(sql.toString(),args.toArray(new Object[]{}),deleteDto.getClassIds());
		}else{
			update(sql.toString(),args.toArray(new Object[]{}));
		}
	}

	@Override
	public List<CourseSchedule> findCourseScheduleListByTeacherId(String searchAcadyear, Integer searchSemester,
			String teacherId, String startDate, String endDate) {	
		
		StringBuffer sql = new StringBuffer("select c.* from BASE_COURSE_SCHEDULE c, sys_date_info b where c.is_deleted=0 and c.school_id = b.sch_id and c.day_of_week = (b.weekday -1) and c.week_of_worktime = b.week "
				+ "and to_char(b.info_date, 'yyyyMMdd') >= ? and to_char(b.info_date, 'yyyyMMdd') <=  ? and c.acadyear=? and c.semester=? and c.teacher_id=? ");
		
			List<Object> args = Lists.newArrayList();
			args.add(startDate);
			args.add(endDate);
			args.add(searchAcadyear);
			args.add(searchSemester);
			args.add(teacherId);
			sql.append(" and (exists(select 1 from base_teach_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.is_using=1) " +
					" or exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0)" +
					" or c.class_type ="+CourseSchedule.CLASS_TYPE_TEACH+")");
			
			return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
		
		}

	@Override
	public List<CourseSchedule> findBySchoolIdAndGradeIdAndSubjectId(String schoolId,String acadyear,
			Integer semester,String gradeId, String subjectId) {
		StringBuffer sql = new StringBuffer("select c.* from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.school_id = ? and c.acadyear=? and c.semester=? ");
		List<Object> args = new ArrayList<Object>();
		args.add(schoolId);
		args.add(acadyear);
		args.add(semester);
		if(StringUtils.isNotBlank(gradeId)){
			sql.append(" and (c.class_id in (select id from base_class where grade_id = ?) or c.class_id in (select id from base_teach_class where grade_id = ?))");
			args.add(gradeId);
			args.add(gradeId);
		}
		if(StringUtils.isNotBlank(subjectId)){
			sql.append(" and subject_id = ?");
			args.add(subjectId);
		}
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	}

	@Override
	public List<CourseSchedule> findCourseScheduleListByTeacherIdIn(String searchAcadyear, Integer searchSemester, Integer week, String[] teacherIds) {
		StringBuffer sql = new StringBuffer("select * from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.acadyear=? and c.semester=? and "+SQLUtils.toSQLInString(teacherIds,"c.teacher_id",false));
		List<Object> args = Lists.newArrayList();
		args.add(searchAcadyear);
		args.add(searchSemester);
		if(week != null){
			sql.append(" and week_of_worktime=?");
			args.add(week);
		}
		sql.append(" and (exists(select 1 from base_teach_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.is_using=1) " +
				" or exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0)" +
				" or c.class_type ="+CourseSchedule.CLASS_TYPE_TEACH+")");
		
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	}

	@Override
	public Map<String, List<CourseSchedule>> findCourseScheduleMapByFuTeacherId(String acadyear, String semester, String[] teacherIds, Integer week) {
		StringBuffer sql = new StringBuffer("select c.*,tp.teacher_id tid from BASE_COURSE_SCHEDULE c,timetable_teach_plan_ex tp where"
				+ " c.is_deleted=0 and c.acadyear=? and c.semester=? and c.teacher_id<>tp.teacher_id");
		List<Object> args = Lists.newArrayList();
		args.add(acadyear);
		args.add(semester);
		if(week != null){
			sql.append(" and week_of_worktime=?");
			args.add(week);
		}
		sql.append(" and (exists(select 1 from base_teach_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.is_using=1) " +
				" or exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0)" +
				" or c.class_type ="+CourseSchedule.CLASS_TYPE_TEACH+")");
		
		sql.append(" and tp.primary_table_id=c.id and tp.type=2 and tp.teacher_id in ");
		Map<String, List<CourseSchedule>> map=new HashMap<String, List<CourseSchedule>>();
		List<Object[]> list =queryForInSQL(sql.toString(), args.toArray(new Object[0]), teacherIds,new MultiRowMapper<Object[]>(){

			@Override
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				Object[] str =new Object[2];
				str[0]=rs.getString("tid");
				str[1]=setField(rs);
				return str;
			}
			 
		});
		if(CollectionUtils.isNotEmpty(list)){
			for(Object[] s:list){
				String key = s[0].toString();
				if(!map.containsKey(key)){
					map.put(key, new ArrayList<CourseSchedule>()); 
				}
				map.get(s[0]).add((CourseSchedule) s[1]);
			}
		}
		return map;
	}

	@Override
	public List<CourseSchedule> findCourseScheduleListByClassIdes(String acadyear, Integer semester, String[] classId,
			Integer week, String showXN) {
		StringBuffer sql = new StringBuffer("select * from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.acadyear=? and c.semester=? ");
		List<Object> args = Lists.newArrayList();
		args.add(acadyear);
		args.add(semester);
		
		if(week!=null){
			args.add(week);
			sql.append(" and c.week_of_worktime = ? ");
		}
		
		if(StringUtils.isNotBlank(showXN)) {
			if("1".equals(showXN)) {
				//要虚拟
				sql.append(" and (c.class_type<>2 or (c.class_type=2 and c.subject_type<>'3' ))");
			}else {
				//不要虚拟
				sql.append(" and (c.class_type<>1 or (c.class_type=1 and c.subject_type<>'3' ))");
			}
			
			
		}
		
		if(classId!=null && classId.length>0){
			sql.append(" and c.class_id in ");
			return queryForInSQL(sql.toString(), args.toArray(new Object[]{}), classId, new MultiRow());
		}
		return query(sql.toString(), args.toArray(new Object[]{}),new MultiRow());
	}

	@Override
	public List<CourseSchedule> findByWeekTimes(CourseScheduleDto dto, String[] timeStr) {
		StringBuffer sql = new StringBuffer("select * from BASE_COURSE_SCHEDULE c where c.is_deleted=0 and c.school_id=? and c.acadyear=? and c.semester=? ");
		List<Object> args = Lists.newArrayList();
		args.add(dto.getSchoolId());
		args.add(dto.getAcadyear());
		args.add(dto.getSemester());
		if(!dto.isXN()) {
			//不要虚拟
			sql.append(" and (c.class_type<>1 or (c.class_type=1 and c.subject_type<>'3' ))");
		}
		
		sql.append(" and  c.week_of_worktime || '_' || day_of_week || '_' || period_interval ||  '_' || period  in  " + SQLUtils.toSQLInString(timeStr));
		if(dto.getClassIds()!=null && dto.getClassIds().length>0){
			sql.append(" and c.class_id in ");
			return queryForInSQL(sql.toString(), args.toArray(new Object[]{}), dto.getClassIds(), new MultiRow());
		}
		return query(sql.toString(), args.toArray(new Object[]{}),new MultiRow());
	}
	
	@Override
	public List<String> findAllPlaceIds(String schoolId, String acadyear, Integer semester,
			Integer week, String gradeId){
		StringBuffer sql = new StringBuffer("select distinct c.place_id from BASE_COURSE_SCHEDULE c where"
				+ " c.is_deleted=0 and c.school_id=? and c.acadyear=? and c.semester=? and week_of_worktime>=? ");
		List<Object> args = Lists.newArrayList();
		args.add(schoolId);
		args.add(acadyear);
		args.add(semester);
		args.add(week);
		if(StringUtils.isNotBlank(gradeId)){
			sql.append(" and (exists(select 1 from base_teach_class bc where bc.id=c.class_id and bc.is_deleted=0 and bc.is_using=1 and bc.grade_id=?) " +
					" or exists(select 1 from base_class bc where bc.id=c.class_id and bc.is_deleted=0 and is_graduate =0 and bc.grade_id=?))");
			args.add(gradeId);
			args.add(gradeId);
		}

		return query(sql.toString(), args.toArray(new Object[]{}),new MultiRowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
			
		});
	}
}
