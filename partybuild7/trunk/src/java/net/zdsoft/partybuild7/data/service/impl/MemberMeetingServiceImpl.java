package net.zdsoft.partybuild7.data.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.zdsoft.basedata.entity.Teacher;
import net.zdsoft.basedata.remote.service.TeacherRemoteService;
import net.zdsoft.basedata.service.impl.BaseServiceImpl;
import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.framework.utils.EntityUtils;
import net.zdsoft.framework.utils.SUtils;
import net.zdsoft.partybuild7.data.dao.MeetingParticipatorDao;
import net.zdsoft.partybuild7.data.dao.MemberMeetingDao;
import net.zdsoft.partybuild7.data.entity.MeetingParticipator;
import net.zdsoft.partybuild7.data.entity.MemberMeeting;
import net.zdsoft.partybuild7.data.service.MemberMeetingService;
import net.zdsoft.system.entity.mcode.McodeDetail;
import net.zdsoft.system.remote.service.McodeRemoteService;

@Service("MemberMeetingService")
public class MemberMeetingServiceImpl extends BaseServiceImpl<MemberMeeting , String>
        implements MemberMeetingService {


    @Autowired
    private MemberMeetingDao memberMeetingDao;
    @Autowired
    private MeetingParticipatorDao meetingParticipatorDao;
    @Autowired
    private TeacherRemoteService teacherRemoteService;
    @Autowired
    private McodeRemoteService mcodeRemoteService;
    @Override
    /**
     * 根据teacherId 查询信息
     */
    public List<MemberMeeting> getMemberMeetingListByTeacherId(String teachId) {

        List<MeetingParticipator> meetingParticipators = meetingParticipatorDao.getAllByMemberId(teachId);
        List<MemberMeeting> meetingList;
		if (CollectionUtils.isNotEmpty(meetingParticipators)) {
			List<String> meetingIds = new ArrayList<String>();
			for (MeetingParticipator participator : meetingParticipators) {
				meetingIds.add(participator.getMeetingId());
			}
			meetingList = memberMeetingDao.getMeetingsByIds(meetingIds
					.toArray(new String[0]));
		} else {
			meetingList = new ArrayList<MemberMeeting>();
		}
        return meetingList;
    }

    @Override
    protected BaseJpaRepositoryDao<MemberMeeting, String> getJpaDao() {
        return memberMeetingDao;
    }

    @Override
    protected Class<MemberMeeting> getEntityClass() {
        return MemberMeeting.class;
    }

    @Override
    public MemberMeeting getMeetingInfoById(String id) {
        MemberMeeting memberMeeting = memberMeetingDao.getMeetingInfoById(id);
        if(memberMeeting == null){
        	return null;
        }
        if (memberMeeting.getRunningAccountType() != null) {
        	String ms = mcodeRemoteService.findByMcodeIds("DM-TZFL");
        	Map<String ,McodeDetail> mcodeDetailMap = EntityUtils.getMap(SUtils.dt(ms, McodeDetail.class), "thisId");
			int runningAccountType = memberMeeting.getRunningAccountType();
			McodeDetail detail = mcodeDetailMap.get(String
					.valueOf(runningAccountType));
			if (detail != null) {
				memberMeeting
						.setRunningAccountTypeStr(detail.getMcodeContent());
			}
		}
		List<MeetingParticipator> meetingParticipators = meetingParticipatorDao.findAllByMeetingId(id);
        List<String> teaIds = new ArrayList<String>();
        for(MeetingParticipator participator : meetingParticipators){
            teaIds.add(participator.getPartyMemberId());
        }
        
        String ts = teacherRemoteService.findListByIds(teaIds.toArray(new String[0]));
        Map<String ,Teacher> teacherMap = EntityUtils.getMap(SUtils.dt(ts, Teacher.class), "id");
        StringBuffer buffer = new StringBuffer();
        int size = meetingParticipators.size();
        for(int i=0;i<size; i++){
            Teacher teacher = teacherMap.get(meetingParticipators.get(i).getPartyMemberId());
            String name = "";
            if( teacher != null){
                name = teacher.getTeacherName();
                if( i == size -1 ){
                    buffer.append(name);
                }else{
                    buffer.append(name + "、");
                }
            }


        }
        memberMeeting.setMemberNames(buffer.toString());
        return memberMeeting;
    }

    /**
     * 根据teacherId 查询最新一条信息
     *
     * @param teacherId
     * @return
     */
    @Override
    public MemberMeeting getMeetingByTeacherId(String teacherId) {
        List<MemberMeeting> meetingList = getMemberMeetingListByTeacherId(teacherId);
        MemberMeeting memberMeeting = null;
        if(CollectionUtils.isNotEmpty(meetingList)){
            memberMeeting = meetingList.get(0);
        }

        return memberMeeting;
    }
}
