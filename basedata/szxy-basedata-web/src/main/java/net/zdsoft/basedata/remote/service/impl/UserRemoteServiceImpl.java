package net.zdsoft.basedata.remote.service.impl;

import static net.zdsoft.basedata.enums.UserStateCode.NORMAL;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.zdsoft.basedata.dao.DeptDao;
import net.zdsoft.basedata.dao.UnitDao;
import net.zdsoft.basedata.dao.UserDao;
import net.zdsoft.basedata.dto.OpenAccountInfo;
import net.zdsoft.basedata.dto.UserDto;
import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Family;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.enums.UserOwnerTypeCode;
import net.zdsoft.basedata.enums.UserStateCode;
import net.zdsoft.basedata.enums.UserTypeCode;
import net.zdsoft.basedata.remote.AccountAlreadyOpenException;
import net.zdsoft.basedata.remote.UsernameAlreadyExistsException;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.basedata.service.FamilyService;
import net.zdsoft.basedata.service.StudentService;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.framework.entity.Constant;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.entity.TR;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.PWD;
import net.zdsoft.framework.utils.PassportClientUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.framework.utils.UuidUtils;
import net.zdsoft.framework.utils.Validators;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;
import net.zdsoft.system.remote.service.SysOptionRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;


@Service("userRemoteService")
@com.alibaba.dubbo.config.annotation.Service
public class UserRemoteServiceImpl extends BaseRemoteServiceImpl<User, String> implements UserRemoteService {

    @Autowired
    private UserService userService;
    @Autowired
    private SysOptionRemoteService sysOptionRemoteService;
    @Autowired
    private UnitDao unitDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private FamilyService familyService;
    @Autowired
    private TeacherService teacherService;


    @Override
    protected BaseService<User, String> getBaseService() {
        return userService;
    }

    @Override
    public String findByUsername(String username) {
        return SUtils.s(userService.findByUsername(username));
    }

    @Override
    public String findByMobilePhones(Integer ownerType, String... mobilePhones) {
        return SUtils.s(userDao.findByMobilephones(ownerType, mobilePhones));
    }

    @Override
    public String findByMobilePhones(String... mobilePhones) {
        return SUtils.s(userDao.findByIsDeletedIsAndMobilePhoneIn(0,mobilePhones));
    }

    @Override
    public String findAdmin(String unitId) {
        return SUtils.s(userDao.findAdmin(unitId));
    }

    @Override
    public String findByAccountId(String accountId) {
        return SUtils.s(userDao.findByAccountId(accountId));
    }

    @Override
    public String findByUserTypes(Integer... userTypes) {
        return SUtils.s(userDao.findByUserTypes(userTypes));
    }

    @Override
    public String findByOwnerIds(String[] ownerIds) {
        return SUtils.s(userDao.findByOwnerIds(ownerIds));
    }
    
    @Override
    public String findByOwnerId(String ownerId) {
    	return SUtils.s(userDao.findByOwnerId(ownerId));
    }
    
    @Override
    public String findByOwnerIds(String[] ownerIds, String page) {
        List<User> users = userDao.findByOwnerIds(ownerIds, Pagination.toPageable(page));
        long count = userDao.count(new Specifications<User>().addIn("ownerId", ownerIds, String.class)
                .getSpecification(true));
        return SUtils.s(users, count);
    }

    @Override
    public Long findMaxDisplayOrder(String unitId) {
        return userDao.findMaxDisplayOrder(unitId);
    }

    @Override
    public String findByOwnerType(String[] unitIds, Integer ownerType) {
        return SUtils.s(userDao.findByOwnerType(unitIds, ownerType));
    }

    @Override
    public String findByOwnerType(final String[] unitIds, final Integer ownerType, String page) {
        List<User> users = userDao.findByOwnerType(unitIds, ownerType, Pagination.toPageable(page));
        long count = userDao.count(new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> r, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                return cq.where(
                        cb.and(r.get("unitId").as(String.class).in(unitIds),
                                cb.equal(r.get("ownerType").as(Integer.class), ownerType))).getRestriction();
            }
        });
        return SUtils.s(users, count);
    }

    @Override
    public String findByUnitIdsWithDeleted(String... unitIds) {
        return SUtils.s(userDao.findByUnitIdsWithDeleted(unitIds));
    }

    @Override
    public String findByUnitIds(String... unitIds) {
        return SUtils.s(userDao.findByUnitIds(unitIds));
    }

    @Override
    public String findAdmins(String... unitIds) {
        return SUtils.s(userDao.findAdmins(unitIds));
    }

    @Override
    public String findByUsernames(String... usernames) {
        return SUtils.s(userDao.findByUsernames(usernames));
    }

    @Override
    public String saveAllEntitys(String entitys) {
        User[] dt = SUtils.dt(entitys, new TR<User[]>() {
        });
        return SUtils.s(userService.saveAllEntitys(dt));
    }
    
    public void saveUser(User user) {
    	try {
			userService.saveUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
    }

    @Override
    public String findByDeptId(String deptId, String leader) {
        if (leader != null && "1".equals(leader)) {
            return SUtils.s(userService.findLeaderByDept(deptId, true));
        }
        else {
            return SUtils.s(userService.findByDeptId(deptId));
        }
    }

    @Override
    public String findByRealName(String realName, String pageIndex) {
        List<UserDto> userDtoList = new ArrayList<UserDto>();
        List<User> userList = userService.findByRealName(realName, pageIndex);
        if (userList != null) {
            List<String> deptIds = EntityUtils.getList(userList, "deptId");
            List<String> unitIds = EntityUtils.getList(userList, "unitId");
            List<Dept> deptList = deptDao.findAllById(deptIds);
            List<Unit> unitList = unitDao.findAllById(unitIds);
            Map<String, Dept> deptMap = EntityUtils.getMap(deptList, "id");
            Map<String, Unit> unitMap = EntityUtils.getMap(unitList, "id");
            String servername = Evn.getRequest() == null ? null : Evn.getRequest().getServerName();
            String fileUrl = sysOptionRemoteService.getFileUrl(servername);
            for (User user : userList) {
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setRealName(user.getRealName());
                userDto.setDeptName(deptMap.get(user.getDeptId()) == null ? "" : deptMap.get(user.getDeptId())
                        .getDeptName());
                userDto.setUnitName(unitMap.get(user.getUnitId()) == null ? "" : unitMap.get(user.getUnitId())
                        .getUnitName());
                String avatarUrl = user.getAvatarUrl();
                if (!Validators.isEmpty(avatarUrl)) {
                    // 微课掌上通这边上传头像，数据库中存完整路径
                    if (!avatarUrl.startsWith("http")) {
                        // 本地存储，数据库中只是file的后半部分
                        userDto.setAvatarUrl(fileUrl + avatarUrl);
                    }
                    else {
                        userDto.setAvatarUrl(avatarUrl);
                    }
                }
                userDtoList.add(userDto);
            }
        }

        return SUtils.s(userDtoList);
    }
    
    @Override
    public String findByRealName(String realName) {
        List<User> userList = userService.findByRealName(realName);
        return SUtils.s(userList);
    }

    @Override
    public String findByUnitId(String unitId, String page) {
        Pagination pagination = SUtils.dc(page, Pagination.class);
        List<User> users = userService.findByUnitId(unitId, pagination);
        return SUtils.s(users, (long) pagination.getMaxRowCount());
    }

    @Override
    public String findByUnitIdAndRealNameAndOwnerTypeAndUserType(String unitId, String realName, Integer ownerType,
            Integer[] userTypes, String page) {
//        Pagination pagination = SUtils.dc(page, Pagination.class);
//        List<User> userList = userService.findByUnitIdAndRealNameAndOwnerTypeAndUserTypes(unitId, realName, ownerType,
//                userTypes, pagination);
//        return SUtils.s(userList, pagination != null ? (long) pagination.getMaxRowCount() : 0);
    	return findByUnitIdAndRealNameAndOwnerTypeAndUserTypeAndUserName(unitId, realName, ownerType,
              userTypes,null, page);
    }


	@Override
	public String findByUnitIdAndRealNameAndOwnerTypeAndUserTypeAndUserName(
			String unitId, String realName, Integer ownerType,
			Integer[] userTypes, String userName, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<User> userList = userService.findByUnitIdAndRealNameAndOwnerTypeAndUserTypesAndUserName(unitId, realName, ownerType,
                userTypes,userName,pagination);
        return SUtils.s(userList, pagination != null ? (long) pagination.getMaxRowCount() : 0);
	}
    
    
    
    @Override
    public void updatePasswordByUsername(String username, String password) {
        User user = userService.findByUsername(username);
        userService.updatePasswordByUsername(username, password, user!=null?user.getId():"");
    }

    @Override
    public void updateStateByUsername(int state, String username) {
        User user = userService.findByUsername(username);
        userService.updateStateByUsername(username, state, user!=null?user.getId():"");
    }

    @Override
    public void initTopAdmin(String userJson) {
        User topAdmin = SUtils.dc(userJson, User.class);
        userService.addTopAdmin(topAdmin);
    }

    @Override
    public void updatePasswordByAccountId(String password, Date modifyTime, String accountId) throws Exception {
        userService.updatePasswordByAccountId(password, modifyTime, accountId);
    }

    @Override
    public void updatePhoneById(String id, String mobilePhone, Date modifyTime, String ownerId, int ownerType,
            String accountId) throws Exception {
        userService.updatePhoneById(id, mobilePhone, modifyTime, ownerId, ownerType, accountId);
    }

    @Override
    public void updateAvatarUrlById(String avatarUrl, Date modifyTime, String id) {
        userService.updateAvatarUrlById(avatarUrl, modifyTime, id);
    }

    @Override
    public Integer countUserNum(String[] unitIds, Integer[] userTypes) {
        return userService.getUsersByUnitIdsOrUserTypes(unitIds, userTypes).size();
    }

    @Override
    public String findByRealNameAndIdentityCard(String realName, String identityCard) {
        return SUtils.s(userService.findByRealNameAndIdentityCard(realName,identityCard));
    }

    @Override
    public String findByMobilePhoneAndPasswordIn(String mobilePhone, String[] password) {
        return SUtils.s(userService.findByMobilePhoneAndPasswordIn(mobilePhone, password));
    }
    
	@Override
	public boolean updateUser(User user) {
		if (user == null) {
			return Boolean.FALSE;
		}
		userService.update(user, user.getId(), new String[] { "ownerId", "sex",
				"unitId", "classId", "deptId", "birthday" });
		return Boolean.TRUE;
	}
	
	@Override
	public boolean updateUserDeleted(User user) {
		if (user == null) {
			return Boolean.FALSE;
		}
		userService.update(user, user.getId(), new String[] { "isDeleted" });
		return Boolean.TRUE;
	}
	
	public void deleteAllByIds(String... id) {
		userService.deleteAllByIds(id);
	}
	
    @Override
    public boolean updateUser2(String userId, String realName, Integer sex, String mobilePhone, Date birthday,
    		Teacher teacher) {
        User user = userService.findOne(userId);
        if ( userId == null ) {
            return Boolean.FALSE;
        }
        user.setRealName(realName);
        user.setSex(sex);
        user.setMobilePhone(mobilePhone);
        user.setBirthday(birthday);
        userService.update(user, userId, new String[]{"realName", "sex", "mobilePhone", "birthday"});
        if ( user.getOwnerType() == null ) {
            return Boolean.TRUE;
        }
        switch ( user.getOwnerType() ) {
            case User.OWNER_TYPE_FAMILY:
                Family family = new Family();
                family.setRealName(realName);
                family.setSex(sex);
                family.setMobilePhone(mobilePhone);
                family.setBirthday(birthday);
                familyService.update(family, user.getOwnerId(), new String[]{"realName", "sex", "mobilePhone", "birthday"});
                break;
            case User.OWNER_TYPE_TEACHER:
                teacher.setTeacherName(realName);
                teacher.setSex(sex);
                teacher.setMobilePhone(mobilePhone);
                teacher.setBirthday(birthday);
                teacherService.update(teacher, user.getOwnerId(), new String[]{"teacherName", "sex", "mobilePhone", "birthday","nation","polity","identityCard"});
                break;
            case User.OWNER_TYPE_STUDENT:
                Student student = new Student();
                student.setStudentName(realName);
                student.setSex(sex);
                student.setMobilePhone(mobilePhone);
                student.setBirthday(birthday);
                studentService.update(student, user.getOwnerId(), new String[]{"studentName", "sex", "mobilePhone", "birthday"});
                break;
            default:;
        }
        return Boolean.TRUE;
    }
    @Override
    public boolean updateUser(String userId, String realName, Integer sex, String mobilePhone, Date birthday) {
        User user = userService.findOne(userId);
        if ( userId == null ) {
            return Boolean.FALSE;
        }
        user.setRealName(realName);
        user.setSex(sex);
        user.setMobilePhone(mobilePhone);
        user.setBirthday(birthday);
        userService.update(user, userId, new String[]{"realName", "sex", "mobilePhone", "birthday"});
        if ( user.getOwnerType() == null ) {
            return Boolean.TRUE;
        }
        switch ( user.getOwnerType() ) {
            case User.OWNER_TYPE_FAMILY:
                Family family = new Family();
                family.setRealName(realName);
                family.setSex(sex);
                family.setMobilePhone(mobilePhone);
                family.setBirthday(birthday);
                familyService.update(family, user.getOwnerId(), new String[]{"realName", "sex", "mobilePhone", "birthday"});
                break;
            case User.OWNER_TYPE_TEACHER:
                Teacher teacher = new Teacher();
                teacher.setTeacherName(realName);
                teacher.setSex(sex);
                teacher.setMobilePhone(mobilePhone);
                teacher.setBirthday(birthday);
                teacherService.update(teacher, user.getOwnerId(), new String[]{"teacherName", "sex", "mobilePhone", "birthday"});
                break;
            case User.OWNER_TYPE_STUDENT:
                Student student = new Student();
                student.setStudentName(realName);
                student.setSex(sex);
                student.setMobilePhone(mobilePhone);
                student.setBirthday(birthday);
                studentService.update(student, user.getOwnerId(), new String[]{"studentName", "sex", "mobilePhone", "birthday"});
                break;
            default:;
        }
        return Boolean.TRUE;
    }

    @Override
    public String findTopAdmin() {
        List<User> userList = userService.findListBy("userType",User.USER_TYPE_TOP_ADMIN);
        return SUtils.s(userList != null && userList.size()>0 ? userList.get(0) : "" );
    }

	@Override
	public String findByOwnerType(Integer ownerType) {
		// TODO Auto-generated method stub
		return SUtils.s(userDao.findByOwnerType(ownerType));
	}

    @Override
    public String getUserByDingDingId(String dingDingId) {
        return SUtils.s(userService.getUserByDingDingId(dingDingId));
    }

	@Override
	public String findUserIdsByOwnerIds(String[] ownerIds) {
		 return SUtils.s(userService.findUserIdsListByOwnerIds(ownerIds));
	}

	@Override
	public void updatePassportPasswordByUsername(String username,
			String password) throws PassportException {
		
		User user = userService.findByUsername(username);
		userService.updatePasswordByUsername(username, password, user!=null?user.getId():"");
		
		if (Evn.isPassport()) {
            Account account = PassportClientUtils.getPassportClient().queryAccountByUsername(username);
            if ( account != null) {
            	account.setPassword(password);
                PassportClientUtils.getPassportClient().modifyAccount(account, new String[]{"password"});
            }
        }
	}

	@Override
	public String findByUnitIds(String[] unitIds, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<User> userList = userService.findByUnitIds(unitIds, pagination);
		return SUtils.s(userList, pagination != null ? (long) pagination.getMaxRowCount() : 0);
	}

	@Override
	public String findByModifyTimeGreaterThan(Date date,Integer isDeleted) {
		return SUtils.s(userService.findByModifyTimeGreaterThan(date,isDeleted));
	}
	
	@Override
	public String findByModifyTimeGreaterThan(Date date,Integer isDeleted, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<User> userList = userService.findByModifyTimeGreaterThan(date,isDeleted,pagination);
		return SUtils.s(userList, pagination != null ? (long) pagination.getMaxRowCount() : 0);
	}

	@Override
	public String findByOwnerTypeAndModifyTimeGreaterThan(int ownerType,
			Date mDate, String... uidList) {
		return SUtils.s(userService.findByOwnerTypeAndModifyTimeGreaterThan(ownerType,mDate,uidList));
	}

	@Override
	public String findByUserNameAndUnitIn(String userName,
			Set<String> unitIdList, String page) {
		Pagination pagination = SUtils.dc(page, Pagination.class);
		List<User> userList;
		if(CollectionUtils.isNotEmpty(unitIdList)){
			userList = userService.findByUserNameAndUnitIn(userName,unitIdList.toArray(new String[unitIdList.size()]),pagination);
		}else {
			userList = userService.findByUserNameAndUnitIn(userName,null,pagination);
		}
		return SUtils.s(userList, pagination != null ? (long) pagination.getMaxRowCount() : 0);
	}

	@Override
	public String findByUserIdCode(String userIdCode) {
		return SUtils.s(userService.findByUserIdCode(userIdCode));
	}

	@Override
	public String findByOwnerTypeAndIdentityCard(int ownerType,
			String identityCard) {
		return SUtils.s(userService.findByOwnerTypeAndIdentityCard(ownerType,identityCard));
	}

	private Logger logger = LoggerFactory.getLogger(UserRemoteServiceImpl.class);
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void batchUpdatePassword(String[] userIds, String newPassword)  {

        try {
            if (Evn.isPassport()) {
                List<User> users =  userService.findListByIdIn(userIds);
                Account[] accounts = users.stream().map(u->{
                    Account account = new Account();
                    account.setPassword(PWD.encodeIfNot(newPassword));
                    account.setId(u.getAccountId());
                    return account;
                }).toArray(Account[]::new);

                PassportClientUtils.getPassportClient().modifyAccounts(accounts, new String[]{"password"});
            }
        } catch (PassportException e) {
            logger.error("批量修改密码失败", e);
            throw new RuntimeException("同步修改Passport密码失败");
        }
        //
        userDao.restPassword(userIds, PWD.encodeIfNot(newPassword));
    }

    @Override
    public void openUserAccount(List<OpenAccountInfo> accountInfoList) throws AccountAlreadyOpenException, UsernameAlreadyExistsException {

        List<User> openUsers = new ArrayList<>(accountInfoList.size());

        List<OpenAccountInfo> studentAccounts = accountInfoList.stream()
                .filter(e->UserOwnerTypeCode.STUDENT.equals(e.getOwnerType())).collect(Collectors.toList());
        if (!studentAccounts.isEmpty()) {
            openUsers.addAll(openStudentAccount(studentAccounts));
        }
        List<OpenAccountInfo> familyAccounts = accountInfoList.stream()
                .filter(e->UserOwnerTypeCode.FAMILY.equals(e.getOwnerType())).collect(Collectors.toList());
        if (!familyAccounts.isEmpty()) {
            openUsers.addAll(openFamilyAccount(familyAccounts));
        }
        try {
            if (Evn.isPassport()) {
                List<Account> accounts = syncOpenPassportAccount(openUsers);
                Account[] reAccounts = PassportClientUtils.getPassportClient().addAccounts(accounts.toArray(new Account[0]));
                Map<String,Account> accMap = Maps.newHashMap();
                for(Account account:reAccounts){
                	accMap.put(account.getId(), account);
                }
                for(User user:openUsers){
                	if(accMap.containsKey(user.getAccountId())){
                		Account a = accMap.get(user.getAccountId());
                		if(a!=null){
                			user.setSequence(a.getSequence());
                		}
                	}
                }
            }
        } catch (PassportException e) {
            throw new RuntimeException("Sync passport error");
        }
        userDao.saveAll(openUsers);
    }

    private List<User> openStudentAccount(List<OpenAccountInfo> accountInfos) throws AccountAlreadyOpenException, UsernameAlreadyExistsException {
        String[] studentIds = accountInfos.stream().map(OpenAccountInfo::getId).toArray(String[]::new);
        List<Student> students = studentService.findListByIdIn(studentIds);
        Map<String, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, Function.identity()));
        List<User> users = userDao.findByOwnerIds(studentIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getOwnerId, Function.identity()));

        List<User> studentUsers = new ArrayList<>(studentIds.length);
        for (OpenAccountInfo accountInfo : accountInfos) {
            checkAccount(userMap, accountInfo);
            Student student = studentMap.get(accountInfo.getId());
            if (student == null) {
                throw new RuntimeException(String.format("学生[%s]不存在", accountInfo.getId()));
            }
            User user = new User();
            user.setUsername(accountInfo.getUsername());
            user.setPassword(PWD.encodeIfNot(accountInfo.getPassword()));
            user.setId(UuidUtils.generateUuid());
            user.setOwnerId(accountInfo.getId());
            user.setRealName(student.getStudentName());
            user.setIsDeleted(0);
            user.setOwnerType(UserOwnerTypeCode.STUDENT);
            user.setRegionCode(student.getRegionCode());
            user.setUnitId(student.getSchoolId());
            user.setUserType(UserTypeCode.COMMON_USER);
            user.setClassId(student.getClassId());
            user.setSex(student.getSex());
            user.setIdentityCard(student.getIdentityCard());
            user.setUserState(NORMAL);
            user.setBirthday(student.getBirthday());
            user.setCreationTime(new Date());
            user.setModifyTime(user.getCreationTime());
            user.setAccountId(UuidUtils.generateUuid());
            user.setEventSource(0);

            user.setUserRole(2);
            user.setIconIndex(0);
            user.setEnrollYear(0);
            studentUsers.add(user);
        }
        return studentUsers;
    }

    private List<User> openFamilyAccount(List<OpenAccountInfo> accountInfos) throws UsernameAlreadyExistsException, AccountAlreadyOpenException {
        String[] familyIds = accountInfos.stream().map(OpenAccountInfo::getId).toArray(String[]::new);
        List<Family> families = familyService.findListByIds(familyIds);
        Map<String, Family> familyMap = families.stream().collect(Collectors.toMap(Family::getId, Function.identity()));
        List<User> users = userDao.findByOwnerIds(familyIds);
        Map<String, User> userMap = users.stream().collect(Collectors.toMap(User::getOwnerId, Function.identity()));

        List<User> familyUsers = new ArrayList<>(familyIds.length);
        for (OpenAccountInfo accountInfo : accountInfos) {
            checkAccount(userMap, accountInfo);
            Family family = familyMap.get(accountInfo.getId());
            User user = new User();
            user.setUsername(accountInfo.getUsername());
            user.setPassword(PWD.encodeIfNot(accountInfo.getPassword()));
            user.setId(UuidUtils.generateUuid());
            user.setOwnerType(UserOwnerTypeCode.FAMILY);
            user.setCreationTime(new Date());
            user.setModifyTime(user.getCreationTime());
            user.setOwnerId(family.getId());
            user.setUnitId(family.getSchoolId());
            user.setSex(family.getSex());
            user.setIsDeleted(0);
            user.setUserType(UserTypeCode.COMMON_USER);
            user.setUserState(UserStateCode.NORMAL);
            user.setRegionCode(family.getRegionCode());
            user.setIdentityCard(family.getIdentityCard());
            user.setMobilePhone(family.getMobilePhone());
            user.setBirthday(family.getBirthday());
            user.setRealName(family.getRealName());
            user.setAccountId(UuidUtils.generateUuid());
            user.setEmail(family.getEmail());
            user.setEventSource(0);
            //抱歉，这三个字段我也不知道是什么意思
            user.setUserRole(2);
            user.setIconIndex(0);
            user.setEnrollYear(0);
            familyUsers.add(user);
        }
        return familyUsers;
    }

    private List<Account> syncOpenPassportAccount(List<User> users) {
        List<Account> accounts = new ArrayList<>(users.size());
        for (User user : users) {
        	try {
             	Account acc = PassportClientUtils.getPassportClient().queryAccountByUsername(user.getUsername());
             	if(acc!=null){
             		PassportClientUtils.getPassportClient().deleteAccounts(new String[]{acc.getId()});
             	}
 			} catch (PassportException e) {
 				e.printStackTrace();
 			}
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
            account.setState(user.getUserState());
            if (user.getSex() != null) {
                account.setSex(user.getSex());
            }
            accounts.add(account);
        }
        return accounts;
    }

    private void checkAccount(Map<String, User> userMap, OpenAccountInfo accountInfo) throws AccountAlreadyOpenException, UsernameAlreadyExistsException {
        User user = userMap.get(accountInfo.getId());
        if (user != null) {
            throw new AccountAlreadyOpenException("账号已经开通", accountInfo.getId());
        }
        user = userService.findByUsername(accountInfo.getUsername());
        if (user != null) {
            throw new UsernameAlreadyExistsException("用户名已存在", accountInfo.getUsername());
        }
    }

    @Override
    public Boolean existsUsername(String username) {
        if (Evn.isPassport()) {
            try {
                Account account = PassportClientUtils.getPassportClient().queryAccountByUsername(username);
                if (account != null) {
                    return Boolean.FALSE;
                }
            } catch (PassportException e) {
                throw new RuntimeException("用户名校验Passport查询失败");
            }
        }
        User user = userService.findByUsername(username);
        return user == null;
    }
   
	@Override
	public String findbyIdentityCard(String identityCard) {
		return SUtils.s(userDao.findbyIdentityCard(identityCard));
	}
	
	@Override
	public String openeUserdForMobile(String mobile, int state) {
		List<Family> familys = familyService.findByPhoneNum(mobile);
		if(CollectionUtils.isEmpty(familys)){
			return "2";
		}
		Set<String> ownerIds = EntityUtils.getSet(familys, Family::getId);
		Set<String> stuIds = EntityUtils.getSet(familys, Family::getStudentId);
		List<Student> stus = studentService.findListByIds(stuIds.toArray(new String[stuIds.size()]));
		stus = stus.stream().filter(it -> it.getIsDeleted()==0)
				.collect(Collectors.toList());
		ownerIds.addAll(EntityUtils.getSet(stus, Student::getId));
		List<User> users = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(ownerIds)){
			users = userDao.findByOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
		}
		Set<String> existOwnIds = EntityUtils.getSet(users, User::getOwnerId);
		if(5==state){
			//多家长的学生，判断是否需要删除
			Set<String> notDelStuIds = checkStuUserDel(stus,ownerIds);
			users = users.stream().filter(it -> !notDelStuIds.contains(it.getOwnerId()))
					.collect(Collectors.toList());
			if(CollectionUtils.isNotEmpty(users)&&!delUser(users)){
				return "1";
			}
		}else{
			Set<String> usernames = EntityUtils.getSet(users, User::getUsername);
			//开通账号
			stus = stus.stream().filter(it -> !existOwnIds.contains(it.getId()))
					.collect(Collectors.toList());
			familys = familys.stream().filter(it -> !existOwnIds.contains(it.getId()))
					.collect(Collectors.toList());
			try {
				openUser(stus,familys,mobile,usernames);
			} catch (Exception e) {
				e.printStackTrace();
				return "1";
			}
		}
		return "0";
	}

    @Override
    public String findByUnitIdAndLikeRealName(String unitId,String realName) {
        return SUtils.s(userService.findByUnitIdAndLikeRealName(unitId,realName));
    }

//    @Override
//    public String findByAhUserId(String userAhId) {
//    	return SUtils.s(userService.findByAhUserId(userAhId));
//    }
//    
//    @Override
//    public String findByAhUserIds(String[] userAhIds) {
//    	return SUtils.s(userService.findByAhUserIds(userAhIds));
//    }
    
    private Set<String> checkStuUserDel(List<Student> stus, Set<String> ownerIds) {
		Set<String> fIds = Sets.newHashSet();
		Set<String> sIds = Sets.newHashSet();
		Set<String> stuIds =EntityUtils.getSet(stus, Student::getId);
		ownerIds.removeAll(stuIds);
		if(CollectionUtils.isNotEmpty(stuIds)){
			List<Family> familys = familyService.findByStudentIds(stuIds.toArray(new String[0]));
			for(Family f:familys){
				if(!ownerIds.contains(f.getId())){
					fIds.add(f.getId());
				}
			}
			if(CollectionUtils.isNotEmpty(fIds)){
				List<User> users = userDao.findByOwnerIds(fIds.toArray(new String[fIds.size()]));
				Set<String> ufIds =EntityUtils.getSet(users, User::getOwnerId);
				fIds = ufIds;
			}
			for(Family f:familys){
				if(fIds!=null && fIds.contains(f.getId())){
					sIds.add(f.getStudentId());
				}
			}
		}
		return sIds;
	}

	private void openUser(List<Student> stus, List<Family> familys,String mobile,Set<String> usernames) throws UsernameAlreadyExistsException, AccountAlreadyOpenException {
		List<OpenAccountInfo> accountInfos = Lists.newArrayList();
		String password = "p"+mobile.substring(mobile.length()-6,mobile.length());
		int index = 0;
		for(Student stu:stus){
			String username = "s"+mobile+index;
			while(usernames.contains(username)){
				index++;
				username = "s"+mobile+index;
				if(index>100){//避免死循环
					break;
				}
			}
			index++;
			OpenAccountInfo info = new OpenAccountInfo();
			info.setId(stu.getId());
			info.setOwnerType(User.OWNER_TYPE_STUDENT);
			info.setPassword(password);
			info.setUsername(username);
			accountInfos.add(info);
		}
		index = 0;
		for(Family fml:familys){
			String username = "f"+mobile+index;
			while(usernames.contains(username)){
				index++;
				username = "f"+mobile+index;
				if(index>100){
					break;
				}
			}
			index++;
			OpenAccountInfo info = new OpenAccountInfo();
			info.setId(fml.getId());
			info.setOwnerType(User.OWNER_TYPE_FAMILY);
			info.setPassword(password);
			info.setUsername(username);
			accountInfos.add(info);
			
		}
		if(CollectionUtils.isNotEmpty(accountInfos)){
			openUserAccount(accountInfos);
		}
	}

	private boolean delUser(List<User> users) {
		for(User user:users){
			user.setIsDeleted(Constant.IS_DELETED_TRUE);
			user.setModifyTime(new Date());
		}
		try {
			userService.saveUsers(users);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
	}

	@Override
	public void saveUser(User user, Student student, Account account) throws Exception{
		if(student!=null){
			studentService.save(student);
		}
		if(user!=null){
			userService.saveUser(user);
		}
		
		if(account!=null){
			PassportClientUtils.getPassportClient().addValidAccounts(
					new Account[] {account });
		}
	}

	@Override
	public String findByUserIdCodeIn(String[] userIdCodes) {
		return SUtils.s(userService.findByUserIdCodeIn(userIdCodes));
	}

	@Override
	public String findByUnitIdAndOwnerTypeAll(String unitId,
			int ownerType) {
		return SUtils.s(userService.findByUnitIdAndOwnerTypeAll(unitId,ownerType));
	}
	
}
