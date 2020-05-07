package net.zdsoft.newgkelective.data.dao;

import java.util.List;

public interface NewGkScoreResultJdbcDao {

	public List<Object[]> findCountByReferId(String unitId, String[] referScoreIds);

	public List<Object[]> findCountSubjectByReferId(String unitId, String[] referScoreIds);
}
