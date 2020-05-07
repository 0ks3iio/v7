package net.zdsoft.bigdata.data.echarts;

import com.alibaba.fastjson.JSONObject;
import net.zdsoft.bigdata.data.code.ChartSeries;
import net.zdsoft.bigdata.data.code.ChartType;
import net.zdsoft.echarts.enu.RoseTypeEnum;

/**
 * @author shenke
 * @since 2018/7/25 下午1:54
 */
public class OptionExposeFactory {

    public static Object getOptionExpose(Integer type, ChartSeries chartSeries) {
        switch (chartSeries) {
            case bar:
                OptionExposeBar op = OptionExposeBar.getDefaultOptionExpose();
                op.setExchangeXY(false);
                if (Integer.valueOf(ChartType.BAR_2_STRIPE).equals(type)) {

                }
                else if (Integer.valueOf(ChartType.BAR_LINE).equals(type)) {
                    op.setSmooth(true);
                }
                else if (Integer.valueOf(ChartType.BAR_STRIPE).equals(type)
                        || Integer.valueOf(ChartType.BAR_STACK_STRIPE).equals(type)) {
                    op.setExchangeXY(true);
                }
                return op;
            case line:
                OptionExposeLine opLine = OptionExposeLine.getDefaultOptionExpose();
                opLine.setExchangeXY(false);
                return opLine;
            case scatter:
                return OptionExposeScatter.getDefaultOptionExpose();
            case pie:
                OptionExposePie opPie = OptionExposePie.getDefaultOptionExpose();
                if (Integer.valueOf(ChartType.PIE_DOUGHNUT_COMPOSITE).equals(type)) {
                    opPie.setShowLegend(false);
                    opPie.setShowTooltip(false);
                }
                return opPie;
            case radar:
                return OptionExposeRadar.getDefaultOptionExpose();
            case funnel:
                return OptionExposeFunnel.getDefaultExpose();
            case gauge:
                return OptionExposeGauge.getDefaultExpose();
            case map:
                return OptionExposeMap.getDefaultExpose();
            case wordCloud:
                return OptionExposeWordCloud.getDefault();
            case treeMap:
                return OptionExposeTreeMap.getDefault();
            case sankey:
                return OptionExposeSankey.createDefault();
            case graph:
                return OptionExposeGraph.createDefault();
            default:
                return null;
        }
    }

    public static Object getOptionExpose(String optionExpose, ChartSeries chartSeries, Integer type) {
        switch (chartSeries) {
            case bar:
                return JSONObject.parseObject(optionExpose, OptionExposeBar.class);
            case line:
                return JSONObject.parseObject(optionExpose, OptionExposeLine.class);
            case scatter:
                return JSONObject.parseObject(optionExpose, OptionExposeScatter.class);
            case pie:
                return JSONObject.parseObject(optionExpose, OptionExposePie.class);
            case radar:
                return JSONObject.parseObject(optionExpose, OptionExposeRadar.class);
            case funnel:
                return JSONObject.parseObject(optionExpose, OptionExposeFunnel.class);
            case gauge:
                return JSONObject.parseObject(optionExpose, OptionExposeGauge.class);
            case map:
                return JSONObject.parseObject(optionExpose, OptionExposeMap.class);
            case wordCloud:
                return JSONObject.parseObject(optionExpose, OptionExposeWordCloud.class);
            case treeMap:
                return JSONObject.parseObject(optionExpose, OptionExposeTreeMap.class);
            case sankey:
                return JSONObject.parseObject(optionExpose, OptionExposeSankey.class);
            case graph:
                return JSONObject.parseObject(optionExpose, OptionExposeGraph.class);
            default:
                return null;
        }
    }
}
