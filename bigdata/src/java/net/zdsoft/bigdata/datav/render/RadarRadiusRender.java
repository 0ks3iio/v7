package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupRadius;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.coords.radar.Radar;
import net.zdsoft.echarts.enu.LineType;

import java.util.List;
import java.util.Optional;

/**
 * @author shenke
 * @since 2018/10/16 10:18
 */
public class RadarRadiusRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.radar_radius, diagram.getDiagramType())) {
            return;
        }

        GroupRadius groupRadius = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.radar_radius, GroupRadius.class);
        if (groupRadius == null) {
            return;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Option option = eChartsRenderOption.getOp();
        Optional<Radar> optional = option.radar().stream().findFirst();
        optional.ifPresent(radar -> {
            radar.radius(groupRadius.getRadiusOutter());
            radar.splitLine().show(groupRadius.getShowSplitLine())
                    .lineStyle().width(groupRadius.getSplitLineWidth())
                    .color(groupRadius.getSplitLineColor());
            radar.axisLine().show(groupRadius.getShowAxisLine())
                    .lineStyle().width(groupRadius.getAxisLineWidth())
                    .color(groupRadius.getAxisLineColor());

            radar.splitArea().setShow(groupRadius.getShowSplitArea());
            try {
                if (groupRadius.getSplitLineType() != null) {
                    radar.splitLine().lineStyle().type(LineType.valueOf(groupRadius.getSplitLineType()));
                }
                if (groupRadius.getAxisLineType() != null) {
                    radar.axisLine().lineStyle().type(LineType.valueOf(groupRadius.getAxisLineType()));
                }
            } catch (IllegalArgumentException e) {

            }
        });
    }
}
