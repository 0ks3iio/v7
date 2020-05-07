package net.zdsoft.syncdata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

public class XFUnit  {
    
    private static final long serialVersionUID = 1L;
    private String id;
    private String unitName;
	private Integer unitClass;
	private Integer unitType;
	private Integer regionLevel;
	private String regionCode;
	@Column(updatable=false)
	private String serialNumber;
	private String secondLevelDomain;
	private String parentId;
	private String displayOrder;
	private String postalcode;
	private String email;
	private String fax;
	private String linkMan;
	private String linkPhone;
	private String mobilePhone;
	private String homepage;
	private String address;
	private Integer isTeacherSms;
	private Integer isGuestbookSms;
	private Integer balance;
	private Integer feeType;
	private Integer limitTeacher;
	private Integer isSmsFree;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	private Integer eventSource;
	private String unionCode;
	private String pollCode;
	private Integer unitState;
	private Integer useType;
	private Integer authorized;
	private String unitUseType;
	private Integer unitPartitionNum;
	private String sha1;
	private Integer orgVersion;
	private String orgVisual;
	private String unitEducationType;
	private Double longitude;
	private Double latitude;
	private String unitHeader;
	private String organizationCode;
	private String unitProperty;
	private Integer runSchoolType;
	private String schoolType;
	@Transient
	private String unitShortName;
	@Transient
	private Long sequenceIntId;
	
    private Integer schoolIntId;
    private String schoolIc;
    private String functionTypeCode;
    private String areaTypeCode;

    public Integer getSchoolIntId() {
        return schoolIntId;
    }

    public void setSchoolIntId(Integer schoolIntId) {
        this.schoolIntId = schoolIntId;
    }

    public String getSchoolIc() {
        return schoolIc;
    }

    public void setSchoolIc(String schoolIc) {
        this.schoolIc = schoolIc;
    }

    public String getFunctionTypeCode() {
        return functionTypeCode;
    }

    public void setFunctionTypeCode(String functionTypeCode) {
        this.functionTypeCode = functionTypeCode;
    }

    public String getAreaTypeCode() {
        return areaTypeCode;
    }

    public void setAreaTypeCode(String areaTypeCode) {
        this.areaTypeCode = areaTypeCode;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getUnitClass() {
		return unitClass;
	}

	public void setUnitClass(Integer unitClass) {
		this.unitClass = unitClass;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}

	public Integer getRegionLevel() {
		return regionLevel;
	}

	public void setRegionLevel(Integer regionLevel) {
		this.regionLevel = regionLevel;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSecondLevelDomain() {
		return secondLevelDomain;
	}

	public void setSecondLevelDomain(String secondLevelDomain) {
		this.secondLevelDomain = secondLevelDomain;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getIsTeacherSms() {
		return isTeacherSms;
	}

	public void setIsTeacherSms(Integer isTeacherSms) {
		this.isTeacherSms = isTeacherSms;
	}

	public Integer getIsGuestbookSms() {
		return isGuestbookSms;
	}

	public void setIsGuestbookSms(Integer isGuestbookSms) {
		this.isGuestbookSms = isGuestbookSms;
	}

	public Integer getBalance() {
		return balance;
	}

	public void setBalance(Integer balance) {
		this.balance = balance;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public Integer getLimitTeacher() {
		return limitTeacher;
	}

	public void setLimitTeacher(Integer limitTeacher) {
		this.limitTeacher = limitTeacher;
	}

	public Integer getIsSmsFree() {
		return isSmsFree;
	}

	public void setIsSmsFree(Integer isSmsFree) {
		this.isSmsFree = isSmsFree;
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

	public String getUnionCode() {
		return unionCode;
	}

	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}

	public String getPollCode() {
		return pollCode;
	}

	public void setPollCode(String pollCode) {
		this.pollCode = pollCode;
	}

	public Integer getUnitState() {
		return unitState;
	}

	public void setUnitState(Integer unitState) {
		this.unitState = unitState;
	}

	public Integer getUseType() {
		return useType;
	}

	public void setUseType(Integer useType) {
		this.useType = useType;
	}

	public Integer getAuthorized() {
		return authorized;
	}

	public void setAuthorized(Integer authorized) {
		this.authorized = authorized;
	}

	public String getUnitUseType() {
		return unitUseType;
	}

	public void setUnitUseType(String unitUseType) {
		this.unitUseType = unitUseType;
	}

	public Integer getUnitPartitionNum() {
		return unitPartitionNum;
	}

	public void setUnitPartitionNum(Integer unitPartitionNum) {
		this.unitPartitionNum = unitPartitionNum;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public Integer getOrgVersion() {
		return orgVersion;
	}

	public void setOrgVersion(Integer orgVersion) {
		this.orgVersion = orgVersion;
	}

	public String getOrgVisual() {
		return orgVisual;
	}

	public void setOrgVisual(String orgVisual) {
		this.orgVisual = orgVisual;
	}

	public String getUnitEducationType() {
		return unitEducationType;
	}

	public void setUnitEducationType(String unitEducationType) {
		this.unitEducationType = unitEducationType;
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

	public String getUnitHeader() {
		return unitHeader;
	}

	public void setUnitHeader(String unitHeader) {
		this.unitHeader = unitHeader;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getUnitProperty() {
		return unitProperty;
	}

	public void setUnitProperty(String unitProperty) {
		this.unitProperty = unitProperty;
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

	public String getUnitShortName() {
		return unitShortName;
	}

	public void setUnitShortName(String unitShortName) {
		this.unitShortName = unitShortName;
	}

	public Long getSequenceIntId() {
		return sequenceIntId;
	}

	public void setSequenceIntId(Long sequenceIntId) {
		this.sequenceIntId = sequenceIntId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
