package net.zdsoft.bigdata.data.action;

import net.zdsoft.bigdata.data.entity.Chart;
import net.zdsoft.bigdata.data.service.ChartService;
import net.zdsoft.framework.utils.UuidUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author shenke
 * @since 2019/3/6 上午10:14
 */
@Controller
@RequestMapping("/bigdata/chart/query/adapter")
public class ChartQueryAdapter {

    @Resource
    private ChartService chartService;

    @GetMapping("{id}")
    public String view(@PathVariable("id") String id,
                       @RequestParam(value = "type", required = false, defaultValue = "single") String viewType,
                       Model model) {
        Chart chart = chartService.findOne(id);
        if (chart != null) {
            model.addAttribute("id", id);
            model.addAttribute("type", chart.getChartType());
            model.addAttribute("name", chart.getName());
            model.addAttribute("uuid", UuidUtils.generateUuid());
            model.addAttribute("viewType", viewType);
        } else {
            model.addAttribute("errorMsg", "该图表已经被删除");
            return "/bigdata/v3/common/error.ftl";
        }
        return "/bigdata/chartQuery/view-adapter.ftl";
    }
}
