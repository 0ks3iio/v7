package net.zdsoft.datareport.data.dao;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportResults;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface DataReportResultsDao extends BaseJpaRepositoryDao<DataReportResults,String>{

	@Query("from DataReportResults Where taskId = ?1 order by rowIndex asc")
	List<DataReportResults> findByTaskId(String taskId);

	@Modifying
	@Query("delete from DataReportResults Where taskId = ?1")
	void deleteByTaskId(String taskId);

	@Modifying
	@Query("delete from DataReportResults Where taskId = ?1 and type = ?2")
	void deleteByTaskIdAndType(String taskId,Integer type);
	
	@Query("from DataReportResults Where reportId = ?1 and type = ?2 order by rowIndex asc")
	List<DataReportResults> findByInfoIdAndType(String infoId, Integer type);

	@Modifying
	@Query("delete from DataReportResults Where reportId = ?1")
	void deleteByReportId(String infoId);

	@Query("from DataReportResults Where taskId = ?1 and type = ?2 order by rowIndex asc")
	List<DataReportResults> findByTaskIdAndType(String taskId,Integer type);

}
