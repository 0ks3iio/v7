package net.zdsoft.basedata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_charts_user")
public class ChartsUser extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	@Column(nullable = false)
	private String userId;
	@Column(nullable = false)
	private Integer chartsId;
	
	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public Integer getChartsId() {
		return chartsId;
	}


	public void setChartsId(Integer chartsId) {
		this.chartsId = chartsId;
	}


	@Override
	public String fetchCacheEntitName() {
		return "chartsUser";
	}

}
