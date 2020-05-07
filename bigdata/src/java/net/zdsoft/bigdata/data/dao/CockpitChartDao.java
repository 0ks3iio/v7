package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.CockpitChart;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

import org.springframework.data.jpa.repository.Query;

/**
 * @author ke_shen@126.com
 * @since 2018/4/9 下午6:18
 */
public interface CockpitChartDao extends BaseJpaRepositoryDao<CockpitChart, String> {

    /** 获取某一个大屏的cockpitCharts并根据order排序 */
    @Query(
            value = "from CockpitChart where cockpitId=?1 order by order"
    )
    List<CockpitChart> getCockpitChartsByCockpitId(String cockpitId);

    /** 获取特定大屏特定div的CockpitChart */
    CockpitChart getCockpitChartByCockpitIdAndDivId(String cockpitId, String divId);

    /** 删除大屏的某个div */
    void deleteByCockpitIdAndDivId(String cockpitId, String divId);

    /** 删除某个大屏下面的cockpitChart数据 */
    void deleteByCockpitId(String cockpitId);

    void deleteByChartIdIn(String[] chartId);

    void deleteByIdIn(String[] ids);
}
