package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetadataDao extends BaseJpaRepositoryDao<Metadata, String> {

    @Query("FROM Metadata where mdType= ?1 and isCustom = 0 and status = 1 order by modifyTime desc")
    List<Metadata> findAllByMdTypeOrderByModifyTimeDesc(String mdType);

    @Query("FROM Metadata where mdType='table' and isCustom = 0")
    List<Metadata> findAllTables();

    @Query("FROM Metadata where mdType='table' and isCustom = 1")
    List<Metadata> findAllSystemTables();
    
    @Query("select count(id) from Metadata where dbId = ?1 and status = 1")
    public Integer getCountByDbId(String dbId);

    @Query("FROM Metadata where isProperty = 1 and status = 1")
    List<Metadata> findPropertyMetadata();

    @Query("FROM Metadata where propertyTopicId = ?1 and isProperty = 1 and status = 1")
    List<Metadata> findAllByPropertyTopicId(String propertyTopicId);

    @Query("FROM Metadata where dwRankId = ?1 and isProperty = 1 and status = 1")
    List<Metadata> findAllByDwRankId(String dwRankId);

    @Query("FROM Metadata where dwRankId = ?1 and propertyTopicId = ?2 and isProperty = 1 and status = 1")
    List<Metadata> findAllByDwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    List<Metadata> findAllByMdTypeAndParentId(String mdType, String parentId);

    @Query("select count(1) from Metadata where mdType = ?1 and isProperty = 1 and status = 1")
    Integer countAllByMdType(String mdType);

    @Query("select count(1) from Metadata where dwRankId = ?1 and propertyTopicId = ?2 and isProperty = 1 and status = 1")
    Integer countByDwRankIdAndPropertyTopicId(String dwRankId, String propertyTopicId);

    @Query("FROM Metadata where isSupportApi = 1 and status = 1")
    List<Metadata> findSupportApiMetadata();
    
    @Query("FROM Metadata where tableName = ?1")
    Metadata findByTableName(String tableName);

    @Query("select count(1) from Metadata where propertyTopicId = ?1 and isProperty = 1 and status = 1")
    Integer countByPropertyTopicId(String propertyTopicId);

    List<Metadata> findAllByDbTypeAndIdIn(String dbType, List<String> ids);

    @Query("FROM Metadata where dbType=?1 and isCustom = 0")
    List<Metadata> findTablesByDbType(String mdType);
}
