package net.zdsoft.bigdata.datav.render.crete.other;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.enu.ColorMappingBy;
import net.zdsoft.echarts.enu.RoamEnum;
import net.zdsoft.echarts.enu.RoamEx;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.series.TreeMap;
import net.zdsoft.echarts.series.inner.Breadcrumb;
import net.zdsoft.echarts.style.ItemStyle;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 11:27
 */
public class TreeMapCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.TREE_MAP.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        JData data = new JData();
        data.setType(SeriesEnum.treemap);
        data.setEntryList(EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry));
        Option option = new Option();
        JConverter.convert(data, option);
        option.series().stream().findFirst().ifPresent(series -> {
            ((TreeMap) series).setLeafDepth(1);
            ((TreeMap) series).setRoam(RoamEx.disable());
        });
        return EChartsRenderOption.of(option);
    }
}
