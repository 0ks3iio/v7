package net.zdsoft.familydear.service;

import java.util.Date;
import java.util.List;

import net.zdsoft.activity.dto.FamilyMonthDto;
import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.familydear.entity.FamdearMonth;
import net.zdsoft.framework.entity.Pagination;

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
public interface FamdearMonthService extends BaseService<FamdearMonth, String> {
    public List<FamdearMonth> findListByActivityIds(String [] activityIds);

    public List<FamdearMonth> findListByArrangeIds(String [] arrangeIds);

    public List<FamdearMonth> findListByIdsPage(String [] ids,Pagination pagination);

    List<FamdearMonth> findListByTimePage(Date startTime, Date endTime,String createUserId,String state, String type, Pagination pagination);
    
    public FamilyMonthDto findListByTimeAndDeptId(FamilyMonthDto familyMonthDto,String unitId,String deptId,Date startTime,Date endTime);
}
