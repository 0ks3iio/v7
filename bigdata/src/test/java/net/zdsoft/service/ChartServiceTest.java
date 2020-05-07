/**
 * FileName: ChartServiceTest
 * Author:   shenke
 * Date:     2018/4/24 下午2:44
 * Descriptor:
 */
package net.zdsoft.service;

import net.zdsoft.BaseTest;
import net.zdsoft.bigdata.data.ChartBusinessType;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.service.ChartService;
import org.junit.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shenke
 * @since 2018/4/24 下午2:44
 */
@Rollback
@Transactional(transactionManager = "txManagerJap")
public class ChartServiceTest extends BaseTest {

    @Resource
    private ChartService chartService;

    @Test
    public void testSort() {
        List<Chart> chartList = chartService.getChartsByUnitIdAndBusinessTypeAndSort("402896E94700810F0147008B2FF30010", ChartBusinessType.CHART.getBusinessType());
        for (Chart e : chartList) {
            System.out.println(e.getModifyTime().getTime());
        }
    }
}
