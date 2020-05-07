package net.zdsoft.partybuild7.data.service;

import java.util.List;

import net.zdsoft.basedata.service.BaseService;
import net.zdsoft.partybuild7.data.entity.MemberMeeting;

public interface MemberMeetingService extends BaseService<MemberMeeting ,String> {

    /**
     * 根据teacherId 查询 会议
     * @param teachId
     * @return
     */
    public List<MemberMeeting> getMemberMeetingListByTeacherId(String teachId);

    /**
     * 查询单条 会议信息
     * @param id
     * @return
     */
    public MemberMeeting getMeetingInfoById(String id);

    public MemberMeeting getMeetingByTeacherId(String teacherId);
}
