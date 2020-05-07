package net.zdsoft.datacollection.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.remote.service.UnitRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

@Controller
@RequestMapping("/dc/unit")
public class DcUnitAction extends BaseAction {

	@Autowired
	private UnitRemoteService unitRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/object/{id}")
	public String object(@PathVariable String id) {
		return unitRemoteService.findOneById(id);
	}
}
