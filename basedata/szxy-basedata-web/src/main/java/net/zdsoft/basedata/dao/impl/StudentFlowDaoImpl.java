/*
* Project: v7
* Author : shenke
* @(#) StudentFlowDaoImpl.java Created on 2016-8-5
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.zdsoft.basedata.dao.StudentFlowJdbcDao;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.StudentFlow;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;

/**
 * @description: TODO
 * @author: shenke
 * @version: 1.0
 * @date: 2016-8-5上午11:44:42
 */
public class StudentFlowDaoImpl extends BaseDao<StudentFlow> implements StudentFlowJdbcDao{

	@Override
	public StudentFlow setField(ResultSet rs) throws SQLException {
		StudentFlow studentFlow = new StudentFlow();
		studentFlow.setId(rs.getString("id"));
		studentFlow.setAcadyear(rs.getString("acadyear"));
		studentFlow.setClassId(rs.getString("class_id"));
		studentFlow.setClassName(rs.getString("class_name"));
		studentFlow.setCreationTime(rs.getTimestamp("creation_time"));
		studentFlow.setFlowType(rs.getString("flow_type"));
		studentFlow.setHandleUserId(rs.getString("handle_user_id"));
		studentFlow.setHandleUserName(rs.getString("handle_user_name"));
		studentFlow.setPin(rs.getString("pin"));
		studentFlow.setReason(rs.getString("reason"));
		studentFlow.setSchoolId(rs.getString("school_id"));
		studentFlow.setSemester(rs.getInt("semester"));
		studentFlow.setStudentId(rs.getString("student_id"));
		studentFlow.setSchoolId(rs.getString("school_id"));
		studentFlow.setSchoolName(rs.getString("school_name"));
		return studentFlow;
	}

	@Override
	public List<StudentFlow> searchFlows(String studentname,
			String identityCard, String pin, String flowType, Pagination page) {
		StringBuffer sql = new StringBuffer();
		sql.append("select f.* from base_student_normalflow f, base_student s where s.is_deleted = 0 ");
		sql.append(" and s.student_name = ?")
		   .append(" and s.identity_card = ?")
		   .append(" and f.pin = ? ")
		   .append(" and f.flow_type = ? ")
		   .append(" and s.is_leave_school = ?");
		if(page != null){
			return query(sql.toString(), new Object[]{studentname,identityCard,pin,flowType,Student.STUDENT_LEAVE}, new MultiRow(), page);
		}
		return query(sql.toString(), new Object[]{studentname,identityCard,pin,flowType,Student.STUDENT_LEAVE}, new MultiRow());
	}
	

}
