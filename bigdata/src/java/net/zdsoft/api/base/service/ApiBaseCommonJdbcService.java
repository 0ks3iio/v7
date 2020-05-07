package net.zdsoft.api.base.service;

import java.util.List;

public interface ApiBaseCommonJdbcService {
	
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes);

}
