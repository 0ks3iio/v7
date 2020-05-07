package net.zdsoft.scoremanage.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.HwStatis;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:26
 */
public interface HwStatisDao extends BaseJpaRepositoryDao<HwStatis, String> {

    List<HwStatis> findByUnitIdAndHwPlanIdAndPlanTypeOrderByClaCodeAscStuCodeAsc(String unitId, String planId, String planType);

    List<HwStatis> findByUnitIdAndHwPlanIdAndPlanTypeOrderByClaCodeAscStuCodeAsc(String unitId, String planId, String planType, Pageable page);

    int countByUnitIdAndHwPlanIdAndPlanType(String unitId, String planId, String planType);

    List<HwStatis> findByUnitIdAndHwPlanIdInAndPlanType(String unitId, String[] planIds, String planType);

    HwStatis findByUnitIdAndHwPlanIdAndPlanTypeAndStudentId(String unitId, String planId, String planType, String studentId);

    @Modifying
    @Query("delete from HwStatis where unitId=?1 and gradeId=?2 and planType=?3")
    void deleteCollectByUnitIdAndGradeIdAndPlanType(String unitId, String gradeId, String planType);


    /**
     * 只查询统计数据
     * @param unitId
     * @param planIds
     * @return
     */
    @Query("from HwStatis where unitId=?1  and planType='01' and hwPlanId in ?2 ")
    List<HwStatis> findListByUnitIdAndHwPlanIdIn(String unitId, List<String> planIds);

    @Query("from HwStatis where unitId=?1 and planType=?2 and studentId = ?3 ")
    List<HwStatis> findListByUnitIdAndPlanTypeAndStudentId(String unitId, String planType, String studentId);

    @Query("from HwStatis where unitId=?1 and planType=?2 and classId = ?3 ")
    List<HwStatis> findListByUnitIdAndPlanTypeAndClassId(String unitId, String planType, String classId);

    @Query(nativeQuery = true,
            value = "select * from scoremanage_hw_statis where unit_id = ?1 and plan_type = ?2 and (hw_plan_id || student_id) in (?3)"
    )
    List<HwStatis> findByConditions(String unitId, String planType, List<String> conditions);


    @Query(nativeQuery = true,
            value = "select count(*) from scoremanage_hw_statis_ex where unit_id=?1 and hw_statis_id in (" +
                    "select id from scoremanage_hw_statis where unit_id=?1 and plan_type=?2 and class_id=?3)"
    )
    Integer countDataByPlanTypeAndClassId(String unitId, String planType, String classId);

    @Query(nativeQuery = true,
        value = "select count(*) from scoremanage_hw_statis_ex where unit_id=?1 and hw_statis_id in (" +
                 "select id from scoremanage_hw_statis where unit_id=?1 and plan_type=?2 and student_id=?3)"
    )
    Integer countDataByPlanTypeAndStuId(String unitId, String planType, String stuId);

    @Modifying
    @Query("delete from HwStatis where unitId=?1 and planType=?3 and hwPlanId in ?2" )
    void deleteByUnitIdAndHwPlanIdsAndPlanType(String unitId, List<String> planIds, String planType);
}
