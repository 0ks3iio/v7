package net.zdsoft.datareport.data.dao;

import java.util.List;

import net.zdsoft.datareport.data.entity.DataReportInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface DataReportInfoDao extends BaseJpaRepositoryDao<DataReportInfo,String>{
	
	@Modifying
	@Query("update from DataReportInfo set state = ?1 where id = ?2")
	void updateState(Integer state, String reportId);

	@Query("from DataReportInfo where state = ?1")
	List<DataReportInfo> findByState(Integer state);

	@Query("from DataReportInfo where state in(2,3) and isTimeSend = 0")
	List<DataReportInfo> findShowInfos();

	@Query("from DataReportInfo where title = ?1 and unitId = ?2")
	DataReportInfo findSameTitle(String infoTitle, String unitId);

	@Query("from DataReportInfo where id = ?1")
	DataReportInfo findOne(String infoId);

	@Modifying
	@Query("update from DataReportInfo set endTime = ?1,state = ?2 where id = ?3")
	void updateTime(String endTime, Integer state, String infoId);
}
