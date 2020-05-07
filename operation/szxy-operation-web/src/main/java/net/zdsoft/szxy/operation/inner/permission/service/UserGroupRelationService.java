package net.zdsoft.szxy.operation.inner.permission.service;

import net.zdsoft.szxy.operation.inner.permission.dto.GroupUserCounter;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/4/10 下午2:57
 */
public interface UserGroupRelationService {

    /**
     * 获取每个分组的人员数量信息
     * @param groupIds 分组id列表
     * @return
     */
    List<GroupUserCounter> getGroupUserCounters(String[] groupIds);

    /**
     * 获取指定分组的人员信息
     * @param groupId 分组ID
     * @return
     */
    Set<String> getUsersByGroupId(String groupId);

    /**
     * 获取用户所属分组名称的集合
     * @param userIds 用户ID列表
     * @return Map key=>userId value=>Set 分组名称集合
     */
    Map<String, Set<String>> getGroupNamesByUserId(String[] userIds);

    /**
     * 更新指定分组的成员关系
     * @param groupId 分组ID
     * @param userIds 用户ID
     */
    void updateGroupUsers(String groupId, String[] userIds);

    /**
     * 变更指定成员的分组关系
     * @param userId 用户ID
     * @param groupIds 分组ID
     */
    void updateUserGroups(String userId, String[] groupIds);
}
