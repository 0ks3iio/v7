package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;

import java.util.ArrayList;
import java.util.List;

/**
 * 水平基本柱状图
 * @author shenke
 * @since 2018/9/29 10:27
 */
public abstract class AbstractCommonStripeBarCreator extends AbstractBarCreator {

    protected void exchangeXY(Option option) {
        List<Cartesian2dAxis> xAxis = option.xAxis();
        List<Cartesian2dAxis> yAxis = option.yAxis();
        option.setXAxis(new ArrayList<>(yAxis));
        option.setYAxis(new ArrayList<>(xAxis));
        for (Series series : option.series()) {
            for (Object o : series.getData()) {
                if (o != null && ((BarData) o).getValue().getClass().isArray()) {
                    ((BarData) o).value(reverse((Object[]) ((BarData) o).getValue()));
                }
            }
        }
    }

    private Object[] reverse(Object[] array) {
        Object[] reverseArray = new Object[array.length];
        for (int i = 0, length = array.length; i < length; i++) {
            reverseArray[i] = array[Math.abs(length - i - 1)];
        }
        return reverseArray;
    }
}
