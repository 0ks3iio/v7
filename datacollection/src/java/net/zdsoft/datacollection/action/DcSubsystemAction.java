package net.zdsoft.datacollection.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.system.remote.service.SubSystemRemoteService;

@Controller
@RequestMapping("/dc/subsystem")
public class DcSubsystemAction extends BaseAction {

	@Autowired
	private SubSystemRemoteService subSystemRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/object/{intId}")
	public String object(@PathVariable Integer intId) {
		return subSystemRemoteService.findOneById(intId);
	}
}
