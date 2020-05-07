package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupLinkLine;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.series.Graph;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.style.LineStyle;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/23 下午2:46
 */
public class LinkLineRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.link_line, diagram.getDiagramType())) {
            return;
        }
        GroupLinkLine groupLinkLine = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.link_line, GroupLinkLine.class);
        if (groupLinkLine == null) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        for (Series series : eChartsRenderOption.getOp().series()) {
            if (series instanceof Graph) {
                LineStyle lineStyle = new LineStyle();
                lineStyle.setWidth(groupLinkLine.getLinkLineWidth());
                lineStyle.setColor(groupLinkLine.getLinkLineColor());
                ((Graph) series).setLineStyle(lineStyle);
            }
        }
    }
}
