package net.zdsoft.szxy.operation.inner.permission.service.impl;

import net.zdsoft.szxy.alarm.SzxyMailSender;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.permission.dao.GroupDao;
import net.zdsoft.szxy.operation.inner.permission.dao.GroupModuleRelationDao;
import net.zdsoft.szxy.operation.inner.permission.dao.UserGroupRelationDao;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.GroupModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;
import net.zdsoft.szxy.operation.inner.permission.service.GroupService;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/9 下午12:43
 */
@Service("groupService")
public class GroupServiceImpl implements GroupService {

    @Resource
    private SzxyMailSender szxyMailSender;
    @Resource
    private OpUserService opUserService;
    @Resource
    private GroupDao groupDao;
    @Resource
    private UserGroupRelationDao userGroupRelationDao;
    @Resource
    private GroupModuleRelationDao groupModuleRelationDao;

    @Override
    public Optional<Group> getGroupById(String id) {
        return groupDao.findById(id);
    }

    @Override
    public List<Group> getAllGroups() {
        return groupDao.findAll();
    }

    @Override
    public Page<Group> getAllGroups(Pageable pageable) {
        return groupDao.findAll(pageable);
    }

    @Record(type = RecordType.Service)
    @Override
    public List<Group> getGroupsByUserId(String userId) {
        Set<String> groupIds = userGroupRelationDao.getUserGroupIdsByUserId(userId);
        if (groupIds.isEmpty()) {
            return Collections.emptyList();
        }
        return groupDao.getGroupsByIds(groupIds.toArray(new String[0]));
    }

    @Override
    public boolean existsByGroupName(String groupName) {
        Group group = new Group();
        group.setName(groupName);
        return groupDao.exists(Example.of(group, ExampleMatcher.matchingAll().withIgnoreNullValues()));
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void saveGroups(Group group, List<GroupModuleRelation> groupModuleRelations, List<UserGroupRelation> userGroupRelations) {
        groupDao.save(group);
        if (CollectionUtils.isNotEmpty(groupModuleRelations)) {
            groupModuleRelationDao.saveAll(groupModuleRelations);
        }
        if (CollectionUtils.isNotEmpty(userGroupRelations)) {
            userGroupRelationDao.saveAll(userGroupRelations);
        }
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteGroup(String id) {
        //组
        Optional<Group> group = groupDao.findById(id);
        String name = group.orElse(new Group()).getName();
        groupDao.deleteById(id);
        //组和模块的关系表
        groupModuleRelationDao.deleteByGroupId(id);
        //组和用户的关系表
        Set<String> userIds = userGroupRelationDao.getUsersByGroupId(id);
        userGroupRelationDao.deleteByGroupId(id);
        if (!userIds.isEmpty() && name != null) {
            //发送邮件
            String[] emails = opUserService.getOpUsersByUserIds(userIds.toArray(new String[0]))
                    .stream().map(OpUser::getEmail)
                    .filter(StringUtils::isNotEmpty)
                    .distinct()
                    .toArray(String[]::new);
            sendDeleteGroupMail(emails, name);
        }
    }

    private void sendDeleteGroupMail(String[] emails, String groupName) {
        new Thread(()->{
            for (String email : emails) {
                try {
                    szxyMailSender.sendHtmlMessage(String.format("您所在的用户分组【%s】已被解散", groupName),"运营平台系统通知", email);
                } catch (MessagingException e) {
                    //ignore
                }
            }
        }).start();
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateGroupName(String id, String groupName) {
        groupDao.updateGroupName(groupName, id);
    }
}
