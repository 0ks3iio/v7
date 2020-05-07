package net.zdsoft.newgkelective.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.newgkelective.data.dao.NewGkScoreResultJdbcDao;
import net.zdsoft.newgkelective.data.entity.NewGkScoreResult;

@Repository
public class NewGkScoreResultJdbcDaoImpl extends BaseDao<NewGkScoreResult> implements NewGkScoreResultJdbcDao{

	@Override
	public List<Object[]> findCountByReferId(String unitId, String[] referScoreIds) {
		//in不超过300
//	    String parm = SQLUtils.toSQLInString(referScoreIds);
//		String sql="select count(b.student_id) stu_num,b.refer_score_id refer_score_id from (select distinct d.student_id,d.refer_score_id from newgkelective_score_result d where d.refer_score_id in "+parm
//				+" ) b "+" group by b.refer_score_id";
		
		StringBuffer ff=new StringBuffer("");
		ff.append("select count(b.student_id) stu_num,b.refer_score_id refer_score_id from (select distinct d.student_id,d.refer_score_id from newgkelective_score_result d where d.unit_id=? ");
		ff.append("and exists (select 1 from base_student t where t.id=d.student_id and t.school_id=d.unit_id and t.is_deleted=0 ) ");
		ff.append( "and d.refer_score_id in ");
		
		return queryForInSQL(ff.toString(), 
				new String[]{unitId}, referScoreIds, new MultiRowMapper<Object[]>(){
					@Override
					public Object[] mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Object[] objArr=new Object[2];
						objArr[0]=rs.getObject("stu_num");
						objArr[1]=rs.getObject("refer_score_id");
						return objArr;
					}
				}, ") b "+" group by b.refer_score_id");
		
		
//	   return  query(sql, new MultiRowMapper<Object[]>(){
//
//		   @Override
//			public Object[] mapRow(ResultSet rs, int rowNum)
//					throws SQLException {
//				Object[] objArr=new Object[2];
//				objArr[0]=rs.getObject("stu_num");
//				objArr[1]=rs.getObject("refer_score_id");
//				return objArr;
//			}
//	    	 
//	     });
	    	 
	}

	@Override
	public NewGkScoreResult setField(ResultSet rs) throws SQLException {
		return null;
	}
	
	
	@Override
	public List<Object[]> findCountSubjectByReferId(String unitId, String[] referScoreIds){
		StringBuffer ff=new StringBuffer("");
		ff.append("select count(b.student_id) stum,sum(b.score) scores,b.refer_score_id refer_score_id,b.subject_id from  newgkelective_score_result b where b.unit_id=? ");
		ff.append("and exists (select 1 from base_student t where t.id=b.student_id and t.school_id=b.unit_id and t.is_deleted=0 ) ");
		ff.append( "and b.refer_score_id in ");
		return queryForInSQL(ff.toString(), 
				new String[]{unitId}, referScoreIds, new MultiRowMapper<Object[]>(){
					@Override
					public Object[] mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Object[] objArr=new Object[4];
						objArr[0]=rs.getObject("stum");
						objArr[1]=rs.getObject("scores");
						objArr[2]=rs.getObject("refer_score_id");
						objArr[3]=rs.getObject("subject_id");
						return objArr;
					}
				}, " group by b.refer_score_id,b.subject_id");
	}

}
