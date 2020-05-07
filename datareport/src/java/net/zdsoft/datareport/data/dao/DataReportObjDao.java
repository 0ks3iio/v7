package net.zdsoft.datareport.data.dao;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportObj;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface DataReportObjDao extends BaseJpaRepositoryDao<DataReportObj,String>{
	
	@Query("from DataReportObj Where reportId in (?1) ")
	List<DataReportObj> findByReportIds(String[] infoIds);

	List<DataReportObj> findByObjectId(String objectId);

	@Modifying
	@Query("delete from DataReportObj where reportId = ?1")
	void deleteByReportId(String reportId);

	@Query("from DataReportObj Where id = ?1")
	List<DataReportObj> findOneById(String objId);

	@Query("from DataReportObj Where id in (?1)")
	List<DataReportObj> findByObjIds(String[] objIds);

}
