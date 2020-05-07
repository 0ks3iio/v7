package net.zdsoft.szxy.operation.inner.permission.service.impl;

import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.permission.dao.UserModuleRelationDao;
import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.service.UserModuleRelationService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2019/4/9 下午2:08
 */
@Service("userModuleRelationService")
public class UserModuleRelationServiceImpl implements UserModuleRelationService {

    @Resource
    private UserModuleRelationDao userModuleRelationDao;

    @Override
    public List<UserModuleRelation> getRelationsByUserId(String userId) {
        return userModuleRelationDao.getUserModuleRelationsByUserId(userId);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUserModules(List<UserModuleRelation> userModuleRelations, String userId) {

        if (CollectionUtils.isEmpty(userModuleRelations)) {
            userModuleRelationDao.deleteByUserId(userId);
            return;
        }

        List<UserModuleRelation> oldRelations = userModuleRelationDao.getUserModuleRelationsByUserId(userId);
        Set<String> oldOperateIds = oldRelations.stream().map(UserModuleRelation::getOperateId).collect(Collectors.toSet());

        Set<String> newOperateIds = userModuleRelations.stream().map(UserModuleRelation::getOperateId).collect(Collectors.toSet());

        //得到需要删除的数据
        userModuleRelationDao.deleteAll(oldRelations.stream().filter(e->!newOperateIds.contains(e.getOperateId())).collect(Collectors.toSet()));
        userModuleRelationDao.saveAll(userModuleRelations.stream().filter(e->!oldOperateIds.contains(e.getOperateId())).collect(Collectors.toList()));
    }
}