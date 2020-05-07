package net.zdsoft.szxy.operation.inner.permission.dao;

import net.zdsoft.szxy.operation.inner.permission.dto.GroupUserCounter;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * @author shenke
 * @since 2019/4/2 下午6:10
 */
@Repository
public interface UserGroupRelationDao extends JpaRepository<UserGroupRelation, String> {

    /**
     * 获取指定用户的分组ID
     * @param userId 用户ID
     * @return List
     */
    @Query(value = "select groupId from UserGroupRelation where userId=?1")
    Set<String> getUserGroupIdsByUserId(String userId);

    /**
     * 获取不同分组的用户数量
     * @param groupIds 分组ID列表
     * @return
     */
    List<GroupUserCounter> getGroupUserCounters(String[] groupIds);

    /**
     * 获取不同用户的分组数据
     * @param userIds 用户ID列表
     * @return
     */
    @Query(
            value = "from UserGroupRelation where userId in (?1)"
    )
    List<UserGroupRelation> getUserGroupRelationsByUserIds(String[] userIds);

    /**
     * 获取指定分组的用户ID
     * @param groupId 组Id
     * @return
     */
    @Query(
            value = "select userId from UserGroupRelation where groupId=?1"
    )
    Set<String> getUsersByGroupId(String groupId);

    @Modifying
    void deleteByGroupId(String groupId);

    /**
     * 删除指定用户的指定分组关系
     * @param userId 用户ID
     * @param groupIds 分组ID
     */
    @Modifying
    @Query(
            value = "delete from UserGroupRelation where userId=?1 and groupId in (?2)"
    )
    void deleteUserGroupsByGroupIds(String userId, String[] groupIds);

    @Modifying
    @Query(
            value = "delete from UserGroupRelation where userId in (?1)"
    )
    void deleteByUserId(String[] userIds);
}
