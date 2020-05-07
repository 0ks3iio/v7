package net.zdsoft.eclasscard.data.dao;

import java.util.List;

import net.zdsoft.eclasscard.data.entity.EccTimingSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

public interface EccTimingSetDao extends BaseJpaRepositoryDao<EccTimingSet,String>{

	@Query("From EccTimingSet where unitId = ?1 order by orderIndex asc")
	List<EccTimingSet> findByUnitId(String unitId);

	void deleteByUnitId(String unitId);

}
