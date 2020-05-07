package net.zdsoft.syncdatamq.dao;

import java.util.List;
import java.util.Map;

public interface SymDataModifyTimeJdbcDao {

	List<Object> findDatas(String tableName,  Map<String, Object> paramMap);

}
