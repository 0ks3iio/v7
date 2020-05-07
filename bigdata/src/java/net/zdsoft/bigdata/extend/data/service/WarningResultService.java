package net.zdsoft.bigdata.extend.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.extend.data.entity.WarningResult;
import net.zdsoft.framework.entity.Pagination;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:41.
 */
public interface WarningResultService extends BaseService<WarningResult, String> {

    List<WarningResult> getWarningResultByProjectId(String projectId, String unitId, Pagination page);

    void deleteByProjectIdAndUnitId(String projectId, String unitId);

    Integer countWarningResultByProjectId(String projectId);

    List<WarningResult> getWarningResultByBatchId(String batchId);
    
    /**
     * 执行语法
     * 
     * @param sql
     * @param objs
     *            对应sql中的参数
     */
    public void execSql(String sql, Object[] objs);

    List<WarningResult> findByUnitIdAndProjectIdAndStatus(String unitId, String projectId, Integer status);
}
