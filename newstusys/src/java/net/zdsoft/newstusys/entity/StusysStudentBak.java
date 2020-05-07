package net.zdsoft.newstusys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

/**
 * 学生存档表，按学期存档
 * 
 * @author weixh
 * 2018年9月18日	
 */
@SuppressWarnings("serial")
@Entity
@Table(name="base_student_history")
public class StusysStudentBak extends BaseEntity<String> {
	private String schoolId;
	private String classId;
	private String studentName;
	private String oldName;
	private String studentCode;
	private String unitiveCode;
	private String identityCard;
	private Integer sex;
	private Date birthday;
	private String mobilePhone;
	private Integer isLeaveSchool;
	private String enrollYear;
	private String classInnerCode;
	private String economyState;
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
	private String nativePlace;
	private String homepage;
	private String email;
	private String linkPhone;
	private String linkAddress;
	private String postalcode;
	private String regionCode;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private Integer eventSource;
	private Integer isFreshman;
	private String password;
	private String nowState;
	private String schoolDistrict;
	private String meetexamCode;
	private String sourcePlace;
	private Integer isSpecialid;
	private String dirId;
	private String filePath;
	private String health;
	private String nation;
	private String background;
	private Date polityJoinDate;
	private String homeAddress;
	private Date toSchoolDate;
	private String dorm;
	private String dormTel;
	private String specId;
	private String specpointId;
	private String enterScore;
	private String bankCardNo;
	private String oldSchoolName;
	private Date oldSchoolDate;
	private String identitycardType;
	@ColumnInfo(displayName = "验证码")
	private String pin;
	private String duplexClassGradeId;
	private String stateCode;
	private Integer recruitMode;
	private Integer learnMode;
	private Integer cooperateType;
	private String cooperateSchoolCode;
	private String registerPlace;
	private Integer registerType;
	private Integer isLowAllowce;
	private Integer isEnjoyStateGrants;
	private Double grantStandard;
	private Integer sourceType;
	private String teachingPointsName;
	private String studentCategory;
	private Integer compatriots;
	private String graduateSchool;
	private String remark;
	private String registerCode;
	private String spellName;
	private String homePlace;
	private String lastDegree;
	private String registerAddress;
	private String bloodType;
	private String examCode;
	private String photoNo;
	private Double benefitMoney;
	private String studentMode;
	private String rewardSpecId;
	private String schLengthType;
	private Integer isFamilySide;
	private String studentQq;
	private String oneCardNumber;
	private String seatNumber;
	private String englishName;
	private String schoolinglen;
	private String toschooltype;
	private String country;
	private String marriage;
	private String nativeType;
	private String localPoliceStation;
	private String nowaddress;
	private String homePostalcode;
	//source与sourceType同样代表学生来源 目前5.0与6.0 使用的是这个字段 这边先开放 后续在调整具体选择
	private String source;
	private String cooperateForm;
	private String trainStation;
	private String oldStudentCode;
	private String familyMobile;
	private String religion;
	private String familyOrigin;
	private String compulsoryedu;
	private Integer flowingPeople;
	private Date polityDate;
	private String polityIntroducer;
	private Date partyDate;
	private String partyIntroducer;
	private String zkCode;
	private Double zkResult;
	private String englishLevel;
	private String computerLevel;
	private Integer isPreedu;
	private String strong;
	private Integer registerInfo;
	private String oldSchcode;
	private String wlClassIntention;
	private String formerClassTeacher;
	private String formerClassLeader;
	private Double zkArtResult;
	private Double zkMathResult;
	private Double zkEnglishResult;
	private Double zkIntegratedResult;
	private Double zkPeResult;
	private Double zkExperimentResult;
	private Double zkComputerResult;
	private String zkExtro;
	private String zkStrong;
	private Double rxArtResult;
	private Double rxMathResult;
	private Double rxEnglishResult;
	private Double rxIntegratedResult;
	private Double rxResult;
	private String accountAttribution;
	private String isLocalSchoolEnrollment;
	private String isDuplexClass;
	private String isRereading;
	private String regularClass;
	private Integer age;
	private String degreePlace;
	private String disabilityType;
	private String isBoarding;
	private String isMigration;
	private String isFallEnrollment;
	private String fingerprint;
	private String distance;
	private String trafficWay;
	private Integer isNeedBus;
	private Integer isGovernmentBear;
	private Integer isNeedAssistance;
	private Integer isOrphan;
	private Integer isEnjoyAssistance;
	private Integer isMartyrChild;
	private String identitycardValid;
	private String urbanRegisterType;
	private String planMode;
	private String socialRecruitmentType;
	private String rxqk;
	private String isGetCertification;
	private String isFiveHigherVocational;
	private String belongContinents;
	private String foreignerStudyTime;
	private String isVocationalTechClass;
	private String isRegularClass;
	private String fingerprint2;
	private String studentType;
	// 调入调出验证码
	private String verifyCode;
	private String rewardRemark;
	private String registerStreet;
	
	private String acadyear;
	private Integer semester;
	private String studentId;
	private String teacherId;
	private String className;
	
	@Transient
	private Integer nowChildNum;
	@Transient
	private Integer birthRank;
	@Transient
	private String schoolName;
	
	public static final String BAK_PREFIX = "student_bak_";
	
	public static final String BAK_STATUS = "_status";
	
	public static final String BAK_STATUS_ING = "ing";
	
	public static final String BAK_MSG = "_msg";
	
	public String fetchCacheEntitName() {
		return "StusysStudentBak";
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

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

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public Integer getSemester() {
		return semester;
	}

	public void setSemester(Integer semester) {
		this.semester = semester;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

}
