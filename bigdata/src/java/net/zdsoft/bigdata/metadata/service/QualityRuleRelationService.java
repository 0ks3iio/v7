package net.zdsoft.bigdata.metadata.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.entity.QualityRuleRelation;

import java.util.List;

public interface QualityRuleRelationService extends BaseService<QualityRuleRelation, String> {


    List<QualityRuleRelation> findAllByMetadataId(String metadataId);


    void saveQualityRuleRelation(QualityRuleRelation qualityRuleRelation) throws BigDataBusinessException;
}
