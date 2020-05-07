package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkTeachClassStuStore;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkTeachClassStuStoreDao extends BaseJpaRepositoryDao<GkTeachClassStuStore, String>{

	@Modifying
	@Query("DELETE FROM GkTeachClassStuStore WHERE gkClassId in (?1)")
	public void deleteByClassIds(String[] classIds);
	
	@Query("FROM GkTeachClassStuStore WHERE gkClassId in (?1)")
	public List<GkTeachClassStuStore> findByClassIdIn(String[] classIds);

	@Query("select t2 from GkTeachClassStore t1,GkTeachClassStuStore t2 where t1.id=t2.gkClassId and t1.roundsId = ?1 and t2.studentId in (?2)")
	public List<GkTeachClassStuStore> findByStuIds(String roundId, String[] stuIds);
	
	@Modifying
	@Query("DELETE FROM GkTeachClassStuStore WHERE gkClassId in (?1) and studentId in (?2)")
	public void delete(String[] classIds, String[] stuIds);

}
