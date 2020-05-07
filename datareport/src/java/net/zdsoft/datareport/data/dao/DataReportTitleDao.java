package net.zdsoft.datareport.data.dao;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportTitle;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DataReportTitleDao extends BaseJpaRepositoryDao<DataReportTitle,String>{

	List<DataReportTitle> findByReportId(String infoId);

	@Modifying
	@Query("delete from DataReportTitle where reportId = ?1")
	void deleteByReportId(String reportId);

	@Query("From DataReportTitle where type = ?1 and reportId = ?2")
	DataReportTitle findByTypeAndId(Integer type, String reportId);

}
