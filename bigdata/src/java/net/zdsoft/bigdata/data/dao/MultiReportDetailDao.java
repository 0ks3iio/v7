package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.MultiReportDetail;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author 未知
 */
public interface MultiReportDetailDao extends BaseJpaRepositoryDao<MultiReportDetail	, String> {

	@Query("From MultiReportDetail where reportId = ?1 order by orderId ")
	public List<MultiReportDetail> findMultiReportDetailsByReportId(String reportId);
	
	public void deleteMultiReportDetailsByReportId(String reportId);
	

	 @Query("select max(orderId) from MultiReportDetail where reportId = ?1")
	 public Integer getMaxOrderIdByReportId(String reportId);

	 @Query("select reportId from MultiReportDetail where id=?1")
     String findReportIdById(String componentId);
}
	