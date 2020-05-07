package net.zdsoft.bigdata.frame.data.hive;

import java.util.List;

import net.zdsoft.framework.entity.Json;

public interface HiveClientService {

	/**
	 * 查询hive数据库
	 * @param database
	 * @param sql
	 * @param paramList
	 * @param resultFieldList
	 * @return
	 */
	public List<Json> getDataListFromHive(String database,String sql, List<Json> paramList,
			List<Json> resultFieldList);

	/**
	 * 执行hive语句
	 * @param database
	 * @param sql
	 * @param objs
	 * @return
	 */
	public boolean execSql(String database,String sql);
	
	/**
	 * 执行hive多条语句
	 * @param database
	 * @param sql
	 */
	public void execSqls(String database,List<String> sqls);
	
}
