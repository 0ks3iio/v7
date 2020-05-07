package net.zdsoft.szxy.operation.inner.service;

import net.zdsoft.szxy.operation.inner.entity.OpUser;
import net.zdsoft.szxy.operation.inner.exception.IllegalStateException;
import net.zdsoft.szxy.operation.inner.permission.entity.ModuleOperate;
import net.zdsoft.szxy.operation.inner.permission.entity.UserModuleRelation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2019/1/9 下午3:22
 */
public interface OpUserService {

    /**
     * 根据username查询用户信息
     * @param username 用户名
     * @return Optional
     */
    Optional<OpUser> getOpUserByUsername(String username);

    /**
     * 根据id 查询用户
     * @param userIds 用户ID 数组
     * @return List
     */
    List<OpUser> getOpUsersByUserIds(String[] userIds);

    /**
     * 分页查询所有用户
     * @param pageable 分页参数
     * @return
     */
    Page<OpUser> getAllUsers(Pageable pageable);

    List<OpUser> getAllUsers();

    /**
     * 根据用户ID查询用户
     * @param id 用户ID
     * @return
     */
    Optional<OpUser> getUserById(String id);

    /**
     * 检查指定姓名的账号是否存在
     * @param realName 真实姓名
     * @return
     */
    boolean existsByRealName(String realName);
    /**
     * 检查指定用户名的账号是否存在
     * @param username 用户名
     * @return
     */
    boolean existsByUsername(String username);

    /**
     * 保存或者更新用户信息
     * @param user 用户数据
     * @param groups 分组数据
     * @param relations 单独授权数据
     */
    void saveOrUpdateUser(OpUser user, List<String> groups, List<UserModuleRelation> relations);

    /**
     * 保存用户
     * @param user
     */
    void save(OpUser user);

    /**
     * 变更用户权限
     * @param groups 分组ID
     * @param relations 模块
     * @param userId 用户ID
     */
    void updateUserAuth(List<String> groups, List<UserModuleRelation> relations, String userId);

    /**
     * 删除用户（软删）
     * @param userId 用户ID
     */
    void deleteUser(String userId);

    /**
     * 更新 用户状态
     * @see net.zdsoft.szxy.operation.inner.enums.UserState
     * @param id 用户ID
     * @param state state
     */
    void updateUserState(String id, Integer state) throws IllegalStateException;

    /**
     * 更新用户密码
     * @param id id
     * @param password 原始密码
     */
    void updatePassword(String id, String password);
}
