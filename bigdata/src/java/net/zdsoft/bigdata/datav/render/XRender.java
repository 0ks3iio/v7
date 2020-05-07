package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupX;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisPosition;
import net.zdsoft.echarts.enu.FontWeightEx;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.BarData;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/28 19:08
 */
public class XRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.x, diagram.getDiagramType())) {
            return;
        }
        GroupX groupX = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.x, GroupX.class);
        if (groupX == null) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Option option = eChartsRenderOption.getOp();
        List<Cartesian2dAxis> axes = option.xAxis();
        double max = 0d;
        if (DiagramEnum.STRIPE_PN_BAR.getType().equals(diagram.getDiagramType())) {
            for (Series series : option.getSeries()) {

                for (Object datum : series.getData()) {
                    Object value = ((Object[]) ((BarData) datum).getValue())[0];
                    max = Math.max(NumberUtils.toDouble(value.toString()), max);
                }
            }
        }

        for (Cartesian2dAxis cartesian2dAxis : axes) {
            cartesian2dAxis.show(groupX.getxShow());
            cartesian2dAxis.axisLine().show(groupX.getxLineShow())
                    .lineStyle().color(groupX.getxLineColor());
            cartesian2dAxis.axisLabel().show(groupX.getxLabelShow())
                    .interval(groupX.getxInterval())
                    .rotate(groupX.getxFontRotateAngel())
                    .fontSize(groupX.getxFontSize())
                    .color(groupX.getxFontColor())
                    .fontWeight(FontWeightEx.create(groupX.getxFontWeight()));
            cartesian2dAxis.name(groupX.getxCompany());
            cartesian2dAxis.interval(groupX.getxInterval());
            cartesian2dAxis.splitLine().show(groupX.getxSplitLineShow())
                    .lineStyle().color(groupX.getxSplitLineColor());
            cartesian2dAxis.max(groupX.getxMax());
            cartesian2dAxis.axisTick().show(Boolean.FALSE);
            if (DiagramEnum.STRIPE_PN_BAR.getType().equals(diagram.getDiagramType())) {
                cartesian2dAxis.position(AxisPosition.top);
                cartesian2dAxis.max(max);
            }
        }
    }
}
