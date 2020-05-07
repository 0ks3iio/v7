package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupHeat;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.element.VisualMap;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/17 20:13
 */
public class HeatColorRender implements Render{

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.heat_color, diagram.getDiagramType())) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = ((EChartsRenderOption) ro);
        GroupHeat groupHeat = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.heat_color, GroupHeat.class);
        if (groupHeat == null) {
            return;
        }
        List<VisualMap> visualMaps = eChartsRenderOption.getOp().getVisualMap();
        if (visualMaps != null) {
            for (VisualMap visualMap : visualMaps) {
                visualMap.color(new Object[]{groupHeat.getMaxColor(), groupHeat.getMinColor()});
            }
        }
    }
}
