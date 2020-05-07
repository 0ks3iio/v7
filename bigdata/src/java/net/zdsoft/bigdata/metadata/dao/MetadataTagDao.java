package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.MetadataTag;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface MetadataTagDao extends BaseJpaRepositoryDao<MetadataTag, String> {

    List<MetadataTag> findAllByMdId(String mdId);

    void deleteByMdId(String mdId);

    List<MetadataTag> findAllByTagId(String tagId);
}
