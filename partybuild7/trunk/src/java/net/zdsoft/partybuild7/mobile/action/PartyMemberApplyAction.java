package net.zdsoft.partybuild7.mobile.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.partybuild7.data.entity.OrgUnitRelation;
import net.zdsoft.partybuild7.data.entity.PartyMember;
import net.zdsoft.partybuild7.data.entity.PartyMemberApply;
import net.zdsoft.partybuild7.data.entity.PartyOrg;
import net.zdsoft.partybuild7.data.service.OrgUnitRelationService;
import net.zdsoft.partybuild7.data.service.PartyMemberApplyService;
import net.zdsoft.partybuild7.data.service.PartyMemberService;
import net.zdsoft.partybuild7.data.service.PartyOrgService;
import net.zdsoft.partybuild7.mobile.dto.PartyBuildOrgListDto;

@Controller
@RequestMapping("/mobile/open/partybuild7/partyMemberApply")
public class PartyMemberApplyAction extends MobileAction {
    @Autowired
    private OrgUnitRelationService orgUnitRelationService;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private PartyOrgService partyOrgService;
    @Autowired
    private PartyMemberApplyService partyMemberApplyService;
    @Autowired
    private PartyMemberService partyMemberService;
    @ResponseBody
    @RequestMapping("/save")
    public String savePartyMemberApply(PartyMemberApply partyMemberApply ,ModelMap map){

        PartyMemberApply apply = partyMemberApplyService.getPartyMemberApplyByPartyMemberId(partyMemberApply.getPartyMemberId());

        if(apply != null) partyMemberApply.setId(apply.getId());
        partyMemberApply.setExistsAttachment(0);
        try{
            partyMemberApplyService.savePartyMemberApply(partyMemberApply);
        }catch (Exception e){
            e.printStackTrace();
            error("保存失败");
        }

        return success("保存成功");
    }
    @ControllerInfo("积极分子申请")
    @RequestMapping("/partyMemberApplyLink")
    public String partyMemberLink(String teacherId , boolean isReApply, ModelMap map){
        Teacher teacher = Teacher.dc(teacherRemoteService.findOneById(teacherId));
        PartyMemberApply partyMemberApply = partyMemberApplyService.getPartyMemberApplyByPartyMemberId(teacherId);
//        PartyMember formalPartyMember = partyMemberService.getFormalPartyMemeberById(teacherId);
        PartyMember partyMember = partyMemberService.getPartyMemeberById(teacherId);

        if((partyMember != null && partyMember.getPartyApplicationState() < 1) || (partyMember == null && partyMemberApply == null)){
            map.put("isReadOnly",false);
        }else{
            map.put("isReadOnly" , true);
        }
        map.put("teacherId",teacherId);
        map.put("isReApply" ,isReApply?"1":"0");
        if(partyMemberApply == null){
            partyMemberApply = new PartyMemberApply();
            if(partyMember != null)partyMemberApply.setOrgId(partyMember.getOrgId());
        }else if(!isReApply){

            PartyOrg partyOrg = partyOrgService.findOne(partyMember.getOrgId());
            if(partyOrg != null){
                partyMember.setOrgName(partyOrg.getName());
            }
            map.put("partyMember" ,partyMember);
            map.put("partyMemberApply" ,partyMemberApply);
            return "/partybuild7/mobile/showPartyMemberActivistState.ftl";
        }
        List<OrgUnitRelation > orgUnitRelations = orgUnitRelationService.getAllByUnitId(teacher.getUnitId());
        List<String> orgIds = new ArrayList<>();
        for(OrgUnitRelation relation : orgUnitRelations){
            orgIds.add(relation.getOrgId());
        }
        List<PartyOrg> partyOrgs = partyOrgService.getPartyOrgListByIds(orgIds.toArray(new String[0]));
        List<PartyBuildOrgListDto> orgListDtos = new ArrayList<>();
        Map<String,String> orgMap = new HashMap<>();
        for(PartyOrg org :partyOrgs){
            PartyBuildOrgListDto dto = new PartyBuildOrgListDto();
            dto.setValue(org.getId());
            dto.setText(org.getName());
            orgListDtos.add(dto);
            orgMap.put(org.getId(),org.getName());
        }
        String orgs = JSONArray.toJSONString(orgListDtos);
        map.put("orgs" ,orgs);
        map.put("partyOrgs" ,partyOrgs);

        partyMemberApply.setPartyMemberId(teacherId);
        partyMemberApply.setOrgName(orgMap.get(partyMemberApply.getOrgId()));
        map.put("partyMemberApply", partyMemberApply);

        return "/partybuild7/mobile/partyMemberActivistApply.ftl";

    }

//    @RequestMapping("/orgList")
//    public String getOrgListByName( String teacherId, ModelMap map){
//
//
//        PartyMember partyMember = partyMemberService.getFormalPartyMemeberById(teacherId);
//        if(partyMember == null){
//
//            return "";
//        }
//        String orgId = partyMember.getOrgId();
//
//        PartyOrg org = partyOrgService.findOne(orgId);
//        map.put("orgName" ,org.getName());
//        List<PartyMember> partyMemberAwaitingList = partyMemberService.getPartyMemberListByOrgIdStates(orgId,PartyMember.PARTY_MEMBER_STATE_NORMAL, PartyMember.PARTY_IS_NOT_DENY, new int[]{PartyMember.PARTY_STATE_NOT});
//        List<PartyMember> partyMemberConfirmList = partyMemberService.getPartyMemberListByOrgIdStates(orgId,PartyMember.PARTY_MEMBER_STATE_NORMAL, PartyMember.PARTY_IS_NOT_DENY, new int[]{PartyMember.PARTY_STATE_ACTIVIST});
//        List<PartyMember> partyMemberNotConfirmList = partyMemberService.getPartyMemberListByOrgIdStates(orgId,PartyMember.PARTY_MEMBER_STATE_NORMAL, PartyMember.PARTY_IS_DENY, new int[]{PartyMember.PARTY_STATE_NOT});
//
//        map.put("partyMemberAwaitingList" ,partyMemberAwaitingList);
//        map.put("partyMemberConfirmList" ,partyMemberConfirmList);
//        map.put("partyMemberNotConfirmList" ,partyMemberNotConfirmList);
//        return "/partybuild7/mobile/partyMemberActivistList.ftl";
//    }


//    @RequestMapping("/activistState")
//    public String showActivistState(String teacherId , ModelMap map){
//
//        PartyMember partyMember = partyMemberService.getPartyMemeberById(teacherId);
//        PartyOrg partyOrg = partyOrgService.findOne(partyMember.getOrgId());
//        if(partyOrg != null){
//            partyMember.setOrgName(partyOrg.getName());
//        }
//        map.put("partyMember" ,partyMember);
//
//        PartyMemberApply partyMemberApply = partyMemberApplyService.getPartyMemberApplyByPartyMemberId(teacherId);
//        map.put("partyMemberApply" ,partyMemberApply);
//        return "/partybuild7/mobile/showPartyMemberActivistState.ftl";
//    }
    @ResponseBody
    @RequestMapping("/delete")
    public String doDelete(String partyMemberId ){
        try{
            partyMemberApplyService.deleteByPartyMemberId(partyMemberId);
        }catch(Exception e){
            e.printStackTrace();
            return error();
        }
        return success();
    }

}
