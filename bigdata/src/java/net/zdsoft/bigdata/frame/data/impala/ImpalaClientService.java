package net.zdsoft.bigdata.frame.data.impala;

import net.zdsoft.framework.entity.Json;

import java.util.List;

public interface ImpalaClientService {

	/**
	 * impala jdbc查询
	 * 
	 * @param dbName
	 * @param sql
	 * @param paramList
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDataListFromImpala(String dbName, String sql,
			List<Json> paramList, List<Json> resultFieldList);

	/**
	 * impala jdbc查询
	 *
	 * @param dbName
	 * @param sql
	 * @param paramList
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDataListFromImpala(String dataSourceId, String dbName, String sql,
											List<Json> paramList, List<Json> resultFieldList);

	/**
	 * 多维模型查询
	 * 
	 * @param dbName
	 *            项目名称
	 * @param sql
	 *            语句
	 * @param paramList
	 *            参数
	 * @param resultFieldList
	 *            结果
	 * @param rowDimensionList
	 *            行维度
	 * @param columnDimensionList
	 *            列维度
	 * @param indexList
	 *            指标
	 * @return json 含列头 数据 和列数
	 */
	public String getDataListFromImpala(String dbName, String sql,
			List<Json> paramList, List<Json> resultFieldList,
			List<Json> rowDimensionList, List<Json> columnDimensionList,
			List<Json> indexList);

	/**
	 * 多维模型查询
	 *
	 * @param dataSourceId
	 * 			  数据源id
	 * @param dbName
	 *            项目名称
	 * @param sql
	 *            语句
	 * @param paramList
	 *            参数
	 * @param resultFieldList
	 *            结果
	 * @param rowDimensionList
	 *            行维度
	 * @param columnDimensionList
	 *            列维度
	 * @param indexList
	 *            指标
	 * @return json 含列头 数据 和列数
	 */
	public String getDataListFromImpala(String dataSourceId, String dbName, String sql,
										List<Json> paramList, List<Json> resultFieldList,
										List<Json> rowDimensionList, List<Json> columnDimensionList,
										List<Json> indexList);

	/**
	 * 执行sql
	 * 
	 * @param database
	 * @param sql
	 * @return
	 */
	public boolean execSql(String database, String sql);

	/**
	 * 批量执行sqls
	 * 
	 * @param database
	 * @param sqls
	 */
	public void execSqls(String database, List<String> sqls);
}
