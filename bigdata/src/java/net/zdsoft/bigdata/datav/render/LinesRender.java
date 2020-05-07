package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupLines;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.series.Lines;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/17 20:24
 */
public class LinesRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.line_common, diagram.getDiagramType())) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = ((EChartsRenderOption) ro);
        GroupLines groupLines = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.line_common, GroupLines.class);
        if (groupLines == null) {
            return;
        }
        for (Series series : eChartsRenderOption.getOp().series()) {
            if (series instanceof Lines) {
                ((Lines) series).lineStyle().color(groupLines.getLinesColor()).width(groupLines.getLinesWidth());
                ((Lines) series).effect().period(groupLines.getLinesPeriod());
            }
        }
    }
}
