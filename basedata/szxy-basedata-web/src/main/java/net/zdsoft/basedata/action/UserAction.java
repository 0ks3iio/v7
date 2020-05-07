package net.zdsoft.basedata.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.service.TeacherService;
import net.zdsoft.basedata.service.UserService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.entity.JsonArray;
import net.zdsoft.framework.entity.Pagination;

@Controller
@RequestMapping("/basedata")
public class UserAction extends BaseAction {

	@Autowired
	private UserService userService;

	@Autowired
	private TeacherService teacherService;

	@RequestMapping("/user/{id}/delete")
	@ResponseBody
	public String doDelete(@PathVariable String id, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession sesion) {
		Teacher teacher = teacherService.findByUserId(id);
		if (teacher != null) {
			return error("存在相关教师，不能删除！");
		}
		userService.deleteAllByIds(id);
		return "删除成功！";
	}

	@ResponseBody
	@RequestMapping("/unit/{unitId}/users")
	public String users(@PathVariable String unitId, ModelMap map, HttpServletRequest request, HttpServletResponse response, HttpSession sesion) {
		Pagination page = createPagination(request);
		List<User> users = userService.findByUnitId(unitId, page);
		JsonArray array = new JsonArray();
		for (User user : users) {
			array.add(user);
		}
		return returnJqGridData(page, array);
	}

}
