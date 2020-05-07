package net.zdsoft.szxy.operation.inner.permission.service;

import net.zdsoft.szxy.operation.inner.permission.entity.GroupModuleRelation;

import java.util.List;

/**
 * @author shenke
 * @since 2019/4/11 上午9:16
 */
public interface GroupModuleRelationService {

    /**
     * 根据分组ID查询对应的模块关系
     * @param groupId 分组ID
     * @return
     */
    List<GroupModuleRelation> getGroupModuleRelationsByGroupId(String groupId);

    /**
     * 变更指定分组的权限
     * @param groupId 分组ID
     * @param relations 新的权限关系
     * @param regionCode 新的行政区划
     */
    void updateGroupModuleRelations(String groupId, String regionCode, List<GroupModuleRelation> relations);
}
