package net.zdsoft.bigdata.datav.render;

import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.parameter.GroupSeries;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.series.Graph;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Sankey;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.GraphData;
import net.zdsoft.echarts.series.data.PieData;
import net.zdsoft.echarts.series.data.SData;
import net.zdsoft.echarts.series.data.SankeyData;
import net.zdsoft.echarts.style.ItemStyle;
import net.zdsoft.echarts.style.LineStyle;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shenke
 * @since 2018/9/29 13:44
 */
public class SeriesRender implements Render {

    @Override
    public void doRender(Object ro, List<DiagramParameter> parameters, Diagram diagram) {
        if (!NeedRenderChecker.contain(GroupKey.series, diagram.getDiagramType())) {
            return;
        }
        List<GroupSeries> groupSeries = NeedRenderGroupBuilder.buildGroups(parameters, GroupKey.series, GroupSeries.class);
        if (CollectionUtils.isEmpty(groupSeries)) {
            return;
        }
        EChartsRenderOption eChartsRenderOption = (EChartsRenderOption) ro;
        Option option = eChartsRenderOption.getOp();

        if (DiagramEnum.COMMON_PIE.getType() <=diagram.getDiagramType()
                && diagram.getDiagramType()<=DiagramEnum.COMMON_PIE_ANNULAR_ROSE.getType()) {
            renderPie(groupSeries, option);
        }
        else if (DiagramEnum.SANKEY.getType().equals(diagram.getDiagramType())) {
            renderSankey(groupSeries, option);
        }
        else if (DiagramEnum.COMMON_FUNNEL.getType().equals(diagram.getDiagramType())) {
            renderFunnel(groupSeries, option);
        }
        else if (DiagramEnum.COMMON_RADAR.getType().equals(diagram.getDiagramType())) {
            renderRadar(groupSeries, option);
        }
        else if (DiagramEnum.GRAPH.getType().equals(diagram.getDiagramType())) {
            renderGraph(groupSeries, option);
        }
        else {
            renderECharts(groupSeries, option);
        }

        option.setColor(groupSeries.stream().map(GroupSeries::getColor).toArray(String[]::new));
    }

    private void renderECharts(List<GroupSeries> groupSeries, Option option) {
        Collections.sort(groupSeries);
        //arrayName + seriesName 作为KEY，防止用户将系列值输入相同的值
        Map<String, GroupSeries> gsMap = groupSeries.stream().collect(Collectors.toMap(GroupSeries::getSeriesName, Function.identity(), (k, v)-> v));
        for (Series series : option.getSeries()) {
            GroupSeries gs = gsMap.get(series.getId());
            if (gs != null) {
                series.itemStyle().color(gs.getColor());
            }
        }
        option.setColor(groupSeries.stream().map(GroupSeries::getColor).collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    private void renderPie(List<GroupSeries> groupSeries, Option option) {
        Collections.sort(groupSeries);
        option.setColor(groupSeries.stream().map(GroupSeries::getColor).toArray(String[]::new));
    }

    private void renderSankey(List<GroupSeries> groupSeries, Option option) {
        Collections.sort(groupSeries);
        Map<Integer, GroupSeries> counterMap = new HashMap<>(groupSeries.size());
        for (int i = 0; i < groupSeries.size(); i++) {
            counterMap.put(i, groupSeries.get(i));
        }
        for (Series series : option.getSeries()) {
            int counter = 0;
            for (Graph.Link link : ((Sankey) series).getLinks()) {
                LineStyle<Graph.Link, LineStyle> lineStyle = new LineStyle();
                if (counter == counterMap.size()) {
                    counter = 0;
                }
                lineStyle.color(counterMap.get(counter).getColor());
                link.setLineStyle(lineStyle);
                counter++;
            }

            int c = 0;
            for (SData d : ((Sankey) series).getData()) {
                if (c == counterMap.size()) {
                    c = 0;
                }
                ((SankeyData) d).itemStyle().color(counterMap.get(c).getColor());
                c++;
            }

        }
        option.setColor(groupSeries.stream().map(GroupSeries::getColor).toArray(String[]::new));
    }

    private void renderRadar(List<GroupSeries> groupSeries, Option option) {
        Collections.sort(groupSeries);
        option.setColor(groupSeries.stream().map(GroupSeries::getColor).toArray(String[]::new));
    }

    private void renderFunnel(List<GroupSeries> groupSeries, Option option) {
        Collections.sort(groupSeries);
        option.setColor(groupSeries.stream().map(GroupSeries::getColor).toArray(String[]::new));
    }

    private void renderGraph(List<GroupSeries> groupSeries, Option option) {
        Map<String, GroupSeries> seriesMap = groupSeries.stream().collect(Collectors.toMap(GroupSeries::getSeriesName, Function.identity(), (k1, k2) -> k2));
        Collections.sort(groupSeries);
        for (Series series : option.series()) {
            if (series instanceof Graph) {
                for (SData sd : ((Graph) series).getData()) {
                    GroupSeries gs = seriesMap.get(((GraphData) sd).getCategory());
                    if (gs != null) {
                        ((GraphData) sd).itemStyle().setColor(gs.getColor());
                        ((GraphData) sd).setSymbolSize(gs.getSymbolSize());
                    }
                }
            }
        }
    }
}
