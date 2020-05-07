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
import net.zdsoft.echarts.element.Tooltip;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 10:44
 */
public class SankeyCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.SANKEY.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        JData data = EntryUtils.parseLinkData(result.getValue(), EntryUtils.EntryMapping.entry);
        if (data == null) {
            throw new EntryUtils.DataException("数据异常");
        }
        data.setType(SeriesEnum.sankey);
        Option option = new Option();
        JConverter.convert(data, option);
        earlyRender(option);
        return EChartsRenderOption.of(option);
    }

    private void earlyRender(Option option) {
        for (Series series : option.series()) {
            series.label().color("#ffffff");
        }
        Tooltip tooltip = new Tooltip();
        tooltip.show(Boolean.TRUE);
        option.tooltip(tooltip);
    }
}
