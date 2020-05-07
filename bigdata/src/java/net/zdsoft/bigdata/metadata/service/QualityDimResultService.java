package net.zdsoft.bigdata.metadata.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.metadata.entity.QualityDimResult;

public interface QualityDimResultService extends BaseService<QualityDimResult, String>{
	/**
	 * 根据类型获取质量维度结果list
	 * @param type
	 * @return
	 */
	public List<QualityDimResult> findQualityDimResultsByType(Integer type);
	
	public List<QualityDimResult> findQualityDimResultsByTypeDesc(Integer type);
	
	/**
	 * 根据类型获取质量维度结果list
	 * @param type
	 * @return
	 */
	public List<QualityDimResult> findQualityDimResultsByTypeAndStatus(Integer type,Integer status);
	
	/**
	 * 根据类型和名称获取结果
	 * @param type
	 * @param dimName
	 * @return
	 */
	public List<QualityDimResult> findQualityDimResultsByTypeAndDimName(Integer type,String dimName);
	
	/**
	 * 更新历史数据状态
	 */
	public void updateHistoryDataStatus();
	
	public void deleteAll();
}
