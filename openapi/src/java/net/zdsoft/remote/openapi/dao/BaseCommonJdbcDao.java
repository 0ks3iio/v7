package net.zdsoft.remote.openapi.dao;

import java.util.List;

public interface BaseCommonJdbcDao {

	int[] updateList(String sql, List<Object[]> objList, int[] argTypes);

}
