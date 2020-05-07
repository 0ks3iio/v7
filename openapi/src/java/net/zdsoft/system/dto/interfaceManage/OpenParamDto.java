package net.zdsoft.system.dto.interfaceManage;

public class OpenParamDto {
	private String id;
	private String uri;
	private String interfaceName;
	private String type;
	private String paramName;
	private String paramColumnName;
	private String description;
	private int mandatory;
	private String mcodeId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getMcodeId() {
		return mcodeId;
	}
	public void setMcodeId(String mcodeId) {
		this.mcodeId = mcodeId;
	}
}
