package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupRadius;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/16 10:18
 */
public class RadiusRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.radius, diagram.getDiagramType())) {
            return;
        }

        GroupRadius groupRadius = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.radius, GroupRadius.class);
        if (groupRadius == null) {
            return;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Option option = eChartsRenderOption.getOp();
        for (Series series : option.series()) {
            if (series instanceof Pie) {
                ((Pie) series).radius(new Object[]{groupRadius.getRadiusInner(), groupRadius.getRadiusOutter()});
                if (groupRadius.getRadiusCenterLeft() != null || groupRadius.getRadiusCenterTop() != null) {
                    ((Pie) series).center(new String[]{groupRadius.getRadiusCenterLeft(), groupRadius.getRadiusCenterTop()});
                }

            }
        }
    }
}
