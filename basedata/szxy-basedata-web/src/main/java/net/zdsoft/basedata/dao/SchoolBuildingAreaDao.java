package net.zdsoft.basedata.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.SchoolBuildingArea;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author yangsj  2017-1-24下午5:17:10
 */
public interface SchoolBuildingAreaDao extends BaseJpaRepositoryDao<SchoolBuildingArea, String> {

	@Modifying
	@Query("delete from SchoolBuildingArea where id in (?1)")
	void deleteAllByIds(String... id);

}
