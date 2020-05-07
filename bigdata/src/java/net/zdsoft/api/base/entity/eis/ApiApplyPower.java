package net.zdsoft.api.base.entity.eis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * @author yangsj  2018年7月11日下午5:38:05
 * 数据集的控制
 */
@Entity
@Table(name = "bg_openapi_apply_power")
public class ApiApplyPower extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
	private String powerName;
	private String powerColumnName;
	private String powerValue;
	@Column(nullable = false, length = 32)
	private String ticketKey;
	private String interfaceId;
	private String type;
	
	@Override
	public String fetchCacheEntitName() {
		return "openApiApplyPower";
	}

	public String getPowerName() {
		return powerName;
	}

	public void setPowerName(String powerName) {
		this.powerName = powerName;
	}

	public String getPowerColumnName() {
		return powerColumnName;
	}

	public void setPowerColumnName(String powerColumnName) {
		this.powerColumnName = powerColumnName;
	}

	public String getPowerValue() {
		return powerValue;
	}

	public void setPowerValue(String powerValue) {
		this.powerValue = powerValue;
	}

	public String getTicketKey() {
		return ticketKey;
	}

	public void setTicketKey(String ticketKey) {
		this.ticketKey = ticketKey;
	}

	public String getInterfaceId() {
		return interfaceId;
	}

	public void setInterfaceId(String interfaceId) {
		this.interfaceId = interfaceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
