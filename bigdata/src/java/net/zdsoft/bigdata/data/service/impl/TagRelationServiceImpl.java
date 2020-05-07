package net.zdsoft.bigdata.data.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.TagRelationDao;
import net.zdsoft.bigdata.data.entity.TagRelation;
import net.zdsoft.bigdata.data.service.TagRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Created by wangdongdong on 2018/5/15 10:19.
 */
@Service
public class TagRelationServiceImpl extends BaseServiceImpl<TagRelation, String> implements TagRelationService {

    @Resource
    private TagRelationDao tagRelationDao;

    @Override
    protected BaseJpaRepositoryDao<TagRelation, String> getJpaDao() {
        return tagRelationDao;
    }

    @Override
    protected Class<TagRelation> getEntityClass() {
        return TagRelation.class;
    }

    @Override
    public List<TagRelation> findTagRelationByBusinessId(String businessId) {
        Assert.isTrue(StringUtils.isNotBlank(businessId), "业务id不能为空");

        return tagRelationDao.getTagRelationsByBusinessId(businessId);
    }

    @Override
    public List<TagRelation> getByTagIds(String[] tagIds) {
        return tagRelationDao.findTagRelationsByTagIdIn(tagIds);
    }

    @Override
    public void deleteByBusinessId(String businessId) {
        Assert.isTrue(StringUtils.isNotBlank(businessId), "业务id不能为空");

        tagRelationDao.deleteTagRelationByBusinessId(businessId);
    }

    @Override
    public void deleteByBusinessId(String[] businessIds) {
        Assert.isTrue(ArrayUtils.isNotEmpty(businessIds), "businessIds not empty");
        tagRelationDao.deleteByBusinessIdIn(businessIds);
    }

    @Override
    public List<TagRelation> findByTagId(String tagId) {
        Assert.isTrue(StringUtils.isNotBlank(tagId), "标签id不能为空");

        return tagRelationDao.getTagRelationsByTagId(tagId);
    }

    @Override
    public List<TagRelation> getByBusinessId(String[] businessIds) {
        return tagRelationDao.getAllByBusinessIdIn(businessIds);
    }
}
