package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.entity.Metadata;

import java.util.List;
import java.util.Optional;

/**
 * Created by wangdongdong on 2019/1/7 11:41.
 */
public interface MetadataService extends BaseService<Metadata, String> {

    List<Metadata> findByMdType(String mdType);

    List<Metadata> findAllTables();
    
    public Integer getCountByDbId(String dbId);

    void saveMetadata(Metadata metadata) throws BigDataBusinessException;

    void saveMetadata(Metadata metadata, String[] tags) throws BigDataBusinessException;

    void deleteMetadata(String id) throws BigDataBusinessException;

    /**
     * 获取所有纳入数据资产的元数据
     * @return
     */
    List<Metadata> findPropertyMetadata();

    /**
     * 根据主题查询
     * @param propertyTopicId
     * @return
     */
    List<Metadata> findByPropertyTopicId(String propertyTopicId);

    /**
     * 根据层级查询
     * @param dwRankId
     * @return
     */
    List<Metadata> findByDwRankId(String dwRankId);

    /**
     * 根据层级和主题查询元数据
     * @param dwRankId
     * @param propertyTopicId
     * @return
     */
    List<Metadata> findByDwRankAndPropertyTopic(String dwRankId, String propertyTopicId);

    /**
     * 查询表所有视图
     * @param id
     * @return
     */
    List<Metadata> findTableViewById(String id);

    /**
     * 查询表所有索引
     * @param id
     * @return
     */
    List<Metadata> findTableIndexById(String id);

    /**
     * 统计某个层次+主题的元数据数量
     * @param dwRankId
     * @param propertyTopicId
     * @return
     */
    Integer countByDwRankAndPropertyTopic(String dwRankId, String propertyTopicId);

    /**
     * 统计某个主题的元数据数量
     * @param propertyTopicId
     * @return
     */
    Integer countByPropertyTopic(String propertyTopicId);

    /**
     * 统计总的元数据数量
     * @return
     */
    Integer countAllMetadata();

    /**
     * 获取所有支持api的元数据
     * @return
     */
    List<Metadata> findSupportApiMetadata();

    /**
     * 获取所有系统内置表
     * @return
     */
    List<Metadata> findAllSystemTables();
    
    /**
     * 根据表名称或列名称查找
     * @return
     */
    List<Metadata> findTableNameOrColName(String tableName, String columnName);

    /**
     * 根据数据库类型和id集合查询
     * @param dbType
     * @param ids
     * @return
     */
    List<Metadata> findByDbTypeAndIds(String dbType, List<String> ids);

    /**
     *  根据 metadata查找
     * @param metadataId
     * @return
     */
    Optional<Metadata> findByMetadataId(String metadataId);

    /**
     *  根据dbType查找tables
     * @param dbType
     * @return
     */
    List<Metadata> findTablesByDbType(String dbType);
    
    Metadata findByTableName(String tableName);
}
