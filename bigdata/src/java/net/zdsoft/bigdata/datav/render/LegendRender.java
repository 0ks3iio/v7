package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupLegend;
import net.zdsoft.bigdata.datav.parameter.GroupSeries;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.element.Legend;
import net.zdsoft.echarts.element.inner.LegendData;
import net.zdsoft.echarts.enu.FontWeightEx;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.Orient;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.series.Funnel;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Radar;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.FunnelData;
import net.zdsoft.echarts.series.data.PieData;
import net.zdsoft.echarts.series.data.RadarData;
import net.zdsoft.echarts.series.data.SData;
import net.zdsoft.echarts.style.LegendTextStyle;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 图例
 * @author shenke
 * @since 2018/9/27 15:34
 */
public class LegendRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.legend, diagram.getDiagramType())) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = ((EChartsRenderOption) ro);
        GroupLegend groupLegend = NeedRenderGroupBuilder.buildGroup(parameters, GroupKey.legend, GroupLegend.class);
        if (groupLegend == null) {
            return;
        }
        List<GroupSeries> groupSeries = NeedRenderGroupBuilder.buildGroups(parameters, GroupKey.series, GroupSeries.class);
        List<LegendData> legendNames = new ArrayList<>(groupSeries.size());
        Legend legend = eChartsRenderOption.getOp().legend();
        if (DiagramEnum.COMMON_PIE.getType() <=diagram.getDiagramType()
                && diagram.getDiagramType()<= DiagramEnum.COMMON_PIE_ANNULAR_ROSE.getType()) {
            LinkedHashSet<String> nameSet = new LinkedHashSet<>();
            for (Series series : eChartsRenderOption.getOp().getSeries()) {
                for (SData data : ((Pie) series).getData()) {
                    if (!"transparent".equals(((PieData) data).itemStyle().getColor())) {
                        nameSet.add(((PieData) data).getName());
                    }
                }
            }
            for (String s : nameSet) {
                LegendData legendData = new LegendData();
                legendData.name(s);
                legendNames.add(legendData);
            }
        }
        else if (DiagramEnum.COMMON_FUNNEL.getType().equals(diagram.getDiagramType())) {
            LinkedHashSet<String> nameSet = new LinkedHashSet<>();
            for (Series series : eChartsRenderOption.getOp().getSeries()) {
                for (SData data : ((Funnel) series).getData()) {
                    nameSet.add(((FunnelData) data).getName());
                }
            }
            for (String s : nameSet) {
                LegendData legendData = new LegendData();
                legendData.name(s);
                legendNames.add(legendData);
            }
        }
        else if (DiagramEnum.COMMON_RADAR.getType().equals(diagram.getDiagramType())) {
            LinkedHashSet<String> nameSet = new LinkedHashSet<>();
            for (Series series : eChartsRenderOption.getOp().getSeries()) {
                for (SData data : ((Radar) series).getData()) {
                    nameSet.add(((RadarData) data).getName());
                }
            }
            for (String s : nameSet) {
                LegendData legendData = new LegendData();
                legendData.name(s);
                legendNames.add(legendData);
            }
        }
        else {
            Map<String, String> nameedMap = new HashMap<>();
            for (GroupSeries series : groupSeries) {
                LegendData legendData = new LegendData();
                legendData.name(series.getSeriesShowName());
                legendNames.add(legendData);
                nameedMap.put(series.getSeriesName(), series.getSeriesShowName());
            }

            for (Series series : eChartsRenderOption.getOp().getSeries()) {
                String name = nameedMap.get(series.getId());
                if (StringUtils.isNotBlank(name)) {
                    series.setName(name);
                }
            }
        }

        legend.setData(new LinkedHashSet<>(legendNames));
        legend.left(LeftEx.create(groupLegend.getLegendPositionTransverse()));
        legend.top(TopEx.create(groupLegend.getLegendPositionPortrait()));
        try {
            legend.orient(Orient.valueOf(groupLegend.getLegendOrient()));
        } catch (IllegalArgumentException e) {
            legend.orient(Orient.horizontal);
        }

        LegendTextStyle textStyle = new LegendTextStyle();
        textStyle.color(groupLegend.getLegendColor());
        legend.show(groupLegend.getLegendShow()).textStyle(textStyle);
    }


}
