package net.zdsoft.basedata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_charts_role_user")
public class ChartsRoleUser extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	@Column(nullable = false)
	private String userId;
	@Column(nullable = false)
	private Integer chartsRoleId;
	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Integer getChartsRoleId() {
		return chartsRoleId;
	}


	public void setChartsRoleId(Integer chartsRoleId) {
		this.chartsRoleId = chartsRoleId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "chartsRoleUser";
	}

}
