package net.zdsoft.partybuild7.data.dao;

import java.util.List;

import net.zdsoft.framework.dao.BaseJpaRepositoryDao;
import net.zdsoft.partybuild7.data.entity.MeetingParticipator;

import org.springframework.data.jpa.repository.Query;

public interface MeetingParticipatorDao extends BaseJpaRepositoryDao<MeetingParticipator,String> {

    @Query(nativeQuery = true,value="select * from pb_meeting_participator where meeting_id = ?1")
    public List<MeetingParticipator> findAllByMeetingId(String meetingId);

    @Query(nativeQuery = true,value="select * from pb_meeting_participator where party_member_id = ?1")
    public List<MeetingParticipator> getAllByMemberId(String teaId);
}
