package net.zdsoft.szxy.operation.inner.permission.service;

import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.GroupModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author shenke
 * @since 2019/4/9 下午12:43
 */
public interface GroupService {

    Optional<Group> getGroupById(String id);
    /**
     * 获取所有分组信息
     * @return list
     */
    List<Group> getAllGroups();

    /**
     * 分页查询分组信息
     * @param pageable 分页参数
     * @return
     */
    Page<Group> getAllGroups(Pageable pageable);

    /**
     * 获取指定用户的分组数据
     * @param userId 用户ID
     * @return List
     */
    List<Group> getGroupsByUserId(String userId);

    /**
     * 判定指定的分组名称是否存在
     * @param groupName 分组名称
     * @return
     */
    boolean existsByGroupName(String groupName);

    /**
     * 新增分组
     * @param group 分组
     * @param groupModuleRelations 分组和模块关系 可以为空
     * @param userGroupRelations 用户和分组关系 可以为空
     */
    void saveGroups(Group group, List<GroupModuleRelation> groupModuleRelations, List<UserGroupRelation> userGroupRelations);

    /**
     * 删除组， 和组相关的数据都会被删除
     * @param id 组ID
     */
    void deleteGroup(String id);

    /**
     * 更新分组名称
     * @param id 分组ID
     * @param groupName 新的名称
     */
    void updateGroupName(String id, String groupName);
}
