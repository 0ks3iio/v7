package net.zdsoft.comprehensive.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "scoremanage_compre_param")
public class CompreParameter extends BaseEntity<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unitId;

	private String parkey;

	private String type;

	private String name;

	private String openYear;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getParkey() {
		return parkey;
	}

	public void setParkey(String parkey) {
		this.parkey = parkey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenYear() {
		return openYear;
	}

	public void setOpenYear(String openYear) {
		this.openYear = openYear;
	}

	@Override
	public String fetchCacheEntitName() {
		return "getComPar";
	}

	@Override
	public String toString() {
		return "CompreParameter{" +
				"unitId='" + unitId + '\'' +
				", parkey='" + parkey + '\'' +
				", type='" + type + '\'' +
				", name='" + name + '\'' +
				", openYear='" + openYear + '\'' +
				'}';
	}
}
