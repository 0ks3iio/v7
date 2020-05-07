package net.zdsoft.partybuild7.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="pb_party_organization")
public class PartyOrg extends BaseEntity<String> {
    private String name;
    private String orgType;
    private String parentOrgId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "PartyOrg";
    }
}
