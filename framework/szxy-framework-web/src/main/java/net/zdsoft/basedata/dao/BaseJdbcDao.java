package net.zdsoft.basedata.dao;

import java.util.List;

public interface BaseJdbcDao {

	public void execSql(String sql, Object[] objs);

	public List<Object[]> findBySql(String sql, Object[] params);

	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes);
}
