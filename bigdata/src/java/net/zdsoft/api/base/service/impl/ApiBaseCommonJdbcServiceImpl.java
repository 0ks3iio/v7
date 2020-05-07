package net.zdsoft.api.base.service.impl;

import java.util.List;




import net.zdsoft.api.base.dao.ApiBaseCommonJdbcDao;
import net.zdsoft.api.base.service.ApiBaseCommonJdbcService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("apiBaseCommonJdbcService")
public class ApiBaseCommonJdbcServiceImpl implements ApiBaseCommonJdbcService {

	@Autowired
	ApiBaseCommonJdbcDao baseCommonJdbcDao;

	@Override
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes) {
		return baseCommonJdbcDao.updateList(sql, objList, argTypes);
	}
	
	
	
}
