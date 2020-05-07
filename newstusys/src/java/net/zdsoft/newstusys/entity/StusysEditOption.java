package net.zdsoft.newstusys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 学生家长端编辑开关
 * 
 * @author weixh
 * 2019年9月3日	
 */
@SuppressWarnings("serial")
@Entity
@Table(name="stusys_edit_option")
public class StusysEditOption extends BaseEntity<String> {
	private String unitId;
	private int isOpen;
	private Date modifyTime;
	
	@Transient
	private String displayCols;

	public String fetchCacheEntitName() {
		return "StusysEditOption";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getDisplayCols() {
		return displayCols;
	}

	public void setDisplayCols(String displayCols) {
		this.displayCols = displayCols;
	}

}
