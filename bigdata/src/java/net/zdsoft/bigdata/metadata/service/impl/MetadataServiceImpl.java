package net.zdsoft.bigdata.metadata.service.impl;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.dao.MetadataDao;
import net.zdsoft.bigdata.metadata.entity.Metadata;
import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.bigdata.metadata.entity.MetadataTableColumn;
import net.zdsoft.bigdata.metadata.service.*;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by wangdongdong on 2019/1/7 14:16.
 */
@Service
public class MetadataServiceImpl extends BaseServiceImpl<Metadata, String> implements MetadataService {


    @Resource
    private MetadataDao metadataDao;
    @Resource
    private MetadataTableColumnService metadataTableColumnService;
    @Resource
    private MetadataRelationService metadataRelationService;
    @Resource
    private MetadataTagService metadataTagService;

    @Resource
    private QualityRelatedTableService qualityRelatedTableService;

    @Override
    protected BaseJpaRepositoryDao<Metadata, String> getJpaDao() {
        return metadataDao;
    }

    @Override
    protected Class<Metadata> getEntityClass() {
        return Metadata.class;
    }

    @Override
    public List<Metadata> findByMdType(String mdType) {
        return metadataDao.findAllByMdTypeOrderByModifyTimeDesc(mdType);
    }

    @Override
    public List<Metadata> findAllTables() {
        return metadataDao.findAllTables();
    }

    @Override
    public void saveMetadata(Metadata metadata) throws BigDataBusinessException {
        List<Metadata> listForName = this.findListBy(new String[]{"name", "mdType"}, new String[]{metadata.getName(), metadata.getMdType()});
        metadata.setModifyTime(new Date());
        if (StringUtils.isBlank(metadata.getId())) {
            if (listForName.size() > 0) {
                throw new BigDataBusinessException("该名称已存在!");
            }
            metadata.setCreationTime(new Date());
            metadata.setId(UuidUtils.generateUuid());
            if ("hbase".equals(metadata.getDbType())) {
                metadata.setTableName(metadata.getTableName().toUpperCase());
            }
            metadataDao.save(metadata);
            return;
        }

        // 验证名称和code是否存在
        if (listForName.size() > 0) {
            if (!listForName.get(0).getId().equals(metadata.getId())) {
                throw new BigDataBusinessException("该名称已存在!");
            }
        }
        metadataDao.update(metadata, new String[]{"dbId", "dbType", "name", "tableName",
                "remark", "modifyTime", "isCustom", "isProperty", "propertyTopicId", "dwRankId", "isSupportApi", "parentId", "isPhoenix", "status"});
    }

    @Override
    public void saveMetadata(Metadata metadata, String[] tags) throws BigDataBusinessException {
        this.saveMetadata(metadata);
        metadataTagService.saveMetadataTag(metadata.getId(), tags);
    }

    @Override
    public void deleteMetadata(String id) throws BigDataBusinessException {
        List<MetadataRelation> relations = metadataRelationService.findBySourceId(id);
        List<MetadataRelation> target = metadataRelationService.findByTargetId(id);
        if (relations.size() > 0 || target.size() > 0) {
            throw new BigDataBusinessException("该元数据存在血缘关系，您不能删除!");
        }
        boolean isExistRelated = qualityRelatedTableService.isExistRelated(id);
        if (isExistRelated) {
            throw new BigDataBusinessException("该元数据存在关联表关系, 您不能删除");
        }
        metadataRelationService.deleteByTargetId(id);
        metadataTableColumnService.deleteByMetadataId(id);
        metadataDao.deleteById(id);
    }

    @Override
    public List<Metadata> findPropertyMetadata() {
        return metadataDao.findPropertyMetadata();
    }

    @Override
    public List<Metadata> findByPropertyTopicId(String propertyTopicId) {
        return metadataDao.findAllByPropertyTopicId(propertyTopicId);
    }

    @Override
    public List<Metadata> findByDwRankId(String dwRankId) {
        return metadataDao.findAllByDwRankId(dwRankId);
    }

    @Override
    public List<Metadata> findByDwRankAndPropertyTopic(String dwRankId, String propertyTopicId) {
        return metadataDao.findAllByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId);
    }

    @Override
    public List<Metadata> findTableViewById(String id) {
        return metadataDao.findAllByMdTypeAndParentId("tableView", id);
    }

    @Override
    public List<Metadata> findTableIndexById(String id) {
        return metadataDao.findAllByMdTypeAndParentId("tableIndex", id);
    }

    @Override
    public Integer countByDwRankAndPropertyTopic(String dwRankId, String propertyTopicId) {
        return metadataDao.countByDwRankIdAndPropertyTopicId(dwRankId, propertyTopicId);
    }

    @Override
    public Integer countByPropertyTopic(String propertyTopicId) {
        return metadataDao.countByPropertyTopicId(propertyTopicId);
    }

    @Override
    public Integer countAllMetadata() {
        return metadataDao.countAllByMdType("table");
    }

    @Override
    public List<Metadata> findSupportApiMetadata() {
        return metadataDao.findSupportApiMetadata();
    }

    @Override
    public List<Metadata> findAllSystemTables() {
        return metadataDao.findAllSystemTables();
    }

    @Override
    public Integer getCountByDbId(String dbId) {
        return metadataDao.getCountByDbId(dbId);
    }

    @Override
    public List<Metadata> findTableNameOrColName(String tableName, String columnName) {
        List<Metadata> metas = Lists.newArrayList();
        Metadata meta = metadataDao.findByTableName(tableName);
        List<MetadataTableColumn> cols = metadataTableColumnService.findByColumnName(columnName);
        Set<String> metaIds = EntityUtils.getSet(cols, MetadataTableColumn::getMetadataId);
        if (CollectionUtils.isNotEmpty(metaIds)) {
            metas = findByIdIn(metaIds.toArray(new String[0]));
        }
        if (meta != null) metas.add(meta);
        return metas;
    }

    @Override
    public List<Metadata> findByDbTypeAndIds(String dbType, List<String> ids) {
        return metadataDao.findAllByDbTypeAndIdIn(dbType, ids);
    }

    @Override
    public Optional<Metadata> findByMetadataId(String metadataId) {
        return metadataDao.findById(metadataId);
    }

    @Override
    public List<Metadata> findTablesByDbType(String dbType) {
        return metadataDao.findTablesByDbType(dbType);
    }

	@Override
	public Metadata findByTableName(String tableName) {
		return metadataDao.findByTableName(tableName);
	}

}
