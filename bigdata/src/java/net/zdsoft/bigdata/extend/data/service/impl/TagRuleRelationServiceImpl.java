package net.zdsoft.bigdata.extend.data.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.TagRuleRelationDao;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.service.TagRuleRelationService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;

import org.springframework.stereotype.Service;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class TagRuleRelationServiceImpl extends BaseServiceImpl<TagRuleRelation, String> implements TagRuleRelationService {

    @Resource
    private TagRuleRelationDao tagRuleRelationDao;

    @Override
    protected BaseJpaRepositoryDao<TagRuleRelation, String> getJpaDao() {
        return tagRuleRelationDao;
    }

    @Override
    protected Class<TagRuleRelation> getEntityClass() {
        return TagRuleRelation.class;
    }

    @Override
    public void saveAllTagRuleRelation(List<TagRuleRelation> tagRuleRelations, String tagId, String profileCode) {
        // 删除原有的
        tagRuleRelationDao.deleteTagRuleRelationByTagId(tagId);
        this.saveTagRulations(tagRuleRelations, null);
    }

    @Override
    public List<TagRuleRelation> getTagRuleRelationByTagId(String tagId) {
        return tagRuleRelationDao.getTagRuleRelationsByTagId(tagId);
    }

    @Override
    public void deleteByTagId(String tagId) {
        tagRuleRelationDao.deleteTagRuleRelationByTagId(tagId);
    }

    @Override
    public void saveWarningRuleRelation(List<TagRuleRelation> tagRuleRelations, String projectId) {
        // 删除原有的
        tagRuleRelationDao.deleteTagRuleRelationByProjectId(projectId);
        this.saveTagRulations(tagRuleRelations, projectId);
    }

    @Override
    public void deleteByProjectId(String projectId) {
        tagRuleRelationDao.deleteTagRuleRelationByProjectId(projectId);
    }

    @Override
    public List<TagRuleRelation> getTagRuleRelationByProjectId(String projectId) {
        return tagRuleRelationDao.getTagRuleRelationsByProjectId(projectId);
    }

    @Override
    public Map<String, List<TagRuleRelation>> getTagRuleRelationMapByProfileCode(String profileCode) {
        if ("warning".equals(profileCode)) {
            return tagRuleRelationDao.getTagRuleRelationsByProfileCode(profileCode).stream().collect(Collectors.groupingBy(TagRuleRelation::getProjectId, Collectors.toList()));
        }
        return tagRuleRelationDao.getTagRuleRelationsByProfileCode(profileCode).stream().collect(Collectors.groupingBy(TagRuleRelation::getTagId, Collectors.toList()));
    }

    private void saveTagRulations(List<TagRuleRelation> tagRuleRelations, String projectId) {
        tagRuleRelations.forEach(e -> {
            e.setCreationTime(new Date());
            e.setModifyTime(new Date());
            e.setId(UuidUtils.generateUuid());
            if (projectId != null) {
                e.setProjectId(projectId);
            }
        });

        tagRuleRelationDao.saveAll(tagRuleRelations);
    }

}
