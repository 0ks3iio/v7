package net.zdsoft.scoremanage.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.scoremanage.data.entity.HwPlanSet;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:29
 */
public interface HwPlanSetService extends BaseService<HwPlanSet, String>{

    /**
     * 删除设置
     * @param unitId
     * @param planId
     */
    void deleteByUnitIdAndHwPlanId(String unitId, String planId);

    void saveAndDelete(List<HwPlanSet> planSetList, String[] delIds, List<String> delPlanIds);

    List<HwPlanSet> findListByPlanIdIn(List<String> planIds);
}
