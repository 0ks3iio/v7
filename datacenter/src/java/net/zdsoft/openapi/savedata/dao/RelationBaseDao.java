package net.zdsoft.openapi.savedata.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.openapi.savedata.entity.RelationBase;

import org.springframework.data.jpa.repository.Query;

/**
 * @author yangsj  2018年8月2日上午10:54:36
 */
public interface RelationBaseDao extends BaseJpaRepositoryDao<RelationBase, String>{

	/**
	 * @param area
	 * @param type
	 * @param relationIds
	 * @return
	 */
	@Query("FROM RelationBase where area = ?1 and type = ?2 and relationId in ?3 ")
	List<RelationBase> findByAreaAndTypeAndRelationIdIn(String area, String type, String... relationIds);

}
