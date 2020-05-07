package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupLabel;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.enu.FontWeightEx;
import net.zdsoft.echarts.enu.PositionEx;
import net.zdsoft.echarts.series.Funnel;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.inner.LabelLine;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/16 10:22
 */
public class LabelRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.label, diagram.getDiagramType())) {
            return;
        }

        GroupLabel groupLabel = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.label, GroupLabel.class);
        if (groupLabel == null) {
            return;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;

        for (Series series : eChartsRenderOption.getOp().series()) {
            if (series instanceof Pie ) {
                LabelLine<Pie> labelLine = new LabelLine<>();
                labelLine.show(groupLabel.getLabelLineShow())
                        .length(groupLabel.getLabelLineLength());
                ((Pie) series).labelLine(labelLine);
            }
            if (series instanceof Funnel ) {
                LabelLine<Funnel> labelLine = new LabelLine<>();
                labelLine.show(groupLabel.getLabelLineShow())
                        .length(groupLabel.getLabelLineLength());
                ((Funnel) series).labelLine(labelLine);
            }
            if (groupLabel.getLabelPosition() != null) {
                series.label().setPosition(PositionEx.create(groupLabel.getLabelPosition()));
            }
            series.label().show(groupLabel.getLabelShow())
                    .formatter(groupLabel.getLabelFormatter())
                    .color(groupLabel.getLabelFontColor())
                    .fontSize(groupLabel.getLabelFontSize())
                    .fontWeight(FontWeightEx.create(groupLabel.getLabelFontWeight()));
        }
    }
}
