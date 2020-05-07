package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.entity.DiagramParameter;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.enu.GroupKey;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.SeriesEnum;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 柱状折线混合图
 * @author shenke
 * @since 2018/9/29 13:43
 */
public class CompositeBarLineCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.COMPOSITE_BAR_LINE.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        Assert.notNull(earlyParameters, "创建折线-柱状复合图形时 EarlyParameter 不能为空");
        Map<String, String> seriesTypeMap = parseNameTypes(earlyParameters.stream().filter(ep-> GroupKey.series.name().equals(ep.getGroupKey())).collect(Collectors.toList()));
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        Map<String, List<JData.Entry>> nameEntries = new HashMap<>(seriesTypeMap.size());
        for (JData.Entry entry : entryList) {
            List<JData.Entry> entries = nameEntries.computeIfAbsent(entry.getName(), key->new ArrayList<>());
            entries.add(entry);
        }
        Option option = createOption(seriesTypeMap, nameEntries);
        return EChartsRenderOption.of(option);
    }

    private Map<String, String> parseNameTypes(List<EarlyParameter> earlyParameters) {
        Map<String, String> seriesTypeMap = new HashMap<>();
        for (EarlyParameter earlyParameter : earlyParameters) {

            if (earlyParameter.getEarlyParameters().stream().anyMatch(p->p.getArrayName()!=null)) {
                Map<String, List<DiagramParameter>> everyArrays = new HashMap<>();
                for (DiagramParameter parameter : earlyParameter.getEarlyParameters()) {
                    everyArrays.computeIfAbsent(parameter.getArrayName(), k-> new ArrayList<>()).add(parameter);
                }
                for (Map.Entry<String, List<DiagramParameter>> entry : everyArrays.entrySet()) {
                    String seriesType = null;
                    String seriesName = null;
                    for (DiagramParameter diagramParameter : entry.getValue()) {
                        if ("seriesType".equals(diagramParameter.getKey())) {
                            seriesType = diagramParameter.getValue();
                        }
                        if ("seriesName".equals(diagramParameter.getKey())) {
                            seriesName = diagramParameter.getValue();
                        }
                    }
                    Assert.notNull(seriesType, "折线柱状复合图形每个系列的type不能为空");
                    Assert.notNull(seriesName, "折线柱状复合图形每个系列的seriesName不能为空");
                    seriesTypeMap.put(seriesName, seriesType);
                }
            }
        }
        return seriesTypeMap;
    }

    private Option createOption(Map<String, String> seriesTypeMap, Map<String, List<JData.Entry>> nameEntries) {
        Option option = new Option();
        boolean hasCreatedBar = false;
        for (Map.Entry<String, List<JData.Entry>> entry : nameEntries.entrySet()) {
            String seriesType = seriesTypeMap.get(entry.getKey());
            SeriesEnum seriesEnum = SeriesEnum.bar;
            if (seriesType != null) {
                seriesEnum = SeriesEnum.valueOf(seriesType);
            }
            //if (hasCreatedBar) {
            //    seriesEnum = SeriesEnum.line;
            //}
            if (SeriesEnum.bar.equals(seriesEnum)) {
                hasCreatedBar = true;
            }
            JData data = createJData(entry.getValue(), seriesEnum, option);
            JConverter.convert(data, option);
        }
        if (option.getSeries() == null) {
            option.setSeries(Collections.emptyList());
        }
        return option;
    }

    private JData createJData(List<JData.Entry> entryList, SeriesEnum seriesType, Option option) {
        JData data = new JData();
        data.setType(seriesType);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        if (option.grid().isEmpty()) {
            data.setSelfCoordSys(true);
            data.setSelfXAxis(true);
            data.setSelfYAxis(true);
        }
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);
        return data;
    }
}
