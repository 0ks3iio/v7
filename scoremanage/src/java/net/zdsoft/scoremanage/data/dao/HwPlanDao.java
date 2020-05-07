package net.zdsoft.scoremanage.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.HwPlan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:26
 */
public interface HwPlanDao extends BaseJpaRepositoryDao<HwPlan, String>{

    List<HwPlan> findByUnitIdAndAcadyearAndSemesterAndIsDeletedOrderByCreationTimeDesc(String unitId, String acadyear, String semester, int isDeleted, Pageable page);

    HwPlan findFirstByUnitIdAndAcadyearAndSemesterAndGradeIdAndExamIdAndIsDeletedOrderByCreationTimeDesc(String unitId, String acadyear, String semester, String gradeId, String examId, int isDeleted);

    int countByUnitIdAndAcadyearAndSemesterAndIsDeleted(String unitId, String acadyear, String semester, int isDeleted);

    List<HwPlan> findByUnitIdAndAcadyearAndSemesterAndExamIdInAndIsDeleted(String unitId, String acadyear, String semester, List<String> examIds, int isDeleted);
    List<HwPlan> findByUnitIdAndAcadyearAndSemesterAndExamIdIn(String unitId, String acadyear, String semester, List<String> examIds);


    @Query("from HwPlan where  gradeId in ?1 and isDeleted=0")
    List<HwPlan> findPlanListByGradeIdIn(String[] gradeIds);

    @Query("from HwPlan where unitId=?1 and  gradeId = ?2 and isDeleted=0")
    List<HwPlan> findPlanListByGradeId(String unitId, String gradeId);


    @Query(nativeQuery = true,
        value = "select * from scoremanage_hw_plan where is_deleted=0 and (unit_id || acadyear || semester || grade_id || exam_id) in (?1)"
    )
    List<HwPlan> findByOldConditions(String[] oldConditions);
}
