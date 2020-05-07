package net.zdsoft.partybuild7.mobile.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.zdsoft.basedata.entity.User;
import net.zdsoft.basedata.remote.service.UserRemoteService;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.config.Session;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.partybuild7.data.constant.PartyBuildConstant;
import net.zdsoft.partybuild7.data.entity.Activity;
import net.zdsoft.partybuild7.data.entity.MemberMeeting;
import net.zdsoft.partybuild7.data.entity.OrgTrend;
import net.zdsoft.partybuild7.data.entity.PartyMember;
import net.zdsoft.partybuild7.data.service.ActivityService;
import net.zdsoft.partybuild7.data.service.MemberMeetingService;
import net.zdsoft.partybuild7.data.service.OrgTrendService;
import net.zdsoft.partybuild7.data.service.PartyMemberService;
import net.zdsoft.system.entity.server.Model;
import net.zdsoft.system.remote.service.ModelRemoteService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.TypeReference;

/**
 * 党建首页
 * 
 * @author weixh
 * @since 2017-9-20 上午10:50:02
 */
@Controller
@RequestMapping("/mobile/open/partybuild7")
public class PbIndexAction extends MobileAction {
	@Autowired
	private ActivityService activityService;
	@Autowired
    private MemberMeetingService memberMeetingService;
	@Autowired
    private OrgTrendService orgTrendService;
	@Autowired
	private PartyMemberService partyMemberService;
	@Autowired
	private UserRemoteService userRemoteService;
	@Autowired
	private ModelRemoteService modelRemoteService;
	
	@ControllerInfo(value="跳转到首页")
	@RequestMapping("/index")
	public String showIndex(String syncUserId, ModelMap map, HttpServletRequest request, HttpSession httpSession){
		try {
			map.put("indexPage", true);
			if(StringUtils.isBlank(syncUserId)){
				return errorFtl(map, "syncUserId信息为空，请联系管理员");
			}
			User us = User.dc(userRemoteService.findOneById(syncUserId));
			if(us == null){
				return errorFtl(map, "用户信息不存在或非教师登录！");
			}
			String ownerId = us.getOwnerId();
//			ownerId = "BFD48AEAFB0341C2921E86C33FBD31D3";//tcsls
//			PartyMember pm = partyMemberService.getFormalPartyMemeberById(ownerId);
//			if(pm == null){
//				return errorFtl(map, PartyBuildConstant.USER_NOT_INIT);
//			}
			map.put("userId", syncUserId);
			return showHomepage(ownerId, map, request, httpSession);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return errorFtl(map, "发生未知错误");
		}
	}
	
	@ControllerInfo(value="首页")
	@RequestMapping("/homepage")
	public String showHomepage(String teacherId, ModelMap map, HttpServletRequest request, HttpSession httpSession){
		map.put("indexPage", true);
		
		String userId = "";
		if(map.containsKey("userId")){
			userId = (String) map.get("userId");
		} else {
			userId = request.getParameter("userId");
		}
		List<Integer> modelIds = (List<Integer>)httpSession.getAttribute(PartyBuildConstant.PB_MODEL_SESSION + teacherId);

		if (modelIds == null) {
			List<Model> modelEns = SUtils
					.dt(modelRemoteService.findByUserId(userId), new TypeReference<List<Model>>() {
					});
			modelIds = new ArrayList<Integer>();
			for(Model mm : modelEns){
				if(mm.getSubSystem() != PartyBuildConstant.SUBSYSTEM_ID_PB){
					continue;
				}
				modelIds.add(mm.getId());
			}
			httpSession.setAttribute(PartyBuildConstant.PB_MODEL_SESSION + teacherId, modelIds);
		}
		if(CollectionUtils.isNotEmpty(modelIds)){
			if(CollectionUtils.containsAny(modelIds, PartyBuildConstant.jjfzkcSet)){
				map.put("jjfzkc", true);
			}
			if(CollectionUtils.containsAny(modelIds, PartyBuildConstant.rdspSet)){
				map.put("rdsp", true);
			}
			if(CollectionUtils.containsAny(modelIds, PartyBuildConstant.dyzzSet)){
				map.put("dyzz", true);
			}
		}
		
		PartyMember pm = partyMemberService.getFormalPartyMemeberById(teacherId);
		if(pm != null){
			Activity commonAct = activityService.findNewByMemberId(teacherId, PartyBuildConstant.PB_ACTIVITY_LEVEL_COMMON);
			if(commonAct != null){
				commonAct.setTimeStr(PartyBuildConstant.getTimeStr(commonAct.getCreationTime()));
			}
			Activity leaderAct = activityService.findNewByMemberId(teacherId, PartyBuildConstant.PB_ACTIVITY_LEVEL_LEADER);
			if(leaderAct != null){
				leaderAct.setTimeStr(PartyBuildConstant.getTimeStr(leaderAct.getCreationTime()));
			}
			MemberMeeting meet = memberMeetingService.getMeetingByTeacherId(teacherId);
			if(meet != null){
				meet.setTimeStr(PartyBuildConstant.getTimeStr(meet.getCreationTime()));
			}
			OrgTrend orgTrend = orgTrendService.getOrgTrendByTeacherId(teacherId);
			if(orgTrend != null){
				orgTrend.setTimeStr(PartyBuildConstant.getTimeStr(orgTrend.getCreationTime()));
			}
			
			map.put("enterOrg", true);
			
//			if(orgTrend == null && meet== null 
//					&& commonAct == null && leaderAct == null){
//				return errorFtl(map, "您当前没有可查看的党建相关内容！");
//			}
			map.put("orgTrend", orgTrend);
			map.put("meet", meet);
			map.put("commonAct", commonAct);
			map.put("leaderAct", leaderAct);
		} else {
			
		}
		
		map.put("teacherId", teacherId);
		map.put("userId", userId);
		return "/partybuild7/mobile/index.ftl";
	}
	
}
