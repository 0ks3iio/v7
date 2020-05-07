package net.zdsoft.base.service.impl;

import java.util.List;

import net.zdsoft.base.dao.BaseCommonJdbcDao;
import net.zdsoft.base.service.BaseCommonJdbcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("baseCommonJdbcService")
public class BaseCommonJdbcServiceImpl implements BaseCommonJdbcService {

	@Autowired
	BaseCommonJdbcDao baseCommonJdbcDao;

	@Override
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes) {
		return baseCommonJdbcDao.updateList(sql, objList, argTypes);
	}
	
	
	
}
