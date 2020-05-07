package net.zdsoft.familydear.dao;

import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.dao
 * @ClassName: FamdearActualReportDao
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 9:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 9:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface FamdearActualReportDao extends BaseJpaRepositoryDao<FamdearActualReport, String> {
	
	@Query("FROM FamdearActualReport WHERE state='1' and unitId =?1 and arriveTime>=?2 and arriveTime<=?3")
	public List<FamdearActualReport> getListByTime(String unitId,Date startTime,Date endTime);
	
	@Query("FROM FamdearActualReport WHERE state='1' and unitId =?1 and year=?2 and arriveTime>?3 and backTime<?4  and arrangeId in ?5")
	public List<FamdearActualReport> getListByUnitIdAndOthers(String unitId,String year,Date startDate,Date endDate,String[] arrangeIds);
}
