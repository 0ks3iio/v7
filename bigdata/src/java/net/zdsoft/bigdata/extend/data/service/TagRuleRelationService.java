package net.zdsoft.bigdata.extend.data.service;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface TagRuleRelationService extends BaseService<TagRuleRelation, String> {

    /**
     * 保存标签
     * @param tagRuleRelations
     * @param tagId
     * @param profileCode
     */
    void saveAllTagRuleRelation(List<TagRuleRelation> tagRuleRelations, String tagId, String profileCode);

    /**
     * 根据标签id获取标签关系
     * @param tagId
     * @return
     */
    List<TagRuleRelation> getTagRuleRelationByTagId(String tagId);

    /**
     * 根据标签id删除
     * @param tagId
     */
    void deleteByTagId(String tagId);

    /**
     * 保存预警规则
     * @param tagRuleRelations
     * @param projectId
     */
    void saveWarningRuleRelation(List<TagRuleRelation> tagRuleRelations, String projectId);

    /**
     * 根据预警项目id删除
     * @param projectId
     */
    void deleteByProjectId(String projectId);

    /**
     * 根据预警项目id获取标签关系
     * @param projectId
     * @return
     */
    List<TagRuleRelation> getTagRuleRelationByProjectId(String projectId);

    /**
     * 根据对象获取标签关系
     *         标签库   map(tagId, List<TagRuleRelation>)
     *         预警项目 map(projectId, List<TagRuleRelation>)
     * @param profileCode
     * @return
     */
    Map<String, List<TagRuleRelation>> getTagRuleRelationMapByProfileCode(String profileCode);
}
