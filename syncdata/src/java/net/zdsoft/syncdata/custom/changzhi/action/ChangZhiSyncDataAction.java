package net.zdsoft.syncdata.custom.changzhi.action;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.BaseSyncSaveRemoteService;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.BaseAction;
import net.zdsoft.framework.config.Evn;
import net.zdsoft.syncdata.custom.changzhi.service.CzSyncService;

/**
 * 
 * @author weixh
 * 2019年6月26日	
 */
@Controller
@RequestMapping("/syncdata/sxcz")
@Lazy
public class ChangZhiSyncDataAction extends BaseAction {
	@Autowired
	private CzSyncService czSyncService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private BaseSyncSaveRemoteService baseSyncSaveRemoteService;
	
	@RequestMapping("/syncEdu")
	public String syncEdu(HttpServletRequest request, ModelMap map) {
		try {
			czSyncService.saveEdu();
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步edu失败："+e.getMessage());
		}
		return promptFlt(map, "同步edu结束，请查看结果。");
	}
	
	@RequestMapping("/syncSchool")
	public String syncSchool(HttpServletRequest request, ModelMap map) {
		try {
			czSyncService.saveSchool();
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步学校失败："+e.getMessage());
		}
		return promptFlt(map, "同步学校结束，请查看结果。");
	}

	@RequestMapping("/syncTeacher")
	public String syncTeacher(HttpServletRequest request, ModelMap map) {
		try {
			czSyncService.saveSyncTeacher();
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步教师失败："+e.getMessage());
		}
		return promptFlt(map, "同步教师结束，请查看结果。");
	}
	
	@RequestMapping("/syncAccount")
	public String syncAccount(HttpServletRequest request, ModelMap map) {
		try {
			if(!Evn.isPassport()) {
				return promptFlt(map, "没有连接passport。");
			}
			List<User> users = userRemoteService.findListObjectBy(new String[] {"isDeleted"}, new String[] {"0"});
			users = users.stream().filter(u->u.getSequence()==null).collect(Collectors.toList());
			if(CollectionUtils.isEmpty(users)) {
				return promptFlt(map, "没有需要处理的用户数据。");
			}
			baseSyncSaveRemoteService.saveUserToPassport(users);
		} catch (Exception e) {
			e.printStackTrace();
			return promptFlt(map, "同步用户失败："+e.getMessage());
		}
		return promptFlt(map, "同步用户结束，请查看结果。");
	}
}
