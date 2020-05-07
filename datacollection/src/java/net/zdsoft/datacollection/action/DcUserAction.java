package net.zdsoft.datacollection.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

@Controller
@RequestMapping("/dc/user")
public class DcUserAction extends BaseAction {

	@Autowired
	private UserRemoteService userRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/object/{id}")
	public String object(@PathVariable String id) {
		return userRemoteService.findOneById(id);
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/objects/{ids}")
	public String objects(@PathVariable String ids) {
		return userRemoteService.findListByIds(ids.split(","));
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/objectsWithIds")
	public String objectsWithIds(@RequestParam String ids) {
		return userRemoteService.findListByIds(ids.split(","));
	}
}
