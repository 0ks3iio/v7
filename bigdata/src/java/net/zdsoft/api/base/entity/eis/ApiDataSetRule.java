package net.zdsoft.api.base.entity.eis;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * @author yangsj
 *
 */
@Entity
@Table(name = "bg_openapi_dataset_rule")
public class ApiDataSetRule extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		return "apiDatasetRule";
	}
	private String dsId;
	private String paramType;
	private String paramValue;
	private String paramJson;

	public String getDsId() {
		return dsId;
	}
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getParamJson() {
		return paramJson;
	}
	public void setParamJson(String paramJson) {
		this.paramJson = paramJson;
	}
}
