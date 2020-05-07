package net.zdsoft.remote.openapi.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.remote.openapi.dao.BaseCommonJdbcDao;

@Repository
public class BaseCommonJdbcDaoImpl extends BaseDao<Object> implements BaseCommonJdbcDao{

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
