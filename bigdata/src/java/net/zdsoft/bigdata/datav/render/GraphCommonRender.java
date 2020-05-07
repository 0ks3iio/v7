package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GraphCommon;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.series.Graph;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/23 下午2:40
 */
public class GraphCommonRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.graph_common, DiagramEnum.GRAPH.getType())) {
            return;
        }

        GraphCommon graphCommon = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.graph_common, GraphCommon.class);
        if (graphCommon == null) {
            return;
        }

        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        for (Series series : eChartsRenderOption.getOp().series()) {
            if (series instanceof Graph) {
                Graph.Force force = ((Graph) series).getForce();
                if (force == null) {
                    force = new Graph.Force();
                }
                force.setRepulsion(graphCommon.getRepulsion());
                force.setEdgeLength(graphCommon.getEdgeLength());
                ((Graph) series).setForce(force);
            }
        }
    }
}
