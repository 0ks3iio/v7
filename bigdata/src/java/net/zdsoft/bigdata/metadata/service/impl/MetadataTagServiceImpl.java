package net.zdsoft.bigdata.metadata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.metadata.dao.MetadataTagDao;
import net.zdsoft.bigdata.metadata.entity.MetadataTag;
import net.zdsoft.bigdata.metadata.service.MetadataTagService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2019/1/7 14:16.
 */
@Service
public class MetadataTagServiceImpl extends BaseServiceImpl<MetadataTag, String> implements MetadataTagService {

    @Resource
    private MetadataTagDao metadataTagDao;

    @Override
    protected BaseJpaRepositoryDao<MetadataTag, String> getJpaDao() {
        return metadataTagDao;
    }

    @Override
    protected Class<MetadataTag> getEntityClass() {
        return MetadataTag.class;
    }

    @Override
    public List<MetadataTag> findByMetadataId(String metadataId) {
        return metadataTagDao.findAllByMdId(metadataId);
    }

    @Override
    public void saveMetadataTag(String metadataId, String[] tagIds) {
        metadataTagDao.deleteByMdId(metadataId);
        if (ArrayUtils.isEmpty(tagIds)) {
            return;
        }
        for (String tagId : tagIds) {
            MetadataTag metadataTag = new MetadataTag();
            metadataTag.setTagId(tagId);
            metadataTag.setMdId(metadataId);
            metadataTag.setCreationTime(new Date());
            metadataTag.setId(UuidUtils.generateUuid());
            metadataTagDao.save(metadataTag);
        }
    }

    @Override
    public List<MetadataTag> findByTagId(String tagId) {
        return metadataTagDao.findAllByTagId(tagId);
    }
}
