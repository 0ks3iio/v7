package net.zdsoft.basedata.service.impl;

import java.util.List;

import net.zdsoft.basedata.dao.BaseJdbcDao;
import net.zdsoft.basedata.service.BaseJdbcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseJdbcServiceImpl implements BaseJdbcService {

	@Autowired
	private BaseJdbcDao baseJdbcDao;

	@Override
	public void execSql(String sql, Object[] objs) {
		baseJdbcDao.execSql(sql, objs);
	}

	@Override
	public List<Object[]> findBySql(String sql, Object[] params) {
		return baseJdbcDao.findBySql(sql, params);
	}

	@Override
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes) {
		return baseJdbcDao.updateList(sql, objList, argTypes);
	}

}
