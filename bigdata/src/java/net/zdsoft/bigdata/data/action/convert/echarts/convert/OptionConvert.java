package net.zdsoft.bigdata.data.action.convert.echarts.convert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import net.zdsoft.bigdata.data.action.convert.echarts.op.ErrorOptionWrap;
import net.zdsoft.bigdata.data.echarts.OptionEx;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.framework.config.EisContextLoaderListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.stereotype.Component;

/**
 * @author ke_shen@126.com
 * @since 2018/4/11 下午8:45
 */
@Component
public class OptionConvert implements Function<IChart<IChart.ChartConfig, Result>, OptionEx> {

    private Logger logger = LoggerFactory.getLogger(OptionConvert.class);

    //@Resource
    //private SeriesConvert seriesConvert;

    private List<DataFormatConvert> converts;

    @PostConstruct
    public void initOptionConvert() {
        Map<String, DataFormatConvert> convertMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(
                EisContextLoaderListener.getCurrentWebApplicationContext(), DataFormatConvert.class,
                true,true);
        converts = convertMap.entrySet().parallelStream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public OptionEx apply(IChart<IChart.ChartConfig, Result> iChart) {

        if (iChart.getQueryResult().hasError()) {
            logger.error("查询出错", iChart.getQueryResult().getException());
            return ErrorOptionWrap.error(iChart.getQueryResult().getException().getMessage());
        }
        long start = System.currentTimeMillis();

        OptionEx ex = null;
        for (DataFormatConvert convert : converts) {
            if ((ex = convert.convert(iChart)) != null) {
                break;
            }
        }

        //Option option = Option.option().chartId(iChart.getConfig().getChartId())
        //        .title().show(true).text(iChart.getConfig().getName()).option();
        if (logger.isDebugEnabled()) {
            logger.debug("转换用时 [{}ms]", System.currentTimeMillis() - start);
        }
        if (ex == null) {
            logger.error("无法解析ChartType={}数据", iChart.getConfig().getChartType());
            return ErrorOptionWrap.error("无法解析ChartType=" + iChart.getConfig().getChartType() + "数据");
        }
        //额外的数据
        ex.setChartType(iChart.getConfig().getChartType());
        return ex;
    }

    //public Option convert(CockpitChart cockpitChart, Result result) {
    //    return apply(new IOptionChart(new CockpitChartConfigAdapter(cockpitChart), IResultAtapter.convert(result)));
    //}

    public OptionEx convert(Chart chart, Result result) {
        return apply(new IOptionChart(new ChartChartConfigAdapter(chart), IResultAtapter.convert(result)));
    }

    static class ChartChartConfigAdapter extends AbstractChartConfig {

        public ChartChartConfigAdapter(Chart chart) {
            super(chart.getName(), chart.getChartType(), chart.getId());
        }
    }

    @FunctionalInterface
    public interface Convert<C, T extends OptionEx> {
        T convert(C iChart);
    }
}
