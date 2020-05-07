package net.zdsoft.bigdata.frame.data.phoenix;

import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.framework.entity.Json;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public interface PhoenixClientService {

	/**
	 * 查询hbase数据库
	 * @param dataSourceId
	 * @param sql
	 * @return
	 */
	public List<Json> getDataListFromPhoenix(String dataSourceId, String sql, List<Json> paramList);

	/**
	 * 根据元数据创建表
	 * @param mdId
	 * @return
	 * @throws Exception
	 */
	public Boolean createTableByMetadata(String mdId) throws Exception;

	Boolean createTableViewByMetadata(String mdId) throws BigDataBusinessException;

	Boolean createTableIndexByMetadataIndex(String indexId) throws BigDataBusinessException;

    /**
     * 执行sqls
     * @param sqls
     * @throws SQLException
     */
    public void upsert(List<String> sqls) throws SQLException;


    public DataSource getDataSource();
}
