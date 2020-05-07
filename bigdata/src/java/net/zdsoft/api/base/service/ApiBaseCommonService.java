package net.zdsoft.api.base.service;

import java.util.List;
import java.util.Map;

public interface ApiBaseCommonService {

    /**
     * 取出指定表的所有内容
     * @param tableName
     * @param paramMap 参数，kv键值对
     * @param columnMap TODO
     * @param metadataId 元数据主键id
     * @return 最后一行是分页信息，其他的是数据信息
     */
    public List<Map<String, Object>> getDataMapByParamMap(String tableName,
            Map<String, String> paramMap, Map<String, String> columnMap, String metadataId);
    
    
    /**
     * 执行语法
     * @param sql
     * @param objs 对应sql中的参数
     */
    public void execSql(String sql, Object[] objs);
    
    public List<Object[]> querySql(String sql, Object[] objs);


	public int updateData(String sql, List<Object[]> insertObjList,int[] argTypes, String metadataId);
}
