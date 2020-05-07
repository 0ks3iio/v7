package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_family")
public class Family extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@Column(updatable=false)
	private String studentId;
	private String schoolId;
	private String relation;
	private String realName;
	private Integer isGuardian;
	private String company;
	private String duty;
	private String linkPhone;
	private String workCode;
	private String professionCode;
	private String dutyLevel;
	private String politicalStatus;
	private String maritalStatus;
	private String emigrationPlace;
	private Date birthday;
	private String culture;
	private String identityCard;
	private String nation;
	private String homepage;
	private String remark;
	private String postalcode;
	private String linkAddress;
	private String email;
	private String mobilePhone;
	private String officeTel;
	private Integer sex;
	private String regionCode;
	private String chargeNumber;
	private Integer chargeNumberType;
	private Integer isLeaveSchool;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	private Integer eventSource;
	private String receiveInfomation;
	private String smartMobilePhone;
	private String identitycardType;
	private String health;
	private String registerPlace;
	private String relationRemark;
	private Integer openUserStatus;
	private String country;
	
	@Transient
	private Long sequenceIntId;
	@Transient
	private String schoolName;
	@Transient
	private String studentName;
	
	
	@Override
	public String fetchCacheEntitName() {
		return "family";
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getIsGuardian() {
		return isGuardian;
	}

	public void setIsGuardian(Integer isGuardian) {
		this.isGuardian = isGuardian;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getProfessionCode() {
		return professionCode;
	}

	public void setProfessionCode(String professionCode) {
		this.professionCode = professionCode;
	}

	public String getDutyLevel() {
		return dutyLevel;
	}

	public void setDutyLevel(String dutyLevel) {
		this.dutyLevel = dutyLevel;
	}

	public String getPoliticalStatus() {
		return politicalStatus;
	}

	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getEmigrationPlace() {
		return emigrationPlace;
	}

	public void setEmigrationPlace(String emigrationPlace) {
		this.emigrationPlace = emigrationPlace;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCulture() {
		return culture;
	}

	public void setCulture(String culture) {
		this.culture = culture;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getLinkAddress() {
		return linkAddress;
	}

	public void setLinkAddress(String linkAddress) {
		this.linkAddress = linkAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getOfficeTel() {
		return officeTel;
	}

	public void setOfficeTel(String officeTel) {
		this.officeTel = officeTel;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getChargeNumber() {
		return chargeNumber;
	}

	public void setChargeNumber(String chargeNumber) {
		this.chargeNumber = chargeNumber;
	}

	public Integer getChargeNumberType() {
		return chargeNumberType;
	}

	public void setChargeNumberType(Integer chargeNumberType) {
		this.chargeNumberType = chargeNumberType;
	}

	public Integer getIsLeaveSchool() {
		return isLeaveSchool;
	}

	public void setIsLeaveSchool(Integer isLeaveSchool) {
		this.isLeaveSchool = isLeaveSchool;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getEventSource() {
		return eventSource;
	}

	public void setEventSource(Integer eventSource) {
		this.eventSource = eventSource;
	}

	public String getReceiveInfomation() {
		return receiveInfomation;
	}

	public void setReceiveInfomation(String receiveInfomation) {
		this.receiveInfomation = receiveInfomation;
	}

	public String getSmartMobilePhone() {
		return smartMobilePhone;
	}

	public void setSmartMobilePhone(String smartMobilePhone) {
		this.smartMobilePhone = smartMobilePhone;
	}

	public String getIdentitycardType() {
		return identitycardType;
	}

	public void setIdentitycardType(String identitycardType) {
		this.identitycardType = identitycardType;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getRegisterPlace() {
		return registerPlace;
	}

	public void setRegisterPlace(String registerPlace) {
		this.registerPlace = registerPlace;
	}

	public String getRelationRemark() {
		return relationRemark;
	}

	public void setRelationRemark(String relationRemark) {
		this.relationRemark = relationRemark;
	}

	public Long getSequenceIntId() {
		return sequenceIntId;
	}

	public void setSequenceIntId(Long sequenceIntId) {
		this.sequenceIntId = sequenceIntId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public Integer getOpenUserStatus() {
		return openUserStatus;
	}

	public void setOpenUserStatus(Integer openUserStatus) {
		this.openUserStatus = openUserStatus;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
}
