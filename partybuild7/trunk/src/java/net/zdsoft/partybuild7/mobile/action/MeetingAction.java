package net.zdsoft.partybuild7.mobile.action;

import java.util.List;

import net.zdsoft.framework.action.MobileAction;
import net.zdsoft.framework.annotation.ControllerInfo;
import net.zdsoft.partybuild7.data.entity.MemberMeeting;
import net.zdsoft.partybuild7.data.entity.PartyBuildAttachment;
import net.zdsoft.partybuild7.data.service.MemberMeetingService;
import net.zdsoft.partybuild7.data.service.PartyBuildAttachmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mobile/open/partybuild7")
public class MeetingAction extends MobileAction {

    @Autowired
    private MemberMeetingService memberMeetingService;
    @Autowired
    private PartyBuildAttachmentService partyBuildAttachmentService;
    @RequestMapping("/memberMeeting/meetingList")
    @ControllerInfo(value="获取会议列表")
    public String getMeetingList(String teacherId ,ModelMap map){
        List<MemberMeeting> memberMeetings =memberMeetingService.getMemberMeetingListByTeacherId(teacherId);
        map.put("memberMeetings",memberMeetings);
        map.put("teacherId", teacherId);
        ///mobile/open/partybuild7/memberMeeting/meetingList?teaId=4028808F5118FDF90151190CBDA60065
        return "/partybuild7/mobile/meetingList.ftl";
    }

    @RequestMapping("/memberMeeting/info")
    @ControllerInfo(value="会议详情")
    public String getMeetingInfo(String meetingId ,ModelMap map){
        MemberMeeting memberMeeting = memberMeetingService.getMeetingInfoById(meetingId);
		map.put("memberMeeting" ,memberMeeting);
        List<PartyBuildAttachment> partyBuildAttachments = partyBuildAttachmentService.getAttachmentsByObjectId(meetingId);
        map.put("attachments" , partyBuildAttachments);
        return "/partybuild7/mobile/showMeetingInfo.ftl";
    }
}
