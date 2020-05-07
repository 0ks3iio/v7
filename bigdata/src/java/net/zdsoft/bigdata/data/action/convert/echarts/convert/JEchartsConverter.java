/**
 * FileName: JEchartsConverter.java
 * Author:   shenke
 * Date:     2018/6/25 下午1:45
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import net.zdsoft.bigdata.data.echarts.EchartOptionWrap;
import net.zdsoft.bigdata.data.manager.api.Result;

import org.springframework.stereotype.Component;

/**
 * @author shenke
 * @since 2018/6/25 下午1:45
 */
@Component
public class JEchartsConverter implements DataFormatConvert<IChart<IChart.ChartConfig, Result>, EchartOptionWrap> {

    @Override
    public EchartOptionWrap convert(IChart<IChart.ChartConfig, Result> config) {

        return null;
    }
}
