package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserDao extends BaseJpaRepositoryDao<User, String> {

    @Query("from User where unitId IN ?1 and isDeleted = 0")
    List<User> findByUnitIds(String... unitIds);

    @Query("from User where deptId = ?1 and isDeleted = 0")
    List<User> findByDeptId(String deptId);

    @Query("from User where unitId IN ?1")
    List<User> findByUnitIdsWithDeleted(String... unitIds);

    @Query("Select count(*) from User where unitId = ?1 and isDeleted = 0")
    Integer countByUnitId(String unitId);

    @Query("From User where unitId = ?1 and isDeleted = 0")
    List<User> findByUnitId(String unitId, Pageable page);

    @Query("From User where ownerId = ?1 and isDeleted = 0")
    User findByOwnerId(String ownerId);

    @Query("From User where username = ?1 and isDeleted = 0")
    User findByUsername(String username);

    @Query("From User where isDeleted = 0 and username in ?1")
    List<User> findByUsernames(String... usernames);

    @Query("From User where isDeleted = 0 and ownerType = ?1 and mobilePhone in ?2")
    List<User> findByMobilephones(Integer ownerType, String... mobilePhones);

    //@Query("From User where isDeleted = 0 and mobilePhone in ?2")
    List<User> findByIsDeletedIsAndMobilePhoneIn(Integer isDeleted, String[] mobilePhone);

    @Query("From User where isDeleted = 0 and ownerType = 2 and unitId = ?1 and (userType = 0 or userType = 1)")
    User findAdmin(String unitId);

    @Query("From User where isDeleted = 0 and ownerType = 2 and (userType = 0 or userType = 1) and unitId in ?1")
    List<User> findAdmins(String... unitIds);

    @Query("From User where isDeleted = 0 and accountId = ?1")
    User findByAccountId(String accountId);

    @Query("From User where isDeleted = 0 and userType in ?1")
    List<User> findByUserTypes(Integer... userTypes);

    @Query("From User where isDeleted = 0 and ownerId In ?1")
    List<User> findByOwnerIds(String[] ownerIds);

    @Query("From User where isDeleted = 0 and ownerId In ?1")
    List<User> findByOwnerIds(String[] ownerIds, Pageable page);

    @Query("select max(u.displayOrder) from User u where isDeleted = 0")
    Long findMaxDisplayOrder(String unitId);

    @Query("From User where unitId IN ?1 and isDeleted = 0 and ownerType = ?2")
    List<User> findByOwnerType(String[] unitIds, Integer ownerType);

    @Query("From User where unitId IN ?1 and isDeleted = 0 and ownerType = ?2")
    List<User> findByOwnerType(String[] unitIds, Integer ownerType, Pageable page);

    @Modifying
    @Query("update User set isDeleted = 1 where id in (?1)")
    void deleteAllByIds(String... id);

    /**
     * 查询单位职务用户
     *
     * @param unitIds
     * @param ownerType
     * @return
     */
    @Query(nativeQuery = true, value = "select * from base_user where unit_id = ?1 and is_deleted = 0 and exists (select 1 from base_teacher_duty where is_deleted = 0 and duty_code = ?2 and teacher_id = base_user.owner_id)")
    List<User> findByUnitDutyCode(String unitId, String dutyCode);

    /**
     * 查询半模糊匹配的教师
     *
     * @param realName
     * @return
     */
    @Query("From User where realName like ?1 and isDeleted=0 and ownerType=2")
    List<User> findByRealName(String realName, Pageable page);
    
    /**
     * 查询教师
     *
     * @param realName
     * @return
     */
    @Query("From User where realName = ?1 and isDeleted=0 and ownerType=2")
    List<User> findByRealName(String realName);

    /**
     * 查询单位部门领导(除去职务是科员)
     *
     * @param unitIds
     * @param ownerType
     * @return
     */
    @Query(nativeQuery = true, value = "select * from base_user where dept_id = ?1 and is_deleted = 0 and exists (select 1 from base_teacher_duty where is_deleted = 0 and duty_code <> '114' and teacher_id = base_user.owner_id)")
    List<User> findLeaderByDept(String deptId);

    @Modifying
    @Query("update User set password=?1,modify_time=?2,upPwdDate=?3,loginDate=?3 where username=?4")
    void updatePasswordByUsername(String password, Date modifyTime, Date upPwdDate, String username);

    @Modifying
    @Query("update User set userState=?1 where username=?2")
    void updateStateByUsername(int state, String username);

    @Modifying
    @Query("update User set password=?1,modify_time=?2, upPwdDate=?3,loginDate=?3 where accountId=?4")
    void updatePasswordByAccountId(String password, Date modifyTime, Date upPwdDate, String accountId);

    @Modifying
    @Query("update User set mobilePhone=?1,modify_time=?2 where id=?3")
    void updatePhoneById(String mobilePhone, Date modifyTime, String id);

    @Modifying
    @Query("update User set avatarUrl=?1,modify_time=?2 where id=?3")
    void updateAvatarUrlById(String avatarUrl, Date modifyTime, String id);

    List<User> findByMobilePhoneAndPasswordIn(String mobilePhone, String[] password);
    
    /**
     * 根据单位和更新时间获取教师用户
     *
     * @param 
     * @return
     */
    @Query("From User where unit_id = ?1 and modify_time>= ?2 and ownerType=2")
    List<User> findByUnitIdAndModifyTime(String unitId, Date modifyTime);
    
    /**
     * 根据单位获取所有教师用户
     *
     * @param 
     * @return
     */
    @Query("From User where unit_id = ?1 and ownerType=2")
    List<User> findAllByUnitId(String unitId);
    
    /**
     * 更新用户钉钉号
     */
    @Modifying
    @Query("update User set dingding_id=?1 where id=?2")
    void updateDingDingIdById(String dingdingId, String id);

	/**
	 * @param ownerType
	 * @return
	 */
    @Query("From User where ownerType = ?1 and isDeleted = 0 ")
    List<User> findByOwnerType(Integer ownerType);

    @Query(value = "select * from base_user where dingding_id=?1 AND IS_DELETED=0", nativeQuery = true)
    User getUserByDingDingId(String dingDingId);
   
    @Query("select id From User where isDeleted = 0 and ownerId In ?1")
	List<String> findUserIdsListByOwnerIds(String[] ownerIds);
    
    @Query("From User where isDeleted = 0 and ownerId In ?1")
	List<User> findUsersListByOwnerIds(String[] ownerIds);

    @Query("From User where modify_time > ?1")
	List<User> findByModifyTimeGreaterThan(Date date);

    @Query("From User where modify_time > ?1 and isDeleted = ?2")
	List<User> findByModifyTimeGreaterThanAndIsDeleted(Date date,
			Integer isDeleted);

    @Query("From User where ownerType = ?1 and  modify_time > ?2 and unitId in ?3")
	List<User> findByOwnerTypeAndModifyTimeGreaterThan(int ownerType,
			Date mDate, String... uidList);

    @Query("From User where userIdCode = ?1 and isDeleted=0 ")
	User findByUserIdCode(String userIdCode);

    
	User findByOwnerTypeAndIdentityCard(int ownerType, String identityCard);

	@Modifying
	@Query(
	        value = "update User set password=?2 where id in (?1)"
    )
    void restPassword(String[] userIds, String newPassword);

	@Modifying
    @Query(
            value = "update User set isDeleted=1 where unitId=?1"
    )
	void deleteUsersByUnitId(String unitId);

   @Query("From User where isDeleted = 0 and identityCard = ?1")
	List<User> findbyIdentityCard(String identityCard);

    @Query("from User where  unitId=?1  and realName like ?2  and isDeleted='0' order by ownerType")
    List<User> findByUnitIdAndLikeRealName(String unitId,String realName);

//	User findByAhUserId(String ahUserId);
//	
//	@Query("From User where ahUserId IN ?1 and isDeleted = 0")
//	List<User> findByAhUserIds(String[] userAhIds);


    @Query("From User where userIdCode in ?1")
	List<User> findByUserIdCodeIn(String[] userIdCodes);

    @Query("From User where unitId = ?1 and ownerType = ?2 ")
	List<User> findByUnitIdAndOwnerTypeAll(String unitId, int ownerType);



}
