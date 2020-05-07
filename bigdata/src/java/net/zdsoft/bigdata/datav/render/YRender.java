package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupY;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.enu.FontWeightEx;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/9/28 19:23
 */
public class YRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.y, diagram.getDiagramType())) {
            return;
        }

        GroupY groupY = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.y, GroupY.class);
        if (groupY != null) {
            EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
            Option option = eChartsRenderOption.getOp();
            int counter = 0;
            for (Cartesian2dAxis cartesian2dAxis : option.yAxis()) {
                if (DiagramEnum.STRIPE_PN_BAR.getType().equals(diagram.getDiagramType())) {
                    if (counter>0) {
                        cartesian2dAxis.show(Boolean.FALSE);
                        cartesian2dAxis.axisLabel().show(Boolean.FALSE);
                    } else {
                        cartesian2dAxis.axisLine().show(Boolean.FALSE);
                    }
                } else {
                    cartesian2dAxis.show(groupY.getyShow()).interval(groupY.getyInterval());
                    cartesian2dAxis.axisLine().show(groupY.getyLineShow())
                            .lineStyle().color(groupY.getyLineColor());
                    cartesian2dAxis.axisLabel().show(groupY.getyLabelShow())
                            .interval(groupY.getyInterval())
                            .rotate(groupY.getyFontRotateAngel())
                            .color(groupY.getyFontColor())
                            .fontWeight(FontWeightEx.create(groupY.getyFontWeight()))
                            .fontSize(groupY.getyFontSize());
                    cartesian2dAxis.splitLine().show(groupY.getySplitLineShow())
                            .lineStyle().color(groupY.getySplitLineColor());

                    cartesian2dAxis.max(groupY.getyMax());
                    if (groupY.getyMin() != null) {
                        cartesian2dAxis.min(groupY.getyMin());
                    }
                    cartesian2dAxis.name(Optional.ofNullable(groupY.getyCompany()).orElse("")).nameTextStyle().color(groupY.getyFontColor());

                    cartesian2dAxis.axisTick().show(Boolean.FALSE);
                }
                counter++;
            }
        }
    }
}
