package net.zdsoft.base.entity.eis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "eis_openapi_param")
public class OpenApiParameter extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    @Override
    public String fetchCacheEntitName() {
        return "openApiParameter";
    }
    @Column(name = "interface_id", nullable = false, length = 32)
    private String interfaceId;
    @Column(name = "param_name")
    private String paramName;
    @Column(name = "param_column_name")
    private String paramColumnName;
    @Column
    private String description;
    @Column
    private int mandatory;
    @Column(name = "display_order")
    private int displayOrder;
    @Column(name = "mcode_id")
    private String mcodeId;

    public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamColumnName() {
        return paramColumnName;
    }

    public void setParamColumnName(String paramColumnName) {
        this.paramColumnName = paramColumnName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMandatory() {
        return mandatory;
    }

    public void setMandatory(int mandatory) {
        this.mandatory = mandatory;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getMcodeId() {
        return mcodeId;
    }

    public void setMcodeId(String mcodeId) {
        this.mcodeId = mcodeId;
    }
}
