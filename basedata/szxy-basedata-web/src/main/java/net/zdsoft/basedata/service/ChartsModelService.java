package net.zdsoft.basedata.service;

import java.util.List;

import net.zdsoft.basedata.entity.ChartsModel;

public interface ChartsModelService {

	/**
	 * 获取模块中图表启用的
	 * 数据库
	 * @param modelId
	 * @param modelType
	 * @param tagType
	 * @return
	 */
	List<ChartsModel> findByModelId(Integer modelId, String modelType, String tagType);

	/**
	 * 获取所有启用的
	 * 数据库
	 * @return
	 */
	List<ChartsModel> findAll();

}
