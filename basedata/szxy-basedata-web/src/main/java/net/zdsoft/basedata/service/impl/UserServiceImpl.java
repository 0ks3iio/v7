package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.dao.DeptDao;
import net.zdsoft.basedata.dao.FamilyDao;
import net.zdsoft.basedata.dao.StudentDao;
import net.zdsoft.basedata.dao.TeacherDao;
import net.zdsoft.basedata.dao.UnitDao;
import net.zdsoft.basedata.dao.UserDao;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.entity.UserDept;
import net.zdsoft.basedata.service.UserDeptService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UrlUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.RolePermission;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.remote.service.ModelRemoteService;
import net.zdsoft.system.remote.service.RolePermissionRemoteService;
import net.zdsoft.system.remote.service.RoleRemoteService;
import net.zdsoft.system.remote.service.SysOptionRemoteService;
import net.zdsoft.system.remote.service.UserRoleRemoteService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private FamilyDao familyDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private UserDeptService userDeptService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private UnitDao unitDao;

    @Override
    public User findByUsername(final String username) {
        return userDao.findByUsername(username);
    }

    @Override
    protected BaseJpaRepositoryDao<User, String> getJpaDao() {
        return userDao;
    }

    @Override
    public List<User> findByDeptId(final String deptId) {
        List<User> users = userDao.findByDeptId(deptId);

        return users;
    }

    @Override
    public List<User> findByUnitId(final String unitId) {
        List<User> users = userDao.findByUnitIds(unitId);
        return users;
    }

    @Override
    public List<User> findByUnitId(String unitId, Pagination page) {
        return userDao.findByUnitId(unitId, Pagination.toPageable(page));
    }

    @Override
    public User findByOwnerId(final String ownerId) {
        return userDao.findByOwnerId(ownerId);
    }

    @Override
    protected Class<User> getEntityClass() {
        return User.class;
    }

    @Override
    public List<User> saveAllEntitys(User... user) {
        return userDao.saveAll(checkSave(user));
    }

    @Override
    public void deleteAllByIds(String... id) {
        if (id != null && id.length > 0) {
            userDao.deleteAllByIds(id);
        }
    }

    @Override
    public List<User> findByUnitDutyCode(String unitId, String dutyCode) {
        List<User> userList = userDao.findByUnitDutyCode(unitId, dutyCode);
        if (userList != null) {
            for (User user : userList) {
                String avatarUrl = user.getAvatarUrl();
                if (!Validators.isEmpty(avatarUrl)) {
                    // 微课掌上通这边上传头像，数据库中存完整路径
                    if (!avatarUrl.startsWith("http")) {
                        // 本地存储，数据库中只是file的后半部分
                        String fileUrl = sysOptionRemoteService.findValue(Constant.FILE_URL);
                        user.setAvatarUrl(fileUrl + avatarUrl);
                    }
                }
            }
        }
        return userList;
    }

    @Override
    public List<User> findByRealName(String realName, String pageIndex) {
        if (!Validators.isEmpty(realName)) {
            Pagination page = new Pagination(Integer.parseInt(pageIndex), 20, false);
            Pageable pageable = Pagination.toPageable(page);
            List<User> users = userDao.findByRealName(realName + "%", pageable);
            if (pageable.getPageNumber() < NumberUtils.toInt(pageIndex)) {
                return new ArrayList<User>();
            } else {
                return users;
            }
        }
        return new ArrayList<User>();
    }

    @Override
    public List<User> findByRealName(String realName) {
        if (!Validators.isEmpty(realName)) {
            return userDao.findByRealName(realName);
        }
        return new ArrayList<User>();
    }

    @Override
    public List<User> findLeaderByDept(String deptId) {
        return findLeaderByDept(deptId, false);

    }

    @Override
    public List<User> findLeaderByDept(String deptId, boolean needAvatarUrl) {
        List<User> userList = userDao.findLeaderByDept(deptId);
        if (userList != null && needAvatarUrl) {
            for (User user : userList) {
                String avatarUrl = user.getAvatarUrl();
                if (!Validators.isEmpty(avatarUrl)) {
                    // 微课掌上通这边上传头像，数据库中存完整路径
                    if (avatarUrl.startsWith("http")) {
                        user.setAvatarUrl(avatarUrl);
                    }
                    // 本地存储，数据库中只是file的后半部分
                    else {
                        String fileUrl = sysOptionRemoteService.findValue(Constant.FILE_URL);
                        user.setAvatarUrl(fileUrl + avatarUrl);
                    }
                }
            }
        }
        return userList;
    }

    @Override
    public List<User> findByOwnerType(String[] unitIds, Integer ownerType) {
        return userDao.findByOwnerType(unitIds, ownerType);
    }

    @Override
    public List<User> findByUnitIds(final String[] unitIds, Pagination page) {
        // TODO Auto-generated method stub
        return findAll(new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                List<Order> orders = Lists.newArrayList();
                ps.add(root.<String>get("unitId").in(unitIds));
                ps.add(cb.equal(root.<String>get("isDeleted"), "0"));

                return query.where(ps.toArray(new Predicate[0])).orderBy(orders).getGroupRestriction();
            }
        }, page);
    }

    @Override
    public List<User> findByUnitIdAndRealNameAndOwnerTypeAndUserTypes(final String unitId, final String realName,
                                                                      final Integer ownerType, final Integer[] userTypes, Pagination page) {
        return findByUnitIdAndRealNameAndOwnerTypeAndUserTypesAndUserName(unitId, realName, ownerType, userTypes, null, page);
    }

    @Override
    public List<User> findByUnitIdAndRealNameAndOwnerTypeAndUserTypesAndUserName(final String unitId, final String realName,
                                                                                 final Integer ownerType, final Integer[] userTypes, final String userName, Pagination page) {
        Specification<User> specification = new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                ps.add(cb.equal(root.get("unitId").as(String.class), unitId));
                ps.add(cb.equal(root.get("isDeleted").as(Integer.class), 0));
                ps.add(cb.equal(root.get("userState").as(Integer.class), 1));
                if (null != ownerType) {
                    ps.add(cb.equal(root.get("ownerType").as(Integer.class), ownerType));
                }

                if (StringUtils.isNotEmpty(realName)) {
                    ps.add(cb.like(root.get("realName").as(String.class), realName + "%"));
                }
                if (null != userTypes) {
                    ps.add(root.get("userType").in(userTypes));
                }
                if (StringUtils.isNotBlank(userName)) {
                    ps.add(cb.like(root.get("username").as(String.class), userName + "%"));
                }

                List<Order> orderList = new ArrayList<Order>();
                orderList.add(cb.asc(root.get("realName").as(String.class)));

                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<User> findAll = userDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        } else {
            return userDao.findAll(specification);
        }
    }

    @Override
    public void updatePasswordByUsername(String username, String password, String id) {
        userDao.updatePasswordByUsername(password, new Date(), new Date(), username);
    }

    @Override
    public void updateStateByUsername(String username, int state, String id) {
        userDao.updateStateByUsername(state, username);
    }

    @Autowired
    private RoleRemoteService roleRemoteService;
    @Autowired
    private RolePermissionRemoteService rolePermissionRemoteService;
    @Autowired
    private UserRoleRemoteService userRoleRemoteService;
    @Autowired
    private ModelRemoteService modelRemoteService;

    @Override
    public void addTopAdmin(User topAdmin) {
        // 查找默认部门
        List<Dept> depts = deptDao.findByUnitIdAndParentId(topAdmin.getUnitId(), Constant.GUID_ZERO);
        String deptId = null;
        if (!CollectionUtils.isEmpty(depts)) {
            deptId = depts.get(0).getId();
        }
        Teacher teacher = convertAdminTeacher(topAdmin, deptId);
        userDao.save(topAdmin);
        teacherDao.save(teacher);

        dealRole(topAdmin);
    }

    private Teacher convertAdminTeacher(User user, String deptId) {
        Teacher teacher = new Teacher();
        teacher.setId(user.getOwnerId());
        teacher.setTeacherCode("0001");
        teacher.setUnitId(user.getUnitId());
        teacher.setSex(0);
        teacher.setDeptId(deptId);
        teacher.setCreationTime(user.getCreationTime());
        teacher.setModifyTime(user.getModifyTime());
        teacher.setIsDeleted(0);
        teacher.setEventSource(0);
        teacher.setTeacherName(user.getRealName());
        return teacher;
    }

    /**
     * 处理用户角色
     *
     * @param us
     */
    private void dealRole(User us) {
        if (roleRemoteService.countBy("identifier", "topAdmin") == 0) {
            // 初始化角色
            Role role = new Role();
            role.setId(UuidUtils.generateUuid());
            role.setIdentifier("topAdmin");
            role.setUnitId(us.getUnitId());
            role.setName("顶级单位管理员");
            role.setIsActive("1");
            role.setDescription("顶级单位管理员系统管理");
            role.setSubSystem("99");
            role.setRoleType(2);
            role.setIsSystem(0);

            UserRole userRole = new UserRole();
            userRole.setId(UuidUtils.generateUuid());
            userRole.setUserId(us.getId());
            userRole.setRoleId(role.getId());

            List<Model> modelList = SUtils.dt(modelRemoteService.findListBySubId(99), Model.class);
            List<RolePermission> rolePermissionList = Lists.newArrayList();
            for (Model model : modelList) {
                if (!new Integer(1).equals(model.getUnitClass())) {
                    continue;
                }
                RolePermission rolePermission = new RolePermission();
                rolePermission.setId(UuidUtils.generateUuid());
                rolePermission.setRoleId(role.getId());
                rolePermission.setModuleId(model.getId().toString());
                rolePermission.setType(3);
                rolePermissionList.add(rolePermission);
            }
            roleRemoteService.saveAllEntitys(SUtils.s(Lists.newArrayList(role)));
            userRoleRemoteService.saveAll(SUtils.s(Lists.newArrayList(userRole)));
            rolePermissionRemoteService.saveAll(SUtils.s(rolePermissionList));
        }
    }

    public void saveUsers(List<User> users) throws Exception {
        Set<String> unitIds = EntityUtils.getSet(users, "unitId");
        List<Unit> units = unitDao.findAllById(unitIds);
        Map<String, Unit> unitMap = EntityUtils.getMap(units, "id");
        userDao.saveAll(users);
        if (Evn.isPassport()) {
            List<String> accountDeletedIds = new ArrayList<>();
            List<User> accountAdds = new ArrayList<>();
            Map<String, User> accountUserMap = new HashMap<>();
            for (User us : users) {
                if (us.getIsDeleted() == 1) {
                    accountDeletedIds.add(us.getAccountId());
                } else {
                    accountAdds.add(us);
                    accountUserMap.put(us.getAccountId(), us);
                }
            }

            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(accountDeletedIds)) {
                PassportClientUtils.getPassportClient().deleteAccounts(accountDeletedIds.toArray(new String[0]));
            }
            List<Account> accounts = new ArrayList<>();
            for (User us : accountAdds) {
                Unit un = unitMap.get(us.getUnitId());
                Account account = new Account();
                account.setBirthday(us.getBirthday());
                account.setRealName(us.getRealName());
                account.setUsername(us.getUsername());
                account.setPhone(us.getMobilePhone());
                account.setSex(us.getSex());
                int type = 12; // 用户类型:11：中小学生、12：在职教师、13：退休教师、14：在校大学生、15：家长
                int fixedType = 2;// 1：学生、2：教师、3：家长
                account.setType(type);
                if (us.getOwnerType() == null || us.getOwnerType() == 0) {
                    account.setFixedType(fixedType);
                } else {
                    account.setFixedType(us.getOwnerType());
                }
                account.setPassword(us.getPassword());
                account.setRegisterTime(us.getCreationTime());
                account.setId(us.getAccountId());
                account.setState(1);
                account.setRegionId(un.getRegionCode());
                Map<String, String> map = account.getProperties();
                if (map == null)
                    map = new HashMap<String, String>();
                // 设置任职学校
                map.put("currentSchoolName", un.getUnitName());
                account.setProperties(map);
                accounts.add(account);
            }

            try {
                Account[] retAccounts = PassportClientUtils.getPassportClient()
                        .addAccounts(accounts.toArray(new Account[0]));
                if (retAccounts != null) {
                    for (Account account : retAccounts) {
                        if (account != null) {
                            accountUserMap.get(account.getId()).setSequence(account.getSequence());
                        }
                    }
                    userDao.saveAll(accountUserMap.values());
                }
            } catch (Exception e) {

            }
        }
    }

    public void saveUser(User us) throws Exception {
        Unit un = unitDao.findOne(Unit.class, us.getUnitId(), new String[]{"regionCode", "unitName"});
        if (un == null) {
            return;
        }
        userDao.save(us);
        dealRole(us);
        if (Evn.isPassport()) {
            Account account = new Account();
            account.setBirthday(us.getBirthday());
            account.setRealName(us.getRealName());
            account.setUsername(us.getUsername());
            account.setPhone(us.getMobilePhone());
            account.setSex(NumberUtils.toInt(String.valueOf(us.getSex())));
            int type = 12; // 用户类型:11：中小学生、12：在职教师、13：退休教师、14：在校大学生、15：家长
            int fixedType = 2;// 1：学生、2：教师、3：家长
            account.setType(type);
            if (us.getOwnerType() == null || us.getOwnerType() == 0) {
                account.setFixedType(fixedType);
            } else {
                account.setFixedType(us.getOwnerType());
            }
            account.setPassword(us.getPassword());
            account.setRegisterTime(us.getCreationTime());
            account.setId(us.getAccountId());
            account.setState(1);
            account.setRegionId(un.getRegionCode());
            Map<String, String> map = account.getProperties();
            if (map == null)
                map = new HashMap<String, String>();
            // 设置任职学校
            map.put("currentSchoolName", un.getUnitName());
            account.setProperties(map);
            if (us.getIsDeleted() == 1) {
                PassportClientUtils.getPassportClient().deleteAccounts(new String[]{us.getAccountId()});
            } else {
                Account passportAccount = PassportClientUtils.getPassportClient().queryAccount(account.getId());
                if (passportAccount == null) {
                    try {
                        Account retAccount = PassportClientUtils.getPassportClient().addAccount(account);
                        if (retAccount != null) {
                            us.setSequence(retAccount.getSequence());
                        }
                    } catch (Exception e) {
                        account.setSequence(0);
                        Account retAccount = PassportClientUtils.getPassportClient().addAccount(account);
                        if (retAccount != null) {
                            us.setSequence(retAccount.getSequence());
                        }
                    }
                } else {
                    PassportClientUtils.getPassportClient().modifyAccount(account,
                            new String[]{"sex", "realName", "password", "nickname", "modifyTime", "homepage", "birthday"});
                }
                userDao.update(us, new String[]{"sequence"});
            }
        }

    }

    @Override
    public void updatePasswordByAccountId(String password, Date modifyTime, String accountId) throws Exception {
        // 本地
        userDao.updatePasswordByAccountId(password, modifyTime, new Date(), accountId);
        // passport
        PassportClientUtils.getPassportClient().modifyAccountPassword(accountId, password);
    }

    @Override
    public void updatePhoneById(String id, String mobilePhone, Date modifyTime, String ownerId, int ownerType,
                                String accountId) throws Exception {
        userDao.updatePhoneById(mobilePhone, modifyTime, id);
        if (ownerType == User.OWNER_TYPE_FAMILY) {
            Family family = new Family();
            family.setId(ownerId);
            family.setMobilePhone(mobilePhone);
            familyDao.update(family, new String[]{"mobilePhone"});
        } else if (ownerType == User.OWNER_TYPE_STUDENT) {
            Student student = new Student();
            student.setId(ownerId);
            student.setMobilePhone(mobilePhone);
            studentDao.update(student, new String[]{"mobilePhone"});
        } else if (ownerType == User.OWNER_TYPE_TEACHER) {
            Teacher teacher = new Teacher();
            teacher.setId(ownerId);
            teacher.setMobilePhone(mobilePhone);
            teacherDao.update(teacher, new String[]{"mobilePhone"});
        }
        Account account = new Account();
        account.setId(accountId);
        account.setPhone(mobilePhone);
        PassportClientUtils.getPassportClient().modifyAccount(account, new String[]{"phone"});
    }

    @Override
    public void updateAvatarUrlById(String avatarUrl, Date modifyTime, String id) {
        userDao.updateAvatarUrlById(avatarUrl, modifyTime, id);
    }

    @Override
    public List<User> getUsersByUnitIdsOrUserTypes(final String[] unitIds, final Integer[] userTypes) {
        if ((null == unitIds || unitIds.length == 0) && (null == userTypes || userTypes.length == 0)) {
            return new ArrayList<User>();
        }

        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();

                if (null != unitIds) {
                    In<String> in = cb.in(root.get("unitId").as(String.class));
                    for (int i = 0; i < unitIds.length; i++) {
                        in.value(unitIds[i]);
                    }
                    ps.add(in);
                }

                if (null != userTypes) {
                    In<Integer> in = cb.in(root.get("ownerType").as(Integer.class));
                    for (int i = 0; i < userTypes.length; i++) {
                        in.value(userTypes[i]);
                    }
                    ps.add(in);
                }
                cq.where(ps.toArray(new Predicate[0]));
                return cq.getRestriction();
            }
        };

        return userDao.findAll(specification);
    }

    @Override
    public List<User> findByRealNameAndIdentityCard(String realName, String identityCard) {
        List<Teacher> teachers = teacherDao.findByTeacherNameAndIdentityCard(realName, identityCard);
        List<Family> families = familyDao.findByRealNameAndIdentityCard(realName, identityCard);
        List<String> userIds = EntityUtils.<String>merge(EntityUtils.<String, Teacher>getList(teachers, "id"),
                EntityUtils.<String, Family>getList(families, "id"));
        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }
        return findListByIn("ownerId", EntityUtils.toArray(userIds, String.class));
    }

    @Override
    public List<User> findByMobilePhoneAndPasswordIn(String mobilePhone, String[] password) {
        return userDao.findByMobilePhoneAndPasswordIn(mobilePhone, password);
    }

    /**
     * 根据单位和更新时间获取用户
     *
     * @param
     * @return
     */
    @Override
    public List<User> findByUnitIdAndModifyTime(String unitId, Date modifyTime) {
        List<User> userList = userDao.findByUnitIdAndModifyTime(unitId, modifyTime);
        assembledRelativeDepts(userList);
        return userList;
    }

    /**
     * 根据单位获取所有教师用户
     *
     * @param
     * @return
     */
    @Override
    public List<User> findAllByUnitId(String unitId) {
        List<User> userList = userDao.findAllByUnitId(unitId);
        assembledRelativeDepts(userList);
        return userList;
    }

    private void assembledRelativeDepts(List<User> userList) {
    	if(CollectionUtils.isEmpty(userList)) {
    		return;
    	}
        Set<String> userIds = new HashSet<String>();
        for (User user : userList) {
            userIds.add(user.getId());
        }
        Map<String, List<UserDept>> userDeptMap = userDeptService.findMapByUserIds(userIds.toArray(new String[0]));
        for (User user : userList) {
            List<UserDept> tempDeptList = userDeptMap.get(user.getId());
            if (org.apache.commons.collections.CollectionUtils.isNotEmpty(tempDeptList))
                user.setDeptList(tempDeptList);
        }
    }

    /**
     * 更新用户钉钉号
     */
    @Override
    public void updateDingDingIdById(String dingdingId, String id) {
        userDao.updateDingDingIdById(dingdingId, id);
    }

    @Override
    public User getUserByDingDingId(String dingDingId) {
        return userDao.getUserByDingDingId(dingDingId);
    }
    
//    @Override
//    public List<User> findByAhUserIds(String[] userAhIds) {
//        if (ArrayUtils.isEmpty(userAhIds)) {
//            return new ArrayList<User>();
//        }
//        List<User> users = new ArrayList<User>();
//        if (userAhIds.length <= 1000) {
//        	users = userDao.findByAhUserIds(userAhIds);
//        } else {
//            int cyc = userAhIds.length / 1000 + (userAhIds.length % 1000 == 0 ? 0 : 1);
//            for (int i = 0; i < cyc; i++) {
//                int max = (i + 1) * 1000;
//                if (max > userAhIds.length)
//                    max = userAhIds.length;
//                List<User> userlist = userDao.findByAhUserIds(userAhIds);
//                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userlist)) {
//                	users.addAll(userlist);
//                }
//            }
//        }
//        if (org.apache.commons.collections.CollectionUtils.isEmpty(users)) {
//        	users = new ArrayList<User>();
//        }
//        return users;
//    }
    
    @Override
    public List<String> findUserIdsListByOwnerIds(String[] ownerIds) {
        if (ArrayUtils.isEmpty(ownerIds)) {
            return new ArrayList<String>();
        }
        List<String> userIds = new ArrayList<String>();
        if (ownerIds.length <= 1000) {
            userIds = userDao.findUserIdsListByOwnerIds(ownerIds);
        } else {
            int cyc = ownerIds.length / 1000 + (ownerIds.length % 1000 == 0 ? 0 : 1);
            for (int i = 0; i < cyc; i++) {
                int max = (i + 1) * 1000;
                if (max > ownerIds.length)
                    max = ownerIds.length;
                List<String> userIdList = userDao.findUserIdsListByOwnerIds(ownerIds);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIdList)) {
                    userIds.addAll(userIdList);
                }
            }
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(userIds)) {
            userIds = new ArrayList<String>();
        }
        return userIds;
    }

    @Override
    public List<User> findUsersListByOwnerIds(String[] ownerIds, boolean needAvatarUrl) {
        if (ArrayUtils.isEmpty(ownerIds)) {
            return new ArrayList<User>();
        }
        List<User> users = new ArrayList<User>();
        if (ownerIds.length <= 1000) {
            users = userDao.findUsersListByOwnerIds(ownerIds);
        } else {
            int cyc = ownerIds.length / 1000 + (ownerIds.length % 1000 == 0 ? 0 : 1);
            for (int i = 0; i < cyc; i++) {
                int max = (i + 1) * 1000;
                if (max > ownerIds.length)
                    max = ownerIds.length;
                List<User> userIdList = userDao.findUsersListByOwnerIds(ArrayUtils.subarray(ownerIds, i * 1000, max));
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(userIdList)) {
                    users.addAll(userIdList);
                }
            }
        }
        if (org.apache.commons.collections.CollectionUtils.isEmpty(users)) {
            users = new ArrayList<User>();
        } else {
            if (needAvatarUrl) {
                for (User user : users) {
                    String avatarUrl = user.getAvatarUrl();
                    if (!Validators.isEmpty(avatarUrl)) {
                        // 微课掌上通这边上传头像，数据库中存完整路径
                        if (avatarUrl.startsWith("http")) {
                            user.setAvatarUrl(avatarUrl);
                        }
                        // 本地存储，数据库中只是file的后半部分
                        else {
                            String fileUrl = sysOptionRemoteService.findValue(Constant.FILE_URL);

                            if (StringUtils.endsWith(fileUrl, "/")) {
                                avatarUrl = fileUrl + "store/" + UrlUtils.ignoreFirstLeftSlash(avatarUrl);
                            } else {
                                avatarUrl = fileUrl + "/store/" + UrlUtils.ignoreFirstLeftSlash(avatarUrl);
                            }
                            user.setAvatarUrl(avatarUrl);
                        }
                    }
                }
            }
        }
        return users;
    }

    @Override
    public List<User> findByModifyTimeGreaterThan(Date date, Integer isDeleted) {
        if (isDeleted == null) {
            return userDao.findByModifyTimeGreaterThan(date);
        }
        return userDao.findByModifyTimeGreaterThanAndIsDeleted(date, isDeleted);
    }

    @Override
    public List<User> findByModifyTimeGreaterThan(final Date date, Integer isDeleted, Pagination page) {
        // TODO Auto-generated method stub
        return findAll(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> ps = Lists.newArrayList();
                if (isDeleted != null) {
                    ps.add(cb.equal(root.get("isDeleted").as(Integer.class), isDeleted));
                }
                ps.add(cb.greaterThan(root.get("modifyTime").as(Date.class), date));
                query.where(ps.toArray(new Predicate[0]));
                return query.getRestriction();
            }
        }, page);
    }

    @Override
    public List<User> findByOwnerTypeAndModifyTimeGreaterThan(int ownerType,
                                                              Date mDate, String... uidList) {
        return userDao.findByOwnerTypeAndModifyTimeGreaterThan(ownerType, mDate, uidList);
    }

    @Override
    public List<User> findByUserNameAndUnitIn(String userName, String[] uidList, Pagination page) {
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> ps = new ArrayList<Predicate>();
                List<Order> orderList = new ArrayList<Order>();
                if (StringUtils.isNotBlank(userName)) {
                    ps.add(cb.like(root.get("username").as(String.class), userName + "%"));
                }
                if (uidList != null) {
                    ps.add(root.<String>get("unitId").in(uidList));
                }
                ps.add(cb.equal(root.<String>get("isDeleted"), "0"));
                cq.where(ps.toArray(new Predicate[0])).orderBy(orderList);
                return cq.getRestriction();
            }
        };
        if (page != null) {
            Pageable pageable = Pagination.toPageable(page);
            Page<User> findAll = userDao.findAll(specification, pageable);
            page.setMaxRowCount((int) findAll.getTotalElements());
            return findAll.getContent();
        } else {
            return userDao.findAll(specification);
        }
    }

    @Override
    public User findByUserIdCode(String userIdCode) {
        return userDao.findByUserIdCode(userIdCode);
    }

    @Override
    public User findByOwnerTypeAndIdentityCard(int ownerType, String identityCard) {
        return userDao.findByOwnerTypeAndIdentityCard(ownerType, identityCard);
    }
//    @Override
//    public User findByAhUserId(String userAhId) {
//    	return userDao.findByAhUserId(userAhId);
//    }
    @Override
    public void deleteUsersByUnitId(String unitId) throws PassportException {
        if (Evn.isPassport()) {
            PassportClientUtils.getPassportClient().deleteAccounts(
                    userDao.findAllByUnitId(unitId).stream().map(User::getAccountId)
                            .filter(StringUtils::isNotBlank).toArray(String[]::new)
            );
        }
        userDao.deleteUsersByUnitId(unitId);
    }

    @Override
    public List<User> findByUnitIdAndLikeRealName(String unitId, String realName) {
        return userDao.findByUnitIdAndLikeRealName(unitId,realName);
    }

	@Override
	public List<User> findByUserIdCodeIn(String[] userIdCodes) {
		return userDao.findByUserIdCodeIn(userIdCodes);
	}

	@Override
	public List<User> findByUnitIdAndOwnerTypeAll(String unitId, int ownerType) {
		return userDao.findByUnitIdAndOwnerTypeAll(unitId,ownerType);
	}
}
