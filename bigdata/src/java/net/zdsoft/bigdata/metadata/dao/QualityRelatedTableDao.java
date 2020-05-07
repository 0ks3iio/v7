package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.QualityRelatedTable;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QualityRelatedTableDao extends BaseJpaRepositoryDao<QualityRelatedTable, String> {
    List<QualityRelatedTable> findByMdId(String metadataId);

    @Query("FROM QualityRelatedTable where mdId=?1 and relatedMdId=?2")
    QualityRelatedTable findByMdIdAndRelatedMdId(String mdId, String relatedMdId);

    @Query("FROM QualityRelatedTable where mdId=?1 or relatedMdId=?1")
    QualityRelatedTable isExistRelated(String id);
}
