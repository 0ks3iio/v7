package net.zdsoft.szxy.base.dao;

import net.zdsoft.szxy.base.dto.UserUpdater;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.monitor.Record;
import net.zdsoft.szxy.monitor.RecordType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author shenke
 * @since 2019/3/19 下午3:48
 */
@Repository
public interface UserDao extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {


    /**
     * 根据用户ID查询用户数据
     * @param s 用户ID
     * @return Optional
     */
    @Record(type = RecordType.SQL, val = "from User where id=?1 and isDeleted=0")
    @Query(value = "from User where id=?1 and isDeleted=0")
    @Override
    Optional<User> findById(String s);

    /**
     * 根据用户名查询用户数据
     * @param username 用户名
     * @return
     */
    @Query(value = "from User where username=?1 and isDeleted=0")
    User getUserByUsername(String username);

    /**
     * 根据用户名批量查询用户信息
     * @param usernames 用户名
     * @return
     */
    @Query(value = "from User where username in (?1) and isDeleted=0")
    Stream<User> getUsersByUsername(String[] usernames);

    /**
     * 查询指定单位的全部用户数据
     * @param unitId 单位ID
     * @return
     */
    @Query(value = "from User where unitId=?1 and isDeleted=0")
    List<User> getUsersByUnitId(String unitId);

    /**
     * 删除指定单位下的用户（软删）
     * @param unitId 单位ID
     */
    @Modifying
    @Query(value = "update User set isDeleted=1, modifyTime=:#{new java.util.Date()} where unitId=?1")
    void deleteUsersByUnitId(String unitId);

    /**
     * 根据用户ID列表查询用户数据
     * @param userIds 用户Id列表
     * @return
     */
    @Query(value = "from User where id in (?1)")
    Stream<User> getUsersByUserIds(String[] userIds);

    /**
     * 根据OwnerId查询用户信息
     * @param ownerIds ownerId array
     * @return
     */
    @Query(value = "from User where ownerId in (?1) and isDeleted=0")
    Stream<User> getUsersByOwnerId(String[] ownerIds);

    /**
     * 根据OwnerId查询用户信息
     * @param ownerId ownerId
     * @return
     */
    Optional<User> getUserByOwnerId(String ownerId);

    /**
     * 批量重置密码
     * @param userIds 用户ID列表
     * @param password 密码
     */
    @Modifying
    @Query(value = "update User set password=?2 where id in (?1)")
    void batchUpdatePassword(String[] userIds, String password);

    /**
     * 更新指定用户的排序号
     * @param id 用户ID
     * @param displayOrder 新的排序号
     */
    @Modifying
    @Query(value = "update User set displayOrder=?2 where id=?1")
    void updateDisplayOrderById(String id, Integer displayOrder);

    /**
     * 动态更新用户数据
     * @param updateUser
     */
    @Modifying
    void updateUser(UserUpdater updateUser);

    /**
     * 获取用户名的最大maxCode
     * @param classCode 班级编号
     * @return
     */
    @Query(
            value = "SELECT max(replace(username,'class_code','')) as maxcode FROM base_user WHERE unit_id=?2 and is_deleted = 0 and username like ?1%",
            nativeQuery = true
    )
    Integer getUsernameMaxCode(String classCode, String unitId);
}
