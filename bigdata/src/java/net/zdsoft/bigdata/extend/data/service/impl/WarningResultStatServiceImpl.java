package net.zdsoft.bigdata.extend.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.bigdata.extend.data.dao.WarningResultStatDao;
import net.zdsoft.bigdata.extend.data.entity.WarningResultStat;
import net.zdsoft.bigdata.extend.data.service.WarningResultStatService;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:42.
 */
@Service
public class WarningResultStatServiceImpl extends BaseServiceImpl<WarningResultStat, String> implements WarningResultStatService {

    @Resource
    private WarningResultStatDao warningResultStatDao;

    @Override
    protected BaseJpaRepositoryDao<WarningResultStat, String> getJpaDao() {
        return warningResultStatDao;
    }

    @Override
    protected Class<WarningResultStat> getEntityClass() {
        return WarningResultStat.class;
    }

    @Override
    public List<WarningResultStat> getWarningResultStatByUnitId(String unitId, Pagination page) {
        Integer count = warningResultStatDao.countByUnitId(unitId);
        page.setMaxRowCount(count == null ? 0 : count.intValue());
        return warningResultStatDao.getWarningResultStatByUnitId(unitId, Pagination.toPageable(page));
    }

    @Override
    public List<WarningResultStat> getWarningProjectIdByUnitIdAndProjectName(String unitId, String projectName, Pagination page) {
        projectName = "%" + (StringUtils.isNotBlank(projectName) ? projectName : "") + "%";
        Page<WarningResultStat> result = warningResultStatDao.getWarningProjectIdByUnitIdAndProjectName(unitId, projectName, Pagination.toPageable(page));
        page.setMaxRowCount((int) result.getTotalElements());
        return result.getContent();
    }

    @Override
    public void deletWarnResultStatByProjectId(String projectId) {
        warningResultStatDao.deleteWarningResultStatByProjectId(projectId);
    }
}
