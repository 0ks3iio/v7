package net.zdsoft.scoremanage.data.dao;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.scoremanage.data.entity.HwPlanSet;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author niuchao
 * @date 2019/11/5 11:26
 */
public interface HwPlanSetDao extends BaseJpaRepositoryDao<HwPlanSet, String> {

    @Modifying
    @Query("delete from HwPlanSet where id in ?1")
    void deleteByIdIn(String[] delIds);
    @Modifying
    @Query("delete from HwPlanSet where hwPlanId in ?1")
    void deleteByHwPlanIdIn(List<String> delPlans);

    void deleteByUnitIdAndHwPlanId(String unitId, String planId);

    @Query("from HwPlanSet where hwPlanId in ?1")
    List<HwPlanSet> findListByHwPlanIdIn(List<String> planIds);
}
