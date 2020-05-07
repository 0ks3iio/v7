package net.zdsoft.bigdata.metadata.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.metadata.dao.MetadataIndexDao;
import net.zdsoft.bigdata.metadata.entity.MetadataIndex;
import net.zdsoft.bigdata.metadata.service.MetadataIndexService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.StringUtils;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wangdongdong on 2019/1/7 14:16.
 */
@Service
public class MetadataIndexServiceImpl extends BaseServiceImpl<MetadataIndex, String> implements MetadataIndexService {


    @Resource
    private MetadataIndexDao metadataIndexDao;

    @Override
    protected BaseJpaRepositoryDao<MetadataIndex, String> getJpaDao() {
        return metadataIndexDao;
    }

    @Override
    protected Class<MetadataIndex> getEntityClass() {
        return MetadataIndex.class;
    }

    @Override
    public List<MetadataIndex> findByMetadataId(String metadataId) {
        return metadataIndexDao.findAllByMdId(metadataId);
    }

    @Override
    public void saveMetadataIndex(MetadataIndex metadataIndex) throws BigDataBusinessException {
        List<MetadataIndex> listForName = this.findListBy(new String[]{"name", "mdId"}, new String[]{metadataIndex.getName(), metadataIndex.getMdId()});
        metadataIndex.setModifyTime(new Date());
        if (StringUtils.isBlank(metadataIndex.getId())) {
            if (listForName.size() > 0) {
                throw new BigDataBusinessException("该名称已存在!");
            }
            metadataIndex.setCreationTime(new Date());
            metadataIndex.setId(UuidUtils.generateUuid());
            metadataIndexDao.save(metadataIndex);
            return;
        }

        // 验证名称和code是否存在
        if (listForName.size() > 0) {
            if (!listForName.get(0).getId().equals(metadataIndex.getId())) {
                throw new BigDataBusinessException("该名称已存在!");
            }
        }
        metadataIndexDao.update(metadataIndex, new String[]{"name", "type", "columns", "modifyTime"});
    }
}
