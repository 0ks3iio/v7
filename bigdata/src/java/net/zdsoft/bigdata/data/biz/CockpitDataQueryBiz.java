package net.zdsoft.bigdata.data.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.zdsoft.bigdata.data.DataSourceType;
import net.zdsoft.bigdata.data.action.convert.echarts.convert.OptionConvert;
import net.zdsoft.bigdata.data.action.convert.echarts.op.ErrorOptionWrap;
import net.zdsoft.bigdata.data.echarts.OptionEx;
import net.zdsoft.bigdata.data.entity.Api;
import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.entity.CockpitChart;
import net.zdsoft.bigdata.data.manager.IResult;
import net.zdsoft.bigdata.data.manager.InvocationBuilder;
import net.zdsoft.bigdata.data.manager.api.Invoker;
import net.zdsoft.bigdata.data.manager.api.Result;
import net.zdsoft.bigdata.data.service.ApiService;
import net.zdsoft.bigdata.data.utils.JexlContextHolder;
import net.zdsoft.framework.utils.ServletUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

/**
 * 这里不再区分是大屏还是基本图表
 * @author ke_shen@126.com
 * @since 2018/4/13 下午1:40
 */
@Service
public class CockpitDataQueryBiz {

    private Logger logger = LoggerFactory.getLogger(CockpitDataQueryBiz.class);

    @Resource
    private OptionConvert optionConvert;
    @Resource
    private ApiService apiService;
    @Resource
    private Invoker invoker;

    /**
     * 整个驾驶仓数据的查询
     * cockpitChartList 应当是该驾驶仓完整的图表明细数据
     * 该方法并不会对这些数据进行检查，这里认为这些数据是完全正确的
     *
     * * 数据将被封装为以下格式：
     * 其中DivId为每一个功能块的ID
     * 当divId所需要的数据结构并不是Echarts标准图表时
     * Option将自适用为标准key:value形式
     * 另外Option扩展了两个字段success和message分别表示查询是否出错
     * 当success为false时message将对应错误消息(后台异常的堆栈信息)
     * 当success为true时，message将为空
     * {
     * divId: option,
     * divId2: option2
     * ....
     * }
     */
    public String cockpitDataQuery(List<CockpitChart> cockpitChartList) {
        JSONObject options = new JSONObject();
        //for (CockpitChart cockpitChart : cockpitChartList) {
        //
        //    /*
        //     * 这里要将查询得到的数据结合数据库里面每个图表具体的配置
        //     * 将这些信息封装成页面所需要的参数
        //     * 最终应该封装成Option对象
        //     * 目前这里的封装不是很全面，echarts option的所有属性基本都要可配置
        //     * 如需更大的灵活性请扩展 Option及其组件
        //     */
        //
        //    Option option = null;
        //    if (DataSourceType.DB.match(cockpitChart.getDataSourceType())) {
        //        option = optionConvert.convert(cockpitChart, invoker.invoke(
        //                InvocationBuilder.getInstance().dataSourceId(cockpitChart.getDatabaseId())
        //                        .updateInterval(cockpitChart.getUpdateInterval())
        //                        .queryStatement(cockpitChart.getDataSet())
        //                        .type(DataSourceType.DB).build()
        //        ));
        //
        //    } else if (DataSourceType.API.match(cockpitChart.getDataSourceType())) {
        //        Api api = apiService.findOne(cockpitChart.getApiId());
        //        if (api == null) {
        //            logger.error("API is null cockpitId: {}, ApiId: {}", cockpitChart.getId(), cockpitChart.getApiId());
        //            option = Option.option("所查询数据源不合法，API为空");
        //        } else {
        //            option = optionConvert.convert(cockpitChart, invoker.invoke(
        //                    InvocationBuilder.getInstance().dataSourceId(cockpitChart.getApiId())
        //                            .updateInterval(cockpitChart.getUpdateInterval())
        //                            .queryStatement(api.getUrl())
        //                            .type(DataSourceType.API)
        //                            .build()
        //            ));
        //        }
        //
        //    } else if (DataSourceType.STATIC.match(cockpitChart.getDataSourceType())) {
        //        option = optionConvert.convert(cockpitChart, new IResult(cockpitChart.getDataSet(), null));
        //    } else {
        //        option = Option.option(String.format("该图表对应的数据源不合法, divId %s, dataSourceType %s",
        //                cockpitChart.getDivId(), cockpitChart.getDataSourceType()));
        //    }
        //    options.put(cockpitChart.getDivId(), option.toJSONString());
        //}
        return options.toJSONString();
    }

    /**
     * 查询单个图表的数据
     * @param session TODO
     * @return MessagerOption
     */
    public OptionEx executeQuery(HttpSession session, Chart chart) {
        JexlContextHolder.setJexlContext(buildJexlEvaludateContext(session));
        try {
            if (DataSourceType.DB.getValue() == chart.getDataSourceType()) {
                return optionConvert.convert(chart, invoker.invoke(
                        InvocationBuilder.getInstance().dataSourceId(chart.getDatabaseId())
                                .updateInterval(chart.getUpdateInterval())
                                .queryStatement(chart.getDataSet())
                                .type(DataSourceType.DB).build()
                ));
            }
            else if (DataSourceType.API.getValue() == chart.getDataSourceType()){
                Api api = apiService.findOne(chart.getApiId());
                return optionConvert.convert(chart, invoker.invoke(
                        InvocationBuilder.getInstance().dataSourceId(chart.getApiId())
                                .updateInterval(chart.getUpdateInterval())
                                .queryStatement(api.getUrl())
                                .type(DataSourceType.API).build()
                ));
            }
            else if (DataSourceType.STATIC.getValue() == chart.getDataSourceType()) {
                return optionConvert.convert(chart, new IResult(chart.getDataSet(), null));
            }
            else {
                return ErrorOptionWrap.error("不支持的数据源类型 " + chart.getDataSourceType());
            }
        } finally {
            JexlContextHolder.clearJexlContext();
        }
    }

    public Result executeQueryResult(HttpSession session, Chart chart) {
        JexlContextHolder.setJexlContext(buildJexlEvaludateContext(session));
        try {
            if (DataSourceType.DB.getValue() == chart.getDataSourceType()) {
                return invoker.invoke(
                        InvocationBuilder.getInstance().dataSourceId(chart.getDatabaseId())
                                .updateInterval(Optional.ofNullable(chart.getUpdateInterval()).orElse(0))
                                .queryStatement(chart.getDataSet())
                                .type(DataSourceType.DB).build()
                );
            } else if (DataSourceType.API.getValue() == chart.getDataSourceType()) {
                Api api = apiService.findOne(chart.getApiId());
                return invoker.invoke(
                        InvocationBuilder.getInstance().dataSourceId(chart.getApiId())
                                .updateInterval(Optional.ofNullable(chart.getUpdateInterval()).orElse(0))
                                .queryStatement(api.getUrl())
                                .type(DataSourceType.API).build()
                );
            } else if (DataSourceType.STATIC.getValue() == chart.getDataSourceType()) {
                return new IResult(chart.getDataSet(), null);
            } else {
                return null;
            }
        } finally {
            JexlContextHolder.clearJexlContext();
        }
    }



    public List<OptionEx> executeQuery(HttpSession session, Chart ... charts) {
        if (charts == null || charts.length == 0) {
            return Collections.emptyList();
        }
        JexlContextHolder.setJexlContext(buildJexlEvaludateContext(session));
        try {
            List<OptionEx> options = new ArrayList<>(charts.length);
            for (Chart chart : charts) {
                if (DataSourceType.DB.getValue() == chart.getDataSourceType()) {
                     options.add(optionConvert.convert(chart, invoker.invoke(
                            InvocationBuilder.getInstance().dataSourceId(chart.getDatabaseId())
                                    .updateInterval(chart.getUpdateInterval())
                                    .queryStatement(chart.getDataSet())
                                    .type(DataSourceType.DB).build()
                    )));
                }
                else if (DataSourceType.API.getValue() == chart.getDataSourceType()){
                    Api api = apiService.findOne(chart.getApiId());
                    options.add(optionConvert.convert(chart, invoker.invoke(
                            InvocationBuilder.getInstance().dataSourceId(chart.getApiId())
                                    .updateInterval(chart.getUpdateInterval())
                                    .queryStatement(api.getUrl())
                                    .type(DataSourceType.API).build()
                    )));
                }
                else if (DataSourceType.STATIC.getValue() == chart.getDataSourceType()) {
                    options.add(optionConvert.convert(chart, new IResult(chart.getDataSet(), null)));
                }
                else {
                    options.add(ErrorOptionWrap.error("不支持的数据源类型 " + chart.getDataSourceType()));
                }
            }
            return options;
        } finally {
            JexlContextHolder.clearJexlContext();
        }
    }

    private Object buildJexlEvaludateContext(HttpSession session) {
        return ServletUtils.getLoginInfo(session);
    }
}
