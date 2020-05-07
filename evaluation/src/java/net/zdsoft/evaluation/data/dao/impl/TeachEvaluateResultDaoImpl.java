package net.zdsoft.evaluation.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.evaluation.data.constants.EvaluationConstants;
import net.zdsoft.evaluation.data.dao.TeachEvaluateResultJdbcDao;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResult;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.MapRowMapper;
import net.zdsoft.framework.utils.MultiRowMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;

@Repository
public class TeachEvaluateResultDaoImpl extends BaseDao<TeachEvaluateResult> implements TeachEvaluateResultJdbcDao {
	
	@Override
	public Set<String> getResultGradeClsId(String projectId) {
		String sql = "select distinct (grade_id || ',' || class_id) as gradeClsId from teach_evaluate_result where state = '2' and project_id = ?";
		List<String> list =  query(sql, new Object[]{projectId}, new MultiRowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("gradeClsId");
			}});
		Set<String> gradeClsIds = new HashSet<String>();
		gradeClsIds.addAll(list);
		return gradeClsIds;
	}
	@Override
	public Set<String> getResultClsSubTeaIds(String projectId) {
		String sql = "select distinct (class_id || ',' || subject_id || ',' || teacher_id) as clsSubTeaId from teach_evaluate_result where state = '2' and project_id = ?";
		List<String> list =  query(sql, new Object[]{projectId}, new MultiRowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("clsSubTeaId");
			}});
		Set<String> clsSubTeaIds = new HashSet<String>();
		clsSubTeaIds.addAll(list);
		return clsSubTeaIds;
	}
	@Override
	public Set<String> getResultSubIds(String projectId) {
		String sql = "select distinct (subject_id || ',' || teacher_id) as subTeaId from teach_evaluate_result where state = '2' and project_id = ?";
		List<String> list =  query(sql, new Object[]{projectId}, new MultiRowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("subTeaId");
			}});
		Set<String> subTeaIds = new HashSet<String>();
		subTeaIds.addAll(list);
		return subTeaIds;
	}
	public Set<String> getStuIdByProjectId(String projectId, String classId, String subId, String teacherId) {
		StringBuffer sql = new StringBuffer("select distinct operator_id from teach_evaluate_result where state = '2' and project_id = ?");
        List<Object> args = Lists.newArrayList();
		args.add(projectId);
		if(StringUtils.isNotBlank(classId)){
			sql.append(" and class_id = ?");
			args.add(classId);
		}
		if(StringUtils.isNotBlank(subId)){
			sql.append(" and subject_id = ?");
			args.add(subId);
		}
		if(StringUtils.isNotBlank(teacherId)){
			sql.append(" and teacher_id = ?");
			args.add(teacherId);
		}
		List<String> list =  query(sql.toString(), args.toArray(new Object[0]), new MultiRowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("operator_id");
			}});
		Set<String> stuIds = new HashSet<String>();
		stuIds.addAll(list);
		return stuIds;
	}
	@Override
	public Map<String, Integer> getCountMapByProjectIds(String[] projectIds) {
		String sql = "select project_id, count(1) as num from teach_evaluate_result where project_id in";
		return queryForInSQL(sql, null, projectIds,new MapRowMapper<String,Integer>() {
			@Override
			public String mapRowKey(ResultSet rs, int rowNum)
					throws SQLException {
				return rs.getString("project_id");
			}
			@Override
			public Integer mapRowValue(ResultSet rs, int rowNum)
					throws SQLException {
				return rs.getInt("num");
			}
			
		} ," group by project_id");
	}
	
	@Override
	public Map<String, Float> findTeaRankBy(String projectId,String evaluateType) {
		String sql = "select stat_teacher_id,stat_class_id,sum(score) as countScore from teach_evaluate_result_stat where project_id=? and dimension=? and option_id is null group by stat_class_id,stat_teacher_id";
		String dimension = null; 
		if(StringUtils.equals(evaluateType, EvaluationConstants.EVALUATION_TYPE_TOTOR)) {
			dimension = EvaluationConstants.STATE__DIMENSION_ONE;
		}else {
			dimension = EvaluationConstants.STATE__DIMENSION_CLASS;
		}
		return queryForMap(sql, new Object[] {projectId,dimension},new MapRowMapper<String,Float>() {
			@Override
			public String mapRowKey(ResultSet rs, int rowNum)
					throws SQLException {
				return rs.getString("stat_teacher_id")+","+rs.getString("stat_class_id");
			}
			@Override
			public Float mapRowValue(ResultSet rs, int rowNum)
					throws SQLException {
				System.out.println(rs.getFloat("countScore"));
				return rs.getFloat("countScore");
			}
		});
	}
	
	@Override
	public TeachEvaluateResult setField(ResultSet rs) throws SQLException {
		return null;
	}
	@Override
	public Set<String> getResultGradeTeaId(String projectId) {
		String sql = "select distinct (grade_id || ',' || teacher_id) as gradeTeaId from teach_evaluate_result where state = '2' and project_id = ?";
		List<String> list =  query(sql, new Object[]{projectId}, new MultiRowMapper<String>(){
			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("gradeTeaId");
			}});
		Set<String> gradeTeaIds = new HashSet<String>();
		gradeTeaIds.addAll(list);
		return gradeTeaIds;
	}

}
