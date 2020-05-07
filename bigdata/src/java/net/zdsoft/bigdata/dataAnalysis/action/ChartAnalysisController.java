package net.zdsoft.bigdata.dataAnalysis.action;

import net.zdsoft.bigdata.data.vo.Response;
import net.zdsoft.bigdata.dataAnalysis.biz.AnalysisBiz;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 *
 * 多维图表分析
 * Created by wangdongdong on 2019/6/19 8:58.
 */
@Controller
@RequestMapping("/bigdata/data/analyse")
public class ChartAnalysisController {

    @Resource
    private AnalysisBiz analysisBiz;


    @RequestMapping("/queryChart")
    @ResponseBody
    public Response queryChart(@RequestParam(value = "chartDimensionId", required = false) String chartDimensionId,
                                   @RequestParam(value = "chartLegendId", required = false) String chartLegendId,
                                   @RequestParam(value = "chartMeasureArray[]", required = false) String[] measureParam,
                                   String chartType,
                                   String filterDataMap,
                                   String chartOrderDataMap,
                                   String modelId) {
        try {
            return Response.ok().data(analysisBiz.queryChart(chartDimensionId, chartLegendId, measureParam, filterDataMap, modelId, chartType, chartOrderDataMap)).build();
        } catch (Exception e) {
            return Response.error().message("查询出错，请检查维度是否配置正确!").build();
        }
    }


}
