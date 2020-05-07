/**
 * FileName: ChartDaoTest.java
 * Author:   shenke
 * Date:     2018/5/25 上午11:00
 * Descriptor:
 */
package net.zdsoft;

import net.zdsoft.bigdata.data.dao.ChartDao;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.framework.entity.Pagination;
import org.junit.Test;
import org.springframework.data.domain.Page;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2018/5/25 上午11:00
 */
public class ChartDaoTest extends BaseTest {

    @Resource
    private ChartDao chartDao;

    @Test
    public void _testComplexNativeQuery() {
        String unitId = "402896E94700810F0147008B2FF30010";
        String userId = "4028801055234FF80155238815CB0025";
        Pagination pagination = new Pagination();
        pagination.setPageIndex(1);
        pagination.setPageSize(8);
        Page<Chart> pages =  chartDao.getCurrentUserEditCharts(userId, unitId, pagination.toPageable());

    }

    @Test
    public void _testComplexNativeQueryWithTags() {
        String unitId = "402896E94700810F0147008B2FF30010";
        String userId = "4028801055234FF80155238815CB0025";
        Pagination pagination = new Pagination();
        pagination.setPageIndex(1);
        pagination.setPageSize(8);
        String[] tagIds = new String[] {"80449035055092193012178608218622"};
        Page<Chart> pages =  chartDao.getCurrentUserEditCharts(userId, unitId, tagIds, pagination.toPageable());
    }
}
