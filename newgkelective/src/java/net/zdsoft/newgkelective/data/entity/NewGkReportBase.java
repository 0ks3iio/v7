package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="newgkelective_report_base")
public class NewGkReportBase extends BaseEntity<String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String reportId;
	//01：教师 02：场地
	private String dataKeyType;
	//data_key_type:01 科目id
	//data_key_type:02 32个0代表场地
	private String keyId;
	private int num;
	private Date creationTime;
	private Date modifyTime;
	@Override
	public String fetchCacheEntitName() {
		return "newGkReportBase";
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
	public String getDataKeyType() {
		return dataKeyType;
	}
	public void setDataKeyType(String dataKeyType) {
		this.dataKeyType = dataKeyType;
	}
	public String getKeyId() {
		return keyId;
	}
	public void setKeyId(String keyId) {
		this.keyId = keyId;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
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
