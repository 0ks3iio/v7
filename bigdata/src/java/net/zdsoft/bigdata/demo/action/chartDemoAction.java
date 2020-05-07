package net.zdsoft.bigdata.demo.action;

import net.zdsoft.basedata.remote.service.DeptRemoteService;
import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.bigdata.frame.data.redis.BgRedisUtils;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/bigdata/common/chart/demo")
public class chartDemoAction extends BigdataBaseAction {

	@Autowired
	private UnitRemoteService unitRemoteService;

	@Autowired
	private DeptRemoteService deptRemoteService;

	@Autowired
	private UserRemoteService userRemoteService;

	@RequestMapping("/map")
	public String map(ModelMap map) {
		return "/bigdata/demo/3dmap.ftl";
	}

}
