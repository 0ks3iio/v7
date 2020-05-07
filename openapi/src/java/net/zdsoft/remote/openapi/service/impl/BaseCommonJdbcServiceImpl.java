package net.zdsoft.remote.openapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.remote.openapi.dao.BaseCommonJdbcDao;
import net.zdsoft.remote.openapi.service.BaseCommonJdbcService;

@Service("baseCommonJdbcService")
public class BaseCommonJdbcServiceImpl implements BaseCommonJdbcService {

	@Autowired
	BaseCommonJdbcDao baseCommonJdbcDao;

	@Override
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes) {
		// TODO Auto-generated method stub
		return baseCommonJdbcDao.updateList(sql, objList, argTypes);
	}
	
	
	
}
