package net.zdsoft.evaluation.data.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import net.zdsoft.evaluation.data.entity.TeachEvaluateItem;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachEvaluateItemDao extends BaseJpaRepositoryDao<TeachEvaluateItem, String>{

	@Query("FROM TeachEvaluateItem where unitId = ?1 and itemType =?2 and evaluateType =?3 and projectId =?4 order by itemNo")
	public List<TeachEvaluateItem> findByDto(String unitId,String itemType,String evaluateType,String projectId);
	
	@Query("FROM TeachEvaluateItem where unitId = ?1 and evaluateType =?2 and itemName =?3 and projectId =?4")
	public List<TeachEvaluateItem> findForCheck(String unitId,String evaluateType,String itemName,String projectId);
	
	@Query("FROM TeachEvaluateItem where unitId = ?1 and evaluateType =?2 and projectId = ?3 order by itemType, itemNo")
	public List<TeachEvaluateItem> findByUnitIdAndEvaluateType(String unitId,
			String evaluateType,String projectId);
	
	public List<TeachEvaluateItem> findByProjectId(String projectId);
}
