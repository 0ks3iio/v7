package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.Database;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * @author ke_shen@126.com
 * @since 2018/4/8 下午5:04
 */
public interface DatabaseDao extends BaseJpaRepositoryDao<Database, String> {

	@Query("From Database where unitId = ?1 order by creationTime desc")
	public List<Database> findDatabasesByUnitId(String unitId);

}
