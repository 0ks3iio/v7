package net.zdsoft.scoremanage.data.service.impl;

import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.dao.HwPlanSetDao;
import net.zdsoft.scoremanage.data.entity.HwPlanSet;
import net.zdsoft.scoremanage.data.service.HwPlanSetService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:32
 */
@Service("hwPlanSetService")
public class HwPlanSetServiceImpl extends BaseServiceImpl<HwPlanSet, String> implements HwPlanSetService {

    @Autowired
    private HwPlanSetDao hwPlanSetDao;

    @Override
    protected BaseJpaRepositoryDao<HwPlanSet, String> getJpaDao() {
        return hwPlanSetDao;
    }

    @Override
    protected Class<HwPlanSet> getEntityClass() {
        return HwPlanSet.class;
    }

    @Override
    public void saveAndDelete(List<HwPlanSet> planSetList, String[] delIds, List<String> delPlans) {
        if(CollectionUtils.isNotEmpty(planSetList)) {
            hwPlanSetDao.saveAll(planSetList);
        }
        if(delIds!=null && delIds.length>0){
            hwPlanSetDao.deleteByIdIn(delIds);
        }
        if(CollectionUtils.isNotEmpty(delPlans)){
            hwPlanSetDao.deleteByHwPlanIdIn(delPlans);
        }
    }

    @Override
    public List<HwPlanSet> findListByPlanIdIn(List<String> planIds) {
        return hwPlanSetDao.findListByHwPlanIdIn(planIds);
    }

    @Override
    public void deleteByUnitIdAndHwPlanId(String unitId, String planId) {
        hwPlanSetDao.deleteByUnitIdAndHwPlanId(unitId, planId);
    }
}
