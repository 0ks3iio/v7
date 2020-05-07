package net.zdsoft.szxy.operation.inner.permission.service.impl;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.permission.dao.GroupDao;
import net.zdsoft.szxy.operation.inner.permission.dao.UserGroupRelationDao;
import net.zdsoft.szxy.operation.inner.permission.dto.GroupUserCounter;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;
import net.zdsoft.szxy.operation.inner.permission.service.GroupService;
import net.zdsoft.szxy.operation.inner.permission.service.UserGroupRelationService;
import net.zdsoft.szxy.utils.UuidUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/10 下午2:59
 */
@Service("userGroupRelationService")
public class UserGroupRelationServiceImpl implements UserGroupRelationService {

    @Resource
    private UserGroupRelationDao userGroupRelationDao;
    @Resource
    private GroupDao groupDao;

    @Record(type = RecordType.Service)
    @Override
    public List<GroupUserCounter> getGroupUserCounters(String[] groupIds) {

        return userGroupRelationDao.getGroupUserCounters(groupIds);
    }

    @Override
    public Set<String> getUsersByGroupId(String groupId) {
        return userGroupRelationDao.getUsersByGroupId(groupId);
    }

    @Record(type = RecordType.Service)
    @Override
    public Map<String, Set<String>> getGroupNamesByUserId(String[] userIds) {
        if (ArrayUtils.isEmpty(userIds)) {
            return new HashMap<>(1);
        }

        List<UserGroupRelation> relations = userGroupRelationDao.getUserGroupRelationsByUserIds(userIds);

        String[] groupIds = relations.stream().map(UserGroupRelation::getGroupId).toArray(String[]::new);
        Map<String, String> nameMap = ArrayUtils.isEmpty(groupIds) ? Collections.emptyMap() : groupDao.getGroupsByIds(groupIds)
                .stream().collect(Collectors.toMap(Group::getId, Group::getName));

        Map<String, Set<String>> names = new HashMap<>();
        for (UserGroupRelation relation : relations) {
            names.computeIfAbsent(relation.getUserId(), e-> new HashSet<>()).add(nameMap.get(relation.getGroupId()));
        }

        return names;
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateGroupUsers(String groupId, String[] userIds) {
        Set<String> existsUsers = getUsersByGroupId(groupId);

        Set<String> newUsers = new HashSet<>(Arrays.asList(userIds));
        newUsers.removeAll(existsUsers);

        existsUsers.removeAll(Arrays.asList(userIds));
        if (!newUsers.isEmpty()) {
            userGroupRelationDao.saveAll(newUsers.stream().map(e -> {
                UserGroupRelation userGroupRelation = new UserGroupRelation();
                userGroupRelation.setId(UuidUtils.generateUuid());
                userGroupRelation.setUserId(e);
                userGroupRelation.setGroupId(groupId);
                return userGroupRelation;
            }).collect(Collectors.toList()));
        }
        if (!existsUsers.isEmpty()) {
            userGroupRelationDao.deleteByUserId(existsUsers.toArray(new String[0]));
        }
        //TODO 发送邮件提醒用户组变更
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUserGroups(String userId, String[] groupIds) {
        //为空则删除指定用户的所有分组关系
        if (ArrayUtils.isEmpty(groupIds)) {
            userGroupRelationDao.deleteByUserId(new String[]{userId});
            return;
        }

        Set<String> oldGroupIds = userGroupRelationDao.getUserGroupIdsByUserId(userId);

        Set<String> newGroupIds = new HashSet<>(Arrays.asList(groupIds));
        newGroupIds.removeAll(oldGroupIds);

        oldGroupIds.removeAll(new HashSet<>(Arrays.asList(groupIds)));

        //删除
        if (!oldGroupIds.isEmpty()) {
            userGroupRelationDao.deleteUserGroupsByGroupIds(userId, oldGroupIds.toArray(new String[0]));
        }
        if (!newGroupIds.isEmpty()) {
            userGroupRelationDao.saveAll(newGroupIds.stream().map(e->{
                UserGroupRelation userGroupRelation = new UserGroupRelation();
                userGroupRelation.setUserId(userId);
                userGroupRelation.setId(UuidUtils.generateUuid());
                userGroupRelation.setGroupId(e);
                return userGroupRelation;
            }).collect(Collectors.toList()));
        }
    }
}
