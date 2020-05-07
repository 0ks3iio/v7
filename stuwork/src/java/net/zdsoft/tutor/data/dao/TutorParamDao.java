package net.zdsoft.tutor.data.dao;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.entity.TutorParam;

/**
 * @author yangsj  2017年9月11日下午8:13:25
 */
public interface TutorParamDao extends BaseJpaRepositoryDao<TutorParam, String> {


	/**
	 * @param unitId
	 * @param paramType
	 * @return
	 */
	@Query("From TutorParam  Where unitId = ?1 and paramType = ?2")
	TutorParam findByUnitIdAndPtype(String unitId, String paramType);

}
