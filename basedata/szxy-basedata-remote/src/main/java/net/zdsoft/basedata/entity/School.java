package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_school")
public class School extends BaseEntity<String>{
	
	public static final int DEFAULT_RUN_SCHOOL_TYPE = 831;
	private static final long serialVersionUID = 1L;
	private String schoolName;
	private String englishName;
	private String shortName;
	private String schoolCode;
	private String address;
	private String regionCode;
	private String schoolmaster;
	private String partyMaster;
	private Date buildDate;
	private String anniversary;
	private Integer runSchoolType;
	private String schoolType;
	private Integer regionType;
	private Integer regionEconomy;
	private Integer regionNation;
	private Integer gradeYear;
	private Integer gradeAge;
	private Integer juniorYear;
	private Integer juniorAge;
	private Integer seniorYear;
	private Integer infantYear;
	private Integer infantAge;
	private String primaryLang;
	private String secondaryLang;
	private String recruitRegion;
	private String organizationCode;
	private String introduction;
	private String area;
	private String serialNumber;
	private String sections;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private int isDeleted;
	private int eventSource;
	private String schoolDistrictId;
	private String governor;
	private String postalcode;
	private String linkPhone;
	private String fax;
	private String email;
	private String homepage;
	private Integer emphases;
	private Integer isBoorish;
	private Integer demonstration;
	private String feature;
	private Integer publicAssist;
	private Double builtupArea;
	private Double greenArea;
	private Double sportsArea;
	private String legalPerson;
	private String industry;
	private String remark;
	private String schoolPropStat;
	private String schoolTypeGroup;
	private String ledgerSchoolType;
	private String regionManageDept;
	private String regionManageDeptCode;
	private String addressCode;
	private String legaRegistrationNumber;
	private String landCertificateNo;
	private Double longitude;
	private Double latitude;
	private String fillEmail;
	private String statStuffName;
	private String statStuffContact;
	private String isSchDistrictChief;
	private String statRegionCode;
	private String kgLevel;
	private String mobilePhone;
	private String regionPropertyCode;
	private String natureType;
	private Integer isMinoritySchool;
	private String schoolSetupName;
	private String specialRegionCode;
	@Transient
	private Long sequenceIntId;
	@Transient
	private String parentId;  //父单位id

	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	@Override
	public String fetchCacheEntitName() {
		return "school";
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getSchoolmaster() {
		return schoolmaster;
	}

	public void setSchoolmaster(String schoolmaster) {
		this.schoolmaster = schoolmaster;
	}

	public String getPartyMaster() {
		return partyMaster;
	}

	public void setPartyMaster(String partyMaster) {
		this.partyMaster = partyMaster;
	}

	public Date getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}

	public String getAnniversary() {
		return anniversary;
	}

	public void setAnniversary(String anniversary) {
		this.anniversary = anniversary;
	}

	public Integer getRunSchoolType() {
		return runSchoolType;
	}

	public void setRunSchoolType(Integer runSchoolType) {
		this.runSchoolType = runSchoolType;
	}

	public String getSchoolType() {
		return schoolType;
	}

	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}

	public Integer getRegionType() {
		return regionType;
	}

	public void setRegionType(Integer regionType) {
		this.regionType = regionType;
	}

	public Integer getRegionEconomy() {
		return regionEconomy;
	}

	public void setRegionEconomy(Integer regionEconomy) {
		this.regionEconomy = regionEconomy;
	}

	public Integer getRegionNation() {
		return regionNation;
	}

	public void setRegionNation(Integer regionNation) {
		this.regionNation = regionNation;
	}

	public Integer getGradeYear() {
		return gradeYear;
	}

	public void setGradeYear(Integer gradeYear) {
		this.gradeYear = gradeYear;
	}

	public Integer getGradeAge() {
		return gradeAge;
	}

	public void setGradeAge(Integer gradeAge) {
		this.gradeAge = gradeAge;
	}

	public Integer getJuniorYear() {
		return juniorYear;
	}

	public void setJuniorYear(Integer juniorYear) {
		this.juniorYear = juniorYear;
	}

	public Integer getJuniorAge() {
		return juniorAge;
	}

	public void setJuniorAge(Integer juniorAge) {
		this.juniorAge = juniorAge;
	}

	public Integer getSeniorYear() {
		return seniorYear;
	}

	public void setSeniorYear(Integer seniorYear) {
		this.seniorYear = seniorYear;
	}

	public Integer getInfantYear() {
		return infantYear;
	}

	public void setInfantYear(Integer infantYear) {
		this.infantYear = infantYear;
	}

	public Integer getInfantAge() {
		return infantAge;
	}

	public void setInfantAge(Integer infantAge) {
		this.infantAge = infantAge;
	}

	public String getPrimaryLang() {
		return primaryLang;
	}

	public void setPrimaryLang(String primaryLang) {
		this.primaryLang = primaryLang;
	}

	public String getSecondaryLang() {
		return secondaryLang;
	}

	public void setSecondaryLang(String secondaryLang) {
		this.secondaryLang = secondaryLang;
	}

	public String getRecruitRegion() {
		return recruitRegion;
	}

	public void setRecruitRegion(String recruitRegion) {
		this.recruitRegion = recruitRegion;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSections() {
		return sections;
	}

	public void setSections(String sections) {
		this.sections = sections;
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

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getEventSource() {
		return eventSource;
	}

	public void setEventSource(int eventSource) {
		this.eventSource = eventSource;
	}

	public String getSchoolDistrictId() {
		return schoolDistrictId;
	}

	public void setSchoolDistrictId(String schoolDistrictId) {
		this.schoolDistrictId = schoolDistrictId;
	}

	public String getGovernor() {
		return governor;
	}

	public void setGovernor(String governor) {
		this.governor = governor;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public Integer getEmphases() {
		return emphases;
	}

	public void setEmphases(Integer emphases) {
		this.emphases = emphases;
	}

	public Integer getIsBoorish() {
		return isBoorish;
	}

	public void setIsBoorish(Integer isBoorish) {
		this.isBoorish = isBoorish;
	}

	public Integer getDemonstration() {
		return demonstration;
	}

	public void setDemonstration(Integer demonstration) {
		this.demonstration = demonstration;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Integer getPublicAssist() {
		return publicAssist;
	}

	public void setPublicAssist(Integer publicAssist) {
		this.publicAssist = publicAssist;
	}

	public Double getBuiltupArea() {
		return builtupArea;
	}

	public void setBuiltupArea(Double builtupArea) {
		this.builtupArea = builtupArea;
	}

	public Double getGreenArea() {
		return greenArea;
	}

	public void setGreenArea(Double greenArea) {
		this.greenArea = greenArea;
	}

	public Double getSportsArea() {
		return sportsArea;
	}

	public void setSportsArea(Double sportsArea) {
		this.sportsArea = sportsArea;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSchoolPropStat() {
		return schoolPropStat;
	}

	public void setSchoolPropStat(String schoolPropStat) {
		this.schoolPropStat = schoolPropStat;
	}

	public String getSchoolTypeGroup() {
		return schoolTypeGroup;
	}

	public void setSchoolTypeGroup(String schoolTypeGroup) {
		this.schoolTypeGroup = schoolTypeGroup;
	}

	public String getLedgerSchoolType() {
		return ledgerSchoolType;
	}

	public void setLedgerSchoolType(String ledgerSchoolType) {
		this.ledgerSchoolType = ledgerSchoolType;
	}

	public String getRegionManageDept() {
		return regionManageDept;
	}

	public void setRegionManageDept(String regionManageDept) {
		this.regionManageDept = regionManageDept;
	}

	public String getRegionManageDeptCode() {
		return regionManageDeptCode;
	}

	public void setRegionManageDeptCode(String regionManageDeptCode) {
		this.regionManageDeptCode = regionManageDeptCode;
	}

	public String getAddressCode() {
		return addressCode;
	}

	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	public String getLegaRegistrationNumber() {
		return legaRegistrationNumber;
	}

	public void setLegaRegistrationNumber(String legaRegistrationNumber) {
		this.legaRegistrationNumber = legaRegistrationNumber;
	}

	public String getLandCertificateNo() {
		return landCertificateNo;
	}

	public void setLandCertificateNo(String landCertificateNo) {
		this.landCertificateNo = landCertificateNo;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getFillEmail() {
		return fillEmail;
	}

	public void setFillEmail(String fillEmail) {
		this.fillEmail = fillEmail;
	}

	public String getStatStuffName() {
		return statStuffName;
	}

	public void setStatStuffName(String statStuffName) {
		this.statStuffName = statStuffName;
	}

	public String getStatStuffContact() {
		return statStuffContact;
	}

	public void setStatStuffContact(String statStuffContact) {
		this.statStuffContact = statStuffContact;
	}

	public String getIsSchDistrictChief() {
		return isSchDistrictChief;
	}

	public void setIsSchDistrictChief(String isSchDistrictChief) {
		this.isSchDistrictChief = isSchDistrictChief;
	}

	public String getStatRegionCode() {
		return statRegionCode;
	}

	public void setStatRegionCode(String statRegionCode) {
		this.statRegionCode = statRegionCode;
	}

	public String getKgLevel() {
		return kgLevel;
	}

	public void setKgLevel(String kgLevel) {
		this.kgLevel = kgLevel;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getRegionPropertyCode() {
		return regionPropertyCode;
	}

	public void setRegionPropertyCode(String regionPropertyCode) {
		this.regionPropertyCode = regionPropertyCode;
	}

	public String getNatureType() {
		return natureType;
	}

	public void setNatureType(String natureType) {
		this.natureType = natureType;
	}

	public Integer getIsMinoritySchool() {
		return isMinoritySchool;
	}

	public void setIsMinoritySchool(Integer isMinoritySchool) {
		this.isMinoritySchool = isMinoritySchool;
	}

	public String getSchoolSetupName() {
		return schoolSetupName;
	}

	public void setSchoolSetupName(String schoolSetupName) {
		this.schoolSetupName = schoolSetupName;
	}

	public String getSpecialRegionCode() {
		return specialRegionCode;
	}

	public void setSpecialRegionCode(String specialRegionCode) {
		this.specialRegionCode = specialRegionCode;
	}

	public Long getSequenceIntId() {
		return sequenceIntId;
	}

	public void setSequenceIntId(Long sequenceIntId) {
		this.sequenceIntId = sequenceIntId;
	}

}
