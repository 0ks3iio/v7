package net.zdsoft.diathesis.data.dao.impl;

import net.zdsoft.diathesis.data.dao.DiathesisRecordJdbcDao;
import net.zdsoft.diathesis.data.entity.DiathesisRecord;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SQLUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DiathesisRecordDaoImpl extends BaseDao<DiathesisRecord> implements DiathesisRecordJdbcDao {

	@Override
	public DiathesisRecord setField(ResultSet rs) throws SQLException {
		DiathesisRecord diathesisRecord = new DiathesisRecord();
		diathesisRecord.setId(rs.getString("id"));
		diathesisRecord.setUnitId(rs.getString("unit_id"));
		diathesisRecord.setProjectId(rs.getString("project_id"));
		diathesisRecord.setStuId(rs.getString("stu_id"));
		diathesisRecord.setStatus(rs.getString("status"));
		diathesisRecord.setCreationTime(rs.getTimestamp("creation_time"));
		diathesisRecord.setOperator(rs.getString("operator"));
		diathesisRecord.setAuditOpinion(rs.getString("audit_opinion"));
		diathesisRecord.setAuditTime(rs.getTimestamp("audit_time"));
		diathesisRecord.setAuditor(rs.getString("auditor"));
		diathesisRecord.setAcadyear(rs.getString("acadyear"));
		diathesisRecord.setSemester(rs.getInt("semester"));
		diathesisRecord.setGradeCode(rs.getString("grade_code"));
		return diathesisRecord;
	}

	@Override
	public List<DiathesisRecord> findListByCondition(String unitId, String projectId, String acadyear, String semester,
			String classId, String[] classIds, String studentId, String[] status, Pagination page) {
		StringBuilder sb = new StringBuilder("select nr.* from newdiathesis_record nr where nr.unit_id=? and nr.project_id=?");
		List<Object> listOfArgs = new ArrayList<Object>();
		listOfArgs.add(unitId);
		listOfArgs.add(projectId);
		
		if(StringUtils.isNotBlank(acadyear)){
			sb.append(" and nr.acadyear=?");
			listOfArgs.add(acadyear);
		}
		
		if(StringUtils.isNotBlank(semester)){
			sb.append("and nr.semester=?");
			listOfArgs.add(semester);
		}
		
		if(StringUtils.isNotBlank(classId)){
			sb.append(" and exists (select 1 from base_student bs where bs.class_id=? and bs.is_deleted=0 and bs.is_leave_school=0 and bs.id=nr.stu_id)");
			listOfArgs.add(classId);
		}
		
		if(ArrayUtils.isNotEmpty(classIds)){
			sb.append(" and exists (select 1 from base_student bs where bs.class_id in "
					+ SQLUtils.toSQLInString(classIds)
					+ " and bs.is_deleted=0 and bs.is_leave_school=0 and bs.id=nr.stu_id)");
		}
		
		if(StringUtils.isNotBlank(studentId)){
			sb.append(" and nr.stu_id=?");
			listOfArgs.add(studentId);
		}
		
		if(ArrayUtils.isNotEmpty(status)){
			sb.append(" and status in "+SQLUtils.toSQLInString(status));
		}
		
		sb.append(" order by creation_time desc");
		
		if(page!=null){
			return query(sb.toString(), listOfArgs.toArray(new Object[0]), new MultiRow(), page);
		}
		return query(sb.toString(), listOfArgs.toArray(new Object[0]), new MultiRow());
	}

}
