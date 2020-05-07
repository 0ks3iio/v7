package net.zdsoft.system.service.user;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.UserRole;

public interface UserRoleService extends BaseService<UserRole, String> {

    public List<UserRole> findByUserId(String userId);

    /**
     * 查找用户所属用户组
     * 
     * @author cuimq
     * @param userIds
     * @return
     */
    public Map<String, List<Role>> findUserIdAndRoles(String[] userIds);

    /**
     * 查找用户组下用户成员
     * 
     * @author cuimq
     * @param roleIds
     * @return
     */
    public Map<String, List<User>> findRoleIdAndUsers(String[] roleIds);

    /**
     * 查找群组成员id
     * 
     * @author cuimq
     * @param roleId
     * @return
     */
    public List<String> findUserIdsByRoleId(String roleId);

    /**
     * 查找用户所属用户组
     * 
     * @author cuimq
     * @param userId
     * @return
     */
    public List<String> findRoleIdsByUserId(String userId);
    
    public void updateRemoteId(String id, String oldId);

	public List<UserRole> findByRoleIdAndUserIdIn(String roleId, String[] ids);

	public void deleteByRoleIdAndUserIdIn(String roleId, String[] ids);
	
	public void deleteByUserIdIn( String[] ids);
}
