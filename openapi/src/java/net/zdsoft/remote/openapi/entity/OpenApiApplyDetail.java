package net.zdsoft.remote.openapi.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_openapi_apply_detail")
public class OpenApiApplyDetail extends BaseEntity<String> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    @Column(name = "apply_id")
    private String applyId;
    @Column(name = "business_id")
    private String businessId;

    private Integer type;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String fetchCacheEntitName() {
        return "openapiapplydetail";
    }

}
