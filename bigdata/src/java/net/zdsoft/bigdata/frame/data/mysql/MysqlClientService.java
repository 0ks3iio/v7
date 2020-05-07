package net.zdsoft.bigdata.frame.data.mysql;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.framework.entity.Json;

import java.util.List;

public interface MysqlClientService {

	/**
	 * 查询mysql数据库
	 * @param dataSourceId
	 * @param database
	 * @param sql
	 * @param paramList
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDataListFromMysql(String dataSourceId,String database,String sql, List<Json> paramList,
			List<Json> resultFieldList) ;

	/**
	 * 查询mysql数据库
	 * @param dataSourceId
	 * @param database
	 * @param sql
	 * @param paramList
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDataListFromMysqlThrowException(String dataSourceId,String database,String sql, List<Json> paramList,
										   List<Json> resultFieldList) throws BigDataBusinessException;

	/**
	 * 执行mysql语句
	 * @param dataSourceId
	 * @param database
	 * @param sql
	 * @param objs
	 * @return
	 */
	public boolean execSql(String dataSourceId,String database,String sql, Object[] objs);
	
	/**
	 * 执行mysql多条语句
	 * @param dataSourceId
	 * @param database
	 * @param sql
	 */
	public void execSqls(String dataSourceId,String database,List<String> sqls);
	
	/**
	 * 执行存储过程
	 * @param database
	 * @param sql
	 * @param inParams
	 * @param outParams
	 * @return
	 * @throws Exception
	 */
	public Json execProc(String dataSourceId,String database,String sql, List<Json> inParams, List<Json> outParams);
	
	/**
	 * 多维模型查询
	 * @param dbName 数据库名称
	 * @param sql 语句
	 * @param paramList 参数
	 * @param resultFieldList 结果
	 * @param rowDimensionList 行维度
	 * @param columnDimensionList 列维度
	 * @param indexList 指标
	 * @return json 含列头 数据 和列数
	 */
	public String getDataListFromMysql(String dataSourceId,String dbName, String sql,
			List<Json> paramList, List<Json> resultFieldList,List<Json> rowDimensionList,List<Json> columnDimensionList,List<Json> indexList);
	
	
	/**
	 * 获取数据库用量信息
	 * @param dbName
	 * @return
	 */
	public Json getDbUsageInfo(String dataSourceId,String dbName);
}
