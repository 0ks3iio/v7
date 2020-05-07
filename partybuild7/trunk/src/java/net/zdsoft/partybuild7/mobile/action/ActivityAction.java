package net.zdsoft.partybuild7.mobile.action;

import java.util.List;

import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.partybuild7.data.entity.Activity;
import net.zdsoft.partybuild7.data.service.ActivityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @author weixh
 * @since 2017-9-21 下午3:01:23
 */
@Controller
@RequestMapping("/mobile/open/partybuild7/activity")
public class ActivityAction extends MobileAction {
	@Autowired
	private ActivityService activityService;
	
	@ControllerInfo(value="活动列表")
	@RequestMapping("/list/index")
	public String showIndex(String teacherId, int level, ModelMap map){
		map.put("teacherId", teacherId);
		map.put("level", level);
		return "/partybuild7/mobile/actIndex.ftl";
	}
	
	@ControllerInfo(value="活动列表")
	@RequestMapping("/list/page")
	public String showList(String teacherId, int level, int pageIndex, ModelMap map){
		if(pageIndex == 0){
			pageIndex = 1;
		}
		Pagination page = new Pagination(pageIndex, 1000, false);
		List<Activity> acts = activityService.findByMemberId(teacherId, level, page.toPageable());
		map.put("teacherId", teacherId);
		map.put("level", level);
		map.put("acts", acts);
		return "/partybuild7/mobile/actList.ftl";
	}

	@ControllerInfo(value="活动详情")
	@RequestMapping("/detail")
	public String showDetail(String id, ModelMap map){
		Activity act = activityService.findById(id);
		map.put("act", act);
		return "/partybuild7/mobile/actDetail.ftl";
	}
}
