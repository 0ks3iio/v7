package net.zdsoft.bigdata.system.action;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.bigdata.system.entity.BgUserAuth;
import net.zdsoft.bigdata.system.service.BgUserAuthService;
import net.zdsoft.bigdata.v3.index.action.BigdataBaseAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/bigdata/userauth")
public class BgUserAuthController extends BigdataBaseAction {

	@Autowired
	private BgUserAuthService bgUserAuthService;

	@RequestMapping("/index")
	public String index(ModelMap map) {
		List<BgUserAuth> authList =bgUserAuthService.findAllAuthUserList();
		map.put("authList", authList);
		return "/bigdata/system/userauth/authUserIndex.ftl";
	}

	@RequestMapping("/userSelect")
	public String userSelect(String username,String realname,ModelMap map) {
		map.put("isResult", 0);
		map.put("username", username);
		map.put("realname", realname);
		List<BgUserAuth> userAuthList =new ArrayList<BgUserAuth>();
		if(StringUtils.isBlank(username) && StringUtils.isBlank(realname)){
			map.put("userList", userAuthList);
		}else{
			map.put("isResult", 1);
			userAuthList = bgUserAuthService.findUserList(getLoginInfo().getUnitId(),username,realname);
		}
		map.put("userAuthList", userAuthList);
		return "/bigdata/system/userauth/authUserSelect.ftl";
	}

	
	@ResponseBody
	@ControllerInfo("add user")
	@RequestMapping("/addUser")
	public String addUser(String userId) {
		try {
			bgUserAuthService.saveUserAuth(userId);
			return success("添加成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}
	
	@ResponseBody
	@ControllerInfo("delete user")
	@RequestMapping("/updateStatus")
	public String updateStatus(String id,Integer status) {
		try {
			bgUserAuthService.updateStatusById(id, status);
			return success("删除成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}

	@ResponseBody
	@ControllerInfo("update superUser")
	@RequestMapping("/updateSuperUser")
	public String updateSuperUser(String id,Integer isSuperUser) {
		try {
			bgUserAuthService.updateSuperUserById(id, isSuperUser);
			return success("更新成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}
	
	@ResponseBody
	@ControllerInfo("delete user")
	@RequestMapping("/deleteUser")
	public String deleteUser(String id) {
		try {
			bgUserAuthService.delete(id);
			return success("删除成功");
		} catch (Exception e) {
			return error("出错了:" + e.getMessage());
		}
	}
}
