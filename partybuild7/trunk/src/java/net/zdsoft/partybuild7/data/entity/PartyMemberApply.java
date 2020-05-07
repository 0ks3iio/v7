package net.zdsoft.partybuild7.data.entity;

import net.zdsoft.framework.entity.BaseEntity;


import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="pb_member_apply")
public class PartyMemberApply extends BaseEntity<String> {
    private String partyMemberId;
    private String applicationContent;
    private Integer existsAttachment;

    private String orgId;
    @Transient
    private String orgName;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String fetchCacheEntitName() {
        return "PartyMemberApply";
    }

    public String getPartyMemberId() {
        return partyMemberId;
    }

    public void setPartyMemberId(String partyMemberId) {
        this.partyMemberId = partyMemberId;
    }

    public String getApplicationContent() {
        return applicationContent;
    }

    public void setApplicationContent(String applicationContent) {
        this.applicationContent = applicationContent;
    }

    public Integer getExistsAttachment() {
        return existsAttachment;
    }

    public void setExistsAttachment(Integer existsAttachment) {
        this.existsAttachment = existsAttachment;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
