package net.zdsoft.gkelective.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.gkelective.data.entity.GkCondition;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface GkConditionDao extends BaseJpaRepositoryDao<GkCondition,String>{
	@Query("From GkCondition where roundsId = ?1 and type = ?2 order by creationTime")
	public List<GkCondition> findByRoundsIdAndType(String roundsId, String type);
//	
	@Query("SELECT COUNT(*) From GkCondition where roundsId = ?1 and type = ?2 order by creationTime")
	public int findNumByRoundsIdAndType(String roundsId, String type);
	
	@Modifying
	@Query("DELETE FROM GkCondition WHERE id in (?1)")
	public void deleteByIds(String... ids);

	@Query("From GkCondition where roundsId = ?1 order by creationTime")
	public List<GkCondition> findByRoundsId(String roundsId);
	
	@Modifying
	@Query("DELETE FROM GkCondition where roundsId = ?1")
	public void deleteByRoundsId(String roundsId);
//	@Query("From GkCondition where subjectArrangeId in (?1) ")
//	public List<GkCondition> findByRoundsIdAndType(String[] ids);
}
