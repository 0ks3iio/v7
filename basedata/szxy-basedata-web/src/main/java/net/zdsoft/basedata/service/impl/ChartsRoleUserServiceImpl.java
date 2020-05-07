package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.ChartsRoleUserDao;
import net.zdsoft.basedata.entity.ChartsRoleUser;
import net.zdsoft.basedata.service.ChartsRoleUserService;
import net.zdsoft.framework.entity.Constant;

@Service("chartsRoleUserService")
public class ChartsRoleUserServiceImpl implements ChartsRoleUserService {

	@Autowired
	private ChartsRoleUserDao chartsRoleUserDao;

	@Override
	public List<ChartsRoleUser> findByUserId(String userId) {
		List<ChartsRoleUser> list = chartsRoleUserDao.findByUserId(userId);
		if(CollectionUtils.isNotEmpty(list)){
			return list;
		}else{
			list = chartsRoleUserDao.findByUserId(Constant.GUID_ZERO);
		}
		return list;
	}

}
