package net.zdsoft.bigdata.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Dept;
import net.zdsoft.basedata.entity.Unit;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.system.dao.BgUserRoleDao;
import net.zdsoft.bigdata.system.entity.BgUserRole;
import net.zdsoft.bigdata.system.service.BgUserRoleService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgUserRoleService")
public class BgUserRoleServiceImpl extends BaseServiceImpl<BgUserRole, String>
		implements BgUserRoleService {

	@Autowired
	private BgUserRoleDao bgUserRoleDao;

	@Autowired
	private UserRemoteService userRemoteService;

	@Autowired
	private DeptRemoteService deptRemoteService;

	@Autowired
	private UnitRemoteService unitRemoteService;

	@Override
	protected BaseJpaRepositoryDao<BgUserRole, String> getJpaDao() {
		return bgUserRoleDao;
	}

	@Override
	protected Class<BgUserRole> getEntityClass() {
		return BgUserRole.class;
	}

	@Override
	public List<BgUserRole> findUserRoleListByUserId(String userId) {
		return bgUserRoleDao.findUserRoleListByUserId(userId);
	}

	@Override
	public List<BgUserRole> findUserRoleListByRoleId(String roleId) {
		List<BgUserRole> userRoleList = bgUserRoleDao
				.findUserRoleListByRoleId(roleId);
		Set<String> userIds = new HashSet<String>();
		for (BgUserRole userRole : userRoleList) {
			userIds.add(userRole.getUserId());
		}
		List<User> userList = User.dt(userRemoteService.findListByIds(userIds
				.toArray(new String[0])));
		Map<String, User> userMap = new HashMap<String, User>();
		Set<String> deptIds = new HashSet<String>();
		Set<String> unitIds = new HashSet<String>();
		for (User user : userList) {
			userMap.put(user.getId(), user);
			deptIds.add(user.getDeptId());
			unitIds.add(user.getUnitId());
		}
		List<Dept> deptList = Dept.dt(deptRemoteService.findListByIds(deptIds
				.toArray(new String[0])));
		List<Unit> unitList = Unit.dt(unitRemoteService.findListByIds(unitIds
				.toArray(new String[0])));
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		Map<String, Unit> unitMap = new HashMap<String, Unit>();
		for (Dept dept : deptList) {
			deptMap.put(dept.getId(), dept);
		}
		for (Unit unit : unitList) {
			unitMap.put(unit.getId(), unit);
		}

		for (BgUserRole userRole : userRoleList) {
			if (userMap.containsKey(userRole.getUserId())) {
				User user = userMap.get(userRole.getUserId());
				userRole.setUsername(user.getUsername());
				userRole.setRealName(user.getRealName());
				userRole.setSex(user.getSex());
				if (deptMap.containsKey(user.getDeptId())) {
					userRole.setDeptName(deptMap.get(user.getDeptId())
							.getDeptName());
				}
				if (unitMap.containsKey(user.getUnitId())) {
					userRole.setUnitName(unitMap.get(user.getUnitId())
							.getUnitName());
				}
			}

		}
		return userRoleList;
	}

	@Override
	public List<BgUserRole> findUserListWithRole(String roleId,
			String username, String realname) {
		List<BgUserRole> userRoleList = bgUserRoleDao
				.findUserRoleListByRoleId(roleId);
		Map<String, BgUserRole> userRoleMap = new HashMap<String, BgUserRole>();
		Set<String> userIds = new HashSet<String>();
		for (BgUserRole userRole : userRoleList) {
			userIds.add(userRole.getUserId());
			userRoleMap.put(userRole.getUserId(), userRole);
		}

		List<BgUserRole> resultList = new ArrayList<BgUserRole>();
		List<User> userList = new ArrayList<User>();
		if (StringUtils.isNotBlank(username)) {
			User _user = User.dc(userRemoteService.findByUsername(username));
			if (_user != null)
				userList.add(_user);
		} else if (StringUtils.isNotBlank(realname)) {
			userList = User.dt(userRemoteService.findByRealName(realname));
		}
		Set<String> deptIds = new HashSet<String>();
		Set<String> unitIds = new HashSet<String>();
		for (User user : userList) {
			deptIds.add(user.getDeptId());
			unitIds.add(user.getUnitId());
		}
		List<Dept> deptList = Dept.dt(deptRemoteService.findListByIds(deptIds
				.toArray(new String[0])));
		List<Unit> unitList = Unit.dt(unitRemoteService.findListByIds(unitIds
				.toArray(new String[0])));
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		Map<String, Unit> unitMap = new HashMap<String, Unit>();
		for (Dept dept : deptList) {
			deptMap.put(dept.getId(), dept);
		}
		for (Unit unit : unitList) {
			unitMap.put(unit.getId(), unit);
		}
		for (User user : userList) {
			BgUserRole result = null;
			if (userRoleMap.containsKey(user.getId())) {
				result = userRoleMap.get(user.getId());
			} else {
				result = new BgUserRole();
				result.setUserId(user.getId());
			}
			result.setUsername(user.getUsername());
			result.setRealName(user.getRealName());
			result.setSex(user.getSex());
			if (deptMap.containsKey(user.getDeptId())) {
				result.setDeptName(deptMap.get(user.getDeptId()).getDeptName());
			}
			if (unitMap.containsKey(user.getUnitId())) {
				result.setUnitName(unitMap.get(user.getUnitId()).getUnitName());
			}

			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public List<BgUserRole> findUserRoleListByModuleId(String moduleId) {
		List<BgUserRole> userRoleList = bgUserRoleDao
				.findUserRoleListByModuleId(moduleId);
		Set<String> userIds = new HashSet<String>();
		for (BgUserRole userRole : userRoleList) {
			userIds.add(userRole.getUserId());
		}
		List<User> userList = User.dt(userRemoteService.findListByIds(userIds
				.toArray(new String[0])));
		Map<String, User> userMap = new HashMap<String, User>();
		Set<String> deptIds = new HashSet<String>();
		Set<String> unitIds = new HashSet<String>();
		for (User user : userList) {
			userMap.put(user.getId(), user);
			deptIds.add(user.getDeptId());
			unitIds.add(user.getUnitId());
		}
		List<Dept> deptList = Dept.dt(deptRemoteService.findListByIds(deptIds
				.toArray(new String[0])));
		List<Unit> unitList = Unit.dt(unitRemoteService.findListByIds(unitIds
				.toArray(new String[0])));
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		Map<String, Unit> unitMap = new HashMap<String, Unit>();
		for (Dept dept : deptList) {
			deptMap.put(dept.getId(), dept);
		}
		for (Unit unit : unitList) {
			unitMap.put(unit.getId(), unit);
		}
		for (BgUserRole userRole : userRoleList) {
			if (userMap.containsKey(userRole.getUserId())) {
				User user = userMap.get(userRole.getUserId());
				userRole.setUsername(user.getUsername());
				userRole.setRealName(user.getRealName());
				userRole.setSex(user.getSex());
				if (deptMap.containsKey(user.getDeptId())) {
					userRole.setDeptName(deptMap.get(user.getDeptId())
							.getDeptName());
				}
				if (unitMap.containsKey(user.getUnitId())) {
					userRole.setUnitName(unitMap.get(user.getUnitId())
							.getUnitName());
				}
			}

		}
		return userRoleList;
	}

	@Override
	public List<BgUserRole> findUserListWithModule(String moduleId,
			String username, String realname) {
		List<BgUserRole> userRoleList = bgUserRoleDao
				.findUserRoleListByModuleId(moduleId);
		Map<String, BgUserRole> userRoleMap = new HashMap<String, BgUserRole>();
		Set<String> userIds = new HashSet<String>();
		for (BgUserRole userRole : userRoleList) {
			userIds.add(userRole.getUserId());
			userRoleMap.put(userRole.getUserId(), userRole);
		}

		List<BgUserRole> resultList = new ArrayList<BgUserRole>();
		List<User> userList = new ArrayList<User>();
		if (StringUtils.isNotBlank(username)) {
			User _user = User.dc(userRemoteService.findByUsername(username));
			if (_user != null)
				userList.add(_user);
		} else if (StringUtils.isNotBlank(realname)) {
			userList = User.dt(userRemoteService.findByRealName(realname));
		}
		Set<String> deptIds = new HashSet<String>();
		Set<String> unitIds = new HashSet<String>();
		for (User user : userList) {
			deptIds.add(user.getDeptId());
			unitIds.add(user.getUnitId());
		}
		List<Dept> deptList = Dept.dt(deptRemoteService.findListByIds(deptIds
				.toArray(new String[0])));
		List<Unit> unitList = Unit.dt(unitRemoteService.findListByIds(unitIds
				.toArray(new String[0])));
		Map<String, Dept> deptMap = new HashMap<String, Dept>();
		Map<String, Unit> unitMap = new HashMap<String, Unit>();
		for (Dept dept : deptList) {
			deptMap.put(dept.getId(), dept);
		}
		for (Unit unit : unitList) {
			unitMap.put(unit.getId(), unit);
		}
		for (User user : userList) {
			BgUserRole result = null;
			if (userRoleMap.containsKey(user.getId())) {
				result = userRoleMap.get(user.getId());
			} else {
				result = new BgUserRole();
				result.setUserId(user.getId());
			}
			result.setUsername(user.getUsername());
			result.setRealName(user.getRealName());
			result.setSex(user.getSex());
			if (deptMap.containsKey(user.getDeptId())) {
				result.setDeptName(deptMap.get(user.getDeptId()).getDeptName());
			}
			if (unitMap.containsKey(user.getUnitId())) {
				result.setUnitName(unitMap.get(user.getUnitId()).getUnitName());
			}

			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public void saveUserRole(String roleId, String userId) {
		BgUserRole userRole = new BgUserRole();
		userRole.setId(UuidUtils.generateUuid());
		userRole.setRoleId(roleId);
		userRole.setUserId(userId);
		userRole.setType(BgUserRole.TYPE_ROLE);
		userRole.setCreationTime(new Date());
		save(userRole);
	}

	@Override
	public void saveUserModuleRole(String moduleId, String userId) {
		BgUserRole userRole = new BgUserRole();
		userRole.setId(UuidUtils.generateUuid());
		userRole.setModuleId(moduleId);
		userRole.setUserId(userId);
		userRole.setType(BgUserRole.TYPE_MODULE);
		userRole.setCreationTime(new Date());
		save(userRole);
	}

	@Override
	public void deleteByModuleIdAndUserId(String moduleId, String userId) {
		bgUserRoleDao.deleteByModuleIdAndUserId(moduleId, userId);
	}

	@Override
	public void deleteByRoleIdAndUserId(String roleId, String userId) {
		bgUserRoleDao.deleteByRoleIdAndUserId(roleId, userId);
	}

	@Override
	public void deleteByRoleId(String roleId) {
		bgUserRoleDao.deleteByRoleId(roleId);
	}

	@Override
	public void deleteByUserId(String userId) {
		bgUserRoleDao.deleteByUserId(userId);
	}

}
