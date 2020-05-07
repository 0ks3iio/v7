package net.zdsoft.partybuild7.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="pb_org_unit_relation")
public class OrgUnitRelation extends BaseEntity<String> {

    private String orgId;

    private String unitId;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    @Override
    public String fetchCacheEntitName() {
        return "OrgUnitRelation";
    }
}
