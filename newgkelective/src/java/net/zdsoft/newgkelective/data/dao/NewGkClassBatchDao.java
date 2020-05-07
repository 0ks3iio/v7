package net.zdsoft.newgkelective.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;

/**
 * 
 * @author weixh
 * @since 2018年6月12日 上午10:00:27
 */
public interface NewGkClassBatchDao extends BaseJpaRepositoryDao<NewGkClassBatch, String> {

	List<NewGkClassBatch> findByDivideIdAndBatch(String divideId, String batch);

	@Query("from NewGkClassBatch where divideId = ?1 and batch=?2 and subjectId = ?3")
	List<NewGkClassBatch> findByBatchAndSubjectId(String divideId, String batch, String subjectId);

	@Modifying
	@Query("delete from NewGkClassBatch where divideId =?1 and divideClassId in (?2) ")
	void deleteByDivideIdAndDivideClassIdIn(String divideId, String[] divideClsIds);
	
	@Modifying
	@Query("delete from NewGkClassBatch where divideId =?1 and divideClassId = ?2 and subjectIds = ?3")
	void deleteByDivideIdAndDivideClsIdSubIds(String divideId, String divideClsId, String subIds);
	
	void deleteByDivideId(String divideId);

    // Basedata Sync Method
    void deleteBySubjectIdIn(String... subjectIds);
    
    @Query("select count(id) from NewGkClassBatch where divideId = ?1") 
	Object countByDivideId(String divideId);

    @Modifying
    @Query("delete from NewGkClassBatch where divideId =?1 and divideClassId in (?2) and subjectType =?3")
	void deleteByClasIdAndSubjectType(String divideId, String[] divideClsIds, String planType);
    
    List<NewGkClassBatch> findByDivideIdAndSubjectType(String divideId, String subjectType);
}
