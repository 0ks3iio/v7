/**
 * FileName: OptionStyleUtils.java
 * Author:   shenke
 * Date:     2018/5/8 下午3:00
 * Descriptor:
 */
package net.zdsoft.bigdata.data.action.convert;

import java.util.List;

import net.zdsoft.bigdata.data.action.convert.echarts.axis.Axis;
import net.zdsoft.bigdata.data.action.convert.echarts.enu.Orient;
import net.zdsoft.bigdata.data.action.convert.echarts.op.Option;

/**
 * @author shenke
 * @since 2018/5/8 下午3:00
 */
public class OptionStyleUtils {

    public static Option convert2CockpitStyle(Option option) {
        option.title().show(false);
        List<Axis> axisList = option.getxAxis();
        if (axisList != null) {
            axisList.forEach(axis -> {
                axis.getAxisLine();
                axis.axisLine().show(Boolean.TRUE).lineStyle().setColor("#fff");
                axis.axisLabel().show(Boolean.TRUE).rotate(60).interval(0).textStyle().color("#fff");
                axis.splitLine().show(Boolean.FALSE);
                axis.axisTick().show(false);
            });
        }
        List<Axis> yAxisList = option.getyAxis();
        if (yAxisList != null) {
            yAxisList.forEach(yAxis -> {
                yAxis.splitLine().show(Boolean.FALSE);
                yAxis.axisLine().show(Boolean.TRUE).lineStyle().setColor("#fff");
                yAxis.axisLabel().show(true).interval(1).textStyle().color("#fff");
                yAxis.axisTick().show(false);
            });
        }
        option.legend().textStyle().color("#fff");
        option.legend().orient(Orient.vertical);
        option.legend().left("0");
        return option;
    }

    public static Option convert2EditStyle(Option option) {
        option.title().show(true);
        List<Axis> axisList = option.getxAxis();
        if (axisList != null) {
            axisList.forEach(axis -> {
                axis.axisLine().show(Boolean.TRUE);
                axis.axisLabel().show(Boolean.TRUE).rotate(60).interval(0);
                axis.splitLine().show(Boolean.FALSE);
                axis.axisTick().show(false);
            });
        }
        List<Axis> yAxisList = option.getyAxis();
        if (yAxisList != null) {
            yAxisList.forEach(yAxis -> {
                //yAxis.splitLine().show(true);
                yAxis.axisLine().show(true);
                yAxis.axisLabel().show(true).interval(1);
                yAxis.axisTick().show(false);
            });
        }
        option.legend().orient(Orient.vertical);
        option.legend().left("0");
        return option;
    }

    private static void showTooltip(Option option) {
        
    }
}
