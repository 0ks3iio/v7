package net.zdsoft.szxy.operation.inner.permission.service.impl;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.permission.dao.GroupDao;
import net.zdsoft.szxy.operation.inner.permission.dao.GroupModuleRelationDao;
import net.zdsoft.szxy.operation.inner.permission.entity.Group;
import net.zdsoft.szxy.operation.inner.permission.entity.GroupModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.service.GroupModuleRelationService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/11 上午9:17
 */
@Service("groupModuleRelationService")
public class GroupModuleRelationServiceImpl implements GroupModuleRelationService {

    @Resource
    private GroupModuleRelationDao groupModuleRelationDao;
    @Resource
    private GroupDao groupDao;

    @Record(type = RecordType.Service)
    @Override
    public List<GroupModuleRelation> getGroupModuleRelationsByGroupId(String groupId) {
        return groupModuleRelationDao.getGroupModuleRelationsByGroupId(groupId);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateGroupModuleRelations(String groupId, String regionCode, List<GroupModuleRelation> relations) {
        if (CollectionUtils.isEmpty(relations)) {
            groupModuleRelationDao.deleteByGroupId(groupId);
            return;
        }
        if (StringUtils.isBlank(regionCode)) {
            regionCode = Group.ALL_REGION;
        }
        groupDao.updateRegionCode(regionCode, groupId);

        Set<String> newOperateIds = relations.stream().map(GroupModuleRelation::getOperateId).collect(Collectors.toSet());

        List<GroupModuleRelation> olds = getGroupModuleRelationsByGroupId(groupId);

        List<GroupModuleRelation> toDeletes = olds.stream().filter(e->!newOperateIds.contains(e.getOperateId())).collect(Collectors.toList());
        Set<String> oldOperateIds = olds.stream().map(GroupModuleRelation::getOperateId).collect(Collectors.toSet());
        List<GroupModuleRelation> toSaves = relations.stream().filter(e->!oldOperateIds.contains(e.getOperateId())).collect(Collectors.toList());

        groupModuleRelationDao.deleteInBatch(toDeletes);
        groupModuleRelationDao.saveAll(toSaves);
    }
}
