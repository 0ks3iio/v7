package net.zdsoft.bigdata.datav.render.crete;

import net.zdsoft.bigdata.data.echarts.EntryUtils;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.datav.entity.Diagram;
import net.zdsoft.bigdata.datav.enu.DiagramEnum;
import net.zdsoft.bigdata.datav.render.EarlyParameter;
import net.zdsoft.echarts.Option;
import net.zdsoft.echarts.convert.JConverter;
import net.zdsoft.echarts.convert.api.JData;
import net.zdsoft.echarts.coords.cartesian2d.Cartesian2dAxis;
import net.zdsoft.echarts.coords.enu.AxisType;
import net.zdsoft.echarts.enu.BottomEx;
import net.zdsoft.echarts.enu.CoordinateSystem;
import net.zdsoft.echarts.enu.LeftEx;
import net.zdsoft.echarts.enu.RightEx;
import net.zdsoft.echarts.enu.SeriesEnum;
import net.zdsoft.echarts.enu.TopEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 水平正负柱状图
 * @author shenke
 * @since 2018/10/10 10:56
 */
public class PositiveNegativeStripeBarCreator extends AbstractCommonStripeBarCreator implements RenderOptionCreator<EChartsRenderOption> {

    @Override
    public EChartsRenderOption create(Result result, Diagram diagram, List<EarlyParameter> earlyParameters) throws EntryUtils.DataException {
        if (!DiagramEnum.STRIPE_PN_BAR.getType().equals(diagram.getDiagramType())) {
            return null;
        }
        List<JData.Entry> entryList = EntryUtils.parse(result.getValue(), EntryUtils.EntryMapping.entry);
        Map<String, List<JData.Entry>> entryMap = new HashMap<>();
        for (JData.Entry entry : entryList) {
            entryMap.computeIfAbsent(entry.getName(), k-> new ArrayList<>()).add(entry);
        }
        if (entryMap.isEmpty()) {
            throw new EntryUtils.DataException("数据格式异常");
        }
        int counter = 1;
        JData leftJData = null;
        JData rightJData = null;
        for (Map.Entry<String, List<JData.Entry>> entry : entryMap.entrySet()) {
            if (counter == 1) {
                leftJData = createCommonData(entry.getValue());
                leftJData.getCoordSysPosition().setRight(RightEx.create("50%"));
            } else if (counter == 2) {
                rightJData = createCommonData(entry.getValue());
                rightJData.getCoordSysPosition().setLeft(LeftEx.create("50%"));
                rightJData.setSelfYAxis(false);
                break;
            }
            counter ++;
        }
        //convert left
        Option option = new Option();
        JConverter.convert(leftJData, option);
        //inverse y
        for (Cartesian2dAxis yAxi : option.getYAxis()) {
            yAxi.inverse(Boolean.TRUE);
        }

        JConverter.convert(rightJData, option);
        exchangeXY(option);

        for (Cartesian2dAxis cartesian2dAxis : option.getYAxis()) {
            if (!cartesian2dAxis.getGridIndex().equals(0)) {
                cartesian2dAxis.axisLabel().show(false);
            }
        }
        return EChartsRenderOption.of(option);
    }

    private JData createCommonData(List<JData.Entry> entryList) {
        JData data = new JData();
        data.setType(SeriesEnum.bar);
        data.setCoordSys(CoordinateSystem.cartesian2d);
        data.setEntryList(entryList);
        data.setSelfCoordSys(true);
        data.setSelfXAxis(true);
        data.setSelfYAxis(true);
        JData.JCoordSysPosition position = new JData.JCoordSysPosition();
        position.setTop(TopEx.create("15%"));
        position.setBottom(BottomEx.create("13%"));
        data.setCoordSysPosition(position);
        data.setXAxisType(AxisType.category);
        JData.JAxisPosition xp = new JData.JAxisPosition();
        JData.JAxisPosition yp = new JData.JAxisPosition();
        data.setXJAxisPosition(xp);
        data.setYJAxisPosition(yp);
        return data;
    }
}
