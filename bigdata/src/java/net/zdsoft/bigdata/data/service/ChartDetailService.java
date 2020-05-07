/**
 * FileName: ChartDetailService.java
 * Author:   shenke
 * Date:     2018/6/27 下午4:37
 * Descriptor:
 */
package net.zdsoft.bigdata.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.bigdata.data.entity.ChartDetail;

/**
 * @author shenke
 * @since 2018/6/27 下午4:37
 */
public interface ChartDetailService extends BaseService<ChartDetail, String> {

    List<ChartDetail> getByChartId(String chartId);
}
