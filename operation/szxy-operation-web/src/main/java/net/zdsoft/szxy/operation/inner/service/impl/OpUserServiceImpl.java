package net.zdsoft.szxy.operation.inner.service.impl;

import net.zdsoft.szxy.base.enu.DeleteCode;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.operation.inner.dao.OpUserDao;
import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.enums.UserState;
import net.zdsoft.szxy.operation.inner.exception.IllegalStateException;
import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import net.zdsoft.szxy.operation.inner.permission.entity.UserGroupRelation;
import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;
import net.zdsoft.szxy.operation.inner.permission.service.UserGroupRelationService;
import net.zdsoft.szxy.operation.inner.permission.service.UserModuleRelationService;
import net.zdsoft.szxy.operation.inner.service.OpUserService;
import net.zdsoft.szxy.utils.AssertUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author shenke
 * @since 2019/1/9 下午3:22
 */
@Service("opUserService")
public class OpUserServiceImpl implements OpUserService {

    @Resource
    private OpUserDao opUserDao;
    @Resource
    private UserGroupRelationService userGroupRelationService;
    @Resource
    private UserModuleRelationService userModuleRelationService;


    @Override
    public Optional<OpUser> getOpUserByUsername(String username) {
        return opUserDao.getOpUserByUsername(username);
    }

    @Override
    public List<OpUser> getOpUsersByUserIds(String[] userIds) {
        AssertUtils.hasElements(userIds, "用户ID Array不能为空");
        return opUserDao.findAllById(Arrays.asList(userIds));
    }

    @Override
    public Page<OpUser> getAllUsers(Pageable pageable) {
        return opUserDao.findAll((Specification<OpUser>) (root, query, criteriaBuilder)
                -> query.where(criteriaBuilder.equal(root.<Integer>get("isDeleted"), DeleteCode.NOT_DELETED))
                .getRestriction(), pageable);
    }


    @Override
    public List<OpUser> getAllUsers() {
        return opUserDao.findAll();
    }

    @Override
    public Optional<OpUser> getUserById(String id) {
        AssertUtils.notNull(id, "用户ID不能为空");
        return opUserDao.findById(id);
    }

    @Record(type = RecordType.SQL)
    @Override
    public boolean existsByRealName(String realName) {
        OpUser user = new OpUser();
        user.setRealName(realName);
        return exists(user);
    }

    @Record(type = RecordType.SQL)
    @Override
    public boolean existsByUsername(String username) {
        OpUser user = new OpUser();
        user.setUsername(username);
        return exists(user);
    }

    private boolean exists(OpUser user) {
        return opUserDao.exists(Example.of(user, ExampleMatcher.matchingAll().withIgnoreNullValues()));
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void saveOrUpdateUser(OpUser user, List<String> groups, List<UserModuleRelation> relations) {
        //1、check User
        Optional<OpUser> exists = getUserById(user.getId());

        //更新 姓名、手机号、邮箱
        if (exists.isPresent()) {
            OpUser originUser = exists.get();
            if (StringUtils.isNotBlank(user.getEmail())) {
                originUser.setEmail(user.getEmail());
            }
            if (StringUtils.isNotBlank(user.getPhone())) {
                originUser.setPhone(user.getPhone());
            }
            if (StringUtils.isNotBlank(user.getRealName())) {
                originUser.setRealName(user.getRealName());
            }
            if (Objects.nonNull(user.getSex())) {
                originUser.setSex(user.getSex());
            }
            originUser.setAuthRegionCode(user.getAuthRegionCode());
            opUserDao.save(originUser);
        }
        //新增
        else {
            opUserDao.save(user);
        }

        updateUserAuth(groups, relations, user.getId());
    }

    @Record(type = RecordType.Service)
    @Override
    public void save(OpUser user) {
        AssertUtils.notNull(user, "user cat't null");
        if (StringUtils.isBlank(user.getId())) {
            user.setId(UuidUtils.generateUuid());
        }
        opUserDao.save(user);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUserAuth(List<String> groups, List<UserModuleRelation> relations, String userId) {
        //2、分组变更
        userGroupRelationService.updateUserGroups(userId, groups == null ? null : groups.toArray(new String[0]));
        //3、单个权限变更
        userModuleRelationService.updateUserModules(relations, userId);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void deleteUser(String userId) {
        opUserDao.deleteById(userId);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUserState(String id, Integer state) throws IllegalStateException {
        if (!UserState.NORMAL.getState().equals(state)
                && !UserState.STOP.getState().equals(state)) {
            throw new IllegalStateException("不合法的状态值", state);
        }
        opUserDao.updateUserState(state, id);
    }

    @Record(type = RecordType.Service)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updatePassword(String id, String password) {
        opUserDao.updatePassword(PasswordUtils.encodeIfNot(password), id);
    }
}
