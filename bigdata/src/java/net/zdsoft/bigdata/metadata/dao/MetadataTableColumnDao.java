package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetadataTableColumnDao extends BaseJpaRepositoryDao<MetadataTableColumn, String> {

    List<MetadataTableColumn> findAllByMetadataIdOrderByOrderId(String metadataId);

    void deleteByMetadataId(String metadataId);

    @Query("FROM MetadataTableColumn where columnName = ?1 ")
	List<MetadataTableColumn> findByColumnName(String columnName);

    @Query(value = "select * FROM BG_METADATA_TABLE_COLUMN where STAT_TYPE is not null", nativeQuery = true)
    List<MetadataTableColumn> findAllModelUsed();

    @Query("select max(orderId) from MetadataTableColumn where metadataId = ?1")
    public Integer getMaxOrderIdByMetadataId(String metadataId);
}
