package net.zdsoft.basedata.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import net.zdsoft.basedata.dao.ChartsUserDao;
import net.zdsoft.basedata.entity.ChartsUser;
import net.zdsoft.basedata.service.ChartsUserService;

@Service("chartsUserService")
public class ChartsUserServiceImpl implements ChartsUserService {

	@Autowired
	private ChartsUserDao chartsUserDao;
	
	@Override
	public List<ChartsUser> findByUserId(String userId) {
		return chartsUserDao.findByUserIdIn(userId);
	}

}
