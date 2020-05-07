package net.zdsoft.basedata.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.CourseJdbcDao;
import net.zdsoft.basedata.entity.Course;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.SQLUtils;

@Repository
public class CourseJdbcDaoImpl extends BaseDao<Course> implements CourseJdbcDao{

	@Override
	public Course setField(ResultSet rs) throws SQLException {
		Course ent = new Course();
		ent.setId(rs.getString("id"));
		ent.setSection(rs.getString("SECTION"));
		ent.setSubjectCode(rs.getString("SUBJECT_CODE"));
		ent.setSubjectName(rs.getString("SUBJECT_NAME")); 
		ent.setIsUsing(rs.getInt("IS_USING")); 
		ent.setUnitId(rs.getString("UNIT_ID")); 
		ent.setIsDeleted(rs.getInt("IS_DELETED")); 
		ent.setEventSource(rs.getInt("EVENT_SOURCE"));
		ent.setCreationTime(rs.getTimestamp("CREATION_TIME")); 
		ent.setModifyTime(rs.getTimestamp("MODIFY_TIME")); 
		ent.setShortName(rs.getString("short_name"));
		ent.setOrderId(toInteger(rs.getBigDecimal("order_id"))); 
		ent.setSubjectType(rs.getString("SUBJECT_TYPE"));
		ent.setFullMark(toInteger(rs.getBigDecimal("FULL_MARK")));
		ent.setType(rs.getString("TYPE"));
		ent.setCourseTypeId(rs.getString("COURSE_TYPE_ID"));
		ent.setInitCredit(toInteger(rs.getBigDecimal("INIT_CREDIT")));
		ent.setInitPassMark(toInteger(rs.getBigDecimal("INIT_PASS_MARK")));
		ent.setBgColor(rs.getString("bg_color"));
		return ent;
	}
	
	private Integer toInteger(BigDecimal bd){
		if(bd==null){
			return null;
		}
		Integer integer = new Integer(bd.intValue());
		return integer;
	}
	@Override
	public List<Course> getCourseBySection(String[] unitIds,String type,String subjName,String courseTypeId,String section,
			Integer isUsing, String subjectType) {
		StringBuffer sql = new StringBuffer("select * from base_course where IS_DELETED = 0 ");
		List<Object> args = Lists.newArrayList();
		
		if(unitIds!=null){
			sql.append(" and UNIT_ID in "+SQLUtils.toSQLInString(unitIds));
		}
		if(StringUtils.isNotBlank(type)){
			args.add(type);
			sql.append(" and TYPE = ?");
		}
		
		if(StringUtils.isNotBlank(subjName)){
			args.add("%"+subjName+"%");
			sql.append(" and SUBJECT_NAME like ? ");
		}
		if(StringUtils.isNotBlank(courseTypeId)){
			args.add(courseTypeId);
			sql.append(" and COURSE_TYPE_ID = ? ");
		}
		
		if(StringUtils.isNotBlank(section)){
			sql.append(" and ( SECTION is null  ");
			if(section.indexOf(",")>0){
				String[] sts = section.split(",");
				String stv=" or (";
				for(String str:sts){
					stv +=" SECTION like '%"+str+"%' or";
				}
				
				sql.append(stv.substring(0, stv.length()-2)+") ");
			}else{
				args.add("%"+section+"%");
				sql.append(" or SECTION like ?");			
			}
			sql.append(" )");
		}
		
		if(isUsing != null){
			args.add(isUsing);
			sql.append(" and IS_USING = ?");
		}
		//wyy.课程来源 教育局 单位
		if(StringUtils.isNotBlank(subjectType)){
			args.add(subjectType);
			sql.append(" and subject_type = ? ");
		}
		
		sql.append(" order by order_id");
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	}
	
	@Override
	public List<Course> getListByCondition(String[] unitIds,String[] types,String subjName,String courseTypeId,String section,Integer isUsing, String subjectType) {
		StringBuffer sql = new StringBuffer("select * from base_course where IS_DELETED = 0 ");
		List<Object> args = Lists.newArrayList();
		
		if(unitIds!=null){
			sql.append(" and UNIT_ID in "+SQLUtils.toSQLInString(unitIds));
		}
		if(types!=null){
			sql.append(" and TYPE in "+SQLUtils.toSQLInString(types));
		}
		
		if(StringUtils.isNotBlank(subjName)){
			args.add("%"+subjName+"%");
			sql.append(" and SUBJECT_NAME like ? ");
		}
		if(StringUtils.isNotBlank(courseTypeId)){
			args.add(courseTypeId);
			sql.append(" and COURSE_TYPE_ID = ? ");
		}
		
//		if(StringUtils.isNotBlank(section)){
//			args.add("%"+section+"%");
//			sql.append(" and SECTION like ? ");
//		}
		if(StringUtils.isNotBlank(section)){
			sql.append(" and ( SECTION is null  ");
			if(section.indexOf(",")>0){
				String[] sts = section.split(",");
				String stv=" or (";
				for(String str:sts){
					stv +=" SECTION like '%"+str+"%' or";
				}
				
				sql.append(stv.substring(0, stv.length()-2)+") ");
			}else{
				args.add("%"+section+"%");
				sql.append(" or SECTION like ?");			
			}
			sql.append(" )");
		}
		if(isUsing != null){
			args.add(isUsing);
			sql.append(" and IS_USING = ?");
		}
		//wyy.课程来源 教育局 单位
		if(StringUtils.isNotBlank(subjectType)){
			args.add(subjectType);
			sql.append(" and subject_type = ? ");
		}
		
		sql.append(" order by order_id");
		return query(sql.toString(), args.toArray(new Object[0]), new MultiRow());
	}
	public void updateIdAndUnitId(List<String[]> courseList) {
		String sql = "update base_course set id=?,unit_id = ? where id=?";
		List<Object[]> objList = new ArrayList<Object[]>();
		for (String[] stu : courseList) {
			Object[] obj = new Object[] { stu[0], stu[1], stu[2] };
			objList.add(obj);
		}
		batchUpdate(sql, objList, new int[] { Types.VARCHAR, Types.VARCHAR,
				Types.VARCHAR });
	}
}
