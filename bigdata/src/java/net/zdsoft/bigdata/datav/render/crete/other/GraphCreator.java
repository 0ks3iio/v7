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
import net.zdsoft.echarts.enu.Layout;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.series.Graph;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/15 10:42
 */
public class GraphCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.GRAPH.getType().equals(diagram.getDiagramType())) {
            return  null;
        }
        JData data = EntryUtils.parseLinkData(result.getValue(), EntryUtils.EntryMapping.entry);
        data.setType(SeriesEnum.graph);
        Option option = new Option();
        JConverter.convert(data, option);
        earlyrender(option);
        return EChartsRenderOption.of(option);
    }

    private void earlyrender(Option option) {
        for (Series series : option.series()) {
            ((Graph) series).setLayout(Layout.force);
            Graph.Force force = new Graph.Force();
            //force.setInitLayout(Layout.circular.name());
            force.setEdgeLength("100");
            force.setRepulsion(200);
            ((Graph) series).setForce(force);
            ((Graph) series).setSymbolSize(25);
        }
    }
}
