package net.zdsoft.basedata.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "base_unit")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Unit extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    private String unitName;
    private Integer unitClass;
    private Integer unitType;
    private Integer regionLevel;
    private String regionCode;
    @Column(updatable = false)
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
    private String rootUnitId;
    @Transient
    private String ahUnitId;
    
    public String getAhUnitId() {
		return ahUnitId;
	}

	public void setAhUnitId(String ahUnitId) {
		this.ahUnitId = ahUnitId;
	}

	/**
     * 备注
     */
    private String remark;

    public String getRootUnitId() {
        return rootUnitId;
    }

    public void setRootUnitId(String rootUnitId) {
        this.rootUnitId = rootUnitId;
    }

    /**
     * 全国顶级教育局（教育部）的regionCode固定长度为六个0
     */
    public static final String TOP_UNIT_REGION_CODE = "000000";

    /**
     * 平台顶级单位guid
     */
    public static final String TOP_UNIT_GUID = "00000000000000000000000000000000";

    public static final String TOP_UNIT_PARENT_GUID_NOT_TRUE = "11111111111111111111111111111111";

    /**
     * 本地单位，非报送
     */
    public static final int UNIT_USETYPE_LOCAL = 1;

    /**
     * 报送单位
     */
    public static final int UNIT_USETYPE_REPORT = 2;

    /**
     * 单位状态-未审核
     */
    public static final int UNIT_MARK_NOTAUDIT = 0;

    /**
     * 单位状态-正常
     */
    public static final int UNIT_MARK_NORAML = 1;

    /**
     * 单位状态-锁定
     */
    public static final int UNIT_MARK_LOCK = 2;

    // 单位行政等级
    /**
     * 国家教育局
     */
    public static final int UNIT_REGION_COUNTRY = 1;

    /**
     * 省教育局
     */
    public static final int UNIT_REGION_PROVINCE = 2;

    /**
     * 市教育局
     */
    public static final int UNIT_REGION_CITY = 3;

    /**
     * 区教育局
     */
    public static final int UNIT_REGION_COUNTY = 4;

    /**
     * 乡镇教育局
     */
    public static final int UNIT_REGION_LEVEL = 5;

    /**
     * 学校
     */
    public static final int UNIT_REGION_SCHOOL = 6;

    // ----------------------------
    // 单位类型定义
    // ----------------------------
    /**
     * 单位类型－顶级教育局
     */
    public static final int UNIT_EDU_TOP = 1; // 顶级教育局

    /**
     * 单位类型－下属教育局
     */
    public static final int UNIT_EDU_SUB = 2; // 下属教育局

    /**
     * 单位类型－托管中小学
     */
    public static final int UNIT_SCHOOL_ASP = 3; // 托管中小学

    /**
     * 单位类型－托管大中专学校
     */
    public static final int UNIT_SCHOOL_COLLEGE = 4; // 托管大中专学校

    /**
     * 单位类型－托管幼儿园
     */
    public static final int UNIT_SCHOOL_KINDERGARTEN = 5; // 托管幼儿园

    /**
     * 单位类型－EISS中小学
     */
    public static final int UNIT_SCHOOL_EISS = 6; // EISS中小学

    /**
     * 单位类型－EISV大中专学校
     */
    public static final int UNIT_SCHOOL_EISV = 7; // EISV大中专学校

    /**
     * 单位类型-非教育局单位
     */
    public static final int UNIT_NOTEDU_NOTSCH = 8;// 非教育局单位

    /**
     * 单位类型-高职校
     */
    public static final int UNIT_HIGH_SCHOOL = 9;// 高职校

    /**
     * 单位分类－教育局
     */
    public static final int UNIT_CLASS_EDU = 1; // 教育局

    /**
     * 单位分类－学校
     */
    public static final int UNIT_CLASS_SCHOOL = 2; // 学校

    /**
     * 单位分类－学区---&gt;改为机构 当作教育局
     */
    public static final int UNIT_CLASS_SCHDISTRICT = 3; // 学区---&gt;改为机构 当作教育局

    public Integer createRegionLevel() {
        if (StringUtils.length(regionCode) == 6) {
            if (StringUtils.equals(StringUtils.substring(regionCode, 2), "0000")) {
                return 2 + BooleanUtils.toInteger(unitClass == UNIT_CLASS_SCHOOL);
            } else if (StringUtils.equals(StringUtils.substring(regionCode, 4), "00")) {
                return 3 + BooleanUtils.toInteger(unitClass == UNIT_CLASS_SCHOOL);
            } else {
                return 4 + BooleanUtils.toInteger(unitClass == UNIT_CLASS_SCHOOL);
            }
        } else
            return 0;
    }

    public static List<Unit> dt(String data) {
        List<Unit> ts = SUtils.dt(data, new TypeReference<List<Unit>>() {
        });
        if (ts == null)
            ts = new ArrayList<Unit>();
        return ts;

    }

    public static Unit dc(String data) {
        return SUtils.dc(data, Unit.class);
    }

    public static List<Unit> dt(String data, Pagination page) {
        JSONObject json = JSONObject.parseObject(data);
        List<Unit> ts = SUtils.dt(json.getString("data"), new TypeReference<List<Unit>>() {
        });
        if (ts == null)
            ts = new ArrayList<Unit>();
        if (json.containsKey("count"))
            page.setMaxRowCount(json.getInteger("count"));
        return ts;

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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
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

    @Override
    public String fetchCacheEntitName() {
        return "unit";
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

    public Integer getEventSource() {
        return eventSource;
    }

    public void setEventSource(Integer eventSource) {
        this.eventSource = eventSource;
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


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
