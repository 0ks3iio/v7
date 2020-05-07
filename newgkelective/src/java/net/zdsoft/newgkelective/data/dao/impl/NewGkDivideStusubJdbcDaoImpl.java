package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;
import net.zdsoft.newgkelective.data.dao.NewGkDivideStusubJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkDivideStusub;

@Repository
public class NewGkDivideStusubJdbcDaoImpl extends BaseDao<NewGkDivideStusub> implements NewGkDivideStusubJdbcDao{

	@Override
	public NewGkDivideStusub setField(ResultSet rs) throws SQLException {
		NewGkDivideStusub sub=new NewGkDivideStusub();
		sub.setClassId(rs.getString("class_id"));
		sub.setClassName(rs.getString("class_name"));
		sub.setStudentCode(rs.getString("student_code"));
		sub.setStudentId(rs.getString("student_id"));
		sub.setStudentName(rs.getString("student_name"));
		sub.setStudentSex(rs.getString("student_sex"));
		sub.setSubjectIds(rs.getString("subject_ids"));
		sub.setSubjectType(rs.getString("subject_type"));
		//sub.setDivideId(rs.getString("divide_id"));
		//sub.setChoiceId(rs.getString("choice_id"));
		//sub.setId(rs.getString("id"));
		//sub.setUnitId(rs.getString("unit_id"));
		//sub.setCreationTime(rs.getTimestamp("creation_time"));
		//sub.setModifyTime(rs.getTimestamp("modify_time"));
		return sub;
	}

	@Override
	public List<NewGkDivideStusub> findByDivideIdAndSubject(String divideId,String subjectType, String[] subjectIdArr) {
		List<Object> obj=new ArrayList<>();
		StringBuffer sql=new StringBuffer("select * from newgkelective_divide_stusub where divide_id= ? ");
		obj.add(divideId);
		if(StringUtils.isNotBlank(subjectType)) {
			sql.append(" and subject_type=? ");
			obj.add(subjectType);
		}
		if(subjectIdArr!=null) {
			for(String s:subjectIdArr) {
				sql.append(" and subject_ids like ? ");
				obj.add("%"+s+"%");
			}
		}
		sql.append(" order by class_name,subject_ids,student_code,student_sex");
		return query(sql.toString(), obj.toArray(new Object[0]),new MultiRow());
	}

	@Override
	public List<NewGkDivideStusub> findNoArrangeXzbStudent(String divideId, String[] subjectIdArr) {
		List<Object> obj=new ArrayList<>();
		StringBuffer sql=new StringBuffer("select * from newgkelective_divide_stusub b where b.divide_id= ? and b.subject_type=? ");
		obj.add(divideId);
		obj.add(NewGkElectiveConstant.SUBJECT_TYPE_A);
		if(subjectIdArr!=null) {
			for(String s:subjectIdArr) {
				sql.append(" and b.subject_ids like ? ");
				obj.add("%"+s+"%");
			}
		}
		sql.append(" and not exists(select 1 from newgkelective_class_student t where t.divide_id=b.divide_id and t.student_id=b.student_id "
				+ "and exists(select 1 from newgkelective_divide_class s where s.divide_id=t.divide_id and s.id=t.class_id and s.class_type=0)) ");
		sql.append(" order by b.class_name,b.subject_ids,b.student_code,b.student_sex");
		return query(sql.toString(), obj.toArray(new Object[0]),new MultiRow());
	}

}
