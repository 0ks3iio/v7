package net.zdsoft.basedata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.entity.Pagination;
import net.zdsoft.framework.utils.SUtils;

@Entity
@Table(name = "base_student")
public class Student extends BaseEntity<String>{
	private static final long serialVersionUID = 8052791882633651159L;
	@ColumnInfo(displayName = "所在学校ID")
	@Column(length=32)
	private String schoolId;
	@ColumnInfo(displayName = "所在班级ID")
	@Column(length=32)
	private String classId;
	@ColumnInfo(displayName = "学生姓名", nullable = false)
	private String studentName;
	@ColumnInfo(displayName = "曾用名")
	private String oldName;
	@Column(length=100)
	@ColumnInfo(displayName = "学号")
	private String studentCode;
	@Column(length=100)
	@ColumnInfo(displayName = "学籍号")
	private String unitiveCode;
	@Column(length=100)
	@ColumnInfo(displayName = "身份证件号码")
	private String identityCard;
	@Column(length=10)
	@ColumnInfo(displayName = "性别", mcodeId = "DM-XB", vtype = ColumnInfo.VTYPE_RADIO)
	private Integer sex;
	@ColumnInfo(displayName = "出生日期")
	private Date birthday;
	@ColumnInfo(displayName = "手机号码")
	@Column(length=50)
	private String mobilePhone;
	private Integer isLeaveSchool;
	@Column(length=9)
	private String enrollYear;
	@ColumnInfo(displayName = "班内编号")
	@Column(length=10)
	private String classInnerCode;
	@Column(length=10)
	private String economyState;
	@Column(length=50)
	private String cardNumber;
	@ColumnInfo(displayName = "独生子女", mcodeId = "DM-BOOLEAN", vtype = ColumnInfo.VTYPE_RADIO)
	private Integer isSingleton;
	@ColumnInfo(displayName = "留守儿童", mcodeId = "DM-BOOLEAN", vtype = ColumnInfo.VTYPE_RADIO)
	private Integer stayin;
	@ColumnInfo(displayName = "贫困家庭", mcodeId = "DM-BOOLEAN", vtype = ColumnInfo.VTYPE_RADIO)
	private Integer boorish;
	private Integer isLocalSource;
	private Integer studentRecruitment;
	private Integer studyMode;
	@Column(length=10)
	private String nativePlace;
	@ColumnInfo(displayName = "个人主页")
	@Column(length=100)
	private String homepage;
	@ColumnInfo(displayName = "电子信箱")
	@Column(length=100)
	private String email;
	@ColumnInfo(displayName = "联系电话")
	@Column(length=50)
	private String linkPhone;
	@ColumnInfo(displayName = "联系地址")
	private String linkAddress;
	@Column(length=10)
	private String postalcode;
	@Column(length=10)
	private String regionCode;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer isDeleted;
	private Integer eventSource;
	private Integer isFreshman;
	@Column(length=50)
	private String password;
	@ColumnInfo(displayName = "当前状态")
	@Column(length=10)
	private String nowState;
	@Column(length=100)
	private String schoolDistrict;
	@Column(length=50)
	private String meetexamCode;
	@Column(length=100)
	private String sourcePlace;
	private Integer isSpecialid;
	@Column(length=32)
	private String dirId;
	@Column(length=100)
	private String filePath;
	@Column(length=10)
	private String health;
	@Column(length=10)
	private String nation;
	@Column(length=10)
	private String background;
	private Date polityJoinDate;
	@Column(length=100)
	private String homeAddress;
	private Date toSchoolDate;
	@Column(length=100)
	private String dorm;
	@Column(length=50)
	private String dormTel;
	@Column(length=32)
	private String specId;
	@Column(length=32)
	private String specpointId;
	@Column(length=10)
	private String enterScore;
	@Column(length=50)
	private String bankCardNo;
	@Column(length=100)
	private String oldSchoolName;
	private Date oldSchoolDate;
	@Column(length=10)
	private String identitycardType;
	@ColumnInfo(displayName = "验证码")
	@Column(length=10)
	private String pin;
	@Column(length=32)
	private String duplexClassGradeId;
	@Column(length=10)
	private String stateCode;
	private Integer recruitMode;
	private Integer learnMode;
	private Integer cooperateType;
	@Column(length=50)
	private String cooperateSchoolCode;
	@Column(length=50)
	private String registerPlace;
	private Integer registerType;
	private Integer isLowAllowce;
	private Integer isEnjoyStateGrants;
	private Double grantStandard;
	private Integer sourceType;
	@Column(length=50)
	private String teachingPointsName;
	@Column(length=50)
	private String studentCategory;
	private Integer compatriots;
	@Column(length=100)
	private String graduateSchool;
	@Column(length=500)
	private String remark;
	@Column(length=10)
	private String registerCode;
	@Column(length=50)
	private String spellName;
	@Column(length=100)
	private String homePlace;
	@Column(length=10)
	private String lastDegree;
	@Column(length=200)
	private String registerAddress;
	@Column(length=10)
	private String bloodType;
	@Column(length=10)
	private String examCode;
	@Transient
	private String credentialType;
	@Transient
	private String credentialCode;
	@Column(length=10)
	private String photoNo;
	private Double benefitMoney;
	@Column(length=10)
	private String studentMode;
	@Column(length=32)
	private String rewardSpecId;
	@Column(length=10)
	private String schLengthType;
	private Integer isFamilySide;
	@Column(length=50)
	private String studentQq;
	@Column(length=50)
	private String oneCardNumber;
	@Column(length=10)
	private String seatNumber;
	@Column(length=50)
	private String englishName;
	@Column(length=10)
	private String schoolinglen;
	@Column(length=10)
	private String toschooltype;
	@Column(length=10)
	private String country;
	@Column(length=10)
	private String marriage;
	@Column(length=10)
	private String nativeType;
	@Column(length=10)
	private String localPoliceStation;
	@Column(length=100)
	private String nowaddress;
	@Column(length=10)
	private String homePostalcode;
	//source与sourceType同样代表学生来源 目前5.0与6.0 使用的是这个字段 这边先开放 后续在调整具体选择
	private String source;
	
	@Column(length=10)
	private String cooperateForm;
	@Column(length=10)
	private String trainStation;
	@Column(length=50)
	private String oldStudentCode;
	@Column(length=50)
	private String familyMobile;
	@Column(length=10)
	private String religion;
	@Transient
	private Integer returnedchinese;
	@Column(length=10)
	private String familyOrigin;
	@Column(length=10)
	private String compulsoryedu;
	private Integer flowingPeople;
	private Date polityDate;
	@Column(length=50)
	private String polityIntroducer;
	private Date partyDate;
	@Column(length=50)
	private String partyIntroducer;
	@Column(length=50)
	private String zkCode;
	private Double zkResult;
	@Column(length=10)
	private String englishLevel;
	@Column(length=10)
	private String computerLevel;
	@Transient
	private String specialty;
	private Integer isPreedu;
	@Column(length=500)
	private String strong;
	private Integer registerInfo;
	@Column(length=50)
	private String oldSchcode;
	@Column(length=100)
	private String wlClassIntention;
	@Column(length=100)
	private String formerClassTeacher;
	@Column(length=100)
	private String formerClassLeader;
	private Double zkArtResult;
	private Double zkMathResult;
	private Double zkEnglishResult;
	private Double zkIntegratedResult;
	private Double zkPeResult;
	private Double zkExperimentResult;
	private Double zkComputerResult;
	@Column(length=100)
	private String zkExtro;
	@Column(length=1000)
	private String zkStrong;
	private Double rxArtResult;
	private Double rxMathResult;
	private Double rxEnglishResult;
	private Double rxIntegratedResult;
	private Double rxResult;
	@Column(length=100)
	private String accountAttribution;
	@Column(length=100)
	private String isLocalSchoolEnrollment;
	@Column(length=100)
	private String isDuplexClass;
	@Column(length=100)
	private String isRereading;
	@Column(length=100)
	private String regularClass;
	private Integer age;
	@Column(length=100)
	private String degreePlace;
	@Column(length=10)
	private String disabilityType;
	@Column(length=10)
	private String isBoarding;
	@Column(length=10)
	private String isMigration;
	@Column(length=10)
	private String isFallEnrollment;
	@Column(length=10)
	private String fingerprint;
	@Column(length=10)
	private String distance;
	@Column(length=10)
	private String trafficWay;
	private Integer isNeedBus;
	private Integer isGovernmentBear;
	private Integer isNeedAssistance;
	private Integer isOrphan;
	private Integer isEnjoyAssistance;
	private Integer isMartyrChild;
	@Column(length=10)
	private String identitycardValid;
	@Column(length=10)
	private String urbanRegisterType;
	@Column(length=10)
	private String planMode;
	@Column(length=10)
	private String socialRecruitmentType;
	@Transient
	private Integer nowChildNum;
	@Transient
	private Integer birthRank;
	@Column(length=10)
	private String rxqk;
	@Column(length=10)
	private String isGetCertification;
	@Column(length=10)
	private String isFiveHigherVocational;
	@Column(length=10)
	private String belongContinents;
	@Column(length=10)
	private String foreignerStudyTime;
	@Column(length=10)
	private String isVocationalTechClass;
	@Column(length=10)
	private String isRegularClass;
	@Column(length=10)
	private String fingerprint2;
	@Transient
	private Long sequenceIntId;
	@Column(length=10)
	private String studentType;
	// 调入调出验证码
	@Column(length=10)
	private String verifyCode;
//	@Transient
	@Column(length=1000)
	private String rewardRemark;
//	@Transient
	@Column(length=100)
	private String registerStreet;

	@Transient
	private String className;
	@Transient
	private String schoolName;

	/**
	 * 学生在校状态 <b>正常</b>
	 */
	public static final int STUDENT_NORMAL = 0;

	/**
	 * 学生在校状态 <b>离校</b>
	 */
	public static final int STUDENT_LEAVE = 1;

	public static final String NOWSTATE_40 = "40";
	// 0幼儿园未分班/1小学未分班/2初中未分班/3高中未分班/9已分班 /-1 临时表到正式表
	public static final int FRESHMAN_9 = 9;

	public static List<Student> dt(String data) {
		List<Student> ts = SUtils.dt(data, new TypeReference<List<Student>>() {
		});
		if (ts == null)
			ts = new ArrayList<Student>();
		return ts;

	}

	public static List<Student> dt(String data, Pagination page) {
		JSONObject json = JSONObject.parseObject(data);
		List<Student> ts = SUtils.dt(json.getString("data"), new TypeReference<List<Student>>() {
		});
		if (ts == null)
			ts = new ArrayList<Student>();
		if (json.containsKey("count"))
			page.setMaxRowCount(json.getInteger("count"));
		return ts;

	}

	public static Student dc(String data) {
		return SUtils.dc(data, Student.class);
	}

	@Override
	public String fetchCacheEntitName() {
		return "student";
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getUnitiveCode() {
		return unitiveCode;
	}

	public void setUnitiveCode(String unitiveCode) {
		this.unitiveCode = unitiveCode;
	}

	public String getIdentityCard() {
		return identityCard;
	}

	public void setIdentityCard(String identityCard) {
		this.identityCard = identityCard;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Integer getIsLeaveSchool() {
		return isLeaveSchool;
	}

	public void setIsLeaveSchool(Integer isLeaveSchool) {
		this.isLeaveSchool = isLeaveSchool;
	}

	public String getEnrollYear() {
		return enrollYear;
	}

	public void setEnrollYear(String enrollYear) {
		this.enrollYear = enrollYear;
	}

	public String getClassInnerCode() {
		return classInnerCode;
	}

	public void setClassInnerCode(String classInnerCode) {
		this.classInnerCode = classInnerCode;
	}

	public String getEconomyState() {
		return economyState;
	}

	public void setEconomyState(String economyState) {
		this.economyState = economyState;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Integer getIsSingleton() {
		return isSingleton;
	}

	public void setIsSingleton(Integer isSingleton) {
		this.isSingleton = isSingleton;
	}

	public Integer getStayin() {
		return stayin;
	}

	public void setStayin(Integer stayin) {
		this.stayin = stayin;
	}

	public Integer getBoorish() {
		return boorish;
	}

	public void setBoorish(Integer boorish) {
		this.boorish = boorish;
	}

	public Integer getIsLocalSource() {
		return isLocalSource;
	}

	public void setIsLocalSource(Integer isLocalSource) {
		this.isLocalSource = isLocalSource;
	}

	public Integer getStudentRecruitment() {
		return studentRecruitment;
	}

	public void setStudentRecruitment(Integer studentRecruitment) {
		this.studentRecruitment = studentRecruitment;
	}

	public Integer getStudyMode() {
		return studyMode;
	}

	public void setStudyMode(Integer studyMode) {
		this.studyMode = studyMode;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Integer getIsFreshman() {
		return isFreshman;
	}

	public void setIsFreshman(Integer isFreshman) {
		this.isFreshman = isFreshman;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNowState() {
		return nowState;
	}

	public void setNowState(String nowState) {
		this.nowState = nowState;
	}

	public String getSchoolDistrict() {
		return schoolDistrict;
	}

	public void setSchoolDistrict(String schoolDistrict) {
		this.schoolDistrict = schoolDistrict;
	}

	public String getMeetexamCode() {
		return meetexamCode;
	}

	public void setMeetexamCode(String meetexamCode) {
		this.meetexamCode = meetexamCode;
	}

	public String getSourcePlace() {
		return sourcePlace;
	}

	public void setSourcePlace(String sourcePlace) {
		this.sourcePlace = sourcePlace;
	}

	public Integer getIsSpecialid() {
		return isSpecialid;
	}

	public void setIsSpecialid(Integer isSpecialid) {
		this.isSpecialid = isSpecialid;
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

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public Date getPolityJoinDate() {
		return polityJoinDate;
	}

	public void setPolityJoinDate(Date polityJoinDate) {
		this.polityJoinDate = polityJoinDate;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public Date getToSchoolDate() {
		return toSchoolDate;
	}

	public void setToSchoolDate(Date toSchoolDate) {
		this.toSchoolDate = toSchoolDate;
	}

	public String getDorm() {
		return dorm;
	}

	public void setDorm(String dorm) {
		this.dorm = dorm;
	}

	public String getDormTel() {
		return dormTel;
	}

	public void setDormTel(String dormTel) {
		this.dormTel = dormTel;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public String getSpecpointId() {
		return specpointId;
	}

	public void setSpecpointId(String specpointId) {
		this.specpointId = specpointId;
	}

	public String getEnterScore() {
		return enterScore;
	}

	public void setEnterScore(String enterScore) {
		this.enterScore = enterScore;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getOldSchoolName() {
		return oldSchoolName;
	}

	public void setOldSchoolName(String oldSchoolName) {
		this.oldSchoolName = oldSchoolName;
	}

	public Date getOldSchoolDate() {
		return oldSchoolDate;
	}

	public void setOldSchoolDate(Date oldSchoolDate) {
		this.oldSchoolDate = oldSchoolDate;
	}

	public String getIdentitycardType() {
		return identitycardType;
	}

	public void setIdentitycardType(String identitycardType) {
		this.identitycardType = identitycardType;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getDuplexClassGradeId() {
		return duplexClassGradeId;
	}

	public void setDuplexClassGradeId(String duplexClassGradeId) {
		this.duplexClassGradeId = duplexClassGradeId;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getRecruitMode() {
		return recruitMode;
	}

	public void setRecruitMode(Integer recruitMode) {
		this.recruitMode = recruitMode;
	}

	public Integer getLearnMode() {
		return learnMode;
	}

	public void setLearnMode(Integer learnMode) {
		this.learnMode = learnMode;
	}

	public Integer getCooperateType() {
		return cooperateType;
	}

	public void setCooperateType(Integer cooperateType) {
		this.cooperateType = cooperateType;
	}

	public String getCooperateSchoolCode() {
		return cooperateSchoolCode;
	}

	public void setCooperateSchoolCode(String cooperateSchoolCode) {
		this.cooperateSchoolCode = cooperateSchoolCode;
	}

	public String getRegisterPlace() {
		return registerPlace;
	}

	public void setRegisterPlace(String registerPlace) {
		this.registerPlace = registerPlace;
	}

	public Integer getRegisterType() {
		return registerType;
	}

	public void setRegisterType(Integer registerType) {
		this.registerType = registerType;
	}

	public Integer getIsLowAllowce() {
		return isLowAllowce;
	}

	public void setIsLowAllowce(Integer isLowAllowce) {
		this.isLowAllowce = isLowAllowce;
	}

	public Integer getIsEnjoyStateGrants() {
		return isEnjoyStateGrants;
	}

	public void setIsEnjoyStateGrants(Integer isEnjoyStateGrants) {
		this.isEnjoyStateGrants = isEnjoyStateGrants;
	}

	public Double getGrantStandard() {
		return grantStandard;
	}

	public void setGrantStandard(Double grantStandard) {
		this.grantStandard = grantStandard;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public String getTeachingPointsName() {
		return teachingPointsName;
	}

	public void setTeachingPointsName(String teachingPointsName) {
		this.teachingPointsName = teachingPointsName;
	}

	public String getStudentCategory() {
		return studentCategory;
	}

	public void setStudentCategory(String studentCategory) {
		this.studentCategory = studentCategory;
	}

	public Integer getCompatriots() {
		return compatriots;
	}

	public void setCompatriots(Integer compatriots) {
		this.compatriots = compatriots;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRegisterCode() {
		return registerCode;
	}

	public void setRegisterCode(String registerCode) {
		this.registerCode = registerCode;
	}

	public String getSpellName() {
		return spellName;
	}

	public void setSpellName(String spellName) {
		this.spellName = spellName;
	}

	public String getHomePlace() {
		return homePlace;
	}

	public void setHomePlace(String homePlace) {
		this.homePlace = homePlace;
	}

	public String getLastDegree() {
		return lastDegree;
	}

	public void setLastDegree(String lastDegree) {
		this.lastDegree = lastDegree;
	}

	public String getRegisterAddress() {
		return registerAddress;
	}

	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getExamCode() {
		return examCode;
	}

	public void setExamCode(String examCode) {
		this.examCode = examCode;
	}

	public String getCredentialType() {
		return credentialType;
	}

	public void setCredentialType(String credentialType) {
		this.credentialType = credentialType;
	}

	public String getCredentialCode() {
		return credentialCode;
	}

	public void setCredentialCode(String credentialCode) {
		this.credentialCode = credentialCode;
	}

	public String getPhotoNo() {
		return photoNo;
	}

	public void setPhotoNo(String photoNo) {
		this.photoNo = photoNo;
	}

	public Double getBenefitMoney() {
		return benefitMoney;
	}

	public void setBenefitMoney(Double benefitMoney) {
		this.benefitMoney = benefitMoney;
	}

	public String getStudentMode() {
		return studentMode;
	}

	public void setStudentMode(String studentMode) {
		this.studentMode = studentMode;
	}

	public String getRewardSpecId() {
		return rewardSpecId;
	}

	public void setRewardSpecId(String rewardSpecId) {
		this.rewardSpecId = rewardSpecId;
	}

	public String getSchLengthType() {
		return schLengthType;
	}

	public void setSchLengthType(String schLengthType) {
		this.schLengthType = schLengthType;
	}

	public Integer getIsFamilySide() {
		return isFamilySide;
	}

	public void setIsFamilySide(Integer isFamilySide) {
		this.isFamilySide = isFamilySide;
	}

	public String getStudentQq() {
		return studentQq;
	}

	public void setStudentQq(String studentQq) {
		this.studentQq = studentQq;
	}

	public String getOneCardNumber() {
		return oneCardNumber;
	}

	public void setOneCardNumber(String oneCardNumber) {
		this.oneCardNumber = oneCardNumber;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getSchoolinglen() {
		return schoolinglen;
	}

	public void setSchoolinglen(String schoolinglen) {
		this.schoolinglen = schoolinglen;
	}

	public String getToschooltype() {
		return toschooltype;
	}

	public void setToschooltype(String toschooltype) {
		this.toschooltype = toschooltype;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	public String getNativeType() {
		return nativeType;
	}

	public void setNativeType(String nativeType) {
		this.nativeType = nativeType;
	}

	public String getLocalPoliceStation() {
		return localPoliceStation;
	}

	public void setLocalPoliceStation(String localPoliceStation) {
		this.localPoliceStation = localPoliceStation;
	}

	public String getNowaddress() {
		return nowaddress;
	}

	public void setNowaddress(String nowaddress) {
		this.nowaddress = nowaddress;
	}

	public String getHomePostalcode() {
		return homePostalcode;
	}

	public void setHomePostalcode(String homePostalcode) {
		this.homePostalcode = homePostalcode;
	}

	/*
	 * public String getSource() { return source; }
	 * 
	 * public void setSource(String source) { this.source = source; }
	 */

	public String getCooperateForm() {
		return cooperateForm;
	}

	public void setCooperateForm(String cooperateForm) {
		this.cooperateForm = cooperateForm;
	}

	public String getTrainStation() {
		return trainStation;
	}

	public void setTrainStation(String trainStation) {
		this.trainStation = trainStation;
	}

	public String getOldStudentCode() {
		return oldStudentCode;
	}

	public void setOldStudentCode(String oldStudentCode) {
		this.oldStudentCode = oldStudentCode;
	}

	public String getFamilyMobile() {
		return familyMobile;
	}

	public void setFamilyMobile(String familyMobile) {
		this.familyMobile = familyMobile;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public Integer getReturnedchinese() {
		return returnedchinese;
	}

	public void setReturnedchinese(Integer returnedchinese) {
		this.returnedchinese = returnedchinese;
	}

	public String getFamilyOrigin() {
		return familyOrigin;
	}

	public void setFamilyOrigin(String familyOrigin) {
		this.familyOrigin = familyOrigin;
	}

	public String getCompulsoryedu() {
		return compulsoryedu;
	}

	public void setCompulsoryedu(String compulsoryedu) {
		this.compulsoryedu = compulsoryedu;
	}

	public Integer getFlowingPeople() {
		return flowingPeople;
	}

	public void setFlowingPeople(Integer flowingPeople) {
		this.flowingPeople = flowingPeople;
	}

	public Date getPolityDate() {
		return polityDate;
	}

	public void setPolityDate(Date polityDate) {
		this.polityDate = polityDate;
	}

	public String getPolityIntroducer() {
		return polityIntroducer;
	}

	public void setPolityIntroducer(String polityIntroducer) {
		this.polityIntroducer = polityIntroducer;
	}

	public Date getPartyDate() {
		return partyDate;
	}

	public void setPartyDate(Date partyDate) {
		this.partyDate = partyDate;
	}

	public String getPartyIntroducer() {
		return partyIntroducer;
	}

	public void setPartyIntroducer(String partyIntroducer) {
		this.partyIntroducer = partyIntroducer;
	}

	public String getZkCode() {
		return zkCode;
	}

	public void setZkCode(String zkCode) {
		this.zkCode = zkCode;
	}

	public Double getZkResult() {
		return zkResult;
	}

	public void setZkResult(Double zkResult) {
		this.zkResult = zkResult;
	}

	public String getEnglishLevel() {
		return englishLevel;
	}

	public void setEnglishLevel(String englishLevel) {
		this.englishLevel = englishLevel;
	}

	public String getComputerLevel() {
		return computerLevel;
	}

	public void setComputerLevel(String computerLevel) {
		this.computerLevel = computerLevel;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public Integer getIsPreedu() {
		return isPreedu;
	}

	public void setIsPreedu(Integer isPreedu) {
		this.isPreedu = isPreedu;
	}

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}

	public Integer getRegisterInfo() {
		return registerInfo;
	}

	public void setRegisterInfo(Integer registerInfo) {
		this.registerInfo = registerInfo;
	}

	public String getOldSchcode() {
		return oldSchcode;
	}

	public void setOldSchcode(String oldSchcode) {
		this.oldSchcode = oldSchcode;
	}

	public String getWlClassIntention() {
		return wlClassIntention;
	}

	public void setWlClassIntention(String wlClassIntention) {
		this.wlClassIntention = wlClassIntention;
	}

	public String getFormerClassTeacher() {
		return formerClassTeacher;
	}

	public void setFormerClassTeacher(String formerClassTeacher) {
		this.formerClassTeacher = formerClassTeacher;
	}

	public String getFormerClassLeader() {
		return formerClassLeader;
	}

	public void setFormerClassLeader(String formerClassLeader) {
		this.formerClassLeader = formerClassLeader;
	}

	public Double getZkArtResult() {
		return zkArtResult;
	}

	public void setZkArtResult(Double zkArtResult) {
		this.zkArtResult = zkArtResult;
	}

	public Double getZkMathResult() {
		return zkMathResult;
	}

	public void setZkMathResult(Double zkMathResult) {
		this.zkMathResult = zkMathResult;
	}

	public Double getZkEnglishResult() {
		return zkEnglishResult;
	}

	public void setZkEnglishResult(Double zkEnglishResult) {
		this.zkEnglishResult = zkEnglishResult;
	}

	public Double getZkIntegratedResult() {
		return zkIntegratedResult;
	}

	public void setZkIntegratedResult(Double zkIntegratedResult) {
		this.zkIntegratedResult = zkIntegratedResult;
	}

	public Double getZkPeResult() {
		return zkPeResult;
	}

	public void setZkPeResult(Double zkPeResult) {
		this.zkPeResult = zkPeResult;
	}

	public Double getZkExperimentResult() {
		return zkExperimentResult;
	}

	public void setZkExperimentResult(Double zkExperimentResult) {
		this.zkExperimentResult = zkExperimentResult;
	}

	public Double getZkComputerResult() {
		return zkComputerResult;
	}

	public void setZkComputerResult(Double zkComputerResult) {
		this.zkComputerResult = zkComputerResult;
	}

	public String getZkExtro() {
		return zkExtro;
	}

	public void setZkExtro(String zkExtro) {
		this.zkExtro = zkExtro;
	}

	public String getZkStrong() {
		return zkStrong;
	}

	public void setZkStrong(String zkStrong) {
		this.zkStrong = zkStrong;
	}

	public Double getRxArtResult() {
		return rxArtResult;
	}

	public void setRxArtResult(Double rxArtResult) {
		this.rxArtResult = rxArtResult;
	}

	public Double getRxMathResult() {
		return rxMathResult;
	}

	public void setRxMathResult(Double rxMathResult) {
		this.rxMathResult = rxMathResult;
	}

	public Double getRxEnglishResult() {
		return rxEnglishResult;
	}

	public void setRxEnglishResult(Double rxEnglishResult) {
		this.rxEnglishResult = rxEnglishResult;
	}

	public Double getRxIntegratedResult() {
		return rxIntegratedResult;
	}

	public void setRxIntegratedResult(Double rxIntegratedResult) {
		this.rxIntegratedResult = rxIntegratedResult;
	}

	public Double getRxResult() {
		return rxResult;
	}

	public void setRxResult(Double rxResult) {
		this.rxResult = rxResult;
	}

	public String getAccountAttribution() {
		return accountAttribution;
	}

	public void setAccountAttribution(String accountAttribution) {
		this.accountAttribution = accountAttribution;
	}

	public String getIsLocalSchoolEnrollment() {
		return isLocalSchoolEnrollment;
	}

	public void setIsLocalSchoolEnrollment(String isLocalSchoolEnrollment) {
		this.isLocalSchoolEnrollment = isLocalSchoolEnrollment;
	}

	public String getIsDuplexClass() {
		return isDuplexClass;
	}

	public void setIsDuplexClass(String isDuplexClass) {
		this.isDuplexClass = isDuplexClass;
	}

	public String getIsRereading() {
		return isRereading;
	}

	public void setIsRereading(String isRereading) {
		this.isRereading = isRereading;
	}

	public String getRegularClass() {
		return regularClass;
	}

	public void setRegularClass(String regularClass) {
		this.regularClass = regularClass;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getDegreePlace() {
		return degreePlace;
	}

	public void setDegreePlace(String degreePlace) {
		this.degreePlace = degreePlace;
	}

	public String getDisabilityType() {
		return disabilityType;
	}

	public void setDisabilityType(String disabilityType) {
		this.disabilityType = disabilityType;
	}

	public String getIsBoarding() {
		return isBoarding;
	}

	public void setIsBoarding(String isBoarding) {
		this.isBoarding = isBoarding;
	}

	public String getIsMigration() {
		return isMigration;
	}

	public void setIsMigration(String isMigration) {
		this.isMigration = isMigration;
	}

	public String getIsFallEnrollment() {
		return isFallEnrollment;
	}

	public void setIsFallEnrollment(String isFallEnrollment) {
		this.isFallEnrollment = isFallEnrollment;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTrafficWay() {
		return trafficWay;
	}

	public void setTrafficWay(String trafficWay) {
		this.trafficWay = trafficWay;
	}

	public Integer getIsNeedBus() {
		return isNeedBus;
	}

	public void setIsNeedBus(Integer isNeedBus) {
		this.isNeedBus = isNeedBus;
	}

	public Integer getIsGovernmentBear() {
		return isGovernmentBear;
	}

	public void setIsGovernmentBear(Integer isGovernmentBear) {
		this.isGovernmentBear = isGovernmentBear;
	}

	public Integer getIsNeedAssistance() {
		return isNeedAssistance;
	}

	public void setIsNeedAssistance(Integer isNeedAssistance) {
		this.isNeedAssistance = isNeedAssistance;
	}

	public Integer getIsOrphan() {
		return isOrphan;
	}

	public void setIsOrphan(Integer isOrphan) {
		this.isOrphan = isOrphan;
	}

	public Integer getIsEnjoyAssistance() {
		return isEnjoyAssistance;
	}

	public void setIsEnjoyAssistance(Integer isEnjoyAssistance) {
		this.isEnjoyAssistance = isEnjoyAssistance;
	}

	public Integer getIsMartyrChild() {
		return isMartyrChild;
	}

	public void setIsMartyrChild(Integer isMartyrChild) {
		this.isMartyrChild = isMartyrChild;
	}

	public String getIdentitycardValid() {
		return identitycardValid;
	}

	public void setIdentitycardValid(String identitycardValid) {
		this.identitycardValid = identitycardValid;
	}

	public String getUrbanRegisterType() {
		return urbanRegisterType;
	}

	public void setUrbanRegisterType(String urbanRegisterType) {
		this.urbanRegisterType = urbanRegisterType;
	}

	public String getPlanMode() {
		return planMode;
	}

	public void setPlanMode(String planMode) {
		this.planMode = planMode;
	}

	public String getSocialRecruitmentType() {
		return socialRecruitmentType;
	}

	public void setSocialRecruitmentType(String socialRecruitmentType) {
		this.socialRecruitmentType = socialRecruitmentType;
	}

	public Integer getNowChildNum() {
		return nowChildNum;
	}

	public void setNowChildNum(Integer nowChildNum) {
		this.nowChildNum = nowChildNum;
	}

	public Integer getBirthRank() {
		return birthRank;
	}

	public void setBirthRank(Integer birthRank) {
		this.birthRank = birthRank;
	}

	public String getRxqk() {
		return rxqk;
	}

	public void setRxqk(String rxqk) {
		this.rxqk = rxqk;
	}

	public String getIsGetCertification() {
		return isGetCertification;
	}

	public void setIsGetCertification(String isGetCertification) {
		this.isGetCertification = isGetCertification;
	}

	public String getIsFiveHigherVocational() {
		return isFiveHigherVocational;
	}

	public void setIsFiveHigherVocational(String isFiveHigherVocational) {
		this.isFiveHigherVocational = isFiveHigherVocational;
	}

	public String getBelongContinents() {
		return belongContinents;
	}

	public void setBelongContinents(String belongContinents) {
		this.belongContinents = belongContinents;
	}

	public String getForeignerStudyTime() {
		return foreignerStudyTime;
	}

	public void setForeignerStudyTime(String foreignerStudyTime) {
		this.foreignerStudyTime = foreignerStudyTime;
	}

	public String getIsVocationalTechClass() {
		return isVocationalTechClass;
	}

	public void setIsVocationalTechClass(String isVocationalTechClass) {
		this.isVocationalTechClass = isVocationalTechClass;
	}

	public String getIsRegularClass() {
		return isRegularClass;
	}

	public void setIsRegularClass(String isRegularClass) {
		this.isRegularClass = isRegularClass;
	}

	public String getFingerprint2() {
		return fingerprint2;
	}

	public void setFingerprint2(String fingerprint2) {
		this.fingerprint2 = fingerprint2;
	}

	public Long getSequenceIntId() {
		return sequenceIntId;
	}

	public void setSequenceIntId(Long sequenceIntId) {
		this.sequenceIntId = sequenceIntId;
	}

	public String getStudentType() {
		return studentType;
	}

	public void setStudentType(String studentType) {
		this.studentType = studentType;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRewardRemark() {
		return rewardRemark;
	}

	public void setRewardRemark(String rewardRemark) {
		this.rewardRemark = rewardRemark;
	}

	public String getRegisterStreet() {
		return registerStreet;
	}

	public void setRegisterStreet(String registerStreet) {
		this.registerStreet = registerStreet;
	}

}
