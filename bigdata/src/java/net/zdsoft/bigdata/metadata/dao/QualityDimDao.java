package net.zdsoft.bigdata.metadata.dao;

import java.util.List;

import net.zdsoft.bigdata.metadata.entity.QualityDim;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * 质量维度 dao
 * @author feekang
 *
 */
public interface QualityDimDao extends BaseJpaRepositoryDao<QualityDim, String>{

	@Query("From QualityDim where type = ?1 order by orderId")
	public List<QualityDim> findQualityDimsByType(Integer type);
	
}
