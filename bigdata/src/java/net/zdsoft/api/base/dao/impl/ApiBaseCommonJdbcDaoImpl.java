package net.zdsoft.api.base.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.zdsoft.api.base.dao.ApiBaseCommonJdbcDao;
import net.zdsoft.framework.dao.BaseDao;

import org.springframework.stereotype.Repository;

@Repository
public class ApiBaseCommonJdbcDaoImpl extends BaseDao<Object> implements ApiBaseCommonJdbcDao{

	@Override
	public Object setField(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes) {
		return batchUpdate(sql,objList,argTypes);
	}

}
