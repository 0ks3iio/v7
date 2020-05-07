package net.zdsoft.basedata.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.SchoolBuilding;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2017-1-24下午5:16:12
 */
public interface SchoolBuildingDao extends BaseJpaRepositoryDao<SchoolBuilding, String> {

	/**
	 * @param ids
	 */
	@Modifying
	@Query("DELETE  from SchoolBuilding   where  id in ?1")
	void deleteByInIds(String[] ids);

}
