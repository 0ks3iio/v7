package net.zdsoft.stutotality.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.stutotality.data.entity.StutotalityType;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * stutotality_type
 * @author
 *
 */
public interface StutotalityTypeDao extends BaseJpaRepositoryDao<StutotalityType, String>{

    void deleteByUnitIdAndGradeId(String unitId,String gradeId);
    @Modifying
    @Query("delete from StutotalityType where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4")
    void deleteByCons(String unitId,String acadyear,String semester,String gradeId);

    @Query("From StutotalityType where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4 and hasStat=?5 ORDER BY orderNumber")
    List<StutotalityType> findByUnitIdAndAcadyearAndSemesterAndGradeIdAndHasStat(String unitId, String acadyear, String semester, String gradeId, Integer hasStat);

    @Query("From StutotalityType where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeId = ?4  ORDER BY orderNumber")
    List<StutotalityType> findByUnitIdAndAcadyearAndSemesterAndGradeId(String unitId, String acadyear, String semester, String gradeId);

    @Query("From StutotalityType where gradeId = ?1 ORDER BY orderNumber asc")
    public List<StutotalityType> findListByGradeId(String gradeId);

    public List<StutotalityType> findByUnitId(String unitId);

    @Query("From StutotalityType where unitId = ?1 and gradeId = ?2 ORDER BY orderNumber")
    public List<StutotalityType> findByUnitIdGradeId(String unitId,String gradeId);

}
