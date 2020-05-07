package net.zdsoft.bigdata.monitor.action;

import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/bigdata/monitor/audit")
public class MonitorDataAuditController extends BigdataBaseAction {

	private static final Logger logger = LoggerFactory
			.getLogger(MonitorDataAuditController.class);

	@RequestMapping("/index")
	public String dataAuditIndex(ModelMap map) {
		return "/bigdata/monitor/audit/dataAudit.ftl";
	}
}
