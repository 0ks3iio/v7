package net.zdsoft.datacollection.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/dc/mcode")
public class DcMcodeAction extends BaseAction {

	@Autowired
	private McodeRemoteService mcodeRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/object/{mcodeId}/{thisId}")
	public String object(@PathVariable String mcodeId, @PathVariable String thisId) {
		return mcodeRemoteService.findByMcodeAndThisId(mcodeId, thisId);
	}
}
