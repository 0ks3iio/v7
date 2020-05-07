/**
 * FileName: ChartDetailDao.java
 * Author:   shenke
 * Date:     2018/6/27 下午4:37
 * Descriptor:
 */
package net.zdsoft.bigdata.data.dao;

import java.util.List;

import net.zdsoft.bigdata.data.entity.ChartDetail;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;

/**
 * @author shenke
 * @since 2018/6/27 下午4:37
 */
public interface ChartDetailDao extends BaseJpaRepositoryDao<ChartDetail, String> {

    List<ChartDetail> getByChartId(String chartId);
}
