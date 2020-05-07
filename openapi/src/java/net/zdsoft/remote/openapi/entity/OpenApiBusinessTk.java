package net.zdsoft.remote.openapi.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_openapi_business_tk")
public class OpenApiBusinessTk extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;

    private String ticketkeyId;
    private String businessId;
    /**
     * 1对象属性 2接口
     */
    private Integer type;
    private Integer isUsing;

    public String getTicketkeyId() {
        return ticketkeyId;
    }

    public void setTicketkeyId(String ticketkeyId) {
        this.ticketkeyId = ticketkeyId;
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

    public Integer getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(Integer isUsing) {
        this.isUsing = isUsing;
    }

    @Override
    public String fetchCacheEntitName() {
        return "openApiBusinessTk";
    }

}
