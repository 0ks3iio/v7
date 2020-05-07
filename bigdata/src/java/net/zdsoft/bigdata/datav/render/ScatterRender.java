package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupScatter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.series.Scatter;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/17 20:18
 */
public class ScatterRender implements Render {


    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.scatter_common, diagram.getDiagramType())) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = ((EChartsRenderOption) ro);
        GroupScatter groupScatter = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.scatter_common, GroupScatter.class);
        if (groupScatter == null) {
            return;
        }
        for (Series series : eChartsRenderOption.getOp().series()) {
            if (series instanceof Scatter) {
                ((Scatter) series).itemStyle().color(groupScatter.getScatterColor());
                ((Scatter) series).label().show(groupScatter.getScatterSizeShow()).formatter("{@2}");
            }
        }
    }
}
