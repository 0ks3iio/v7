package net.zdsoft.eclasscard.data.dao;

import net.zdsoft.eclasscard.data.entity.EccAttenceGatePeriod;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface EccAttenceGatePeriodDao extends BaseJpaRepositoryDao<EccAttenceGatePeriod, String>{

//	@Query("From EclasscardGatePeriod where acadyear = ?1 and semester = ?2 and week = ?3 order by grade")
//	public List<EccAttenceGatePeriod> findListByWeek(String acadyear, String semester,
//			int week);

}
