package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.WarningResultDao;
import net.zdsoft.bigdata.extend.data.entity.WarningResult;
import net.zdsoft.bigdata.extend.data.service.WarningResultService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class WarningResultServiceImpl extends BaseServiceImpl<WarningResult, String> implements WarningResultService {

    @Resource
    private WarningResultDao warningResultDao;

    @Override
    public List<WarningResult> getWarningResultByProjectId(String projectId, String unitId, Pagination page) {
        Integer count = warningResultDao.countByProjectIdAndUnitId(projectId, unitId);
        page.setMaxRowCount(count == null ? 0 : count.intValue());
        return warningResultDao.getWarningResultByProjectId(projectId, unitId, Pagination.toPageable(page));
    }

    @Override
    public void deleteByProjectIdAndUnitId(String projectId, String unitId) {
        warningResultDao.deleteByProjectIdAndUnitId(projectId, unitId);
    }

    @Override
    public Integer countWarningResultByProjectId(String projectId) {
        return warningResultDao.countByProjectId(projectId);
    }

    @Override
    public List<WarningResult> getWarningResultByBatchId(String batchId) {
        return warningResultDao.getWarningResultByBatchId(batchId);
    }
    
    @Override
    public void execSql(String sql, Object[] objs){
		warningResultDao.execSql(sql, objs);
    }

    @Override
    public List<WarningResult> findByUnitIdAndProjectIdAndStatus(String unitId, String projectId, Integer status) {
        return warningResultDao.findByUnitIdAndProjectIdAndStatus(unitId,projectId,status);
    }

    @Override
    protected BaseJpaRepositoryDao<WarningResult, String> getJpaDao() {
        return warningResultDao;
    }

    @Override
    protected Class<WarningResult> getEntityClass() {
        return WarningResult.class;
    }
}
