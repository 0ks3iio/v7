package net.zdsoft.basedata.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import net.zdsoft.basedata.dao.BaseJdbcDao;
import net.zdsoft.framework.dao.BaseDao;
import net.zdsoft.framework.utils.JdbcUtils;
import net.zdsoft.framework.utils.MultiRowMapper;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Repository;

@Repository
public class BaseJdbcDaoImpl extends BaseDao<Object> implements BaseJdbcDao {
	
    @Override
    public void execSql(String sql, Object[] objs) {
        JdbcUtils.getSQL(sql, objs);
        update(sql, objs);
    }

	@Override
	public List<Object[]> findBySql(String sql, Object[] params) {
		
//		Pagination page = new Pagination();
//		page.setPageSize(1000);
//		page.setPageIndex(0);
		return query(sql, params, new MultiRowMapper<Object[]>() {
			@Override
			public Object[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResultSetMetaData meta = rs.getMetaData();
				Object[] os = new Object[meta.getColumnCount()];
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					Object o = rs.getObject(i);
					int columnType = meta.getColumnType(i);
					if (ArrayUtils.contains(new int[] { Types.DATE, Types.TIME, Types.TIMESTAMP, Types.TIME },
							columnType)) {
						o = rs.getTimestamp(i);
					}
					os[i - 1] = o;
				}
				return os;
			}
		});
	}

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
