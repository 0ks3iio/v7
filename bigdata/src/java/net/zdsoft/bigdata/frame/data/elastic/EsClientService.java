package net.zdsoft.bigdata.frame.data.elastic;

import net.zdsoft.framework.entity.Json;

import java.util.List;

public interface EsClientService {

	/**
	 * 插入数据至es
	 * 
	 * @param index
	 * @param type
	 * @param datas
	 */
	public void insertDatas(String index, String type, List<String> datas);

	/**
	 * 普通查询
	 * 
	 * @param index
	 * @param type
	 * @param queryParam
	 * @param resultFieldList
	 * @param page
	 * @param sortParm
	 * @return
	 */
	public List<Json> query(String index, String type, Json queryParam,
			List<String> resultFieldList, Json sortParm, Json page);

	/**
	 * range查询
	 * 
	 * @param index
	 * @param type
	 * @param paramList
	 * @param resultFieldList
	 * @param page
	 * @return
	 */
	public List<Json> rangeQuery(String index, String type,
			List<Json> paramList,List<Json> rangeParamList, List<String> resultFieldList,Json sortParam, Json page);

	/**
	 * 多字段查一值
	 * 
	 * @param index
	 * @param type
	 * @param advanced
	 * @param text
	 * @param resultFieldList
	 * @param fieldNames
	 * @param page
	 * @return
	 */
	public List<Json> multiMatchQuery(String index, String type,
			boolean advanced, String text, List<String> resultFieldList,
			Json page, String... fieldNames);

	/**
	 * 多字段查多值 一个字段对应一值
	 * 
	 * @param index
	 * @param type
	 * @param paramList
	 * @param resultFieldList
	 * @param sortParm
	 * @param page
	 * @return
	 */
	public List<Json> multiGroupQuery(String index, String type,
			List<Json> paramList, List<String> resultFieldList, Json sortParm,
			Json page);

	/**
	 * 聚合统计
	 * 
	 * @param index
	 * @param type
	 * @param dateAggParam
	 * @param dateRangeParam
	 * @param aggList
	 * @param paramList
	 * @return
	 */
	public List<Json> queryAggregation(String index, String type,
			Json dateAggParam, Json dateRangeParam, List<Json> aggList,
			List<Json> paramList);
	
	/**
	 * 判断索引是或存在
	 * @param indexName
	 * @return
	 */
	public boolean isIndexExists(String indexName);

	/**
	 * 新增或者更新索引
	 * @param indexName
	 * @param indexType
	 * @param columns
	 */
	public void upsertIndex(String indexName, String indexType, List<String> columns);
	
	/**
	 * 创建模块操作index
	 */
	public void createModuleOperationIndex();
	
	/**
	 * 创建业务日志index
	 */
	public void createBizOperationIndex();

	/**
	 * 创建日志分析index
	 */
	public void createSqlAnalyseIndex();
}

