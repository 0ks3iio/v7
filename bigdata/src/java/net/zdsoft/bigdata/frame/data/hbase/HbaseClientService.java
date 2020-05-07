package net.zdsoft.bigdata.frame.data.hbase;

import net.zdsoft.framework.entity.Json;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Hbase原生态接口只支持创建表和根据rowkey去查询单条记录
 * <p>
 * 复杂的查询请调用Phoenix接口查询（性能更好)
 * <p>
 * 复杂的统计请调用Hive的MapReduce
 */
public interface HbaseClientService {

    /**
     * 创建hbase表
     *
     * @param tableName
     * @param familys
     * @throws Exception
     */
    Boolean creatTable(String tableName, String[] familys)
            throws Exception;

    /**
     * put数据
     *
     * @param tableName
     * @param key
     * @param datas
     * @throws IOException
     */
    void putData(String tableName, String key, List<Json> datas) throws IOException;

    /**
     * 批量put数据
     *
     * @param tableName
     * @param datas
     * @throws IOException
     */
    void putDatas(String tableName, Map<String, List<Json>> datas) throws IOException;

    /**
     * 根据rowkey获取数据
     *
     * @param tableName
     * @param rowKey
     * @param cols
     * @return
     */
    Json getOneRowAndMultiColumn(String tableName, String rowKey, List<Json> cols);


    /**
     * 表是否存在
     *
     * @param tableName
     * @return
     */
    Boolean isExistTable(String tableName) throws Exception;
    
    public boolean deleteColumnFamily(String tableName,String columnFamilyName) throws IOException;


    /**
     * 求和
     *
     * @param tableName
     * @return
     */
    Long getTotalCount(String tableName);
}
