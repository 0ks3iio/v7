package net.zdsoft.base.service;

import java.util.List;

public interface BaseCommonJdbcService {
	
	public int[] updateList(String sql, List<Object[]> objList, int[] argTypes);

}
