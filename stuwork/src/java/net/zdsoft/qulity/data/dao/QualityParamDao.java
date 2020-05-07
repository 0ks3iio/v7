package net.zdsoft.qulity.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.qulity.data.entity.QualityParam;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 
 */
public interface QualityParamDao extends BaseJpaRepositoryDao<QualityParam, String> {


	/**
	 * @param unitId
	 * @param paramTypes
	 * @return
	 */
	@Query("From QualityParam  Where unitId = ?1 and paramType in (?2)")
	public QualityParam findByUnitIdAndPtype(String unitId, String... paramTypes);

	@Query("From QualityParam  Where unitId = ?1")
	public List<QualityParam> findByUnitId(String unitId);

    public List<QualityParam> findByUnitIdAndParamType(String unitId, String paramType);

    QualityParam findByUnitIdAndParamTypeAndGradeId(String unitId, String paramType, String gradeId);
}
