package net.zdsoft.evaluation.data.dao;

import java.util.List;

import net.zdsoft.evaluation.data.entity.TeachEvaluateRelation;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TeachEvaluateRelationDao extends BaseJpaRepositoryDao<TeachEvaluateRelation, String>{

	List<TeachEvaluateRelation> findByProjectId(String projectId);
	
	List<TeachEvaluateRelation> findByProjectIdIn(String[] projectIds);
	
	void deleteByProjectId(String projectId);
	
	void deleteByIdIn(String[] ids);
}
