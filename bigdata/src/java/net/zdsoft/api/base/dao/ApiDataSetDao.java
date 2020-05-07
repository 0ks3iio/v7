package net.zdsoft.api.base.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.api.base.entity.eis.ApiDataSet;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface ApiDataSetDao extends BaseJpaRepositoryDao<ApiDataSet, String>{

	Integer countByNameLike(String dataSetName);

	@Query("FROM ApiDataSet where name like ?1 order by modifyTime desc")
	List<ApiDataSet> findByNameAndPage(Pageable pageable, String dataSetName);

	
	ApiDataSet findByMdId(String metadataId);

	
	List<ApiDataSet> findByMdIdIn(String[] metadataIds);

}
