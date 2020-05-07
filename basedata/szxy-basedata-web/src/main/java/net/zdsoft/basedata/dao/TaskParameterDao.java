package net.zdsoft.basedata.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.TaskParameter;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface TaskParameterDao extends BaseJpaRepositoryDao<TaskParameter, String>{

	@Query("From TaskParameter where jobId in (?1)")
	List<TaskParameter> findList(String... jobIds);

	@Modifying
	@Query("delete from TaskParameter where id in (?1)")
	void deleteAllByIds(String... ids);

}
