package net.zdsoft.bigdata.data.service;

import java.util.List;
import java.util.Optional;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.CockpitChart;

/**
 *
 * @author ke_shen@126.com
 * @since 2018/4/9 下午6:18
 */
public interface CockpitChartService extends BaseService<CockpitChart, String> {

    /** 获取某一个大屏的所有cockpitChart 并根据order排序 */
    List<CockpitChart> getCockpitChartsByCockpitId(String cockpitId);

    /** 查询某一个大屏某个特定Div的CockpitChart，用于大屏设置 */
    Optional<CockpitChart> getCockpitChartsByCockpitIdAndDivId(String cockpitId, String divId);

    /** 删除大屏的某个div */
    void deleteCockpitChart(String cockpitId, String divId);

    /** 删除某个大屏的所有cockpitChart数据主要用于更换大屏模版 */
    void deleteCockpitChart(String cockpitId);

    /** 删除CockpitChart 由于删除了 chart */
    void deleteCockpitChartByChartId(String[] chartIds);

    void saveCockpitChart(CockpitChart cockpitChart);

    void deleteByIds(String[] ids);
}
