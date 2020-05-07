package net.zdsoft.bigdata.metadata.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.bigdata.metadata.entity.QualityResultDetail;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface QualityResultDetailDao extends
		BaseJpaRepositoryDao<QualityResultDetail, String> {
	
	@Query(value = "delete from bg_md_quality_result_detail", nativeQuery = true)
	@Modifying
	public void deleteAll();
}
