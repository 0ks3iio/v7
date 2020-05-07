package net.zdsoft.bigdata.frame.data.oracle;

import java.util.List;

import net.zdsoft.framework.entity.Json;

public interface OracleClientService {

	/**
	 * 查询oracle数据库
	 * @param dataSourceId
	 * @param sql
	 * @return
	 */
	public List<Json> getDataListFromOracle(String dataSourceId,String sql);

}
