package net.zdsoft.basedata.entity;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;

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
@Table(name = "base_teacher")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Teacher extends BaseEntity<String> {
    private static final long serialVersionUID = 1L;
    // 基本信息
    @ColumnInfo(displayName = "所在部门", nullable = false, vtype = ColumnInfo.VTYPE_SELECT, vsql = "select id, dept_name from base_dept where is_deleted = 0 and unit_id = {unitId}")
    @Column(length=32)
    private String deptId;
    @ColumnInfo(hide = true, displayName = "所在单位")
    @Column(length=32)
    private String unitId;
    @ColumnInfo(displayName = "编号", nullable = false, maxLength = 6)
    @Column(length=32)
    private String teacherCode;
    @ColumnInfo(displayName = "姓名", nullable = false)
    @Column(length=32)
    private String teacherName;
    @ColumnInfo(displayName = "曾用名")
    @Column(length=32)
    private String oldName;
    @ColumnInfo(mcodeId = "DM-XB", displayName = "性别", nullable = false, vtype = ColumnInfo.VTYPE_RADIO)
    private Integer sex;
    @ColumnInfo(format = "yyyy-MM-dd", displayName = "出生日期", vtype = ColumnInfo.VTYPE_DATE)
    private Date birthday;
    @ColumnInfo(mcodeId = "DM-REGION", displayName = "籍贯", vtype = ColumnInfo.VTYPE_SELECT)
    @Column(length=10)
    private String nativePlace;
    @ColumnInfo(mcodeId = "DM-MZ", displayName = "民族", vtype = ColumnInfo.VTYPE_SELECT)
    @Column(length=10)
    private String nation;
    @ColumnInfo(mcodeId = "DM-ZZMM", displayName = "政治面貌", vtype = ColumnInfo.VTYPE_SELECT)
    @Column(length=10)
    private String polity;
    @ColumnInfo(format = "yyyy-MM-dd", displayName = "入党时间", vtype = ColumnInfo.VTYPE_DATE)
    private Date polityJoin;
    @Column(length=10)
    private String academicQualification;
    private Date graduateTime;
    @ColumnInfo(displayName = "毕业学校")
    @Column(length=100)
    private String graduateSchool;
    @ColumnInfo(displayName = "毕业专业")
    @Column(length=100)
    private String major;
    @ColumnInfo(format = "yyyy-MM-dd", displayName = "参加工作时间", vtype = ColumnInfo.VTYPE_DATE)
    private Date workDate;
    @Column(length=10)
    @ColumnInfo(mcodeId = "DM-JSZZBJ", displayName = "在职标志", vtype = ColumnInfo.VTYPE_SELECT)
    private String incumbencySign;
    @Column(length=10)
    @ColumnInfo(displayName = "职务")
    private String title;
    @ColumnInfo(displayName = "证件类型", nullable = false, vtype = ColumnInfo.VTYPE_SELECT, mcodeId = "DM-SFZJLX")
    @Column(length=10)
    private String identityType;
    @ColumnInfo(displayName = "身份证件号码",nullable = false)
    @Column(length=10)
    private String identityCard;
    @ColumnInfo(displayName = "手机号码", length = 11, vtype = ColumnInfo.VTYPE_INT)
    @Column(length=50)
    private String mobilePhone;
    @Column(length=50)
    @ColumnInfo(displayName = "办公室电话")
    private String officeTel;
    @Column(length=10)
    @ColumnInfo(mcodeId = "DM-HKXZ", displayName = "户口类别", vtype = ColumnInfo.VTYPE_SELECT)
    private Integer registerType;
    @Column(length=10)
    @ColumnInfo(mcodeId = "DM-REGION", displayName = "户口所在地", vtype = ColumnInfo.VTYPE_SELECT)
    private String registerPlace;
    @Column(length=100)
    @ColumnInfo(displayName = "电子信箱", vtype = ColumnInfo.VTYPE_EMAIL)
    private String email;
    @Column(length=500)
    @ColumnInfo(displayName = "备注")
    private String remark;
    @Column(length=50)
    @ColumnInfo(displayName = "卡号")
    private String cardNumber;
    @Column(length=100)
    @ColumnInfo(displayName = "个人主页", vtype = ColumnInfo.VTYPE_URL)
    private String homepage;
    @Column(length=50)
    @ColumnInfo(displayName = "联系电话")
    private String linkPhone;
    @Column(length=100)
    @ColumnInfo(displayName = "联系地址")
    private String linkAddress;
    @ColumnInfo(displayName = "邮政编码")
    @Column(length=50)
    private String postalcode;
    @Column(length=50)
    @ColumnInfo(displayName = "行政区划码")
    private String regionCode;
    @ColumnInfo(displayName = "排序号", vtype = ColumnInfo.VTYPE_INT)
    private Integer displayOrder;
    @ColumnInfo(displayName = "卡类型")
    private Integer chargeNumberType;
    @ColumnInfo(displayName = "卡号")
    @Column(length=50)
    private String chargeNumber;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private Date creationTime;
    @Temporal(TemporalType.TIMESTAMP)
    @ColumnInfo(format = "YYYY-MM-DD HH:mm:ss", displayName = "修改时间", vtype = "date", disabled = true)
    private Date modifyTime;
    private Integer isDeleted;
    private Integer eventSource;
    @Column(length=32)
    private String dirId;
    @Column(length=100)
    private String filePath;
    private Integer section;
    @ColumnInfo(displayName = "国家/地区", mcodeId = "DM-COUNTRY", vtype = ColumnInfo.VTYPE_SELECT)
    @Column(length=10)
    private String country;
    @ColumnInfo(displayName = "是否华侨", mcodeId = "DM-BOOLEAN", vtype = ColumnInfo.VTYPE_SELECT)
    @Column(length=10)
    private String returnedChinese;
    @Column(length=10)
    private String weaveType;
    private Integer multiTitle;
    @Column(length=10)
    private String teachStatus;
    @Column(length=32)
    private String instituteId;
    private Date workTeachDate;
    @Column(length=10)
    private String oldAcademicQualification;
    private Date specTechnicalDutyDate;
    @Column(length=100)
    private String homeAddress;
    @Column(length=100)
    private String generalCard;
    @Column(length=10)
    private String specTechnicalDuty;
    @Column(length=10)
    private String highestDegree;
    @Column(length=10)
    private String highestDegreeInstitutions;
    @Column(length=10)
    private String highestDiploma;
    @Column(length=10)
    private String highestDiplomaInstitutions;
    @Column(length=10)
    private String laborContractSituation;
    @Column(length=10)
    private String fiveInsurancePayments;
    @Column(length=10)
    private String certificationCode;
    @Column(length=10)
    private String oneYearTraining;
    @Column(length=10)
    private String mainCourse;
    @Column(length=10)
    private String specialEduTraining;
    @Column(length=10)
    private String preschoolSpecialty;
    @Column(length=10)
    private String courseSituation;
    @Column(length=10)
    private String courseSubjectCategory;
    @Column(length=10)
    private String isDoubleCertification;
    @Column(length=10)
    private String otherCertification;
    @Column(length=10)
    private String otherCertificationLevel;
    @Column(length=10)
    private String enterpriseWorkTime;
    @Column(length=10)
    private String tutorType;
    @Column(length=10)
    private String subjectAreas;
    @Column(length=10)
    private String expertType;
    @Column(length=10)
    private String researchAwards;
    @Column(length=10)
    private String overseasDegree;
    @Column(length=10)
    private String overseasTraining;
    @Column(length=10)
    private String academicStructure;
    @Column(length=10)
    private String infoPin;
    @Column(length=10)
    private String duty;
    @Column(length=32)
    private String subschoolId;
    @Column(length=10)
    private String backboneTeacherLevel;
    @Column(length=10)
    private String jobGroup;
    @Column(length=10)
    private String partyPositions;
    @Column(length=10)
    private String onJobDiploma;
    private Date presentDutyLevelDate;
    @Column(length=10)
    private String employeeType;
    @Column(length=10)
    private String maritalStatus;
    @Column(length=50)
    private String identity;
    @Column(length=10)
    private String archivesIdentity;
    private Date positiveTime;
    private Date thisCityWorkDate;
    private Date thisUnitWorkDate;
    private Date retiredTime;
    @Column(length=10)
    private String teachGrade;
    @Column(length=10)
    private String fullTimeEducation;
    @Column(length=10)
    private String isHaveCountCertification;
    @Column(length=50)
    private String countCertificationCode;
    @Column(length=10)
    private String isPreeEduGraduate;
    @Column(length=10)
    private String joinCommunication;
    private String joinTeaching;
    private Integer continuationSeniority;
    private String partTimePosition;
    private String inPreparation;
    private String freeNormal;
    private String specialTeacher;
    private String certificationType;
    private String weaveUnitId;
    private String putonghuaGrade;
    private String learningPeriod;
    private String normalGraduated;
    private Date educationWorkDate;
    private String job;
    private String isSpecialEduGraduate;
    private String isAppointmentTeacher;
    private String isGiveLessons;
    private String isDoubleTeachers;
    private String subjectBranch;
    private String subjectBranchGroup;
    private String notGivelessonReason;
    private Integer age;
    private String dutyNew;
    private String isLeaveSchool;
    private String nowState;
    private String fileNumber;
    //鼎永同步教师用到
//    @Transient
//    private String fixTeacherId;
    @Transient
    private Long sequenceIntId;
    @Transient
    private String linkName;
    @Transient
    private String filepath;
    @Transient
    private String deptName;
    @Transient
    private String unitName;
    
    @Transient
    private String serverCode;
    
    /**
     * @deprecated 不要用这个
     */
    @Transient
    private String mobilephone;
    private String otherDuty;
    private Date dutyDate;
    private String dutyLevel;
    private String otherDutyLevel;
    private String dutyEx;
    private String dutyExUnit;
    private Date dutyExDate;
    private String jobLevel;
    private String otherJobLevel;
    private Date interruptionStartDate;
    private Date interruptionEndDate;
    private Date reallyWorkDate;
    private String doubleShoulder;
    private Integer salaryLevel;
    private Date weaveUnitDate;
    private String weaveUnitType;
    private Date workUnitDate;
    private String workUnitType;
    private Date contractFinishDate;
    private String workNumber;
    private String signContract;
    private String threeYearsTraining;
    private Integer hidePhone;
    
    public static List<Teacher> dt(String data) {
		List<Teacher> ts = SUtils.dt(data, new TypeReference<List<Teacher>>() {
		});
		if (ts == null)
			ts = new ArrayList<Teacher>();
		return ts;

	}

	public static List<Teacher> dt(String data, Pagination page) {
		JSONObject json = JSONObject.parseObject(data);
		List<Teacher> ts = SUtils.dt(json.getString("data"), new TypeReference<List<Teacher>>() {
		});
		if (ts == null)
			ts = new ArrayList<Teacher>();
		if (json.containsKey("count"))
			page.setMaxRowCount(json.getInteger("count"));
		return ts;

	}

	public static Teacher dc(String data) {
		return SUtils.dc(data, Teacher.class);
	}

//    public String getFixTeacherId() {
//		return fixTeacherId;
//	}
//
//	public void setFixTeacherId(String fixTeacherId) {
//		this.fixTeacherId = fixTeacherId;
//	}

	public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    @Override
    public String fetchCacheEntitName() {
        return "teacher";
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getPolity() {
        return polity;
    }

    public void setPolity(String polity) {
        this.polity = polity;
    }

    public Date getPolityJoin() {
        return polityJoin;
    }

    public void setPolityJoin(Date polityJoin) {
        this.polityJoin = polityJoin;
    }

    public String getAcademicQualification() {
        return academicQualification;
    }

    public void setAcademicQualification(String academicQualification) {
        this.academicQualification = academicQualification;
    }

    public Date getGraduateTime() {
        return graduateTime;
    }

    public void setGraduateTime(Date graduateTime) {
        this.graduateTime = graduateTime;
    }

    public String getGraduateSchool() {
        return graduateSchool;
    }

    public void setGraduateSchool(String graduateSchool) {
        this.graduateSchool = graduateSchool;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public String getIncumbencySign() {
        return incumbencySign;
    }

    public void setIncumbencySign(String incumbencySign) {
        this.incumbencySign = incumbencySign;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard;
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

    public Integer getRegisterType() {
        return registerType;
    }

    public void setRegisterType(Integer registerType) {
        this.registerType = registerType;
    }

    public String getRegisterPlace() {
        return registerPlace;
    }

    public void setRegisterPlace(String registerPlace) {
        this.registerPlace = registerPlace;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

    public Integer getEventSource() {
        return eventSource;
    }

    public void setEventSource(Integer eventSource) {
        this.eventSource = eventSource;
    }

    public String getDirId() {
        return dirId;
    }

    public void setDirId(String dirId) {
        this.dirId = dirId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getReturnedChinese() {
        return returnedChinese;
    }

    public void setReturnedChinese(String returnedChinese) {
        this.returnedChinese = returnedChinese;
    }

    public String getWeaveType() {
        return weaveType;
    }

    public void setWeaveType(String weaveType) {
        this.weaveType = weaveType;
    }

    public Integer getMultiTitle() {
        return multiTitle;
    }

    public void setMultiTitle(Integer multiTitle) {
        this.multiTitle = multiTitle;
    }

    public String getTeachStatus() {
        return teachStatus;
    }

    public void setTeachStatus(String teachStatus) {
        this.teachStatus = teachStatus;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public Date getWorkTeachDate() {
        return workTeachDate;
    }

    public void setWorkTeachDate(Date workTeachDate) {
        this.workTeachDate = workTeachDate;
    }

    public String getOldAcademicQualification() {
        return oldAcademicQualification;
    }

    public void setOldAcademicQualification(String oldAcademicQualification) {
        this.oldAcademicQualification = oldAcademicQualification;
    }

    public Date getSpecTechnicalDutyDate() {
        return specTechnicalDutyDate;
    }

    public void setSpecTechnicalDutyDate(Date specTechnicalDutyDate) {
        this.specTechnicalDutyDate = specTechnicalDutyDate;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getGeneralCard() {
        return generalCard;
    }

    public void setGeneralCard(String generalCard) {
        this.generalCard = generalCard;
    }

    public String getSpecTechnicalDuty() {
        return specTechnicalDuty;
    }

    public void setSpecTechnicalDuty(String specTechnicalDuty) {
        this.specTechnicalDuty = specTechnicalDuty;
    }

    public String getHighestDegree() {
        return highestDegree;
    }

    public void setHighestDegree(String highestDegree) {
        this.highestDegree = highestDegree;
    }

    public String getHighestDegreeInstitutions() {
        return highestDegreeInstitutions;
    }

    public void setHighestDegreeInstitutions(String highestDegreeInstitutions) {
        this.highestDegreeInstitutions = highestDegreeInstitutions;
    }

    public String getHighestDiploma() {
        return highestDiploma;
    }

    public void setHighestDiploma(String highestDiploma) {
        this.highestDiploma = highestDiploma;
    }

    public String getHighestDiplomaInstitutions() {
        return highestDiplomaInstitutions;
    }

    public void setHighestDiplomaInstitutions(String highestDiplomaInstitutions) {
        this.highestDiplomaInstitutions = highestDiplomaInstitutions;
    }

    public String getLaborContractSituation() {
        return laborContractSituation;
    }

    public void setLaborContractSituation(String laborContractSituation) {
        this.laborContractSituation = laborContractSituation;
    }

    public String getFiveInsurancePayments() {
        return fiveInsurancePayments;
    }

    public void setFiveInsurancePayments(String fiveInsurancePayments) {
        this.fiveInsurancePayments = fiveInsurancePayments;
    }

    public String getCertificationCode() {
        return certificationCode;
    }

    public void setCertificationCode(String certificationCode) {
        this.certificationCode = certificationCode;
    }

    public String getOneYearTraining() {
        return oneYearTraining;
    }

    public void setOneYearTraining(String oneYearTraining) {
        this.oneYearTraining = oneYearTraining;
    }

    public String getMainCourse() {
        return mainCourse;
    }

    public void setMainCourse(String mainCourse) {
        this.mainCourse = mainCourse;
    }

    public String getSpecialEduTraining() {
        return specialEduTraining;
    }

    public void setSpecialEduTraining(String specialEduTraining) {
        this.specialEduTraining = specialEduTraining;
    }

    public String getPreschoolSpecialty() {
        return preschoolSpecialty;
    }

    public void setPreschoolSpecialty(String preschoolSpecialty) {
        this.preschoolSpecialty = preschoolSpecialty;
    }

    public String getCourseSituation() {
        return courseSituation;
    }

    public void setCourseSituation(String courseSituation) {
        this.courseSituation = courseSituation;
    }

    public String getCourseSubjectCategory() {
        return courseSubjectCategory;
    }

    public void setCourseSubjectCategory(String courseSubjectCategory) {
        this.courseSubjectCategory = courseSubjectCategory;
    }

    public String getIsDoubleCertification() {
        return isDoubleCertification;
    }

    public void setIsDoubleCertification(String isDoubleCertification) {
        this.isDoubleCertification = isDoubleCertification;
    }

    public String getOtherCertification() {
        return otherCertification;
    }

    public void setOtherCertification(String otherCertification) {
        this.otherCertification = otherCertification;
    }

    public String getOtherCertificationLevel() {
        return otherCertificationLevel;
    }

    public void setOtherCertificationLevel(String otherCertificationLevel) {
        this.otherCertificationLevel = otherCertificationLevel;
    }

    public String getEnterpriseWorkTime() {
        return enterpriseWorkTime;
    }

    public void setEnterpriseWorkTime(String enterpriseWorkTime) {
        this.enterpriseWorkTime = enterpriseWorkTime;
    }

    public String getTutorType() {
        return tutorType;
    }

    public void setTutorType(String tutorType) {
        this.tutorType = tutorType;
    }

    public String getSubjectAreas() {
        return subjectAreas;
    }

    public void setSubjectAreas(String subjectAreas) {
        this.subjectAreas = subjectAreas;
    }

    public String getExpertType() {
        return expertType;
    }

    public void setExpertType(String expertType) {
        this.expertType = expertType;
    }

    public String getResearchAwards() {
        return researchAwards;
    }

    public void setResearchAwards(String researchAwards) {
        this.researchAwards = researchAwards;
    }

    public String getOverseasDegree() {
        return overseasDegree;
    }

    public void setOverseasDegree(String overseasDegree) {
        this.overseasDegree = overseasDegree;
    }

    public String getOverseasTraining() {
        return overseasTraining;
    }

    public void setOverseasTraining(String overseasTraining) {
        this.overseasTraining = overseasTraining;
    }

    public String getAcademicStructure() {
        return academicStructure;
    }

    public void setAcademicStructure(String academicStructure) {
        this.academicStructure = academicStructure;
    }

    public String getInfoPin() {
        return infoPin;
    }

    public void setInfoPin(String infoPin) {
        this.infoPin = infoPin;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getSubschoolId() {
        return subschoolId;
    }

    public void setSubschoolId(String subschoolId) {
        this.subschoolId = subschoolId;
    }

    public String getBackboneTeacherLevel() {
        return backboneTeacherLevel;
    }

    public void setBackboneTeacherLevel(String backboneTeacherLevel) {
        this.backboneTeacherLevel = backboneTeacherLevel;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getPartyPositions() {
        return partyPositions;
    }

    public void setPartyPositions(String partyPositions) {
        this.partyPositions = partyPositions;
    }

    public String getOnJobDiploma() {
        return onJobDiploma;
    }

    public void setOnJobDiploma(String onJobDiploma) {
        this.onJobDiploma = onJobDiploma;
    }

    public Date getPresentDutyLevelDate() {
        return presentDutyLevelDate;
    }

    public void setPresentDutyLevelDate(Date presentDutyLevelDate) {
        this.presentDutyLevelDate = presentDutyLevelDate;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getArchivesIdentity() {
        return archivesIdentity;
    }

    public void setArchivesIdentity(String archivesIdentity) {
        this.archivesIdentity = archivesIdentity;
    }

    public Date getPositiveTime() {
        return positiveTime;
    }

    public void setPositiveTime(Date positiveTime) {
        this.positiveTime = positiveTime;
    }

    public Date getThisCityWorkDate() {
        return thisCityWorkDate;
    }

    public void setThisCityWorkDate(Date thisCityWorkDate) {
        this.thisCityWorkDate = thisCityWorkDate;
    }

    public Date getThisUnitWorkDate() {
        return thisUnitWorkDate;
    }

    public void setThisUnitWorkDate(Date thisUnitWorkDate) {
        this.thisUnitWorkDate = thisUnitWorkDate;
    }

    public Date getRetiredTime() {
        return retiredTime;
    }

    public void setRetiredTime(Date retiredTime) {
        this.retiredTime = retiredTime;
    }

    public String getTeachGrade() {
        return teachGrade;
    }

    public void setTeachGrade(String teachGrade) {
        this.teachGrade = teachGrade;
    }

    public String getFullTimeEducation() {
        return fullTimeEducation;
    }

    public void setFullTimeEducation(String fullTimeEducation) {
        this.fullTimeEducation = fullTimeEducation;
    }

    public String getIsHaveCountCertification() {
        return isHaveCountCertification;
    }

    public void setIsHaveCountCertification(String isHaveCountCertification) {
        this.isHaveCountCertification = isHaveCountCertification;
    }

    public String getCountCertificationCode() {
        return countCertificationCode;
    }

    public void setCountCertificationCode(String countCertificationCode) {
        this.countCertificationCode = countCertificationCode;
    }

    public String getIsPreeEduGraduate() {
        return isPreeEduGraduate;
    }

    public void setIsPreeEduGraduate(String isPreeEduGraduate) {
        this.isPreeEduGraduate = isPreeEduGraduate;
    }

    public String getJoinCommunication() {
        return joinCommunication;
    }

    public void setJoinCommunication(String joinCommunication) {
        this.joinCommunication = joinCommunication;
    }

    public String getJoinTeaching() {
        return joinTeaching;
    }

    public void setJoinTeaching(String joinTeaching) {
        this.joinTeaching = joinTeaching;
    }

    public Integer getContinuationSeniority() {
        return continuationSeniority;
    }

    public void setContinuationSeniority(Integer continuationSeniority) {
        this.continuationSeniority = continuationSeniority;
    }

    public String getPartTimePosition() {
        return partTimePosition;
    }

    public void setPartTimePosition(String partTimePosition) {
        this.partTimePosition = partTimePosition;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getInPreparation() {
        return inPreparation;
    }

    public void setInPreparation(String inPreparation) {
        this.inPreparation = inPreparation;
    }

    public String getFreeNormal() {
        return freeNormal;
    }

    public void setFreeNormal(String freeNormal) {
        this.freeNormal = freeNormal;
    }

    public String getSpecialTeacher() {
        return specialTeacher;
    }

    public void setSpecialTeacher(String specialTeacher) {
        this.specialTeacher = specialTeacher;
    }

    public String getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(String certificationType) {
        this.certificationType = certificationType;
    }

    public String getWeaveUnitId() {
        return weaveUnitId;
    }

    public void setWeaveUnitId(String weaveUnitId) {
        this.weaveUnitId = weaveUnitId;
    }

    public String getPutonghuaGrade() {
        return putonghuaGrade;
    }

    public void setPutonghuaGrade(String putonghuaGrade) {
        this.putonghuaGrade = putonghuaGrade;
    }

    public String getLearningPeriod() {
        return learningPeriod;
    }

    public void setLearningPeriod(String learningPeriod) {
        this.learningPeriod = learningPeriod;
    }

    public String getNormalGraduated() {
        return normalGraduated;
    }

    public void setNormalGraduated(String normalGraduated) {
        this.normalGraduated = normalGraduated;
    }

    public Date getEducationWorkDate() {
        return educationWorkDate;
    }

    public void setEducationWorkDate(Date educationWorkDate) {
        this.educationWorkDate = educationWorkDate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getIsSpecialEduGraduate() {
        return isSpecialEduGraduate;
    }

    public void setIsSpecialEduGraduate(String isSpecialEduGraduate) {
        this.isSpecialEduGraduate = isSpecialEduGraduate;
    }

    public String getIsAppointmentTeacher() {
        return isAppointmentTeacher;
    }

    public void setIsAppointmentTeacher(String isAppointmentTeacher) {
        this.isAppointmentTeacher = isAppointmentTeacher;
    }

    public String getIsGiveLessons() {
        return isGiveLessons;
    }

    public void setIsGiveLessons(String isGiveLessons) {
        this.isGiveLessons = isGiveLessons;
    }

    public String getIsDoubleTeachers() {
        return isDoubleTeachers;
    }

    public void setIsDoubleTeachers(String isDoubleTeachers) {
        this.isDoubleTeachers = isDoubleTeachers;
    }

    public String getSubjectBranch() {
        return subjectBranch;
    }

    public void setSubjectBranch(String subjectBranch) {
        this.subjectBranch = subjectBranch;
    }

    public String getSubjectBranchGroup() {
        return subjectBranchGroup;
    }

    public void setSubjectBranchGroup(String subjectBranchGroup) {
        this.subjectBranchGroup = subjectBranchGroup;
    }

    public String getNotGivelessonReason() {
        return notGivelessonReason;
    }

    public void setNotGivelessonReason(String notGivelessonReason) {
        this.notGivelessonReason = notGivelessonReason;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDutyNew() {
        return dutyNew;
    }

    public void setDutyNew(String dutyNew) {
        this.dutyNew = dutyNew;
    }

    public String getIsLeaveSchool() {
        return isLeaveSchool;
    }

    public void setIsLeaveSchool(String isLeaveSchool) {
        this.isLeaveSchool = isLeaveSchool;
    }

    public String getNowState() {
        return nowState;
    }

    public void setNowState(String nowState) {
        this.nowState = nowState;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public Long getSequenceIntId() {
        return sequenceIntId;
    }

    public void setSequenceIntId(Long sequenceIntId) {
        this.sequenceIntId = sequenceIntId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * @deprecated 不要用这个
     * @return
     */
    public String getMobilephone() {
        return mobilephone;
    }

    /**
     * @deprecated 不要用这个
     * 
     */
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getOtherDuty() {
        return otherDuty;
    }

    public void setOtherDuty(String otherDuty) {
        this.otherDuty = otherDuty;
    }

    public Date getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(Date dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getDutyLevel() {
        return dutyLevel;
    }

    public void setDutyLevel(String dutyLevel) {
        this.dutyLevel = dutyLevel;
    }

    public String getOtherDutyLevel() {
        return otherDutyLevel;
    }

    public void setOtherDutyLevel(String otherDutyLevel) {
        this.otherDutyLevel = otherDutyLevel;
    }

    public String getDutyEx() {
        return dutyEx;
    }

    public void setDutyEx(String dutyEx) {
        this.dutyEx = dutyEx;
    }

    public String getDutyExUnit() {
        return dutyExUnit;
    }

    public void setDutyExUnit(String dutyExUnit) {
        this.dutyExUnit = dutyExUnit;
    }

    public Date getDutyExDate() {
        return dutyExDate;
    }

    public void setDutyExDate(Date dutyExDate) {
        this.dutyExDate = dutyExDate;
    }

    public String getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(String jobLevel) {
        this.jobLevel = jobLevel;
    }

    public String getOtherJobLevel() {
        return otherJobLevel;
    }

    public void setOtherJobLevel(String otherJobLevel) {
        this.otherJobLevel = otherJobLevel;
    }

    public Date getInterruptionStartDate() {
        return interruptionStartDate;
    }

    public void setInterruptionStartDate(Date interruptionStartDate) {
        this.interruptionStartDate = interruptionStartDate;
    }

    public Date getInterruptionEndDate() {
        return interruptionEndDate;
    }

    public void setInterruptionEndDate(Date interruptionEndDate) {
        this.interruptionEndDate = interruptionEndDate;
    }

    public Date getReallyWorkDate() {
        return reallyWorkDate;
    }

    public void setReallyWorkDate(Date reallyWorkDate) {
        this.reallyWorkDate = reallyWorkDate;
    }

    public String getDoubleShoulder() {
        return doubleShoulder;
    }

    public void setDoubleShoulder(String doubleShoulder) {
        this.doubleShoulder = doubleShoulder;
    }

    public Integer getSalaryLevel() {
        return salaryLevel;
    }

    public void setSalaryLevel(Integer salaryLevel) {
        this.salaryLevel = salaryLevel;
    }

    public Date getWeaveUnitDate() {
        return weaveUnitDate;
    }

    public void setWeaveUnitDate(Date weaveUnitDate) {
        this.weaveUnitDate = weaveUnitDate;
    }

    public String getWeaveUnitType() {
        return weaveUnitType;
    }

    public void setWeaveUnitType(String weaveUnitType) {
        this.weaveUnitType = weaveUnitType;
    }

    public Date getWorkUnitDate() {
        return workUnitDate;
    }

    public void setWorkUnitDate(Date workUnitDate) {
        this.workUnitDate = workUnitDate;
    }

    public String getWorkUnitType() {
        return workUnitType;
    }

    public void setWorkUnitType(String workUnitType) {
        this.workUnitType = workUnitType;
    }

    public Date getContractFinishDate() {
        return contractFinishDate;
    }

    public void setContractFinishDate(Date contractFinishDate) {
        this.contractFinishDate = contractFinishDate;
    }

    public String getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(String workNumber) {
        this.workNumber = workNumber;
    }

    public String getSignContract() {
        return signContract;
    }

    public void setSignContract(String signContract) {
        this.signContract = signContract;
    }

    public String getThreeYearsTraining() {
        return threeYearsTraining;
    }

    public void setThreeYearsTraining(String threeYearsTraining) {
        this.threeYearsTraining = threeYearsTraining;
    }

    public Integer getHidePhone() {
        return hidePhone;
    }

    public void setHidePhone(Integer hidePhone) {
        this.hidePhone = hidePhone;
    }

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getServerCode() {
		return serverCode;
	}

	public void setServerCode(String serverCode) {
		this.serverCode = serverCode;
	}
}
