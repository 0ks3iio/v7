package net.zdsoft.partybuild7.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.MemberMeeting;

import org.springframework.data.jpa.repository.Query;

public interface MemberMeetingDao extends BaseJpaRepositoryDao<MemberMeeting ,String> {

    /**
     * 根据ids 来查询会议记录
     * @param ids
     * @return
     */
    @Query(nativeQuery = true,value=" SELECT * FROM pb_meeting_information where id in (?1) order by creation_time desc ")
    public List<MemberMeeting> getMeetingsByIds(String[] ids);

    /**
     * 根据id 来查询 会议
     * @param meetingId
     * @return
     */
    @Query(nativeQuery = true,value="SELECT * FROM pb_meeting_information where id = ?1 ")
    public MemberMeeting getMeetingInfoById(String meetingId);


}
