package net.zdsoft.diathesis.data.dao;

import net.zdsoft.diathesis.data.entity.DiathesisScoreType;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Date: 2019/03/28
 *
 */
public interface DiathesisScoreTypeDao extends BaseJpaRepositoryDao<DiathesisScoreType, String> {

    @Query("from DiathesisScoreType where unitId=?1 and type=?2")
    List<DiathesisScoreType> findByUnitIdAndType(String unitId, String type);

    @Query("from DiathesisScoreType where unitId=?1 and gradeId=?2 and type=?3")
    List<DiathesisScoreType> findByUnitIdAndGradeIdAndType(String unitId, String gradeId, String type);

    DiathesisScoreType findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemester(String gradeId, String type, String scoreType, String gradeCode, Integer semester);

    @Query("from DiathesisScoreType where gradeId=?1 and type=?2 and gradeCode=?3 and semester=?4")
    DiathesisScoreType findByGradeIdAndTypeAndGradeCodeAndSemester(String gradeId, String type, String gradeCode, Integer semester);

    DiathesisScoreType findByGradeIdAndTypeAndScoreTypeAndGradeCodeAndSemesterAndExamName(String gradeId, String type, String scoreType, String gradeCode, Integer semester, String examName);

    List<DiathesisScoreType> findByGradeIdAndGradeCodeAndSemesterAndTypeAndScoreTypeInOrderByScoreType(String gradeId, String gradeCode, Integer semester, String type, String[] scoreType);

    @Query("From DiathesisScoreType where unitId =?1 and gradeId = ?2 and gradeCode = ?3 and semester = ?4 and type = ?5 order by scoreType")
	List<DiathesisScoreType> findListByGradeId(String unitId, String gradeId, String gradeCode, Integer semester,
			String type);

    @Query("from DiathesisScoreType where unitId in (?1) and gradeCode=?2 and year=?3 and semester=?4 and type='4'")
    List<DiathesisScoreType> findListByUnitIdsAndGradeCodeAndSemester(List<String> unitIds, String grade, String year, Integer semester);

    @Query("from DiathesisScoreType where gradeId=?1 and semester=?2 and type=?3 and scoreType=?4")
    List<DiathesisScoreType> findListByGradeIdAndSemesterAndTypeAndScoreType(String gradeId, String semester, String type, String scoreType);

    @Query("from DiathesisScoreType where unitId in (?1) and semester=?2 and year=?3")
    List<DiathesisScoreType> findListByUnitIdsAndSemesterAndYear(List<String> unitIds, Integer semester, String year);

    @Modifying
    @Query("delete from DiathesisScoreType where id in (?1)")
    void deleteByIds(String[] typeIds);

    @Query("select count(*) from DiathesisScoreType where unitId=?1 and type=?2 and scoreType in ?3")
    Integer countByUnitIdAndTypeAndScoreType(String unitId, String type, String[] scoreTypes);

    @Query("from DiathesisScoreType where unitId = ?1 and gradeId=?2 and type=?3 order by gradeCode desc ,semester desc ")
    List<DiathesisScoreType> findListByUnitIdAndGradeIdAndType(String unitId, String gradeId, String type);

	List<DiathesisScoreType> findListByUnitIdAndGradeIdAndTypeIn(String unitId, String gradeId, String[] types);

	@Query("from DiathesisScoreType where unitId = ?1 and type=?2 and gradeId=?3 and gradeCode=?4 and semester=?5 order by type ")
    List<DiathesisScoreType> findByUnitIdAndTypeAndGradeIdAndGradeCodeAndSemester(String unitId, String type, String gradeId, String gradeCode, Integer semester);
}
