package net.zdsoft.api.base.dao;

import java.util.List;

public interface ApiBaseCommonJdbcDao {

	int[] updateList(String sql, List<Object[]> objList, int[] argTypes);

}
