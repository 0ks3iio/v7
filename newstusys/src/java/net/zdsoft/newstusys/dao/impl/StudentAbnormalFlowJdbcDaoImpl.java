package net.zdsoft.newstusys.dao.impl;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SQLUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.dao.StudentAbnormalFlowJdbcDao;
import net.zdsoft.newstusys.entity.StudentAbnormalFlow;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 
 * @author weixh
 * 2019年2月28日	
 */
@Repository
public class StudentAbnormalFlowJdbcDaoImpl extends BaseDao<StudentAbnormalFlow> implements StudentAbnormalFlowJdbcDao {

	@Override
	public StudentAbnormalFlow setField(ResultSet rs) throws SQLException {
		StudentAbnormalFlow abnormalFlow = new StudentAbnormalFlow();
        abnormalFlow.setId(rs.getString("abflowid"));
        abnormalFlow.setApplyId(rs.getString("apply_id"));
        abnormalFlow.setBusinessType(rs.getInt("business_type"));
        abnormalFlow.setSchid(rs.getString("schid"));
        abnormalFlow.setStuid(rs.getString("stuid"));
        abnormalFlow.setFlowtype(rs.getString("flowtype"));
        abnormalFlow.setFlowdate(rs.getTimestamp("flowdate"));
        abnormalFlow.setFlowreason(rs.getString("flowreason"));
        abnormalFlow.setPassdate(rs.getTimestamp("passdate"));
        abnormalFlow.setPassfilecode(rs.getString("passfilecode"));
        abnormalFlow.setFlowsource(rs.getString("flowsource"));
        abnormalFlow.setFlowto(rs.getString("flowto"));
        abnormalFlow.setFlowexplain(rs.getString("flowexplain"));
        abnormalFlow.setAcadyear(rs.getString("acadyear"));
        abnormalFlow.setSemester(rs.getString("semester"));
        abnormalFlow.setOperator(rs.getString("operator"));
        abnormalFlow.setOperateunit(rs.getString("operateunit"));
        abnormalFlow.setUpdatestamp(rs.getLong("updatestamp"));
        abnormalFlow.setFlowtypeoption(rs.getString("flowtypeoption"));
        abnormalFlow.setCurrentclassid(rs.getString("currentclassid"));
        abnormalFlow.setSectionid(rs.getString("sectionid"));
        abnormalFlow.setToschoolid(rs.getString("toschoolid"));
        abnormalFlow.setSchopinion(rs.getString("schopinion"));
        abnormalFlow.setOldunitivecode(rs.getString("oldunitivecode"));
        abnormalFlow.setRemark(rs.getString("remark"));
        return abnormalFlow;
	}

	@Override
	public void save(StudentAbnormalFlow dgStudentAbnormalflow) {
		String sql = "insert into student_abnormalflow(abflowid, apply_id, business_type, schid, stuid, flowtype, flowdate, flowreason, "
				+"passdate, passfilecode, flowsource, flowto, flowexplain, operator, operateunit, updatestamp, "
				+"flowtypeoption, currentclassid, sectionid, toschoolid, schopinion, oldunitivecode, remark, acadyear, semester) "
				+"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		if (StringUtils.isBlank(dgStudentAbnormalflow.getId())){
			dgStudentAbnormalflow.setId(UuidUtils.generateUuid());
		}
		Object[] args = new Object[]{
			dgStudentAbnormalflow.getId(), dgStudentAbnormalflow.getApplyId(), 
			dgStudentAbnormalflow.getBusinessType(), dgStudentAbnormalflow.getSchid(), 
			dgStudentAbnormalflow.getStuid(), dgStudentAbnormalflow.getFlowtype(), 
			dgStudentAbnormalflow.getFlowdate(), dgStudentAbnormalflow.getFlowreason(), 
			dgStudentAbnormalflow.getPassdate(), dgStudentAbnormalflow.getPassfilecode(), 
			dgStudentAbnormalflow.getFlowsource(), dgStudentAbnormalflow.getFlowto(), 
			dgStudentAbnormalflow.getFlowexplain(), dgStudentAbnormalflow.getOperator(), 
			dgStudentAbnormalflow.getOperateunit(), dgStudentAbnormalflow.getUpdatestamp(), 
			dgStudentAbnormalflow.getFlowtypeoption(), dgStudentAbnormalflow.getCurrentclassid(), 
			dgStudentAbnormalflow.getSectionid(), dgStudentAbnormalflow.getToschoolid(), 
			dgStudentAbnormalflow.getSchopinion(), dgStudentAbnormalflow.getOldunitivecode(), 
			dgStudentAbnormalflow.getRemark(), dgStudentAbnormalflow.getAcadyear(), 
			dgStudentAbnormalflow.getSemester()
		};
		update(sql, args);
	}
	
	public List<StudentAbnormalFlow> findAbnormalFlowByFlowByStuid(String stuid, String[] flowtypes){
		String sql = "select * from student_abnormalflow where stuid = ? and flowtype in";
		return queryForInSQL(sql, new Object[] {stuid}, flowtypes, new MultiRow(), " order by updatestamp desc");
	}

	public List<StudentAbnormalFlow> findFlowByStuidsType(String schoolId, String[] stuids, String[] flowtypes){
		String sql = "select * from student_abnormalflow where schid = ? and flowtype in "+ SQLUtils.toSQLInString(flowtypes)+" AND stuid IN";
		return queryForInSQL(sql, new Object[] {schoolId}, stuids, new MultiRow(), " order by stuid,flowtype,updatestamp desc");
	}

	@Override
	public List<StudentAbnormalFlow> findAbnormalFlowByFlowTypes(
			String[] unitIds,String[] flowTypes, Pagination page) {
		String sql = "select * from student_abnormalflow where flowtype in ('"+StringUtils.join(flowTypes, "','")+"') and schid in";
		if(null!=page){
			return queryForInSQL(sql, new Object[] {}, unitIds, new MultiRow()," order by flowdate desc,updatestamp desc",page);
		}
		return queryForInSQL(sql, new Object[] {}, unitIds, new MultiRow()," order by flowdate desc,updatestamp desc");
	}

	@Override
	public List<StudentAbnormalFlow> findAbnormalFlowStudent(String schoolId,
			String acadyear, String semester, String[] flowTypes) {
		String sql = "select * from student_abnormalflow where schid = ? and acadyear = ? and semester = ? and flowtype in";
		return queryForInSQL(sql, new Object[] {schoolId, acadyear,semester }, flowTypes, new MultiRow(), " order by updatestamp desc");
	}

	public List<StudentAbnormalFlow> findAbnormalFlowStudentSection(String schoolId,
																	String acadyear, String semester, String section, String[] flowTypes){
		String sql = "select * from student_abnormalflow where schid = ? and acadyear = ? and semester = ? and sectionid=? and flowtype in";
		return queryForInSQL(sql, new Object[] {schoolId, acadyear,semester,section }, flowTypes, new MultiRow(), " order by updatestamp desc");
	}

}
