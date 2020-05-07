package net.zdsoft.partybuild7.mobile.action;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.partybuild7.data.entity.OrgTrend;
import net.zdsoft.partybuild7.data.entity.PartyBuildAttachment;
import net.zdsoft.partybuild7.data.entity.PartyMember;
import net.zdsoft.partybuild7.data.service.OrgTrendService;
import net.zdsoft.partybuild7.data.service.PartyBuildAttachmentService;
import net.zdsoft.partybuild7.data.service.PartyMemberService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Controller
@RequestMapping("/mobile/open/partybuild7/orgTrend")
public class OrgTrendAction extends MobileAction {
    @Autowired
    private OrgTrendService orgTrendService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Autowired
    private PartyBuildAttachmentService partyBuildAttachmentService;
    @Autowired
    private PartyMemberService partyMemberService;
    @RequestMapping("/list")
    @ControllerInfo("列表显示")
    public String getOrgTrendList(String teacherId , ModelMap map){
        PartyMember partyMemeber = partyMemberService.getPartyMemeberById(teacherId);
        if(partyMemeber != null){
            List<OrgTrend> orgTrendList = orgTrendService.getAllByOrgId(partyMemeber.getOrgId());
            map.put("orgTrendList" , orgTrendList);
        }
        map.put("teacherId", teacherId);
        //orgTrend/showInfo?id=402896C65EA92DC8015EB6EB8A5F01B9
        ////mobile/open/partybuild7/orgTrend/list?teacherId=4028808F5118FDF90151190CBDA60065
        return "/partybuild7/mobile/orgTreadList.ftl";
    }
    @RequestMapping("/showInfo")
    @ControllerInfo("详情显示")
    public String getOrgTrend(String id , ModelMap map){
        OrgTrend orgTrend = orgTrendService.getOrgTrendById(id);
        String ms = mcodeRemoteService.findByMcodeIds("DM-TZFL");
        Map<String ,McodeDetail> mcodeDetailMap = EntityUtils.getMap(SUtils.dt(ms, McodeDetail.class), "thisId"); 
        if (orgTrend != null && orgTrend.getRunningAccountType() != null) {
			int runningAccountType = orgTrend.getRunningAccountType();
			McodeDetail mcodeDetail = mcodeDetailMap.get(String
					.valueOf(runningAccountType));
			if (mcodeDetail != null)
				orgTrend.setRunningAccountTypeStr(mcodeDetail.getMcodeContent());
		}
		map.put("orgTrend" , orgTrend);

        List<PartyBuildAttachment> partyBuildAttachments = partyBuildAttachmentService.getAttachmentsByObjectId(id);

        map.put("attachments" , partyBuildAttachments);

        return "/partybuild7/mobile/showOrgTread.ftl";
    }
}
