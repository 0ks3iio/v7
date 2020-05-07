package net.zdsoft.partybuild7.data.entity;

import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@javax.persistence.Entity
@Table(name="pb_meeting_participator")
public class MeetingParticipator extends BaseEntity<String> {

    private String meetingId;
    private String partyMemberId;

    public String getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public String getPartyMemberId() {
        return partyMemberId;
    }

    public void setPartyMemberId(String partyMemberId) {
        this.partyMemberId = partyMemberId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "MeetingParticipator";
    }
}
