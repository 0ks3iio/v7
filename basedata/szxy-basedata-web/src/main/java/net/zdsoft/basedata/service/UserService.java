package net.zdsoft.basedata.service;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.passport.exception.PassportException;

import java.util.Date;
import java.util.List;

public interface UserService extends BaseService<User, String> {

    User findByUsername(String username);


    User findByOwnerId(String ownerId);

    List<User> findByDeptId(String deptId);

    List<User> findByUnitId(String unitId);

    List<User> findByUnitId(String unitId, Pagination page);

    List<User> saveAllEntitys(User... user);

    void deleteAllByIds(String... id);

    List<User> findByUnitDutyCode(String unitId, String dutyCode);

    /**
     * 根据姓名半模糊查询
     *
     * @param realName
     * @param pageIndex
     * @return
     */
    List<User> findByRealName(String realName, String pageIndex);

    /**
     * 根据姓名查询
     *
     * @param realName
     * @return
     */
    List<User> findByRealName(String realName);

    /**
     * 显示部门成员，不显示头像
     *
     * @param deptId
     * @return
     */
    List<User> findLeaderByDept(String deptId);

    /**
     * 显示部门领导，可以返回头像
     *
     * @param deptId
     * @param needAvatarUrl 是否需要显示头像
     * @return
     */
    List<User> findLeaderByDept(String deptId, boolean needAvatarUrl);

    /**
     * @param unitIds
     * @param page
     * @return
     */
    List<User> findByUnitIds(String[] unitIds, Pagination page);

    /**
     * 根据单位和用户类型获取用户
     *
     * @param unitIds
     * @param ownerType
     * @return
     */
    public List<User> findByOwnerType(String[] unitIds, Integer ownerType);

    /**
     * 查找某单位下的某身份正常状态用户，可根据用户姓名查找
     *
     * @param unitId
     * @param realName
     * @param ownerType
     * @param page
     * @return
     * @author cuimq
     */
    List<User> findByUnitIdAndRealNameAndOwnerTypeAndUserTypes(String unitId, String realName, Integer ownerType,
                                                               Integer[] userTypes, Pagination page);


    List<User> findByUnitIdAndRealNameAndOwnerTypeAndUserTypesAndUserName(
            String unitId, String realName, Integer ownerType,
            Integer[] userTypes, String userName, Pagination pagination);

    /**
     * 更新用户密码
     *
     * @param username
     * @param password
     * @author dingw
     */
    void updatePasswordByUsername(String username, String password, String id);

    /**
     * 更新用户状态
     *
     * @param username
     * @author dingw
     */
    void updateStateByUsername(String username, int state, String id);

    /**
     * 添加顶级管理员
     *
     * @param topAdmin
     * @author dingw
     */
    void addTopAdmin(User topAdmin);

    /**
     * 添加用户
     *
     * @param us
     */
    void saveUser(User us) throws Exception;
    
    /**
     * 批量保存用户
     * @param users
     * @throws Exception
     */
    public void saveUsers(List<User> users) throws Exception;

    /**
     * 根据AccountId更新密码
     *
     * @param password
     * @param modifyTime
     * @param accountId
     */
    void updatePasswordByAccountId(String password, Date modifyTime, String accountId) throws Exception;

    /**
     * 更新基本信息，用户信息，passport账号信息中的手机号
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
     * 更新用户头像url
     *
     * @param avatarUrl
     * @param modifyTime
     * @param id
     */
    void updateAvatarUrlById(String avatarUrl, Date modifyTime, String id);

    /**
     * 根据单位ID和用户身份获取用户列表
     *
     * @param unitIds
     * @param userTypes
     * @return
     */
    List<User> getUsersByUnitIdsOrUserTypes(final String[] unitIds, final Integer[] userTypes);

    List<User> findByRealNameAndIdentityCard(String realName, String identityCard);

    /**
     * @param mobilePhone
     * @param password    encode password or dencode password
     * @return
     */
    List<User> findByMobilePhoneAndPasswordIn(String mobilePhone, String[] password);

    /**
     * 根据单位和更新时间获取用户
     *
     * @param
     * @return
     */

    public List<User> findByUnitIdAndModifyTime(String unitId, Date modifyTime);


    /**
     * 根据单位获取所有教师用户
     *
     * @param
     * @return
     */
    public List<User> findAllByUnitId(String unitId);

    /**
     * 更新用户钉钉号
     */
    public void updateDingDingIdById(String dingdingId, String id);

    /**
     * 根据钉钉Id获取用户信息
     *
     * @param dingDingId 钉钉Id
     * @return User
     */
    User getUserByDingDingId(String dingDingId);

    /**
     * 根据ownerIds获取用户id
     *
     * @param ownerIds
     * @return List<String>
     */
    public List<String> findUserIdsListByOwnerIds(String[] ownerIds);

    /**
     * @param ownerIds
     * @param needAvatarUrl 是否需要头像 是：(有头像的：将组装完整头像路径，没有头像的：根据性别取默认头像)
     * @return
     */
    public List<User> findUsersListByOwnerIds(String[] ownerIds, boolean needAvatarUrl);


    List<User> findByModifyTimeGreaterThan(Date date, Integer isDeleted);


    List<User> findByModifyTimeGreaterThan(Date date, Integer isDeleted,
                                           Pagination page);


    List<User> findByOwnerTypeAndModifyTimeGreaterThan(int ownerType, Date mDate,
                                                       String... uidList);


    List<User> findByUserNameAndUnitIn(String userName, String[] uidList, Pagination page);


    User findByUserIdCode(String userIdCode);

    /**
     * 保证 ownerType + 身份证 是唯一存在的
     *
     * @param ownerType
     * @param identityCard
     * @return
     */
    User findByOwnerTypeAndIdentityCard(int ownerType, String identityCard);

    /**
     * 根据单位ID删除所有用户数据
     * @param unitId
     */
    void deleteUsersByUnitId(String unitId) throws PassportException;

    /**
     * 根据单位id和realName模糊查询
     * @return
     */
    List<User> findByUnitIdAndLikeRealName(String unitId,String realName);

//	User findByAhUserId(String userAhId);
//
//
//	List<User> findByAhUserIds(String[] userAhIds);



	List<User> findByUserIdCodeIn(String[] userIdCodes);


	List<User> findByUnitIdAndOwnerTypeAll(String unitId, int ownerType);

}
