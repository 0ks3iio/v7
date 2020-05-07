package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisScoreInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Date: 2019/04/01
 *
 */
public interface DiathesisScoreInfoDao extends BaseJpaRepositoryDao<DiathesisScoreInfo, String> {

    List<DiathesisScoreInfo> findByTypeAndScoreTypeId(String type, String ScoreTypeId);

    List<DiathesisScoreInfo> findByStuIdAndScoreTypeIdIn(String studentId, String[] ScoreTypeIds);

    @Modifying
    void deleteByScoreTypeId(String scoreTypeId);

    @Query("From DiathesisScoreInfo where unitId=?1 and stuId=?2 ")
	List<DiathesisScoreInfo> findByUnitIdAndStudentId(String unitId, String studentId);

	List<DiathesisScoreInfo> findByScoreTypeIdIn(String[] scoreTypeIds);
	@Modifying
	void deleteByScoreTypeIdIn(String[] scoreTypeIds);

	@Query(nativeQuery = true,value = "select COUNT(*) from NEWDIATHESIS_SCORE_INFO where SCORE_TYPE_ID  in (select id from NEWDIATHESIS_SCORE_TYPE where UNIT_ID=?1 and type=?2)")
    Integer countByUnitIdAndType(String unitId, String type);

    @Query(nativeQuery = true,value = "select COUNT(*) from NEWDIATHESIS_SCORE_INFO where SCORE_TYPE_ID  in (select id from NEWDIATHESIS_SCORE_TYPE where UNIT_ID in ?1 and type=?2)")
    Integer countByUnitIdInAndType(List<String> unitIds, String type);

    @Query("from DiathesisScoreInfo where unitId=?1 and objId in (?2)")
    List<DiathesisScoreInfo> findByUnitIdAndProjectIdIn(String unitId, List<String> projectList);


    @Query("from DiathesisScoreInfo where scoreTypeId=?1 and stuId=?2")
    List<DiathesisScoreInfo> findByScoreTypeIdAndStuId(String scoreTypeId, String stuId);

    @Query("from DiathesisScoreInfo where unitId=?1 and objId in (?2) and scoreTypeId in (?3)")
    List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdIn(String unitId, List<String> projectIds, List<String> typeList);

    @Query("from DiathesisScoreInfo where unitId=?1 and objId in (?2) and scoreTypeId in (?3) and stuId=?4")
    List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdInAndStudentId(String unitId, List<String> projectIds, List<String> typeList, String studentId);

    @Query("from DiathesisScoreInfo where scoreTypeId=?1 and stuId=?2 and objId in (?3)")
    List<DiathesisScoreInfo> findByScoreTypeIdAndStuIdAndProjectIdIn(String scoreTypeId, String stuId, List<String> topIds);

    @Query("from DiathesisScoreInfo where unitId=?1 and objId in (?2) and scoreTypeId in (?3) and evaluateStuId=?4")
    List<DiathesisScoreInfo> findByUnitIdAndProjectIdInAndScoreTypeIdInAndEvaluateId(String unitId, List<String> projectIds, List<String> typeList, String evaluateStuId);
}
