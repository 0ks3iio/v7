package net.zdsoft.bigdata.extend.data.dao;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface TagRuleRelationDao extends BaseJpaRepositoryDao<TagRuleRelation, String> {

    @Transactional
    void deleteTagRuleRelationByTagId(String tagId);

    List<TagRuleRelation> getTagRuleRelationsByTagId(String tagId);

    @Transactional
    void deleteTagRuleRelationByProjectId(String projectId);

    List<TagRuleRelation> getTagRuleRelationsByProjectId(String tagId);

    List<TagRuleRelation> getTagRuleRelationsByProfileCode(String profileCode);
}
