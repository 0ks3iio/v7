package net.zdsoft.system.service.user.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Specifications;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.system.dao.user.UserRoleDao;
import net.zdsoft.system.entity.user.Role;
import net.zdsoft.system.entity.user.UserRole;
import net.zdsoft.system.service.user.RoleService;
import net.zdsoft.system.service.user.UserRoleService;

@Service("userRoleService")
public class UserRoleServiceImpl extends BaseServiceImpl<UserRole, String> implements UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRemoteService userRemoteService;

    @Override
    protected BaseJpaRepositoryDao<UserRole, String> getJpaDao() {
        return userRoleDao;
    }

    @Override
    protected Class<UserRole> getEntityClass() {
        return UserRole.class;
    }

    @Override
    public List<UserRole> findByUserId(String userId) {
        return userRoleDao.findAll(new Specifications<UserRole>().addEq("userId", userId).getSpecification());
    }

    @Override
    public Map<String, List<Role>> findUserIdAndRoles(String[] userIds) {
        Map<String, List<Role>> userIdAndRolesMap = new HashMap<String, List<Role>>();

        // 用户和权限组关联关系
        List<UserRole> userRoleList = userRoleDao.findByUserIds(userIds);

        if (CollectionUtils.isNotEmpty(userRoleList)) {
            Set<String> roleIdSet = EntityUtils.getSet(userRoleList, "roleId");

            Map<String, Role> roleIdAndRoleMap = roleService.findMapByIdIn(roleIdSet.toArray(new String[roleIdSet
                    .size()]));

            for (UserRole userRole : userRoleList) {
                List<Role> roleList = userIdAndRolesMap.get(userRole.getUserId());
                if (CollectionUtils.isEmpty(roleList)) {
                    roleList = new ArrayList<Role>();
                }
                Role role = roleIdAndRoleMap.get(userRole.getRoleId());
                if (null != role) {
                    roleList.add(role);
                }

                userIdAndRolesMap.put(userRole.getUserId(), roleList);
            }
        }

        return userIdAndRolesMap;
    }

    @Override
    public Map<String, List<User>> findRoleIdAndUsers(String[] roleIds) {
        Map<String, List<User>> roleIdAndUsersMap = new HashMap<String, List<User>>();

        // 用户和权限组关联关系
        List<UserRole> userRoleList = userRoleDao.findByRoleIds(roleIds);

        if (CollectionUtils.isNotEmpty(userRoleList)) {
            Set<String> userIdSet = EntityUtils.getSet(userRoleList, "userId");

            List<User> userList = User.dt(userRemoteService.findListByIds(userIdSet.toArray(new String[userIdSet.size()])));
            Map<String, User> userMap = EntityUtils.getMap(userList, "id");

            for (UserRole userRole : userRoleList) {
                List<User> users = roleIdAndUsersMap.get(userRole.getRoleId());
                if (CollectionUtils.isEmpty(users)) {
                    users = new ArrayList<User>();
                }
                User user = userMap.get(userRole.getUserId());
                if (null != user) {
                    users.add(user);
                }

                roleIdAndUsersMap.put(userRole.getRoleId(), users);
            }
        }

        return roleIdAndUsersMap;
    }

    @Override
    public List<String> findUserIdsByRoleId(String roleId) {
        return userRoleDao.findUserIdsByRoleId(roleId);
    }

    @Override
    public List<String> findRoleIdsByUserId(String userId) {
        return userRoleDao.findRoleIdsByUserId(userId);
    }
    
    @Override
	public void updateRemoteId(String id, String oldId) {
    	userRoleDao.updateRemoteId(id,oldId);
	}

	@Override
	public List<UserRole> findByRoleIdAndUserIdIn(String roleId, String[] ids) {
		return userRoleDao.findByRoleIdAndUserIdIn(roleId,ids);
	}

	@Override
	public void deleteByRoleIdAndUserIdIn(String roleId, String[] ids) {
		userRoleDao.deleteByRoleIdAndUserIdIn(roleId,ids);
	}
}
