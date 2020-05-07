package net.zdsoft.evaluation.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.evaluation.data.dto.ResultStatDto;
import net.zdsoft.evaluation.data.entity.TeachEvaluateResultStat;

public interface TeachEvaluateResultStatService extends BaseService<TeachEvaluateResultStat, String> {

	public void deleteByProjectId(String projectId);

	public List<TeachEvaluateResultStat> findByProjectIds(String[] projectIds);
	/**
	 * 统计结果
	 * @param projectId
	 */
	public void statResult(String projectId);
	/**
	 * 组装统计结果
	 * @param projectId
	 * @param stateDimension 统计维度
	 * @param subjectId 在班级维度时使用
	 * @param classId 在班级维度时使用
	 * @return
	 */
	public List<ResultStatDto> getResultStatDto(String projectId,
			String statDimension, String subjectId, String classId,String teacherId);
	
	public List<ResultStatDto> getResultSubStat(String projectId,
			String statDimension, String teacherId, String subId);

	public List<ResultStatDto> getResultStatElectiveDto(String projectId,
			String statDimension);
	/**
	 * 获得全校维度和年级维度的数据
	 * @param projectId
	 * @return
	 */
	public Map<String,Float> getResultStatTeaSchDto(String projectId);
	/**
	 * 获得班级维度的数据
	 * @param projectId
	 * @param clsId TODO
	 * @return
	 */
	public Map<String, Float> getResultStatTeaClsDto(String projectId, String clsId);

	public Map<String, Float> getResultStatTeaDto(String projectId, String teaId);
	/**
	 * @param projectId
	 * @return key:optId_teaId or itemId_teaId
	 */
	public Map<String, Float> getResultStatAllDto(String projectId,String evaluateType);
	
	public List<ResultStatDto> findTeaRankBy(String projectId, String evaluateType);

	/**
	 * 获得全校维度和年级维度的数据
	 * @param projectId
	 * @return
	 */
	public Map<String,Float> getResultStatTeaSchExportDto(String projectId);
	
}
