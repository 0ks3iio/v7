package net.zdsoft.bigdata.data.service;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.CockpitChartStyle;

import java.util.List;

/**
 * @author shenke
 * @since 2018/8/7 上午11:17
 */
public interface CockpitChartStyleService extends BaseService<CockpitChartStyle, String> {

    void deleteStyleByCockpitChartIds(String[] cockpitChartIds);

    List<CockpitChartStyle> getCockpitChartStylesByCockpitChartIds(String[] cockpitChartIds);
}
