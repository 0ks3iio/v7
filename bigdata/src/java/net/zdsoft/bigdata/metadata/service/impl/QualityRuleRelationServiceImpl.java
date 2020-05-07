package net.zdsoft.bigdata.metadata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.dao.QualityRuleRelationDao;
import net.zdsoft.bigdata.metadata.entity.QualityRuleRelation;
import net.zdsoft.bigdata.metadata.service.QualityRuleRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QualityRuleRelationServiceImpl extends BaseServiceImpl<QualityRuleRelation, String> implements QualityRuleRelationService {

    @Resource
    private QualityRuleRelationDao qualityRuleRelationDao;

    @Override
    protected BaseJpaRepositoryDao<QualityRuleRelation, String> getJpaDao() {
        return qualityRuleRelationDao;
    }

    @Override
    protected Class<QualityRuleRelation> getEntityClass() {
        return QualityRuleRelation.class;
    }

    @Override
    public List<QualityRuleRelation> findAllByMetadataId(String metadataId) {
        return qualityRuleRelationDao.findByMetadataId(metadataId);
    }

    @Override
    public void saveQualityRuleRelation(QualityRuleRelation qualityRuleRelation) throws BigDataBusinessException {
        List<QualityRuleRelation> listForName = this.findListBy(new String[]{"name", "metadataId"}, new String[]{qualityRuleRelation.getName(), qualityRuleRelation.getMetadataId()});
        if (StringUtils.isBlank(qualityRuleRelation.getId())) {
            if (listForName.size() > 0) {
                throw new BigDataBusinessException("该数据规则已存在!");
            }
            qualityRuleRelation.setId(UuidUtils.generateUuid());
            qualityRuleRelationDao.save(qualityRuleRelation);
            return;
        }

        // 验证名称和code是否存在
        if (listForName.size() > 0) {
            if (!listForName.get(0).getId().equals(qualityRuleRelation.getId())) {
                throw new BigDataBusinessException("该数据规则已存在!");
            }
        }
        qualityRuleRelationDao.update(qualityRuleRelation, new String[]{"name", "dbType", "tableName"
                , "columnId", "columnName", "ruleTemplateId", "ruleType", "dimCode", "computerType", "detail",
                "threshold", "isAlarm"});
    }
}
