package net.zdsoft.teaeaxam.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teaexam_site")
public class TeaexamSite extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2248412218865549213L;

	@Override
	public String fetchCacheEntitName() {
		return "teaexamSite";
	}
	
	private String schoolId;
	private String unionCode;
	private int siteNum;
	private int capacity;
	private Date creationTime;
	private Date modifyTime;
	
	@Transient
	private String siteName;
	@Transient
	private int setNum;
	@Transient
	private int validNum;
	@Transient
	private int usedNum;
	
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getUnionCode() {
		return unionCode;
	}
	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}
	public int getSiteNum() {
		return siteNum;
	}
	public void setSiteNum(int siteNum) {
		this.siteNum = siteNum;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
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
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public int getSetNum() {
		return setNum;
	}
	public void setSetNum(int setNum) {
		this.setNum = setNum;
	}
	public int getValidNum() {
		return validNum;
	}
	public void setValidNum(int validNum) {
		this.validNum = validNum;
	}
	public int getUsedNum() {
		return usedNum;
	}
	public void setUsedNum(int usedNum) {
		this.usedNum = usedNum;
	}
    
}
