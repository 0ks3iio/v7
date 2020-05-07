package net.zdsoft.szxy.base.service;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.passport.service.client.PassportClient;
import net.zdsoft.szxy.base.api.UserRemoteService;
import net.zdsoft.szxy.base.dao.TeacherDao;
import net.zdsoft.szxy.base.dao.UserDao;
import net.zdsoft.szxy.base.dto.OpenAccount;
import net.zdsoft.szxy.base.exception.NoRollbackException;
import net.zdsoft.szxy.base.query.UserQuery;
import net.zdsoft.szxy.base.dto.UserUpdater;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.enu.DeleteCode;
import net.zdsoft.szxy.base.enu.EventSourceCode;
import net.zdsoft.szxy.base.enu.UserOwnerTypeCode;
import net.zdsoft.szxy.base.enu.UserStateCode;
import net.zdsoft.szxy.base.enu.UserTypeCode;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.base.model.QUser;
import net.zdsoft.szxy.dubbo.jpa.DubboPageImpl;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import net.zdsoft.szxy.utils.AssertUtils;
import net.zdsoft.szxy.utils.UuidUtils;
import net.zdsoft.szxy.utils.crypto.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author shenke
 * @since 2019/3/19 下午3:47
 */
@Service("userRemoteService")
public class UserServiceImpl implements UserRemoteService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserDao userDao;
    @Resource
    private TeacherDao teacherDao;
    @Resource
    private PassportClient passportClient;

    @Record(type = RecordType.Call)
    @Override
    public User getUserById(String id) {
        AssertUtils.notNull("id", "Id不能为空");
        return userDao.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        AssertUtils.notNull(username, "用户名username不能为空");
        return userDao.getUserByUsername(username);
    }

    @Override
    public Boolean existsUsername(String username) {
        User user = new User();
        user.setIsDeleted(DeleteCode.NOT_DELETED);
        user.setUsername(username);
        return userDao.exists(Example.of(user, ExampleMatcher.matchingAll().withIgnoreNullValues()));
    }

    @Override
    public List<User> getUsersByUnitId(String unitId) {
        AssertUtils.notNull(unitId, "单位ID不能为空");
        return userDao.getUsersByUnitId(unitId);
    }

    @Transactional(rollbackFor = NoRollbackException.class)
    @Override
    public List<User> getUsersByOwnerIds(String[] ownerIds) {
        AssertUtils.hasElements(ownerIds, "ownerIds 不能为空（null或者empty array或者[null,null]）");
        return userDao.getUsersByOwnerId(ownerIds).collect(Collectors.toList());
    }

    @Override
    public User getUserByOwnerId(String ownerId) {
        return userDao.getUserByOwnerId(ownerId).orElse(null);
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void openAccount(List<OpenAccount> accounts) throws UsernameAlreadyExistsException, SzxyPassportException {
        //检查用户信息合法性 EIS Passport
        String[] usernames = accounts.stream().map(OpenAccount::getUsername).toArray(String[]::new);
        checkUsername(usernames);

        //create User
        List<User> users = accounts.stream().map(this::createUser).collect(Collectors.toList());
        //同步passport
        try {
            passportClient.addAccounts(users.stream().map(this::createAccount).toArray(Account[]::new));
        } catch (PassportException e) {
            logger.error("Passport 新增用户异常", e);
            throw new SzxyPassportException("调用passport新增用户失败");
        }
        //Base库
        userDao.saveAll(users);
    }

    private void checkUsername(String[] usernames) throws UsernameAlreadyExistsException, SzxyPassportException {
        Stream<User> users = userDao.getUsersByUsername(usernames);
        if (users.count() > 0) {
            throw convertException(users.map(User::getUsername).collect(Collectors.toSet()));
        }
        try {
            Set<String> existsUsernames = passportClient.queryExistsAccountUsernames(usernames);
            if (existsUsernames != null && !existsUsernames.isEmpty()) {
                throw convertException(existsUsernames);
            }
        } catch (PassportException e) {
            logger.error("Check Passport Username error", e);
            throw new SzxyPassportException("Passport校验用户名合法性出错");
        }
    }

    private UsernameAlreadyExistsException convertException(Collection<String> usernames) {
        if (usernames.size() == 1) {
            return new UsernameAlreadyExistsException("用户名已存在", usernames.stream().findFirst().get());
        }
        return new UsernameAlreadyExistsException("用户名已存在", String.join(",", usernames));
    }


    private User createUser(OpenAccount account) {
        User user = new User();
        user.setUsername(account.getUsername());
        user.setPassword(PasswordUtils.encodeIfNot(account.getPassword()));
        user.setId(UuidUtils.generateUuid());
        user.setOwnerId(account.getId());

        user.setIsDeleted(DeleteCode.NOT_DELETED);
        user.setOwnerType(account.getOwnerType());

        user.setRegionCode(account.getRegionCode());
        user.setRealName(account.getRealName());
        user.setUnitId(account.getUnitId());
        user.setClassId(account.getClassId());
        user.setSex(account.getSex());
        user.setIdentityCard(account.getIdentityCard());
        user.setBirthday(account.getBirthday());


        user.setUserType(UserTypeCode.USER_TYPE_COMMON_USER);
        user.setUserState(UserStateCode.USER_MARK_NORMAL);
        user.setCreationTime(new Date());
        user.setModifyTime(user.getCreationTime());
        user.setAccountId(UuidUtils.generateUuid());
        user.setEventSource(EventSourceCode.LOCAL);

        //TODO 不知道有什么作用
        user.setEnrollYear(account.getEnrollYear());
        user.setUserRole(2);
        user.setIconIndex(0);
        return user;
    }

    private Account createAccount(User user) {
        Account account = new Account();
        account.setId(user.getAccountId());
        account.setUsername(user.getUsername());
        account.setPassword(user.getPassword());
        account.setRealName(user.getRealName());
        if (UserOwnerTypeCode.STUDENT.equals(user.getOwnerType())) {
            account.setFixedType(1);
            account.setType(11);
        } else {
            account.setFixedType(3);
            account.setType(15);
        }
        account.setRegionId(user.getRegionCode());
        account.setPhone(user.getMobilePhone());
        account.setEmail(user.getEmail());
        account.setBirthday(user.getBirthday());
        if (user.getSex() != null) {
            account.setSex(user.getSex());
        }
        return account;
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void batchUpdatePassword(String[] userIds, String password) throws SzxyPassportException {
        AssertUtils.hasElements(userIds, "用户ID列表不能为空");
        AssertUtils.notNull(password, "password不能为空");
        String encodePassword = PasswordUtils.encode(password);
        userDao.batchUpdatePassword(userIds, encodePassword);
        Account[] accounts = userDao.getUsersByUserIds(userIds).map(e -> {
            Account account = new Account();
            account.setId(e.getAccountId());
            account.setPassword(encodePassword);
            return account;
        }).toArray(Account[]::new);
        try {
            passportClient.modifyAccounts(accounts, new String[]{"password"});
        } catch (PassportException e) {
            logger.error("同步修改Passport出错", e);
            throw new SzxyPassportException("同步修改Passport密码出错");
        }
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateDisplayOrder(String userId, Integer displayOrder) {
        AssertUtils.notNull(displayOrder, "新的排序号不能为空");
        AssertUtils.isTrue(displayOrder > 0, "排序号非负");
        userDao.updateDisplayOrderById(userId, displayOrder);

        userDao.findById(userId).ifPresent(user->{
            teacherDao.updateDisplayOrder(displayOrder, user.getOwnerId());
        });
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = NoRollbackException.class)
    @Override
    public Map<String, String> getUsernameMapByUserIds(String[] userIds) {
        AssertUtils.hasElements(userIds, "userIds不能为空");
        return userDao.getUsersByUserIds(userIds).collect(Collectors.toMap(User::getId, User::getUsername));
    }

    @Record(type = RecordType.Call)
    @Transactional(rollbackFor = NoRollbackException.class)
    @Override
    public Map<String, String> getUsernameMapByOwnerIds(String[] ownerIds) {
        AssertUtils.hasElements(ownerIds, "ownerIds不能为空");
        return userDao.getUsersByOwnerId(ownerIds).collect(Collectors.toMap(User::getOwnerId, User::getUsername));
    }

    @Record(type = RecordType.Call)
    @Override
    public Page<User> queryUsers(UserQuery query, Pageable page) {
        return DubboPageImpl.of(userDao.findAll((Specification<User>) (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> ps = new ArrayList<>(7);

            if (StringUtils.isNotBlank(query.getUnitId())) {
                ps.add(criteriaBuilder.equal(root.get(QUser.unitId), query.getUnitId()));
            }
            if (StringUtils.isNotBlank(query.getMobilePhone())) {
                ps.add(criteriaBuilder.like(root.get(QUser.mobilePhone), query.getMobilePhone() + "%"));
            }
            if (StringUtils.isNotBlank(query.getRealName())) {
                ps.add(criteriaBuilder.like(root.get(QUser.realName), query.getRealName() + "%"));
            }
            if (StringUtils.isNotBlank(query.getUsername())) {
                ps.add(criteriaBuilder.like(root.get(QUser.username), query.getUsername() + "%"));
            }
            if (Objects.nonNull(query.getUserState())) {
                ps.add(criteriaBuilder.equal(root.get(QUser.userState), query.getUserState()));
            }
            if (Objects.nonNull(query.getOwnerType())) {
                ps.add(criteriaBuilder.equal(root.get(QUser.ownerType), query.getOwnerType()));
            }
            if (StringUtils.isNotBlank(query.getRegionCode())) {
                ps.add(criteriaBuilder.equal(root.get(QUser.regionCode), query.getRegionCode()));
            }
            if (CollectionUtils.isNotEmpty(query.getRegions())) {
                List<Predicate> regionPredicate = new ArrayList<>(query.getRegions().size());
                for (String region : query.getRegions()) {
                    regionPredicate.add(criteriaBuilder.like(root.get(QUser.regionCode), region + "%"));
                }
                ps.add(criteriaBuilder.or(regionPredicate.toArray(new Predicate[0])));
            }
            ps.add(criteriaBuilder.equal(root.get(QUser.isDeleted), DeleteCode.NOT_DELETED));
            return criteriaQuery.where(ps.toArray(new Predicate[0])).getRestriction();
        }, page));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void updateUser(UserUpdater updater) {
        userDao.updateUser(updater);
    }

    @Override
    public Integer getUsernameMaxCode(String classCode, String unitId) {
        return userDao.getUsernameMaxCode(classCode,unitId);
    }
}
