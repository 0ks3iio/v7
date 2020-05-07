package net.zdsoft.szxy.base.api;

import net.zdsoft.szxy.base.RecordName;
import net.zdsoft.szxy.base.dto.OpenAccount;
import net.zdsoft.szxy.base.dto.UserUpdater;
import net.zdsoft.szxy.base.entity.User;
import net.zdsoft.szxy.base.exception.SzxyPassportException;
import net.zdsoft.szxy.base.exception.UsernameAlreadyExistsException;
import net.zdsoft.szxy.base.query.UserQuery;
import net.zdsoft.szxy.monitor.Rpc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 基础数据用户接口
 * @author shenke
 * @since 2019/3/19 上午11:21
 */
@Rpc(domain = RecordName.name)
public interface UserRemoteService {

    /**
     * 根据用户ID查询用户数据
     * @param id 用户ID
     * @return 不存在则返回null
     */
    User getUserById(String id);

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 查询结果为空则返回null
     */
    User getUserByUsername(String username);

    /**
     * 验证用户名是否存在
     * @param username 用户名
     * @return 存在true，不存在则返回false
     */
    Boolean existsUsername(String username);

    /**
     * 查询指定单位的所有用户数据
     * @param unitId 单位ID
     * @return List
     */
    List<User> getUsersByUnitId(String unitId);

    /**
     * 根据OwnerId列表查询 用户数据，OwnerIds的长度不能超过1000
     * 不包含软删的数据
     * @param ownerIds teahcerId studentId 或者familyId
     * @return List
     */
    List<User> getUsersByOwnerIds(String[] ownerIds);

    /**
     * 根据ownerID 查询用户信息
     * @param ownerId ownerId
     * @return
     */
    User getUserByOwnerId(String ownerId);

    /**
     * 动态查询用户的查询接口,不会查询软删的数据
     * <p>
     *      查询参数中regions和RegionCode的匹配模式不一致
     *      <li>
     *          regionCode 全匹配 ex: from base_user where region_code='regionCode'
     *      </li>
     *      <li>
     *          region like查询 ex: from base_user where (region_code like 'region1%' or region_code like 'region2%')
     *      </li>
     * </p>
     * @param query 查询条件
     * @param page 分页参数
     * @return
     */
    Page<User> queryUsers(UserQuery query, Pageable page);

    /**
     * 获取用户名的Map key is id value is username
     * @param userIds 用户ID列表
     * @return Map
     */
    Map<String, String> getUsernameMapByUserIds(String[] userIds);

    /**
     * 获取用户名的Map key is ownerId value is username
     * @param ownerIds 用户OwnerID列表
     * @return Map
     */
    Map<String, String> getUsernameMapByOwnerIds(String[] ownerIds);

    /**
     * 开通账号
     * 一般用于开通学生和家长账号
     */
    void openAccount(List<OpenAccount> accounts) throws UsernameAlreadyExistsException, SzxyPassportException;

    /**
     * 批量重置密码，会同步修改passport的密码
     * @param userIds 用户ID列表
     * @param password 要重置的密码 未加密的，原密码
     */
    void batchUpdatePassword(String[] userIds, String password) throws SzxyPassportException;

    /**
     * 更新用户的排序号，同时级连更新对应教师的排序号（如果有对应的教师信息的）
     * @param userId 用户ID
     * @param displayOrder 新的排序号，不能为空
     */
    void updateDisplayOrder(String userId, Integer displayOrder);

    /**
     * 更新用户信息
     * @param updater 需要更新的数据
     */
    void updateUser(UserUpdater updater);

    /**
     * 获取用户名的最大maxCode
     * @param classCode 班级编号
     * @return
     */
    Integer getUsernameMaxCode(String classCode, String unitId);
}
