package net.zdsoft.bigdata.monitor.action;

import java.util.List;

import net.zdsoft.bigdata.extend.data.entity.MonitorUrl;
import net.zdsoft.bigdata.extend.data.service.MonitorUrlService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统监控
 * 
 * @author jiangf
 *
 */
@RequestMapping("/bigdata/monitor")
@Controller
public class MonitorUrlController extends BigdataBaseAction{

	@Autowired
	private MonitorUrlService monitorUrlService;

	@RequestMapping("/data")
	public String list(Model model) {
		List<MonitorUrl> urlList = monitorUrlService.getMonitorList();
		model.addAttribute("monitorList", urlList);
		return "/bigdata/monitor/dataMonitor.ftl";
	}
}
