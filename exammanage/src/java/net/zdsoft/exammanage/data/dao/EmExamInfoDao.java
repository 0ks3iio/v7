package net.zdsoft.exammanage.data.dao;

import net.zdsoft.exammanage.data.entity.EmExamInfo;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface EmExamInfoDao extends BaseJpaRepositoryDao<EmExamInfo, String> {

    @Query("select t1 from EmExamInfo as t1,EmJoinexamschInfo as t2 where t1.id=t2.examId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3  and t1.isDeleted = 0 order by t1.creationTime desc, t1.examCode desc")
    List<EmExamInfo> findExamInfoJoinList(String unitId, String searchAcadyear, String searchSemester);

    @Query("select examCode from EmExamInfo order by examCode desc")
    List<String> findExamCodeMax();

    @Query("select t1 from EmExamInfo as t1,EmJoinexamschInfo as t2 where t1.id=t2.examId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3  and t1.creationTime >= ?4   and t1.isDeleted = 0 order by t1.creationTime desc, t1.examCode desc")
    List<EmExamInfo> findExamInfoJoinListByDayNew(String unitId,
                                                  String searchAcadyear, String searchSemester, Date date);

    @Query("select t1 from EmExamInfo as t1,EmJoinexamschInfo as t2 where t1.id=t2.examId and t2.schoolId=?1 and t1.acadyear = ?2 and t1.semester = ?3  and t1.creationTime < ?4  and t1.isDeleted = 0 order by t1.creationTime desc, t1.examCode desc")
    List<EmExamInfo> findExamInfoJoinListByDayOld(String unitId,
                                                  String searchAcadyear, String searchSemester, Date date);

    @Modifying
    @Query("update EmExamInfo set isDeleted=1 where id in (?1)")
    void updateIsDelete(String[] ids);

    @Query("select t1 from EmExamInfo as t1,EmJoinexamschInfo as t2 where t1.id=t2.examId and t2.schoolId=?1 and t1.creationTime >= ?2   and t1.isDeleted = 0 order by t1.creationTime desc, t1.examCode desc")
    List<EmExamInfo> findExamInfoJoinListByDayNew(String unitId, Date date);

    @Query("select t1 from EmExamInfo as t1,EmJoinexamschInfo as t2 where t1.id=t2.examId and t2.schoolId=?1 and t1.creationTime < ?2  and t1.isDeleted = 0 order by t1.creationTime desc, t1.examCode desc")
    List<EmExamInfo> findExamInfoJoinListByDayOld(String unitId, Date date);

    @Query("From EmExamInfo where unitId = ?1 and acadyear = ?2 and semester = ?3 and isDeleted = 0 order by examCode desc")
    List<EmExamInfo> findByUnitIdAndAcadyear(String unitId, String acadyear, String searchSemester);

    @Query("From EmExamInfo where unitId = ?1 and acadyear = ?2 and semester = ?3 and gradeCodes=?4 and isDeleted = 0 order by examCode desc")
    List<EmExamInfo> findByUnitIdAndAcadAndGradeId(String unitId, String acadyear, String searchSemester, String gradeCode);

    @Query("From EmExamInfo where id in (?1) and semester = ?2 and isDeleted = 0 order by examCode desc")
    public List<EmExamInfo> findBySemesterAndIdIn(String[] ids, String searchSemester);

    @Query("From EmExamInfo where id in (?1)  and isDeleted = 0 ")
    public List<EmExamInfo> findByIdIn(String[] ids);

    List<EmExamInfo> findByOriginExamId(String originExamId);

    @Query("From EmExamInfo where id in (?1)  and isDeleted = 0 order by examCode desc")
    List<EmExamInfo> findListByIdsNoDel(String[] ids);

    List<EmExamInfo> findByAcadyearAndSemesterAndGradeCodes(String acadyear, String searchSemester, String gradeCode);

    List<EmExamInfo> findByAcadyearAndSemester(String acadyear, String searchSemester);
}
