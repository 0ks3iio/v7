package net.zdsoft.power.remote.dto;

/**
 * @author yangsj  2018年6月20日上午11:45:26
 */
public class UserPowerRemoteDto {

	public static final int TYPE_USER_VALUE = 1;
	public static final int TYPE_ROLE_VALUE = 2;
	private String targetId; //type=1 是user_Id type=2 是 role_id   (字段的类型需要修改成varchar,整合)
	private int type;  //1--代表用户 2--代表角色   
	private String typeName; //类型名称
	private String powerName; //权限名称
	private String description; //权限描述
	private String value; //权限的唯一特征值
	private int source; //权限来源 1--默认 2--其他ap
    private String isActive; //权限是否启用  0--不启用 1 --启用
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getPowerName() {
		return powerName;
	}
	public void setPowerName(String powerName) {
		this.powerName = powerName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
