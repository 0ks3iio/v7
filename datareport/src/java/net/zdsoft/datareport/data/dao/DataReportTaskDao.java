package net.zdsoft.datareport.data.dao;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportTask;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DataReportTaskDao extends BaseJpaRepositoryDao<DataReportTask,String>{

	@Modifying
    @Query("update from DataReportTask set state = ?1 where id = ?2")
	void updateState(Integer state, String taskId);

	@Query("from DataReportTask where reportId = ?1 and isDeleted = 0 order by state asc")
	List<DataReportTask> findByReportId(String infoId);

	@Query("from DataReportTask where id = ?1")
	DataReportTask findOneById(String taskId);
	
	@Modifying
    @Query("delete from DataReportTask where reportId = ?1")
	void deleteByReportId(String infoId);

}
