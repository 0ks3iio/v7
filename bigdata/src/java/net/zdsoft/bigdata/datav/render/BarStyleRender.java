package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupBarCommon;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.series.Bar;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/9/29 11:20
 */
public class BarStyleRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.common, diagram.getDiagramType())) {
            return;
        }
        GroupBarCommon groupBarCommon = NeedRenderGroupBuilder.buildGroup(parameters,
                GroupKey.common, GroupBarCommon.class);
        if (groupBarCommon != null) {
            for (Series series : ((EChartsRenderOption) ro).getOp().getSeries()) {
                if (series instanceof Bar) {
                    if (groupBarCommon.getBarCategoryGap() != null
                            && !"0".equals(groupBarCommon.getBarGap())) {
                        ((Bar) series).barCategoryGap(groupBarCommon.getBarCategoryGap());
                    }
                    if (groupBarCommon.getBarGap() != null
                            && !"0".equals(groupBarCommon.getBarGap())) {
                        ((Bar) series).barGap(groupBarCommon.getBarGap());
                    }
                    if (groupBarCommon.getBarWidth() != null
                            && !new Integer(0).equals(groupBarCommon.getBarWidth())) {
                        ((Bar) series).barWidth(groupBarCommon.getBarWidth());
                    }
                    Object[] radius = barBorderRadius(groupBarCommon.getBarTopBorderRadius(),
                            groupBarCommon.getBarBottomBorderRadius());
                    ((Bar) series).itemStyle().setBarBorderRadius(radius);
                }
            }
        }
    }

    private Object[] barBorderRadius(Integer top, Integer bottom) {
        Object[] radius = new Object[4];
        top = top == null ? 0 : top;
        bottom = bottom == null ? 0 : bottom;
        radius[0] = top;
        radius[1] = top;
        radius[2] = bottom;
        radius[3] = bottom;
        return radius;
    }
}
