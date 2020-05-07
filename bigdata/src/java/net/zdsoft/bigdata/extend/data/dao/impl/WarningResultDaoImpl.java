package net.zdsoft.bigdata.extend.data.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Repository;

import net.zdsoft.bigdata.extend.data.dao.WarningResultJdbcDao;
import net.zdsoft.bigdata.extend.data.entity.WarningResult;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.JdbcUtils;

@Repository
public class WarningResultDaoImpl extends BaseDao<WarningResult> implements WarningResultJdbcDao{

	@Override
	public WarningResult setField(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	 @Override
	    public void execSql(String sql, Object[] objs) {
	        JdbcUtils.getSQL(sql, objs);
	        update(sql, objs);
	    }

}
