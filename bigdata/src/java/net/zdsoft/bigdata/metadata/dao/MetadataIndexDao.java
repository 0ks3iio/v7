package net.zdsoft.bigdata.metadata.dao;

import net.zdsoft.bigdata.metadata.entity.MetadataIndex;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

public interface MetadataIndexDao extends BaseJpaRepositoryDao<MetadataIndex, String> {

    List<MetadataIndex> findAllByMdId(String mdId);
}
