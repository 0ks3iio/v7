package net.zdsoft.scoremanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.dto.SubjectDto;
import net.zdsoft.scoremanage.data.entity.HwStatisEx;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:29
 */
public interface HwStatisExService extends BaseService<HwStatisEx, String>{

    List<SubjectDto> findObjectListByPlanId(String planIds);


    /**
     * 查询成绩和排名详情
     * @param unitId
     * @param planId
     * @param planType
     * @param statisIds
     * @return
     */
    List<HwStatisEx> findListByStatisIds(String unitId, String planId, String planType, String[] statisIds);

    /**
     * 删除详情数据
     * @param unitId
     * @param planId
     * @param planType
     * @param hwStatisIds
     */
    void deleteByUnitIdAndHwPlanIdAndHwStatisIds(String unitId, String planId, String planType, String[] hwStatisIds);

    /**
     * 查询成绩和排名详情
     * @param unitId
     * @param planIds
     * @param planType
     * @param statisIds
     * @return
     */
    List<HwStatisEx> findListByPlanIdsAndStatisIds(String unitId, String[] planIds, String planType, String[] statisIds);

    List<SubjectDto> findObjectListByPlanIdIn(String unitId,String[] planIds);

    /**
     * 按年级id 删除所有的统计数据
     * @param unitId
     * @param gradeId
     */
    void deleteCollectByUnitIdAndGradeIdAndPlanType(String unitId, String gradeId,String planType);

    List<HwStatisEx> findListByUnitIdAndPlanIdIn(String unitId, List<String> planIds);

    /**
     * 只有 汇总数据  planType='02'
     * @param unitId
     * @param planIds
     * @return
     */
    List<HwStatisEx> findListByUnitIdAndPlanIds(String unitId, String[] planIds);

    void deleteByUnitIdAndHwPlanIdsAndPlanType(String unitId, List<String> planIds, String planType);
}
