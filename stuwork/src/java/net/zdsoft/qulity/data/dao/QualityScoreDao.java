package net.zdsoft.qulity.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.qulity.data.entity.QualityScore;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 
 */
public interface QualityScoreDao extends BaseJpaRepositoryDao<QualityScore, String> {
	
	@Query("From QualityScore  Where unitId = ?1 and type = ?2 and ROWNUM < 2")
	public QualityScore findByUnitIdOne(String unitId, String type);
	@Modifying
    @Query("delete from QualityScore Where unitId = ?1 and type = ?2")
	public void deleteByUnitIdAndType(String unitId, String type);

	public List<QualityScore> findByUnitIdAndType(String unitId, String type);

	public void deleteByUnitId(String unitId);
}
