package net.zdsoft.datacollection.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;

@Controller
@RequestMapping("/dc/teacher")
public class DcTeacherAction extends BaseAction {

	@Autowired
	private TeacherRemoteService teacherRemoteService;

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/object/{id}")
	/**
	 * 教师信息
	 * @param id
	 * @return
	 */
	public String teacherById(@PathVariable String id) {
		return teacherRemoteService.findOneById(id);
	}
	
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/objects/{ids}")
	public String teacherByIds(@PathVariable String ids) {
		return teacherRemoteService.findListByIds(ids.split(","));
	}
	
	@ResponseBody
	@RequestMapping("/objectsWithIds")
	@ControllerInfo(ignoreLog=1)
	public String objectsWithIds(@RequestParam String ids) {
		return teacherRemoteService.findListByIds(ids.split(","));
	}

	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/list/unit/{unitId}")
	/**
	 * 单位下的教师信息
	 * @param unitId
	 * @return
	 */
	public String listByUnit(@PathVariable String unitId) {
		return teacherRemoteService.findByUnitId(unitId);
	}
	
	@ResponseBody
	@ControllerInfo(ignoreLog=1)
	@RequestMapping("/list/dept/{id}")
	/**
	 * 部门下的所有教师
	 * @param id
	 * @return
	 */
	public String listByDept(@PathVariable String id) {
		return teacherRemoteService.findByDeptId(id);
	}
}
