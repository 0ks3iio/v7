package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eclasscard_other_set")
public class EccOtherSet extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private Integer nowvalue;
	private Integer type;
	private String param;
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Integer getNowvalue() {
		return nowvalue;
	}

	public void setNowvalue(Integer nowvalue) {
		this.nowvalue = nowvalue;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardOtherSet";
	}

}
