package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;

import java.util.List;
import java.util.Optional;

/**
 * Created by wangdongdong on 2019/1/7 11:41.
 */
public interface MetadataTableColumnService extends BaseService<MetadataTableColumn, String> {

    List<MetadataTableColumn> findByMetadataId(String metadataId);

    void deleteByMetadataId(String id);

    void saveMetadataTableColumn(MetadataTableColumn metadataTableColumn) throws BigDataBusinessException;
    
    List<MetadataTableColumn> findByColumnName(String columnName);

    List<MetadataTableColumn> findAllModelUsed();

    public Integer getMaxOrderIdByMetadataId(String metadataId);

    Optional<MetadataTableColumn> findById(String mdColumnId);
}
