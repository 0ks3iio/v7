package net.zdsoft.bigdata.datav.render.crete.line;

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
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.enu.BottomEx;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.Origin;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.TopEx;
import net.zdsoft.echarts.series.Line;
import net.zdsoft.echarts.series.Series;

import java.util.List;

/**
 * @author shenke
 * @since 2018/10/10 15:52
 */
public class CommonLineAreaCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.COMMON_LINE_AREA.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        JData data = new JData();
        data.setType(SeriesEnum.line);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);
        Option option = new Option();
        JConverter.convert(data, option);
        earlyRender(option);
        return EChartsRenderOption.of(option);
    }

    private void earlyRender(Option option) {
        for (Series series : option.series()) {
            ((Line) series).areaStyle().origin(Origin.start);
        }
        for (Cartesian2dAxis cartesian2dAxis : option.xAxis()) {
            cartesian2dAxis.boundaryGap(Boolean.FALSE);
        }
    }
}
