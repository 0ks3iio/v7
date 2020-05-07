package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.WarningResultStat;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface WarningResultStatService extends BaseService<WarningResultStat, String> {

    List<WarningResultStat> getWarningResultStatByUnitId(String unitId, Pagination page);

    List<WarningResultStat> getWarningProjectIdByUnitIdAndProjectName(String unitId, String projectName, Pagination page);

    /**
     * 根据预警项目id删除
     * @param projectId
     */
    void deletWarnResultStatByProjectId(String projectId);
}
