package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="newgkelective_report_chose")
public class NewGkReportChose extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String reportId;
	//总人数 01， 已选 02 ，未选 03   ，3科  04  ， 单科  05
	private String dataType;
	//3科（科目ids以，隔开），单科（科目id），总人数(32个0)，已选（32个0），未选（32个0）
	private String dataKeys;
	private int boyNumber;
	private int girlNumber;
	private Date creationTime;
	private Date modifyTime;

	@Override
	public String fetchCacheEntitName() {
		return "newGkReportChose";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataKeys() {
		return dataKeys;
	}

	public void setDataKeys(String dataKeys) {
		this.dataKeys = dataKeys;
	}

	public int getBoyNumber() {
		return boyNumber;
	}

	public void setBoyNumber(int boyNumber) {
		this.boyNumber = boyNumber;
	}

	public int getGirlNumber() {
		return girlNumber;
	}

	public void setGirlNumber(int girlNumber) {
		this.girlNumber = girlNumber;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
