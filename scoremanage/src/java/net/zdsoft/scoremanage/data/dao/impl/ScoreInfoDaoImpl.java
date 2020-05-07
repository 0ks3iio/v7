package net.zdsoft.scoremanage.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.MultiRowMapper;
import net.zdsoft.framework.utils.SQLUtils;
import net.zdsoft.scoremanage.data.constant.ScoreDataConstants;
import net.zdsoft.scoremanage.data.dao.ScoreInfoJdbcDao;
import net.zdsoft.scoremanage.data.entity.ScoreInfo;

@Repository
public class ScoreInfoDaoImpl extends BaseDao<ScoreInfo> implements ScoreInfoJdbcDao{

	@Override
	public List<ScoreInfo> findScoreInfoListRanking(String examId, String scoreStatus, String... studentIds) {
		StringBuffer sql=new StringBuffer("select unit_id,exam_id,student_id,class_id,sum(cast(score as decimal)) as all_score from scoremanage_info where class_id is not null and input_type='S' and exam_id=?");
		List<Object> obj = new ArrayList<Object>();
		obj.add(examId);
		if(StringUtils.isNotBlank(scoreStatus)){
			sql.append(" and score_status=? ");
			obj.add(scoreStatus);
		}
		if(studentIds !=null && studentIds.length > 0){
			sql.append(" and "+SQLUtils.toSQLInString(studentIds,"student_id",true));
		}
		sql.append(" group by unit_id,exam_id,student_id,class_id order by all_score desc");
		return query(sql.toString(),obj.toArray(new Object[0]), new MultiRowMapper<ScoreInfo>(){

			@Override
			public ScoreInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
				ScoreInfo sif = new ScoreInfo();
				sif.setUnitId(rs.getString("unit_id"));
				sif.setExamId(rs.getString("exam_id"));
				sif.setStudentId(rs.getString("student_id"));
				sif.setClassId(rs.getString("class_id"));
				sif.setAllScore(rs.getFloat("all_score"));
				return sif;
			}
		});
	}

	@Override
	public ScoreInfo setField(ResultSet rs) throws SQLException {
		ScoreInfo sif = new ScoreInfo();
		sif.setId(rs.getString("id"));
		sif.setStudentId(rs.getString("student_id"));
		sif.setAcadyear(rs.getString("acadyear"));
		sif.setSemester(rs.getString("semester"));
		sif.setExamId(rs.getString("exam_id"));
		sif.setUnitId(rs.getString("unit_id"));
		sif.setSubjectId(rs.getString("subject_id"));
		sif.setClassId(rs.getString("class_id"));
		sif.setTeachClassId(rs.getString("teach_class_id"));
		sif.setGradeType(rs.getString("grade_type"));
		sif.setIsInStat(rs.getString("is_in_stat"));
		sif.setInputType(rs.getString("input_type"));
		sif.setScoreStatus(rs.getString("score_status"));
		sif.setOperatorId(rs.getString("operator_id"));
		sif.setScore(rs.getString("score"));
		sif.setCreationTime(rs.getTimestamp("creation_time"));
		sif.setModifyTime(rs.getTimestamp("modify_time"));
		sif.setToScore(rs.getString("to_score"));
		return sif;
	}

	@Override
	public List<ScoreInfo> findListByExamId(String examId, String inputType, String scoreStatus, String... studentIds) {
		StringBuffer sql=new StringBuffer("select * from scoremanage_info where exam_id=?");
		List<Object> obj = new ArrayList<Object>();
		obj.add(examId);
		if(StringUtils.isNotBlank(scoreStatus)){
			sql.append(" and score_status=?");
			obj.add(scoreStatus);
		}
		if(studentIds !=null && studentIds.length > 0){
			sql.append(" and "+SQLUtils.toSQLInString(studentIds,"student_id",true));
		}
		if(StringUtils.isNotBlank(inputType)){
			sql.append(" and input_type=?");
			obj.add(inputType);
			if(ScoreDataConstants.ACHI_SCORE.equals(inputType))
			sql.append(" order by subject_id,cast(score as decimal) desc");
		}
		return query(sql.toString(),obj.toArray(new Object[0]), new MultiRow());
	}
	
	public Map<String, Integer> findNumByExamIdClsIds(String examId, String... clsIds){
		String sql = "select class_id, subject_id, count(*) as num from scoremanage_info where exam_id='"+examId+"'  " +
				" and exists(select 1 from base_student ss where ss.is_deleted=0 and ss.is_leave_school=0 " +
				" and ss.class_id in "+SQLUtils.toSQLInString(clsIds)+" and ss.id=scoremanage_info.student_id) group by class_id, subject_id";
		return queryForMap(sql, new MapRowMapper<String, Integer>() {

			public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("class_id") + rs.getString("subject_id");
			}

			@Override
			public Integer mapRowValue(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("num");
			}
		});
	}
	
	public Map<String, Integer> findNumByExamIdTeaClsIds(String examId, String... teaClsIds){

		String sql = "select teach_class_id, subject_id, count(*) as num from scoremanage_info where exam_id='"+examId+"'  " +
				" and exists(select 1 from base_teach_class_stu ss where ss.is_deleted=0 and ss.student_id=scoremanage_info.student_id and ss.class_id in "
				+SQLUtils.toSQLInString(teaClsIds)+") group by teach_class_id, subject_id";
		return this.queryForMap(sql, new MapRowMapper<String, Integer>() {

			public String mapRowKey(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("teach_class_id") + rs.getString("subject_id");
			}

			@Override
			public Integer mapRowValue(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("num");
			}
		});
	}

}
