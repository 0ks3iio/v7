package net.zdsoft.bigdata.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.system.dao.BgUserAuthDao;
import net.zdsoft.bigdata.system.entity.BgUserAuth;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.UuidUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("bgUserAuthService")
public class BgUserAuthServiceImpl extends BaseServiceImpl<BgUserAuth, String>
		implements BgUserAuthService {

	@Autowired
	private BgUserAuthDao bgUserAuthDao;

	@Autowired
	private UserRemoteService userRemoteService;

	@Override
	protected BaseJpaRepositoryDao<BgUserAuth, String> getJpaDao() {
		return bgUserAuthDao;
	}

	@Override
	protected Class<BgUserAuth> getEntityClass() {
		return BgUserAuth.class;
	}

	@Override
	public List<BgUserAuth> findAllAuthUserList() {
		List<BgUserAuth> authList = bgUserAuthDao.findAllUserList();
		Set<String> userIds = new HashSet<String>();
		for (BgUserAuth userAuth : authList) {
			userIds.add(userAuth.getUserId());
		}
		List<User> userList = User.dt(userRemoteService.findListByIds(userIds
				.toArray(new String[0])));
		Map<String, User> userMap = new HashMap<String, User>();
		for (User user : userList) {
			userMap.put(user.getId(), user);
		}

		for (BgUserAuth userAuth : authList) {
			if (userMap.containsKey(userAuth.getUserId())) {
				User user = userMap.get(userAuth.getUserId());
				userAuth.setUserName(user.getUsername());
				userAuth.setRealName(user.getRealName());
				userAuth.setSex(user.getSex());
			}
		}
		return authList;
	}

	@Override
	public List<BgUserAuth> findUserList(String unitId, String username,
			String realname) {
		List<BgUserAuth> authList = bgUserAuthDao.findAllUserList();

		Map<String, BgUserAuth> userAuthMap = new HashMap<String, BgUserAuth>();
		Set<String> userIds = new HashSet<String>();
		for (BgUserAuth authUser : authList) {
			userIds.add(authUser.getUserId());
			userAuthMap.put(authUser.getUserId(), authUser);
		}
		List<BgUserAuth> resultList = new ArrayList<BgUserAuth>();
		List<User> userList = new ArrayList<User>();
		if (StringUtils.isNotBlank(username)) {
			User _user = User.dc(userRemoteService.findByUsername(username));
			if (_user != null)
				userList.add(_user);
		} else if (StringUtils.isNotBlank(realname)) {
			userList = User.dt(userRemoteService.findByRealName(realname));
		}
		for (User user : userList) {
			if (!user.getUnitId().equals(unitId)) {
				continue;
			}
			BgUserAuth result = null;
			if (userAuthMap.containsKey(user.getId())) {
				result = userAuthMap.get(user.getId());
			} else {
				result = new BgUserAuth();
				result.setUserId(user.getId());
			}
			result.setUserName(user.getUsername());
			result.setRealName(user.getRealName());
			result.setSex(user.getSex());

			resultList.add(result);
		}
		return resultList;
	}

	@Override
	public void saveUserAuth(String userId) {
		BgUserAuth userAuth = new BgUserAuth();
		userAuth.setId(UuidUtils.generateUuid());
		userAuth.setUserId(userId);
		userAuth.setStatus(1);
		userAuth.setCreationTime(new Date());
		userAuth.setModifyTime(new Date());
		save(userAuth);
	}

	@Override
	public void updateStatusById(String id, Integer status) {
		bgUserAuthDao.updateStatusById(id, status, new Date());
	}

	@Override
	public void updateSuperUserById(String id, Integer isSuperUser) {
		bgUserAuthDao.updateSuperUseryId(id, isSuperUser, new Date());
	}

	@Override
	public boolean isBackgroundUser(String userId, int userType) {
		if (userType == User.USER_TYPE_TOP_ADMIN) {
			return true;
		}
		BgUserAuth userAuth = findOneBy("userId", userId);
		boolean result = false;
		if (userAuth != null && userAuth.getStatus() == 1) {
			result = true;
		}
		return result;
	}

	@Override
	public boolean isSuperUser(String userId) {
		BgUserAuth userAuth = findOneBy("userId", userId);
		boolean result = false;
		if (userAuth != null && userAuth.getIsSuperUser() == 1) {
			result = true;
		}
		return result;
	}

}
