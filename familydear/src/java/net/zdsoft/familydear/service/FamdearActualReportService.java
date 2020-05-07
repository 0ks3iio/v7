package net.zdsoft.familydear.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.familydear.entity.FamDearActivity;
import net.zdsoft.familydear.entity.FamdearActualReport;
import net.zdsoft.framework.entity.Pagination;

import java.util.Date;
import java.util.List;

/**
 * @ProjectName: eis
 * @Package: net.zdsoft.familydear.service
 * @ClassName: FamdearActualReportService
 * @Description: java类作用描述
 * @Author: Sweet
 * @CreateDate: 2019/5/24 9:28
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/5/24 9:28
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public interface FamdearActualReportService extends BaseService<FamdearActualReport, String> {
    public List<FamdearActualReport> getListByArrangeIds(String [] ids,String createUserId,String state, Pagination pagination);
    
    public List<FamdearActualReport> getListByTime(String unitId,Date startTime,Date endTime);
    
    public List<FamdearActualReport> getListByUnitIdAndOthers(String unitId,String year,Date startDate,Date endDate,String[] arrangeIds,String state);

    public List<FamdearActualReport> getListByIdsPage(String [] ids, Pagination pagination);
}
