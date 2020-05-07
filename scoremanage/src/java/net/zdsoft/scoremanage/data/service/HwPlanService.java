package net.zdsoft.scoremanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.scoremanage.data.entity.HwPlan;
import net.zdsoft.scoremanage.data.entity.HwStatis;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:29
 */
public interface HwPlanService extends BaseService<HwPlan, String>{

    /**
     * 根据参数 动态查询,获取所有的方案,
     *
     * 一个exam_id 对应多个方案的时候,取最后的那个方案,过滤其余的
     * @param unitId
     * @param gradeId
     * @param acadyear  可为空
     * @param semester  可为空
     * @return
     * @Author panlf
     */
    List<HwPlan> findListByGradeIdAcadyearAndSemester(String unitId,String gradeId, String acadyear, String semester);

    /**
     *  获得年级下所有的方案,每一场考试,保留最近的方案
     * @param gradeIds
     * @return
     * @Author panlf
     */
    List<HwPlan> findLastPlanListByGradeIdIn(String[] gradeIds);

    /**
     * 查询方案
     * @param unitId
     * @param acadyear
     * @param semester
     * @param page
     * @return
     */
    List<HwPlan> findListByAcadyearAndSemester(String unitId, String acadyear, String semester, Pagination page);

    /**
     * 查询最后保存的方案
     * @param unitId
     * @param acadyear
     * @param semester
     * @param gradeId
     * @param examId
     * @return
     */
    HwPlan findLastOne(String unitId, String acadyear, String semester, String gradeId, String examId);

    /**
     * 保存方案
     * @param addPlanList
     * @param addStatisList
     * @param addStatisExList
     */
    void savePlan(List<HwPlan> addPlanList, List<HwStatis> addStatisList, List<HwStatisEx> addStatisExList);

    /**
     * 删除方案
     * @param unitId
     * @param planId
     * @param userId
     */
    void deletePlan(String unitId, String planId, String userId);

    /**
     * 查询最后保存的方案列表
     * @param unitId
     * @param acadyear
     * @param semester
     * @param examIds
     * @return
     */
    List<HwPlan> findListByExamIds(String unitId, String acadyear, String semester, List<String> examIds);

    /**
     * 查询最后保存的方案列表
     * @param oldConditions
     * @return
     */
    List<HwPlan> findLastListByOldConditions(String[] oldConditions);

    /**
     * 查询最后保存的方案列表
     * @param unitId
     * @param gradeId
     * @Author panlf
     */
    List<HwPlan> findListByGradeId(String unitId, String gradeId);
}
