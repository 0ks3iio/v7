package net.zdsoft.partybuild7.mobile.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.partybuild7.data.constant.PartyBuildConstant;
import net.zdsoft.partybuild7.data.entity.PartyMember;
import net.zdsoft.partybuild7.data.entity.PartyMemberApply;
import net.zdsoft.partybuild7.data.entity.PartyOrg;
import net.zdsoft.partybuild7.data.service.PartyMemberApplyService;
import net.zdsoft.partybuild7.data.service.PartyMemberService;
import net.zdsoft.partybuild7.data.service.PartyOrgService;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author weixh
 * @since 2017-9-29 下午2:16:32
 */
@Controller
@RequestMapping("/mobile/open/partybuild7/memberList")
public class PartyMemberApplyListAction extends MobileAction {
	@Autowired
	private PartyMemberService partyMemberService;
	@Autowired
	private PartyMemberApplyService partyMemberApplyService;
	@Autowired
	private TeacherRemoteService teacherRemoteService;
	@Autowired
	private PartyOrgService partyOrgService;
	
	@ControllerInfo(value="申请信息首页")
	@RequestMapping("/index")
	public String showIndex(String teacherId, int applyState, ModelMap map, HttpServletRequest request){
		PartyMember pm = partyMemberService.getFormalPartyMemeberById(teacherId);
		if(pm == null){
			return errorFtl(map, PartyBuildConstant.USER_NOT_INIT);
		}
		int auditState = NumberUtils.toInt(request.getParameter("auditState"));
		map.put("orgId", pm.getOrgId());
		map.put("applyState", applyState);
		map.put("auditState", auditState);
		map.put("teacherId", teacherId);
		return "/partybuild7/mobile/applyIndex.ftl";
	}
	
	@ControllerInfo(value="申请信息列表")
	@RequestMapping("/list")
	public String showList(String orgId, int applyState, int auditState, ModelMap map){
		List<PartyMember> memberList;
		if (auditState != PartyBuildConstant.MEMBER_AUDIT_STATE_PASS) {
			int isdeny = 0;
			if (auditState == PartyBuildConstant.MEMBER_AUDIT_STATE_DENY) {
				isdeny = 1;
			}
			memberList = partyMemberService.getPartyMemberListByOrgIdStates(
					orgId, isdeny, new int[] { applyState });
		} else {
			memberList = partyMemberService.getMemberListByState(orgId, new int[]{applyState});
		}
//		memberList.addAll(memberList);
		map.put("memberList", memberList);
		map.put("applyState", applyState);
		map.put("auditState", auditState);
		return "/partybuild7/mobile/applyList.ftl";
	}
	
	@ControllerInfo(value="申请信息详情")
	@RequestMapping("/detail")
	public String showDetail(String teacherId, int applyState, int auditState, ModelMap map){
		PartyMember pm = partyMemberService.getPartyMemeberById(teacherId);
		if(pm != null){
			Teacher tea = Teacher.dc(teacherRemoteService.findOneById(teacherId));
			if(tea == null){
				return errorFtl(map, "对应教职工不存在！");
			}
			PartyOrg org = partyOrgService.findOne(pm.getOrgId());
			if(org == null){
				return errorFtl(map, "对应党组织不存在！");
			}
			pm.setTeacherName(tea.getTeacherName());
			pm.setSex(tea.getSex());
			pm.setPhone(tea.getMobilePhone());
			pm.setOrgName(org.getName());
			PartyMemberApply apply = partyMemberApplyService.getPartyMemberApplyByPartyMemberId(pm.getId());
			map.put("apply", apply);
		}
		map.put("mem", pm);
		map.put("applyState", applyState);
		map.put("auditState", auditState);
		return "/partybuild7/mobile/applyInfo.ftl";
	}
	
	@ControllerInfo(value="审核")
	@RequestMapping("/audit")
	@ResponseBody
	public String updateState(String ids, int applyState, int isDeny, String remark, ModelMap map){
		try {
			partyMemberService.saveMember(ids.split(","), applyState, isDeny, remark);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return error("保存失败！");
		}
		return success("保存成功！");
	}

}
