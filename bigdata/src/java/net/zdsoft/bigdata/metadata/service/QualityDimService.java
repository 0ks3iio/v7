package net.zdsoft.bigdata.metadata.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.metadata.entity.QualityDim;

public interface QualityDimService extends BaseService<QualityDim, String>{

	/**
	 * 根据类型获取质量维度list
	 * @param type
	 * @return
	 */
	public List<QualityDim> findQualityDimsByType(Integer type);
	
	/**
	 * 根据类型获取质量维度Map key=code 
	 * @param type
	 * @return
	 */
	public Map<String,QualityDim> findQualityDimMapByType(Integer type);
	
	/**
	 * 更新质量维度
	 * @param qualityDim
	 */
	public void updateQualityDim(QualityDim qualityDim) ;
}
