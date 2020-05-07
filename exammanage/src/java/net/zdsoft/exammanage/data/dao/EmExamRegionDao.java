package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmRegion;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmExamRegionDao extends BaseJpaRepositoryDao<EmRegion, String> {

    @Query("From EmRegion where examId = ?1 and unitId=?2 order by examRegionCode")
    public List<EmRegion> findByExamIdAndUnitId(String examId, String unitId);

    @Query("From EmRegion where examRegionCode = ?1 and examId=?2")
    public List<EmRegion> findByRegionCodeAndExamId(String regionCode, String examId);

    @Query("From EmRegion where examId=?1 and regionCode like ?2% and unitId =?3 order by examRegionCode")
    public List<EmRegion> findByExamIdAndLikeCode(String examId, String regionCode, String unitId);

    public List<EmRegion> findByExamId(String examId);
}
