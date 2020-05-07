package net.zdsoft.bigdata.data.dao;

import net.zdsoft.bigdata.data.entity.CockpitChartStyle;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import java.util.List;

/**
 * @author shenke
 * @since 2018/8/7 上午11:12
 */
public interface CockpitChartStyleDao extends BaseJpaRepositoryDao<CockpitChartStyle, String> {

    void deleteByCockpitChartIdIn(String[] cockpitChartIds);

    List<CockpitChartStyle> getByCockpitChartIdIn(String[] cockpitChartIds);
}
