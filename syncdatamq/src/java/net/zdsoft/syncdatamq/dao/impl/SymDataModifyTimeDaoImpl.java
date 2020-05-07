package net.zdsoft.syncdatamq.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import net.zdsoft.syncdatamq.dao.SymDataModifyTimeJdbcDao;

@Repository
public class SymDataModifyTimeDaoImpl implements SymDataModifyTimeJdbcDao {

	@Override
	public List<Object> findDatas(String tableName, Map<String, Object> paramMap) {
		return new ArrayList<Object>();
	}

}
