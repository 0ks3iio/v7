package net.zdsoft.basedata.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_charts_role_lab")
public class ChartsRoleLab extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	@Column(nullable = false)
	private Integer chartsId;
	@Column(nullable = false)
	private Integer chartsRoleId;

	public Integer getChartsId() {
		return chartsId;
	}

	public void setChartsId(Integer chartsId) {
		this.chartsId = chartsId;
	}

	public Integer getChartsRoleId() {
		return chartsRoleId;
	}

	public void setChartsRoleId(Integer chartsRoleId) {
		this.chartsRoleId = chartsRoleId;
	}
	@Override
	public String fetchCacheEntitName() {
		return "chartsRoleLab";
	}

}
