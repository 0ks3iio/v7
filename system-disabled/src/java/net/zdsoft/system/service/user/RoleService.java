package net.zdsoft.system.service.user;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.user.Role;

public interface RoleService extends BaseService<Role, String> {

    public List<Role> findByUnitId(String unitId);

    public List<Role> saveAllEntitys(Role... role);

    public List<Role> findListByIds(String[] ids);

    /**
     * 根据用户组id和用户组名称在单位下查找用户组
     * 
     * @author cuimq
     * @param id
     * @param name
     * @param unitId
     * @return
     */
    public Role findByIdAndNameAndUnitId(String id, String name, String unitId);

    /**
     * 新增用户组，同时新增用户组权限及用户组成员
     * 
     * @author cuimq
     * @param role
     * @param userIds
     * @param modelIds
     */
    public void insertRole(Role role, String[] userIds, Integer[] modelIds);

    /**
     * 根据id查找用户组信息
     * 
     * @author cuimq
     * @param id
     * @return
     */
    public Role findById(String id);

    /**
     * 修改用户组，同时修改用户组成员及用户组权限
     * 
     * @author cuimq
     * @param role
     * @param userIds
     * @param modelIds
     * @param allModelIds
     */
    public void updateRole(Role role, String[] userIds, Integer[] modelIds, Integer[] allModelIds);

	/**
	 * @param unitId
	 * @param roleTypeOper
	 * @return
	 */
	public List<Role> findByUnitIdAndRoleType(String unitId, int roleTypeOper);

	/**
	 * @param roleName
	 * @param unitId
	 * @return
	 */
	public Role findByNameAndUnitId(String roleName, String unitId);

}
