package net.zdsoft.bigdata.datav.render.crete.pie;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.bigdata.datav.render.crete.EChartsRenderOption;
import net.zdsoft.bigdata.datav.render.crete.RenderOptionCreator;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.enu.PositionEx;
import net.zdsoft.echarts.series.Pie;
import net.zdsoft.echarts.series.Series;
import net.zdsoft.echarts.series.data.PieData;
import net.zdsoft.echarts.series.data.SData;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/10 15:40
 */
public class SingleAngleCreator extends AbstractPieCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.SINGLE_ANNULAR.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        Option option = createOption(entryList);
        earlyRender(option);
        return EChartsRenderOption.of(option);
    }

    private void earlyRender(Option option) {
        for (Series series : option.series()) {
            ((Pie) series).radius(new Object[]{"60%", "80%"});
            for (SData data : ((Pie) series).getData()) {
                ((PieData) data).label().position(PositionEx.create("center"));
            }
        }
    }
}
