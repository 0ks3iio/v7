package net.zdsoft.system.service.user;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.user.Role;

public interface RoleService extends BaseService<Role, String> {

    List<Role> findByUnitId(String unitId);

    List<Role> saveAllEntitys(Role... role);

    List<Role> findListByIds(String[] ids);

    /**
     * 根据用户组id和用户组名称在单位下查找用户组
     *
     * @param id
     * @param name
     * @param unitId
     * @return
     * @author cuimq
     */
    Role findByIdAndNameAndUnitId(String id, String name, String unitId);

    /**
     * 新增用户组，同时新增用户组权限及用户组成员
     *
     * @param role
     * @param userIds
     * @param modelIds
     * @author cuimq
     */
    void insertRole(Role role, String[] userIds, Integer[] modelIds);

    /**
     * 根据id查找用户组信息
     *
     * @param id
     * @return
     * @author cuimq
     */
    Role findById(String id);

    /**
     * 修改用户组，同时修改用户组成员及用户组权限
     *
     * @param role
     * @param userIds
     * @param modelIds
     * @param allModelIds
     * @author cuimq
     */
    void updateRole(Role role, String[] userIds, Integer[] modelIds, Integer[] allModelIds);

    /**
     * @param unitId
     * @param roleTypeOper
     * @return
     */
    List<Role> findByUnitIdAndRoleType(String unitId, int roleTypeOper);

    /**
     * @param roleName
     * @param unitId
     * @return
     */
    Role findByNameAndUnitId(String roleName, String unitId);

    /**
     * 初始化单位默认角色信息
     * @param unitId 单位ID
     * @param unitClass 单位类别
     */
    void initUnitDefaultRole(String unitId, Integer unitClass);

	void insertUserRole(String[] roleIds, String[] userIds);
}
