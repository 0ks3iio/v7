package net.zdsoft.basedata.dao.impl;

import net.zdsoft.basedata.dao.ClassTeachingJdbcDao;
import net.zdsoft.basedata.entity.ClassTeaching;
import net.zdsoft.framework.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClassTeachingJdbcDaoImpl extends BaseDao<ClassTeaching> implements ClassTeachingJdbcDao {

	@Override
	public ClassTeaching setField(ResultSet rs) throws SQLException {
		ClassTeaching ent = new ClassTeaching();
		ent.setId(rs.getString("id"));
		ent.setUnitId(rs.getString("UNIT_ID"));
		ent.setClassId(rs.getString("class_id"));
		ent.setSubjectId(rs.getString("SUBJECT_id"));
		ent.setIsTeaCls(rs.getInt("IS_TEA_CLS"));
		ent.setCredit(rs.getInt("CREDIT"));
		ent.setTeacherId(rs.getString("TEACHER_ID"));
		ent.setCourseHour(rs.getFloat("COURSE_HOUR"));
		ent.setOperatorId(rs.getString("OPERATOR_ID"));
		ent.setAcadyear(rs.getString("ACADYEAR"));
		ent.setSemester(rs.getString("SEMESTER"));
		ent.setIsDeleted(rs.getInt("IS_DELETED"));
		ent.setEventSource(rs.getInt("EVENT_SOURCE"));
		ent.setCreationTime(rs.getTimestamp("CREATION_TIME")); 
		ent.setModifyTime(rs.getTimestamp("MODIFY_TIME"));
		ent.setPunchCard(rs.getInt("PUNCH_CARD"));
		ent.setFullMark(rs.getInt("FULL_MARK"));
		ent.setPassMark(rs.getInt("PASS_MARK"));
		ent.setSubjectType(rs.getString("SUBJECT_TYPE"));
		ent.setWeekType(rs.getInt("WEEK_TYPE"));
		return ent;
	}
	
	private Integer toInteger(BigDecimal bd){
		if(bd==null){
			return null;
		}
		Integer integer = new Integer(bd.intValue());
		return integer;
	}


	public List<ClassTeaching> findClassTeachingListByTeacherId(String unitId, String[] teaherIds){
		String sql="select * from base_class_teaching where is_deleted=0 " +
				"and unit_id=? and " +
				"exists(select 1 from base_class where is_deleted=0 and is_graduate=0 and id=base_class_teaching.class_id) and teacher_id in ";
		return this.queryForInSQL(sql,new Object[]{unitId},teaherIds,new MultiRow());
	}

	public List<ClassTeaching> findClassTeachingListHistoryByTeacherId(String unitId, String[] teaherIds){
		String sql="select * from base_class_teaching where is_deleted=0 " +
				"and unit_id=? and teacher_id in ";
		return this.queryForInSQL(sql,new Object[]{unitId},teaherIds,new MultiRow());
	}
	public List<ClassTeaching> findClassTeachingListByTeacherId(String[] teaherIds){
		String sql="select * from base_class_teaching where is_deleted=0 " +
				"and  exists(select 1 from base_class where is_deleted=0 and is_graduate=0 and id=base_class_teaching.class_id)" +
				"and teacher_id in ";
		return this.queryForInSQL(sql,null,teaherIds,new MultiRow());
	}

	public List<ClassTeaching> findByIsDeletedAndSubjectIdIn(int isDeletedFalse, String[] subjectIds){
		String sql ="select * from base_class_teaching where is_deleted=?" +
				"and exists(select 1 from base_class where is_deleted=0 and is_graduate=0 and id=base_class_teaching.class_id)"
				+" and subject_id in ";
		return this.queryForInSQL(sql,new Object[]{isDeletedFalse},subjectIds,new MultiRow());
	}
}
