package net.zdsoft.basedata.remote.service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import net.zdsoft.basedata.dto.OpenAccountInfo;
import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.AccountAlreadyOpenException;
import net.zdsoft.basedata.remote.UsernameAlreadyExistsException;
import net.zdsoft.passport.entity.Account;
import net.zdsoft.passport.exception.PassportException;

public interface UserRemoteService extends BaseRemoteService<User, String> {

    /**
     * 获取用户信息
     * 
     * @param username
     * @return User
     */
    public String findByUsername(String username);

    public String findByUsernames(String... usernames);

    public String findByMobilePhones(Integer ownerType, String... mobilePhones);

    public String findByMobilePhones(String... mobilePhones);

    public String findByOwnerType(String[] unitIds, Integer ownerType);

    public String findByUnitIds(String... unitIds);
    
    public String findByUnitIds(String[] unitIds,String page);

    public String findByUnitIdsWithDeleted(String... unitIds);

    public String findByOwnerType(String[] unitIds, Integer ownerType, String page);

    public String findAdmin(String unitId);

    public String findAdmins(String... unitIds);

    public String findByAccountId(String accountId);

    public String findByUserTypes(Integer... userTypes);

    public String findByOwnerIds(String[] ownerIds);
    
    public String findByOwnerId(String ownerId);
    
    public String findByOwnerIds(String[] ownerIds, String page);

    public Long findMaxDisplayOrder(String unitId);

    /**
     * 数组形式entitys参数，返回list的json数据
     * 
     * @param entitys
     * @return
     */
    public String saveAllEntitys(String entitys);
    
    /**
     * 新增用户
     * @param user
     */
    public void saveUser(User user);
    
    /**
     * 新增用户
     * @param user
     */
    public void saveUser(User user,Student student,Account account)throws Exception;

    /**
     * 查询部门下的用户
     * 
     * @param deptId
     * @param leader
     *            1为领导
     * @return
     */
    String findByDeptId(String deptId, String leader);

    /**
     * 根据姓名半模糊查询
     * 
     * @param realName
     * @param pageIndex
     * @return
     */
    String findByRealName(String realName, String pageIndex);
    
    /**
     * 根据姓名查询
     * 
     * @param realName
     * @return
     */
    String findByRealName(String realName);

    /**
     * @param unitId
     * @param page
     *            分页参数
     */
    String findByUnitId(String unitId, String page);

    /**
     * 查找某单位下的某身份正常状态用户，可根据用户姓名查找
     * 
     * @author cuimq
     * @param unitId
     * @param realName
     *            可选
     * @param ownerType
     * @param page
     * @return
     */
    String findByUnitIdAndRealNameAndOwnerTypeAndUserType(String unitId, String realName, Integer ownerType,
            Integer[] userTypes, String page);
    
    
    String findByUnitIdAndRealNameAndOwnerTypeAndUserTypeAndUserName(
			String unitId, String realName, Integer integer,
			Integer[] integers, String userName, String page);

    /**
     * 更新用户密码
     * 建议调用这个updatePassportPasswordByUsername 同一个是事物
     * @author dingw
     * @param username
     * @param password
     */
    void updatePasswordByUsername(String username, String password);
    /**
     * 更新用户密码
     * 同步更新passprot
     * @author dingw
     * @param username
     * @param password
     */
    void updatePassportPasswordByUsername(String username, String password) throws PassportException ;

    /**
     * 更新用户状态
     * 
     * @author dingw
     * @param username
     */
    void updateStateByUsername(int state, String username);

    /**
     * 初始化顶级管理员账号
     * 
     * @author dingw
     * @param user
     *            (JSON(User))
     */
    void initTopAdmin(String userJson);

    // void update(String id, String[] properties);

    /**
     * 根据AccountId更新密码
     * 
     * @param password
     * @param modifyTime
     * @param accountId
     */
    void updatePasswordByAccountId(String password, Date modifyTime, String accountId) throws Exception;

    /**
     * 修改用户、基本信息、passport中的手机号
     * 
     * @param id
     * @param mobilePhone
     * @param modifyTime
     * @param ownerId
     * @param ownerType
     * @param accountId
     * @throws Exception
     */
    void updatePhoneById(String id, String mobilePhone, Date modifyTime, String ownerId, int ownerType, String accountId)
            throws Exception;

    /**
     * 修改头像
     * 
     * @param avatarUrl
     * @param modifyTime
     * @param id
     */
    void updateAvatarUrlById(String avatarUrl, Date modifyTime, String id);

    Integer countUserNum(String[] unitIds, Integer[] userTypes);

    String findByRealNameAndIdentityCard(String realName, String identityCard);

    /**
     *
     * @param mobilePhone
     * @param password encode password or dencode password
     * @return
     */
    String findByMobilePhoneAndPasswordIn(String mobilePhone, String[] password);

    /**
     * 更新User，同步更新base_teacher he base_family base_student
     * @return
     */
    boolean updateUser(String userId, String realName, Integer sex, String mobilePhone, Date birthday);
    
    /**
     * 更新User，同步更新base_teacher he base_family base_student
     * @return
     */
    boolean updateUser2(String userId, String realName, Integer sex, String mobilePhone, Date birthday,Teacher teacher);

    String findTopAdmin();
    /**
     * 更新User,更新字段 ownerId，unitId,classId,deptId，birthday
     * @return
     */
	boolean updateUser(User user);
	
	 /**
     * 更新User,更新字段 isDeleted
     * @return
     */
	boolean updateUserDeleted(User user);
	
	public void deleteAllByIds(String... id);

	/**
	 * @param ownerTypeStudent
	 * @return
	 */
	public String findByOwnerType(Integer ownerType);

    /**
     * 根据钉钉Id获取数据
     * @see User
     * @param dingDingId 钉钉Id
     * @return
     */
	String getUserByDingDingId(String dingDingId);
	/**
	 * 根据ownerIds获取用户id
	 * @param ownerIds
	 * @return List<String>
	 */
	public String findUserIdsByOwnerIds(String[] ownerIds);

	/**
	 * 根据modifyTime获取所有用户
	 * @param modifyTime,isDeleted
	 * @return
	 */
	public String findByModifyTimeGreaterThan(Date date,Integer isDeleted);

	String findByModifyTimeGreaterThan(Date date, Integer isDeleted, String page);

	/**
	 * 根据更新时间和单位，获取对应的学生（家长或教师）的数据
	 * @param ownerType
	 * @param mDate
	 * @param uidList
	 * @return
	 */
	public String findByOwnerTypeAndModifyTimeGreaterThan(int ownerType,
			Date mDate, String... uidList);

	public String findByUserNameAndUnitIn(String userName,
			Set<String> unitIdList ,String page);

	public String findByUserIdCode(String userIdCode);

	public String findByOwnerTypeAndIdentityCard(int parseInt,
			String identityCard);

    /**
     * 批量修改用户密码
     * @param userIds
     * @param newPassword
     */
    void batchUpdatePassword(String[] userIds, String newPassword) ;

    /**
     * 批量开通账号
     * @param accountInfoList
     */
    void openUserAccount(List<OpenAccountInfo> accountInfoList) throws AccountAlreadyOpenException, UsernameAlreadyExistsException;

    /**
     * 验证用户名是否存在， 会校验passport，如果对接passport的情况下
     * @param username 用户名
     * @return
     */
    Boolean existsUsername(String username);


	public String findbyIdentityCard(String identityCard);

	 /**
     * 根据手机号开通账号（家长，学生），或改变用户状态
     * @param mobile
     * @param state
     * @return  0操作成功，1操作失败
     */
	public String openeUserdForMobile(String mobile,int state);

    /**
     * 模糊查询 单位下的 用户
     * @param unitId
     * @param realName
     * @return
     */
    String findByUnitIdAndLikeRealName(String unitId, String realName);
    /**
     * 安徽对接专用
     * @param userAhId
     * @return
     */
//	public String findByAhUserId(String userAhId);
	/**
     * 安徽对接专用
     * @param userAhId
     * @return
     */
//	public String findByAhUserIds(String[] userAhIds);


    
	public String findByUserIdCodeIn(String[] userIdCodes);

	/**
	 * 得到所有的用户数据 包含 软删的 
	 * @param unitId
	 * @param ownerTypeTeacher
	 * @return
	 */
	public String findByUnitIdAndOwnerTypeAll(String unitId,int ownerTypeTeacher);

}
