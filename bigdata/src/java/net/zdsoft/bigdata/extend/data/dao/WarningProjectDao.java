package net.zdsoft.bigdata.extend.data.dao;

import net.zdsoft.bigdata.extend.data.entity.WarningProject;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by wangdongdong on 2018/7/9 13:44.
 */
public interface WarningProjectDao extends BaseJpaRepositoryDao<WarningProject, String> {

    @Query("FROM WarningProject where projectName like ?1 and unitId = ?2 order by modifyTime desc")
    List<WarningProject> findAllByPageAndProjectNameLike(Pageable page, String projectName, String unitId);

    @Query("FROM WarningProject where lastWarnDate is not null and projectName like ?1 order by modifyTime desc")
    List<WarningProject> findResultProjectByPage(Pageable pageable, String projectName);

    Integer countWarningProjectByLastWarnDateIsNotNullAndProjectNameLike(String projectName);

    @Query(value = "SELECT * FROM BG_WARNING_PROJECT where START_TIME < SYSDATE and (END_TIME > SYSDATE or END_TIME IS NULL)",
            nativeQuery = true)
    List<WarningProject> findAvailableProject();

    Long countByProjectNameLikeAndUnitIdEquals(String projectName, String unitId);

    @Query(
            value = "select * from bg_warning_project where project_name like ?1 and id in (?2) order by last_warn_date desc",
            countQuery = "select count(*) from bg_warning_project where project_name like ?1 and id in (?2)",
            nativeQuery = true
    )
    Page<WarningProject> findByProjectNameAndIdInPage(String projectName, String[] projectIds, Pageable page);
}
