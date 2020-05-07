package net.zdsoft.datacollection.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.remote.service.ClassRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

@Controller
@RequestMapping("/dc/clazz")
public class DcClassAction extends BaseAction {

	@Autowired
	private ClassRemoteService classRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/teachClass/{teacherId}")
	/**
	 * 担任班主任的班级列表
	 * 
	 * @param teacherId
	 * @return
	 */
	public String teachClass(@PathVariable String teacherId) {
		return classRemoteService.findByTeacherId(teacherId);
	}
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/object/{id}")
	public String object(@PathVariable String id) {
		return classRemoteService.findOneById(id);
	}
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/objectsWithIds")
	public String objectsWithIds(@RequestParam String ids) {
		return classRemoteService.findListByIds(ids.split(","));
	}
 
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/schoolClass/{schoolId}")
	/**
	 * 学校的班级列表
	 * 
	 * @param schoolId
	 * @return
	 */
	public String schoolClass(@PathVariable String schoolId) {
		return classRemoteService.findByAllSchoolId(schoolId);
	}

}
