package net.zdsoft.partybuild7.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 活动参与人
 * 
 * @author weixh
 * @since 2017-9-19 下午1:58:25
 */
@SuppressWarnings("serial")
@Entity
@Table(name="pb_activity_participator")
public class ActivityParticipator extends BaseEntity<String> {
	private String activityId;
	private String partyMemberId;

	/* 
	 * @see net.zdsoft.framework.entity.BaseEntity#fetchCacheEntitName()
	 */
	@Override
	public String fetchCacheEntitName() {
		return "ActivityParticipator";
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getPartyMemberId() {
		return partyMemberId;
	}

	public void setPartyMemberId(String partyMemberId) {
		this.partyMemberId = partyMemberId;
	}

}
