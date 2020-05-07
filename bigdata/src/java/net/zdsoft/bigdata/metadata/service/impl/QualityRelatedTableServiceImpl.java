package net.zdsoft.bigdata.metadata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.metadata.dao.QualityRelatedTableDao;
import net.zdsoft.bigdata.metadata.entity.QualityRelatedTable;
import net.zdsoft.bigdata.metadata.service.QualityRelatedTableService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service("qualityRelatedTableService")
public class QualityRelatedTableServiceImpl extends BaseServiceImpl<QualityRelatedTable, String> implements QualityRelatedTableService {

    @Resource
    private QualityRelatedTableDao qualityRelatedTableDao;

    @Override
    public List<QualityRelatedTable> findAllByMetadataId(String metadataId) {
        return qualityRelatedTableDao.findByMdId(metadataId);
    }

    /**
     * 保存增加/更新后的关系表
     * 需要判断一对一关系是否已存在
     * @param qualityRelatedTable
     */
    @Override
    public void saveQualityRelatedTable(QualityRelatedTable qualityRelatedTable) {
        // 判断是否已存在 已存在则更新
        // 判断是否已存在一对一关联关系
        QualityRelatedTable qualityRelatedTablee = qualityRelatedTableDao.findByMdIdAndRelatedMdId(qualityRelatedTable.getMdId(), qualityRelatedTable.getRelatedMdId());
        if (qualityRelatedTablee == null) {
             qualityRelatedTable.setId(UuidUtils.generateUuid());
             qualityRelatedTable.setCreationTime(new Date());
        }else {
            if(StringUtils.isNotBlank(qualityRelatedTable.getId())) {
                qualityRelatedTableDao.deleteById(qualityRelatedTable.getId());
            }
            qualityRelatedTable.setCreationTime(qualityRelatedTablee.getCreationTime());
            qualityRelatedTable.setId(qualityRelatedTablee.getId());
        }

        qualityRelatedTable.setModifyTime(new Date());
        qualityRelatedTableDao.save(qualityRelatedTable);
    }

    @Override
    public boolean isExistRelated(String id) {
        QualityRelatedTable qualityRelatedTable = qualityRelatedTableDao.isExistRelated(id);
        return qualityRelatedTable != null ? true:false;
    }

    @Override
    protected BaseJpaRepositoryDao<QualityRelatedTable, String> getJpaDao() {
        return qualityRelatedTableDao;
    }

    @Override
    protected Class<QualityRelatedTable> getEntityClass() {
        return QualityRelatedTable.class;
    }
}
