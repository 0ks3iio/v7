package net.zdsoft.evaluation.data.dao;

import java.util.List;

import net.zdsoft.evaluation.data.entity.TeachEvaluateItemOption;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TeachEvaluateItemOptionDao extends BaseJpaRepositoryDao<TeachEvaluateItemOption, String>{

	@Query("FROM TeachEvaluateItemOption where unitId =?1 and itemId in ?2 order by optionNo")
	public List<TeachEvaluateItemOption> findByItemIds(String unitId,String[] itemIds);
	
	@Query("FROM TeachEvaluateItemOption where unitId =?1")
	public List<TeachEvaluateItemOption> findByUnitId(String unitId);
	
	
	@Modifying
	@Query("Delete FROM TeachEvaluateItemOption where unitId =?1 and itemId =?2")
	public void delteByItemId(String unitId,String itemId);
	@Modifying
	@Query("Delete FROM TeachEvaluateItemOption where id in ?1")
	public void delteByIds(String[] ids);
}
