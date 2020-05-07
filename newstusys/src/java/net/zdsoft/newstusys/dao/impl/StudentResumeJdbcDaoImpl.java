package net.zdsoft.newstusys.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.newstusys.dao.StudentResumeJdbcDao;
import net.zdsoft.newstusys.entity.StudentResume;

/**
 * 
 * @author weixh
 * @since 2018年3月6日 下午8:43:51
 */
@Repository
public class StudentResumeJdbcDaoImpl extends BaseDao<StudentResume> implements StudentResumeJdbcDao {

	@Override
	public StudentResume setField(ResultSet rs) throws SQLException {
		StudentResume sr = new StudentResume();
		sr.setId(rs.getString("resumeid"));
		sr.setAuthenticator(rs.getString("authenticator"));
		sr.setDuty(rs.getString("duty"));
		sr.setEnddate(rs.getDate("enddate"));
		sr.setRemark(rs.getString("remark"));
		sr.setSchid(rs.getString("schid"));
		sr.setSchoolname(rs.getString("schoolname"));
		sr.setStartdate(rs.getDate("startdate"));
		sr.setStuid(rs.getString("stuid"));
		sr.setUpdatestamp(rs.getLong("updatestamp"));
		return sr;
	}

	@Override
	public void save(StudentResume sr) {
		String sql = "insert into student_resume(resumeid, stuid, startdate, enddate, authenticator, schoolname, duty," 
				+"remark, updatestamp, schid) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		if(StringUtils.isEmpty(sr.getId())) {
			sr.setId(UuidUtils.generateUuid());
		}
		update(sql, new Object[] {sr.getId(), sr.getStuid(), sr.getStartdate(), sr.getEnddate(), 
				sr.getAuthenticator(), sr.getSchoolname(), sr.getDuty(),
				sr.getRemark(), sr.getUpdatestamp(), sr.getSchid()});
	}

	@Override
	public void update(StudentResume sr) {
		String sql = "update student_resume set startdate=?, enddate=?, authenticator=?, schoolname=?, duty=?," 
				+"remark=?, updatestamp=?, schid=? where resumeid=?";
		if(StringUtils.isEmpty(sr.getId())) {
			sr.setId(UuidUtils.generateUuid());
		}
		update(sql, new Object[] {sr.getStartdate(), sr.getEnddate(), 
				sr.getAuthenticator(), sr.getSchoolname(), sr.getDuty(),
				sr.getRemark(), sr.getUpdatestamp(), sr.getSchid(), sr.getId()});
	}

	@Override
	public void batchSave(List<StudentResume> srs) {
		String sql = "insert into student_resume(resumeid, stuid, startdate, enddate, authenticator, schoolname, duty," 
				+"remark, updatestamp, schid) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		List<Object[]> ar = new ArrayList<Object[]>();
		for (StudentResume sr : srs) {
			if (StringUtils.isEmpty(sr.getId())) {
				sr.setId(UuidUtils.generateUuid());
			}
			ar.add(new Object[] { sr.getId(), sr.getStuid(), sr.getStartdate(), sr.getEnddate(), sr.getAuthenticator(),
							sr.getSchoolname(), sr.getDuty(), sr.getRemark(), sr.getUpdatestamp(),
							sr.getSchid() });
		}
		batchUpdate(sql, ar, new int[] {Types.CHAR, Types.CHAR, Types.DATE, Types.DATE,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.CHAR});
	}

	@Override
	public void batchUpdate(List<StudentResume> srs) {
		String sql = "update student_resume set startdate=?, enddate=?, authenticator=?, schoolname=?, duty=?," 
				+"remark=?, updatestamp=?, schid=? where resumeid=?";
		List<Object[]> ar = new ArrayList<Object[]>();
		for (StudentResume sr : srs) {
			if (StringUtils.isEmpty(sr.getId())) {
				sr.setId(UuidUtils.generateUuid());
			}
			ar.add(new Object[] { sr.getStartdate(), sr.getEnddate(), 
					sr.getAuthenticator(), sr.getSchoolname(), sr.getDuty(),
					sr.getRemark(), sr.getUpdatestamp(), sr.getSchid(), sr.getId() });
		}
		batchUpdate(sql, ar, new int[] {Types.DATE, Types.DATE,
				Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.BIGINT, Types.CHAR, Types.CHAR});
	}

	@Override
	public void deleteByStuIds(String... stuIds) {
		updateForInSQL("delete from student_resume where stuid in", null, stuIds);
	}

	@Override
	public void deleteByIds(String... ids) {
		updateForInSQL("delete from student_resume where resumeid in", null, ids);
	}

	@Override
	public List<StudentResume> findByStuid(String stuId) {
		return query("select * from student_resume where stuid=?", stuId, new MultiRow());
	}

	@Override
	public List<StudentResume> findByStuids(String... stuIds) {
		return queryForInSQL("select * from  student_resume where stuid in ", null, stuIds,new MultiRow());
	}

}
