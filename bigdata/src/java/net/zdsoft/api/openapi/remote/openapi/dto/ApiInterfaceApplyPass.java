package net.zdsoft.api.openapi.remote.openapi.dto;

import java.util.List;

/**
 * @author shenke
 * @since 2019/5/22 下午3:33
 */
public final class ApiInterfaceApplyPass {

    private String id;
    private String interfaceId;
    private Integer maxInvokeNumber;
    private Integer maxCount;
    private List<String> fieldIds;
    private String dataSetRuleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getMaxInvokeNumber() {
        return maxInvokeNumber;
    }

    public void setMaxInvokeNumber(Integer maxInvokeNumber) {
        this.maxInvokeNumber = maxInvokeNumber;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public List<String> getFieldIds() {
        return fieldIds;
    }

    public void setFieldIds(List<String> fieldIds) {
        this.fieldIds = fieldIds;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

	public String getDataSetRuleId() {
		return dataSetRuleId;
	}

	public void setDataSetRuleId(String dataSetRuleId) {
		this.dataSetRuleId = dataSetRuleId;
	}
}
