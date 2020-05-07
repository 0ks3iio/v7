package net.zdsoft.api.openapi.remote.openapi.vo;

import net.zdsoft.api.base.entity.eis.ApiDataSetRule;
import net.zdsoft.api.base.entity.eis.ApiEntity;

import java.util.List;

/**
 * @author shenke
 * @since 2019/5/22 下午1:52
 */
public final class OpenApiInterfaceApplyVo {

    private String id;
    private String name;
    private String uri;
    private String method;
    private Integer status;

    private int maxInvokeNumber;
    private int maxCount;

    private String interfaceId;

    private List<ApiEntity> fields;
    
    private List<ApiDataSetRule> alldataSetRules;

    private ApiDataSetRule dataSetRule;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<ApiEntity> getFields() {
        return fields;
    }

    public void setFields(List<ApiEntity> fields) {
        this.fields = fields;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getMaxInvokeNumber() {
        return maxInvokeNumber;
    }

    public void setMaxInvokeNumber(int maxInvokeNumber) {
        this.maxInvokeNumber = maxInvokeNumber;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public List<ApiDataSetRule> getAlldataSetRules() {
		return alldataSetRules;
	}

	public void setAlldataSetRules(List<ApiDataSetRule> alldataSetRules) {
		this.alldataSetRules = alldataSetRules;
	}

	public ApiDataSetRule getDataSetRule() {
		return dataSetRule;
	}

	public void setDataSetRule(ApiDataSetRule dataSetRule) {
		this.dataSetRule = dataSetRule;
	}
}
