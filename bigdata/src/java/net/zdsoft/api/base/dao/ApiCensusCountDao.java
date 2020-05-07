package net.zdsoft.api.base.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.api.base.entity.eis.ApiCensusCount;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ApiCensusCountDao extends BaseJpaRepositoryDao<ApiCensusCount, String>{

	@Query(value="From ApiCensusCount where key = ?1 and creationTime >= ?2 and creationTime <= ?3 ")
	List<ApiCensusCount> findByKeyAndDate(String key, Date startDate,Date endDate);

	@Query(value="From ApiCensusCount where creationTime >= ?1 and creationTime <= ?2 ")
	List<ApiCensusCount> findByDate(Date dayFirst, Date dayEnd);

	@Query(value="From ApiCensusCount where key = ?1 and value = ?2 and creationTime >= ?3 and creationTime <= ?4 ")
	List<ApiCensusCount> getInterfaceCounts(String key, String resultType,
			Date dayFirst, Date dayEnd);

}
