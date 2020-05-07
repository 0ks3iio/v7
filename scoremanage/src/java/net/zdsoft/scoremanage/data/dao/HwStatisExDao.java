package net.zdsoft.scoremanage.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:26
 */
public interface HwStatisExDao extends BaseJpaRepositoryDao<HwStatisEx, String> {

    void deleteByUnitIdAndHwPlanIdAndPlanTypeAndHwStatisIdIn(String unitId, String planId, String planType, String[] hwStatisIds);

    /**
     * object_type='01'  是 科目类型
     * @param planId
     * @return
     */
    @Query(nativeQuery = true,
            value = "select distinct hw_plan_id,object_id,object_name from scoremanage_hw_statis_ex where object_type='01' and  hw_plan_id = ?1")
    List<Object[]> findObjectListByPlanId(String planId);

    @Query(nativeQuery = true,
            value = "select distinct hw_plan_id,object_id,object_name from scoremanage_hw_statis_ex where unit_id=?1 and hw_plan_id in ?2 and object_type='01'")
    List<Object[]> findObjectListByPlanIdIn(String unitId,String[] planIds);


    void deleteByUnitIdAndHwPlanIdAndHwStatisIdIn(String unitId, String planId, String[] hwStatisIds);

    @Query("from HwStatisEx where unitId=?1 and planType='01' and hwPlanId in ?2")
    List<HwStatisEx> findListByUnitIdAndHwPlanIdIn(String unitId, List<String> planIds);

    /**
     * 删除汇总的年级成绩
     * @param unitId
     * @param gradeId
     */
    @Modifying
    @Query(value = "delete from scoremanage_hw_statis_ex where unit_id=?1 and plan_type=?3 and hw_statis_id in (" +
            "select id from scoremanage_hw_statis where unit_id=?1 and grade_id=?2 and plan_type=?3)"
            ,nativeQuery = true)
    void deleteCollectByUnitIdAndGradeIdAndPlanType(String unitId, String gradeId, String planType);

    @Query("from HwStatisEx where unitId=?1 and planType='02' and hwPlanId in ?2")
    List<HwStatisEx> findListByUnitIdAndHwPlanIds(String unitId, String[] planIds);

    @Modifying
    @Query(value = "delete from scoremanage_hw_statis_ex where unit_id=?1 and plan_type=?3 and hw_plan_id in ?2"
            ,nativeQuery = true)
    void deleteByUnitIdAndHwPlanIdsAndPlanType(String unitId, List<String> planIds, String planType);
}
