package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.QualityRuleRelation;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface QualityRuleRelationDao extends BaseJpaRepositoryDao<QualityRuleRelation, String> {

    List<QualityRuleRelation> findByMetadataId(String metadataId);
}
