package net.zdsoft.basedata.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.zdsoft.basedata.entity.Grade;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

public interface GradeDao extends BaseJpaRepositoryDao<Grade, String> {

    public static final String SQL_AFTER = " and isDeleted = 0 and isGraduate = 0 order by section, openAcadyear desc";

    /**
     * 检索未毕业的年级信息
     *
     * @param unitId
     * @return
     */
    @Query("From Grade Where schoolId = ?1" + SQL_AFTER)
    List<Grade> findByUnitId(String unitId);

    @Query("From Grade Where id in (?1)" + SQL_AFTER)
    List<Grade> findByIdsIn(String[] ids);

    @Query("From Grade Where schoolId = ?1" + SQL_AFTER)
    List<Grade> findByUnitId(String unitId, Pageable page);

    @Query("From Grade Where schoolId = ?1 and isDeleted = 0 and isGraduate = 0 order by openAcadyear desc")
    List<Grade> findByUnitIdOrderByOpenAcadyear(String unitId);

    @Query("From Grade Where isDeleted = 0 and schoolId = ?1 and section = ?2 and openAcadyear = ?3")
    List<Grade> findBySectionAndAcadyear(String unitId, Integer section, String acadyear);

    @Query("From Grade Where isDeleted = 0 and schoolId = ?1 and openAcadyear = ?2")
    List<Grade> findBySchoolIdAndAcadyear(String unitId, String acadyear);

    @Query("From Grade Where schoolId = ?1 and section = ?2 and isDeleted = 0 and isGraduate = 0 order by openAcadyear desc")
    List<Grade> findBySectionAndAcadyear(String unitId, Integer section);

    @Query("From Grade Where isDeleted = 0 and schoolId = ?1 and section in (?2)")
    List<Grade> findByUnitId(String unitId, Integer... section);

    @Modifying
    @Query("update Grade set isDeleted=1 where id in (?1)")
    void updateIsDelete(String... ids);

    @Query("From Grade Where schoolId = ?1 and gradeCode in (?2) " + SQL_AFTER)
    List<Grade> findByUnitIdAndGradeCode(String unitId, String... gradeCode);

    @Query("from Grade where schoolId = ?1 and isDeleted=0 and (cast(substr(?2,6,9) AS integer) - cast(substr(openAcadyear,1,4) AS integer)) <= schoolingLength and (cast(substr(?2,1,4) AS integer) >= cast(substr(openAcadyear,1,4) AS integer)) and isGraduate=?3 order by  section, openAcadyear desc")
    List<Grade> findByUnitIdAndCurrentAcadyear(String unitid, String currentAcadyear, int isGraduate);

    @Query("from Grade where schoolId = ?1 and isDeleted=0 and (cast(substr(?2,6,9) AS integer) - cast(substr(openAcadyear,1,4) AS integer)) <= schoolingLength and (cast(substr(?2,1,4) AS integer) >= cast(substr(openAcadyear,1,4) AS integer)) order by  section, openAcadyear desc")
    List<Grade> findByUnitIdAndCurrentAcadyear(String unitid, String currentAcadyear);

    @Query("from Grade where schoolId = ?1 and isDeleted=0 and section in (?2) and (cast(substr(?3,6,9) AS integer) - cast(substr(openAcadyear,1,4) AS integer)) <= schoolingLength and (cast(substr(?3,1,4) AS integer) >= cast(substr(openAcadyear,1,4) AS integer)) order by section, openAcadyear desc")
    List<Grade> findByUnitIdAndCurrentAcadyearAndSection(String unitid,Integer[] sections, String currentAcadyear);

    @Query("from Grade where schoolId in (?1) and isDeleted=0 and (cast(substr(?2,6,9) AS integer) - cast(substr(openAcadyear,1,4) AS integer)) <= schoolingLength and (cast(substr(?2,1,4) AS integer) >= cast(substr(openAcadyear,1,4) AS integer)) order by schoolId, section, openAcadyear desc")
    List<Grade> findByUnitIdsAndCurrentAcadyear(String[] unitIds, String acadyear);

    @Query("from Grade where teacherId = ?1" + SQL_AFTER)
    List<Grade> findByTeacherId(String teacherId);

    @Modifying
    @Query("update Grade set isGraduate=1 ,modifyTime = ?1 where schoolId = ?2 and openAcadyear = ?3 and section = ?4 and schoolingLength = ?5")
    void updateGraduate(Date date, String schId, String acadYear, Integer section, Integer schoolingLength);

    @Query("from Grade where schoolId = ?1 and openAcadyear = ?2 and isDeleted = 0 and isGraduate = 0 and section in (?3) order by section")
    List<Grade> findBySchidSectionAcadyear(String schoolId, String curAcadyear, Integer[] section);

    @Query("From Grade Where schoolId = ?1 and section in (?2)" + SQL_AFTER)
    List<Grade> findByUnitIdNotGraduate(String schoolId, Integer... section);

    @Query("from Grade where schoolId = ?1 and id = ?2 and isDeleted=0 and (cast(substr(?3,6,9) AS integer) - cast(substr(openAcadyear,1,4) AS integer)) <= schoolingLength and (cast(substr(?3,1,4) AS integer) >= cast(substr(openAcadyear,1,4) AS integer))")
    Grade findByIdAndCurrentAcadyear(String unitId, String gradeId, String searchAcadyear);

    @Query("from Grade where schoolId = ?1 and isDeleted = 0 and isGraduate = ?2")
    List<Grade> findBySchoolIdAndIsGraduate(String unitId, Integer graduated);

    @Modifying
    @Query(
            value = "update Grade set isDeleted=0 where schoolId=?1"
    )
    void deleteGradesBySchoolId(String unitId);

    @Query("from Grade where schoolId in ?1 and isDeleted = 0 and isGraduate = 0 and openAcadyear = ?2")
    List<Grade> findBySchoolIdsAndOpenAcaday(String[] schoolIds, String openAcady);
}
