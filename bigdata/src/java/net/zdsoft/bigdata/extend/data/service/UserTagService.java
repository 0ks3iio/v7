package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.TagRuleRelation;
import net.zdsoft.bigdata.extend.data.entity.UserTag;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface UserTagService extends BaseService<UserTag, String> {


    /**
     * 根据对象和类型查询标签
     * @return
     */
    List<UserTag> getUserTagByProfileCode(String profileCode);

    /**
     * 保存标签
     * @param userTag
     */
    void saveUserTag(UserTag userTag,int saveType);

    /**
     * 删除标签
     * @param id
     */
    void deleteUserTag(String id);

    void saveUserTagAndTagRuleRelation(String updateCycle, List<TagRuleRelation> tagRuleRelations, String tagId, String profileCode, Integer orderId);

    void updateUserTagCondition(UserTag userTag);

    /**
     * 根据对象查询所有二级标签
     * @param profileCode
     * @return
     */
    List<UserTag> getSecondaryUserTagByProfileCode(String profileCode);
    /**
     * 根据对象查询所有一级级标签
     * @param profileCode
     * @return
     */
    List<UserTag> getfirstUserTagByProfileCode(String profileCode);
    /**
     * 根据父标签查询
     * @param parenrId
     * @return
     */
    List<UserTag> getSecondaryUserTagByParenrId(String parenrId);
}
