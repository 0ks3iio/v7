package net.zdsoft.base.dto;


public class OpenEntityDto {

	private String id;
	private String entityName;
	private String displayName;
	private Integer isUsing;
    private String type;
    private String entityType;
    private String entityComment;
    private String mcodeId;
    private int mandatory;
    private int isSensitive;
    private Integer typeMandatory; //推送是否必填字段
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public Integer getIsUsing() {
		return isUsing;
	}
	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getEntityComment() {
		return entityComment;
	}
	public void setEntityComment(String entityComment) {
		this.entityComment = entityComment;
	}
	public String getMcodeId() {
		return mcodeId;
	}
	public void setMcodeId(String mcodeId) {
		this.mcodeId = mcodeId;
	}
	public int getMandatory() {
		return mandatory;
	}
	public void setMandatory(int mandatory) {
		this.mandatory = mandatory;
	}
	public int getIsSensitive() {
		return isSensitive;
	}
	public void setIsSensitive(int isSensitive) {
		this.isSensitive = isSensitive;
	}
	public Integer getTypeMandatory() {
		return typeMandatory;
	}
	public void setTypeMandatory(Integer typeMandatory) {
		this.typeMandatory = typeMandatory;
	}
}
