package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.metadata.entity.QualityRelatedTable;

import java.util.List;

/**
 *  元数据管理-关联表管理
 * @author zhanwz
 * @since 2019年6月20日
 */
public interface QualityRelatedTableService extends BaseService<QualityRelatedTable, String> {
    List<QualityRelatedTable> findAllByMetadataId(String metadataId);

    void saveQualityRelatedTable(QualityRelatedTable qualityRelatedTable);

    boolean isExistRelated(String id);


}
