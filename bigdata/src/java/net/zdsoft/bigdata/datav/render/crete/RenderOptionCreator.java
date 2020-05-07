package net.zdsoft.bigdata.datav.render.crete;

import avro.shaded.com.google.common.collect.Lists;
import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.Colors;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.style.LegendTextStyle;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/27 16:25
 */
public interface RenderOptionCreator<T> {

    /**
     *
     * @param result API SQL 或者其他数据源查询得到的JSON数据的封装
     * @param diagram 图表对象
     * @param earlyParameters 某些类型的图表在 Create Option的时候需要series的参数或者其他类型的参数
     * @return RenderOption的实现
     * @throws EntryUtils.DataException 数据转换异常
     */
    T create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException;

    /**
     * 在渲染实际的参数之前某些参数给定默认值
     * @param option
     */
    default void beforeRenderDefaultGloableTextStyleForECharts(Object option, Integer diagramType) {
        if (option instanceof EChartsRenderOption) {
            String colorWhite = "#ffffff";

            Option op = ((EChartsRenderOption) option).getOp();
            if (op == null) {
                return;
            }
            op.textStyle().color(colorWhite);
            //图例字体默认颜色
            if (!DiagramEnum.GRAPH.getType().equals(diagramType)) {
                LegendTextStyle legendTextStyle = new LegendTextStyle();
                legendTextStyle.color(colorWhite);
                op.legend().textStyle(legendTextStyle);
            }
            //x轴y轴字体默认颜色
            for (Cartesian2dAxis cartesian2dAxis : op.xAxis()) {
                cartesian2dAxis.axisLine().lineStyle().color(colorWhite);
                cartesian2dAxis.axisTick().show(Boolean.FALSE);
            }
            for (Cartesian2dAxis cartesian2dAxis : op.yAxis()) {
                cartesian2dAxis.axisLine().lineStyle().color(colorWhite);
                cartesian2dAxis.axisTick().show(Boolean.FALSE);
            }
            op.color(Colors.colors);
            if (op.getSeries() == null) {
                return;
            }
            for (Series series : op.getSeries()) {
                series.setId(series.getName());
            }
        }
    }
}
