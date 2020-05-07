package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.WarningResultStat;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface WarningResultStatDao extends BaseJpaRepositoryDao<WarningResultStat, String> {

    @Query("FROM WarningResultStat where unitId=?1")
    List<WarningResultStat> getWarningResultStatByUnitId(String unitId, Pageable page);

    Integer countByUnitId(String projectId);

    @Query(
            value = "select a.* from BG_WARNING_RESULT_STAT a INNER JOIN BG_WARNING_PROJECT b on a.PROJECT_ID = b.ID and " +
                    "a.UNIT_ID = :unitId and b.PROJECT_NAME like :projectName",
            countQuery = "select count(*) from (select a.* from BG_WARNING_RESULT_STAT a INNER JOIN BG_WARNING_PROJECT b on a.PROJECT_ID = b.ID and "
                        +"a.UNIT_ID = :unitId and b.PROJECT_NAME like :projectName)",
            nativeQuery = true
    )
    Page<WarningResultStat> getWarningProjectIdByUnitIdAndProjectName(@Param("unitId") String unitId, @Param("projectName") String projectName, Pageable page);

    void deleteWarningResultStatByProjectId(String projectId);
}
