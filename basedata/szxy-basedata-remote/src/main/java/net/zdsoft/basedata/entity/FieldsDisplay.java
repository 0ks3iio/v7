package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_fields_display")
public class FieldsDisplay extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String unitId;
	private String colsType;
	private String parentId;
	private Integer isExistsSubitem;
	private String colsName;
	private String colsCode;
	private String colsKind;
	private String colsMcode;
	private Integer colsOrder;
	private Integer colsUse;

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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getIsExistsSubitem() {
		return isExistsSubitem;
	}

	public void setIsExistsSubitem(Integer isExistsSubitem) {
		this.isExistsSubitem = isExistsSubitem;
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

	public Integer getColsOrder() {
		return colsOrder;
	}

	public void setColsOrder(Integer colsOrder) {
		this.colsOrder = colsOrder;
	}

	public Integer getColsUse() {
		return colsUse;
	}

	public void setColsUse(Integer colsUse) {
		this.colsUse = colsUse;
	}

	@Override
	public String fetchCacheEntitName() {
		return "fieldsDisplay";
	}

}
