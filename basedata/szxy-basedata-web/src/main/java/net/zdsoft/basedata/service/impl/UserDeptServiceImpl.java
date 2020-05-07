package net.zdsoft.basedata.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.UserDeptDao;
import net.zdsoft.basedata.entity.UserDept;
import net.zdsoft.basedata.service.UserDeptService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;


@Service("userDeptService")
public class UserDeptServiceImpl extends BaseServiceImpl<UserDept, String>
		implements UserDeptService {

	@Autowired
	UserDeptDao userDeptDao;

	@Override
	public List<UserDept> findByUserIds(String... userIds) {
		return userDeptDao.findByUserIds(userIds);
	}

	@Override
	public Map<String, List<UserDept>> findMapByUserIds(String... userIds) {
		List<UserDept> userList = userDeptDao.findByUserIds(userIds);
		Map<String, List<UserDept>> resultMap = new HashMap<String, List<UserDept>>();
		for (UserDept userDept : userList) {
			List<UserDept> tempUserList = resultMap.get(userDept.getUserId());
			if (CollectionUtils.isEmpty(tempUserList))
				tempUserList = new ArrayList<UserDept>();
			tempUserList.add(userDept);
			resultMap.put(userDept.getUserId(), tempUserList);
		}
		return resultMap;
	}

	@Override
	protected BaseJpaRepositoryDao<UserDept, String> getJpaDao() {
		return userDeptDao;
	}

	@Override
	protected Class<UserDept> getEntityClass() {
		return UserDept.class;
	}

}
