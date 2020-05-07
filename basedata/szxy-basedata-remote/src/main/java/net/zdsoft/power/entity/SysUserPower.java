package net.zdsoft.power.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2018年6月7日下午1:48:06
 * 用户权限表
 */
@Entity
@Table(name = "sys_user_power")
public class SysUserPower extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	public static final int TYPE_USER_VALUE = 1;
	public static final int TYPE_ROLE_VALUE = 2;
	@Override
	public String fetchCacheEntitName() {
		return "sysUserPower";
	}
	private String targetId; //type=1 是user_Id type=2 是 role_id   (字段的类型需要修改成varchar,整合)
	private String powerId; //sysPower的主键id
	private int type;  //1--代表用户 2--代表角色   

	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getPowerId() {
		return powerId;
	}
	public void setPowerId(String powerId) {
		this.powerId = powerId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
