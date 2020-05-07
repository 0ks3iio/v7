package net.zdsoft.datareport.data.dao;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportColumn;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface DataReportColumnDao extends BaseJpaRepositoryDao<DataReportColumn,String>{

	@Query("From DataReportColumn where reportId = ?1 and type = ?2 order by columnIndex asc")
	List<DataReportColumn> findByIdAndType(String reportId, Integer type);

	@Modifying
	@Query("delete from DataReportColumn where reportId = ?1")
	void deleteByReportId(String reportId);

	List<DataReportColumn> findByReportId(String reportId);

}
