package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.MetadataRelation;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface MetadataRelationDao extends BaseJpaRepositoryDao<MetadataRelation, String> {

    List<MetadataRelation> findBySourceId(String sourceId);

    List<MetadataRelation> findByTargetId(String targetId);

    List<MetadataRelation> findBySourceIdAndSourceType(String sourceId, String sourceType);

    List<MetadataRelation> findByTargetIdAndTargetType(String targetId, String targetType);

    List<MetadataRelation> findByTargetIdAndSourceType(String targetId, String sourceType);

    List<MetadataRelation> findBySourceIdAndTargetType(String sourceId, String targetType);

    void deleteByTargetId(String targetId);

    void deleteBySourceId(String sourceId);

    void deleteBySourceIdAndTargetId(String sourceId, String targetId);

    List<MetadataRelation> findAllBySourceIdIn(List<String> sourceIds);

    List<MetadataRelation> findAllByTargetIdIn(List<String> targetIds);

    List<MetadataRelation> findAllBySourceType(String sourceType);

    List<MetadataRelation> findAllByTargetType(String targetType);
}
