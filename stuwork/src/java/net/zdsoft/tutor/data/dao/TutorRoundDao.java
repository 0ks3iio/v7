package net.zdsoft.tutor.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.tutor.data.entity.TutorRound;

/**
 * @author yangsj  2017年9月11日下午7:30:24
 */
public interface TutorRoundDao extends BaseJpaRepositoryDao<TutorRound, String> {
	String SQL_AFTER=" Order By creationTime desc";
	/**
	 * @param unitId
	 * @return
	 */
	@Query("From TutorRound  Where unitId = ?1 "+SQL_AFTER)
	List<TutorRound> findByUnitId(String unitId);
  
	
	
}
