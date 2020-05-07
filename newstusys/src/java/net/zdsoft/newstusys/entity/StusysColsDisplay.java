package net.zdsoft.newstusys.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * @author weixh
 * 2019年9月3日	
 */
@SuppressWarnings("serial")
@Entity
@Table(name="stusys_cols_display")
public class StusysColsDisplay extends BaseEntity<String> {
	private String unitId;
	private String colsType;
	private String colsName;
	private String colsCode;
	private int colsOrder;
	private int colsConstraint;
	private int colsUse;
	private String colsKind;
	private String colsMcode;

	public String fetchCacheEntitName() {
		return "StusysColsDisplay";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getColsType() {
		return colsType;
	}

	public void setColsType(String colsType) {
		this.colsType = colsType;
	}

	public String getColsName() {
		return colsName;
	}

	public void setColsName(String colsName) {
		this.colsName = colsName;
	}

	public String getColsCode() {
		return colsCode;
	}

	public void setColsCode(String colsCode) {
		this.colsCode = colsCode;
	}

	public int getColsOrder() {
		return colsOrder;
	}

	public void setColsOrder(int colsOrder) {
		this.colsOrder = colsOrder;
	}

	public int getColsConstraint() {
		return colsConstraint;
	}

	public void setColsConstraint(int colsConstraint) {
		this.colsConstraint = colsConstraint;
	}

	public int getColsUse() {
		return colsUse;
	}

	public void setColsUse(int colsUse) {
		this.colsUse = colsUse;
	}

	public String getColsKind() {
		return colsKind;
	}

	public void setColsKind(String colsKind) {
		this.colsKind = colsKind;
	}

	public String getColsMcode() {
		return colsMcode;
	}

	public void setColsMcode(String colsMcode) {
		this.colsMcode = colsMcode;
	}

}
