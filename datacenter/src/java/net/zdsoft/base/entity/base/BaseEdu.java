package net.zdsoft.base.entity.base;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "base_edu")
public class BaseEdu extends DataCenterBaseEntity {
	private static final long serialVersionUID = 4845424977829317908L;
	private String parentId;
	private String eduName;
	private String unionCode;
	private String regionCode;
	private Integer unitType;// 类型

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getEduName() {
		return eduName;
	}

	public void setEduName(String eduName) {
		this.eduName = eduName;
	}

	public String getUnionCode() {
		return unionCode;
	}

	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

	@Override
	public String fetchCacheEntitName() {
		return "baseEsdu";
	}

}
