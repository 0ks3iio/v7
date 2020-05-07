package net.zdsoft.remote.openapi.service;

import java.util.List;

public interface BaseCommonJdbcService {
	
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes);

}
