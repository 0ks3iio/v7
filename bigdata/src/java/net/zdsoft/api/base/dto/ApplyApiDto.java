package net.zdsoft.api.base.dto;

import java.util.List;

public class ApplyApiDto {
	
	private String type;
	private String interfaceId;
	private String dataType;
	private String appIds;
	private List<EntityDto> entityDtos;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInterfaceId() {
		return interfaceId;
	}
	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}
	public List<EntityDto> getEntityDtos() {
		return entityDtos;
	}
	public void setEntityDtos(List<EntityDto> entityDtos) {
		this.entityDtos = entityDtos;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getAppIds() {
		return appIds;
	}
	public void setAppIds(String appIds) {
		this.appIds = appIds;
	}
}
