/**
 * FileName: DataFormatConvert.java
 * Author:   shenke
 * Date:     2018/5/28 下午5:24
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import net.zdsoft.bigdata.data.echarts.OptionEx;
import net.zdsoft.bigdata.data.manager.api.Result;

/**
 * 数据格式转换的抽象接口
 * @author shenke
 * @since 2018/5/28 下午5:24
 */
public interface DataFormatConvert<C extends IChart<IChart.ChartConfig, Result>, S extends OptionEx> {

    S convert(C config);

}
