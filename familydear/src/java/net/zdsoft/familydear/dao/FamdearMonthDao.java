package net.zdsoft.familydear.dao;

import java.util.Date;
import java.util.List;

import net.zdsoft.familydear.entity.FamdearMonth;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

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
public interface FamdearMonthDao extends BaseJpaRepositoryDao<FamdearMonth, String> {
    @Query("FROM FamdearMonth WHERE activityId in (?1)")
    public List<FamdearMonth> findListByActivityIds(String [] activityIds);
    @Query("FROM FamdearMonth WHERE arrangeId in (?1)")
    public List<FamdearMonth> findListByArrangeIds(String [] arrangeIds);
    
    @Query("FROM FamdearMonth WHERE state=1 and unitId = ?1 and activityTime>=?2 and activityEndTime<=?3")
	public List<FamdearMonth> getListByTime(String unitId,Date startTime,Date endTime);
}
