package net.zdsoft.datacollection.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dc_project_range")
public class DcProjectRange extends BaseEntity<String> {

	@Column(length = 32)
	private String projectId;
	@Column(length = 32)
	private String customerId;
	@Column(length = 1)
	private Integer customerType;

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

}
