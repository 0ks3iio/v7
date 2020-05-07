package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.WarningResult;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface WarningResultDao extends BaseJpaRepositoryDao<WarningResult, String>,WarningResultJdbcDao {

    @Query("FROM WarningResult where projectId=?1 and unitId=?2 order by warnDate desc")
    List<WarningResult> getWarningResultByProjectId(String projectId, String unitId, Pageable page);

    Integer countByProjectIdAndUnitId(String projectId, String unitId);

    void deleteByProjectIdAndUnitId(String projectId, String unitId);

    List<WarningResult> getWarningResultByBatchId(String batchId);

    Integer countByProjectId(String projectId);

    List<WarningResult> findByUnitIdAndProjectIdAndStatus(String unitId, String projectId, Integer status);
}
