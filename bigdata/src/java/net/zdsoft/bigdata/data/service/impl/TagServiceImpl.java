package net.zdsoft.bigdata.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.data.dao.TagDao;
import net.zdsoft.bigdata.data.entity.Tag;
import net.zdsoft.bigdata.data.entity.TagRelation;
import net.zdsoft.bigdata.data.exceptions.BigDataBusinessException;
import net.zdsoft.bigdata.data.service.TagRelationService;
import net.zdsoft.bigdata.data.service.TagService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by wangdongdong on 2018/5/15 9:54.
 */
@Service
public class TagServiceImpl extends BaseServiceImpl<Tag, String> implements TagService {

    @Resource
    private TagDao tagDao;
    @Resource
    private TagRelationService tagRelationService;

    @Override
    protected BaseJpaRepositoryDao<Tag, String> getJpaDao() {
        return tagDao;
    }

    @Override
    protected Class<Tag> getEntityClass() {
        return Tag.class;
    }

    @Override
    public List<Tag> findTagsByUnitIdAndTagType(String unitId, Short tagType) {
        Assert.notNull(tagType, "标签类型不能为空");
        Assert.isTrue(StringUtils.isNotBlank(unitId), "单位id不能为空");

        return tagDao.getTagsByUnitIdAndTagType(unitId, tagType);
    }

    @Override
    public List<Tag> findTagsByUnitIdAndTagTypeAndTagNames(String unitId, Short tagType, String[] tagNames) {
        Assert.notNull(unitId, "单位Id不能为空");
        Assert.notNull(tagType, "标签类型不能为空");
        Assert.notEmpty(tagNames, "指定的标签名称不能为空");

        return tagDao.getTagsByUnitIdAndTagTypeAndTagNameIn(unitId, tagType, tagNames);
    }

    @Override
    public void saveTag(Tag tag, String businessId) {
        Assert.notNull(tag);
        Assert.isTrue(StringUtils.isNotBlank(businessId), "业务id不能为空");

        tag.setCreationTime(new Date());
        tag.setId(UuidUtils.generateUuid());
        tagDao.save(tag);
        TagRelation tagRelation = new TagRelation();
        tagRelation.setBusinessId(businessId);
        tagRelation.setTagId(tag.getId());
        tagRelation.setTagName(tag.getTagName());
        tagRelation.setCreationTime(new Date());
        tagRelation.setId(UuidUtils.generateUuid());
        tagRelationService.save(tagRelation);
    }

    @Override
    public void deleteByTagId(String tagId) throws BigDataBusinessException {
        Assert.isTrue(StringUtils.isNotBlank(tagId), "标签id不能为空");

        List<TagRelation> relations = tagRelationService.findByTagId(tagId);
        if (CollectionUtils.isNotEmpty(relations)) {
            throw new BigDataBusinessException("该标签已被引用，不能删除");
        }
        tagDao.deleteById(tagId);
    }


    @Override
    public boolean isUsed(String tagId) {
        return tagRelationService.countBy("tagId", tagId) > 0;
    }

    @Override
    public boolean isUsed(String tagId, String businessId) {
        if (StringUtils.isBlank(businessId)) {
            return isUsed(tagId);
        }
        List<TagRelation> tagRelations = tagRelationService.findByTagId(tagId);
        return tagRelations.stream().anyMatch(tr -> !businessId.equals(tr.getBusinessId()));
    }

    @Override
    public void updateTagRelationByBusinessId(String[] tags, String businessId) {
        //更新tag关系
        tagRelationService.deleteByBusinessId(businessId);
        if (tags != null) {
            Map<String, Tag> tagMap = this.findListByIdIn(tags).stream()
                    .collect(Collectors.toMap(Tag::getId, t -> t));
            TagRelation[] tagRelations = Arrays.stream(tags).map(tagId -> {
                Tag tag = tagMap.get(tagId);
                if (tag != null) {
                    TagRelation tagRelation = new TagRelation();
                    tagRelation.setTagId(tagId);
                    tagRelation.setBusinessId(businessId);
                    tagRelation.setCreationTime(new Date());
                    tagRelation.setId(UuidUtils.generateUuid());
                    tagRelation.setTagName(tag.getTagName());
                    return tagRelation;
                }
                return null;
            }).filter(Objects::nonNull).toArray(TagRelation[]::new);
            tagRelationService.saveAll(tagRelations);
        }
    }
}
