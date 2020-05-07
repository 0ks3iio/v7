package net.zdsoft.datacollection.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.datacollection.entity.DcProjectColumn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface DcProjectColumnDao extends BaseJpaRepositoryDao<DcProjectColumn, String> {
	
	List<DcProjectColumn> findByProjectId(String projectId);
	
	@Query("Delete DcProjectColumn Where projectId = ?1 and unitId = ?2")
	@Modifying
	void delete(String projectId, String unitId);

}
