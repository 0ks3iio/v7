/* 
 * @(#)SimpleStudent.java    Created on May 16, 2011
 * Copyright (c) 2011 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.entity;

import net.zdsoft.framework.entity.BaseEntity;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: May 16, 2011 11:31:05 AM $
 */
public class SimpleStudent extends BaseEntity<String> {
	private static final long serialVersionUID = -3309240014363442437L;
	
	// =============常用字段，值从数据库直接取出==============
	/** 学校id */
	private String schid;
	/** 班级id */
	private String classid;
	/** 姓名 */
	private String stuname;
	/** 学号 */
	private String stucode;
	/** 学籍号 */
	private String unitivecode;
	/** 身份证号码--证件号 */
	private String identitycard;
	/** 身份证件类型 */
	private String identitycardType;
	/** 性别 */
	private Integer sex;
	/** 学生班内编号 */
	private String classorderid;
	/** 国籍 */
	private String nation;
	/** 银行卡号 */
	private String bankCardNo;
	/** 政治面貌 */
	private String background;
	/** 联系电话 */
	private String linkPhone;
	/** 家庭住址 */
	private String homeAddress;
	/** 籍贯,团委需要用到 */
	private String nativePlace;
	/** 入党时间 */
	private Date polityJoinDate;
	/** 手机 */
	private String mobilephone;
	/** 电子邮件 */
	private String email;
	/** 地区码 */
	private String regionCode;
	/** 出生日期 */
	private Date birthday;
	/** 离校在校标志 */
	private Integer leavesign;
	/** 操作密码，学生平台用到 */
	private String password;
	// ============================BaseStudent属性上提 by zhangkc 2015-05-07===========================
	/** 曾用名 */
	private String oldName;
	/** 手机 */
	private String mobilePhone;
	/** 新生入学学年 */
	private String enrollYear;
	/** 家庭经济情况 */
	private String economyState;
	/** 点到卡号 */
	private String cardNumber;
	/** 是否独生子女 */
	private int isSingleton;
	/** 是否留守儿童 */
	private int stayin;
	/** 是否农民工子女 */
	private int boorish;
	/** 是否本地生源 */
	private int isLocalSource;
	/** 就读方式 */
	private int studyMode;
	/** 个人主页 */
	private String homepage;
	/** 联系地址 */
	private String linkAddress;
	/** 邮政编码 */
	private String postalcode;
	/** 是否新生 */
	private int isFreshman;
	/** 当前状态 */
	private String nowState;
	/** 学区id */
	private String schoolDistrict;
	/** 会考证号 */
	private String meetexamCode;
	/** 来源地区 */
	private String sourcePlace;
	/** 是否特殊身份证号 */
	private int isSpecialid;
	/** 健康状况 */
	private String health;
	/** 入学时间 */
	private Date toSchoolDate;
	/** 学生宿舍 */
	private String dorm;
	/** 宿舍电话 */
	private String dormTel;
	/** 录取成绩 */
	private String enterScore;
	/** 来源学校名称 */
	private String oldSchoolName;
	/** 来源学校入学日期 */
	private Date oldSchoolDate;

	// ===============================base_student台账属性上提 by zhangkc 2015-05-07================================
	/** 个人标识码 */
	private String pin;
	/** 是否复式班学生 */
	private String isDuplexClass;
	/** 复式班的年级Id */
	private String duplexClassGradeId;
	/** 户口归属地 */ 
	private String accountAttribution;
	/** 年龄 */ 
	private int age;
	/** 学位所在地 */ 
	private String degreePlace;
	/** 残疾类型 */ 
	private String disabilityType;
	/** 是否寄宿生 */ 
	private String isBoarding;
	/** 是否本学年秋季入学新生 */ 
	private String isFallEnrollment;
	/** 是否随迁子女 */ 
	private String isMigration;
	/** 是否受过学前教育 */ 
	private String isPreedu;
	/** 是否重读生 */ 
	private String isRereading;
	/** 随班就读 */ 
	private String regularClass;
	/** 社会招生类型 */
	private String socialRecruitmentType;
	/** 入学情况*/
	private String rxqk;
	/** 是否获得职业资格证书*/
	private String isGetCertification;
	/** 是否五年制高职中职段学生*/
	private String isFiveHigherVocational;
	/** 外国籍学生所属大洲*/
	private String belongContinents;
	/** 外国籍学生学习时长*/
	private String foreignerStudyTime;
	/** 是否职业技术班学生*/
	private String isVocationalTechClass;
	/** 是否随班就读*/
	private String isRegularClass;
	// ================================EisuStudent属性上提 by zhangkc 2015-05-25===================================
	/** 专业id */
	private String specId;
	/** 专业方向id */
	private String specpointId;

	//===============================base_student_ex表字段聚合 by zhangkc 2015-05-25===================================
	/** 招生模式 DM-ZSLX */
	private int recruitMode;
	/** 学习形式 DM-XXMS */
	private int learnMode;
	/** 联招合作类型 DM-LZHZLX */
	private int cooperateType;
	/** 联招合作学校机构代码 cooperate_school_code */
	private String cooperateSchCode;
	/** 户籍所在地 */
	private String registerPlace;
	/** 户口性质 DM-HKXZ */
	private int registerType;
	/** 户籍性质 DM-HJXZ*/
	private String nativeType;
	/** 是否低保 is_low_allowce*/
	private int isLowAllowance;
	/** 是否享受国家助学金 is_enjoy_state_grants*/
	private int isEnjoyState;
	/** 助学金月发送标准（元） */
	private double grantStandard;
	/** 学生类别 DM-XSLB */
	private String studentType;
	/** 学生类型 DM-XSLX */
	private String studentCategory;
	/** 生源类别 DM-XXSYLB */
	private int sourceType;
	/** 教学点名称 teaching_points_name*/
	private String teachPointsName;
	/** 港澳台侨胞 DM-GATQ */
	private int compatriots;
	/** 毕业学校 */
	private String graduateSchool;
	/** 备注 */
	private String remark;
	/** 注册序号 */
	private String registerCode;
	/** 姓名拼音 */
	private String spellName;
	/** 家庭所在地 */
	private String homePlace;
	/** 学历层次 DM-XL */
	private String lastDegree;
	/** 户口所在详细地址 */
	private String registerAddress;
	/** 血型 DM-XX */
	private String bloodType;
	/** 准考证号 */
	private String examCode;
	/** 拍照号 */
	private String photoNo;
	/** 普惠金 */
	private double benefitMoney;
	/** 学生属性 DM-XSSX */
	private String studentMode;
	/** 奖励专业 */
	private String rewardSpecId;
	/** 学制类型 DM-XZLX */
	private String schLengthType;
	/** 计划性质DM-JHXZ */
	private String planMode;
	/** 监护人是否在身边 */
	private int isFamilySide;
	/** 学生QQ号 */
	private String studentQq;
	/** 一卡通卡号 */
	private String oneCardNumber;
	/** 座位编号 */
	private String seatNumber;
	/** 英文姓名 */
	private String englishName;
	/** 入学方式 DM-CRXFS */
	private String toschooltype;
	/** 国籍 */
	private String country;
	/** 学制 */
	private String schoolinglen;
	/** 婚姻情况 DM-CHYZK */
	private String marriage;
	/** 所属派出所 */
	private String localPoliceStation;
	/** 现住址 */
	private String nowaddress;
	/** 家庭邮政编码 home_postalcode */
	private String homepostalcode;
	/** 学生来源  中职eis，eisu都用这个*/
	private String source;
	/** 联招合作办学形式 */
	private String cooperateForm;
	/** 离家最近火车站 */
	private String trainStation;
	/** 原学号 old_student_code*/
	private String oldStuCode;
	/** 家庭电话 family_mobile */
	private String familymobile;
	
	//================================eis 合并字段 by zhangkc 20150602==================================
	/** 指纹1收集 */ 
	private String fingerprint;
	/** 指纹2收集 */
	private String fingerprint2;
	/** 原学校统一编号 */ 
	private String oldSchcode;
	/** 义教卡编号 */ 
	private String compulsoryedu;
	/** 计算机等级 */ 
	private String computerLevel;
	/** 英语等级 */ 
	private String englishLevel;
	/** 家庭出身 */ 
	private String familyOrigin;
	/** 流动人口 */ 
	private int flowingPeople;
	/** 曾任何班干部 */ 
	private String formerClassLeader;
	/** 初中班主任姓名 */ 
	private String formerClassTeacher;
	/** 是否本校学籍 */ 
	private String isLocalSchoolEnrollment;
	/** 入党时间 */ 
	private Date partyDate;
	/** 入党介绍人情况 */ 
	private String partyIntroducer;
	/** 入团时间 */ 
	private Date polityDate;
	/** 入共青团介绍人情况 */ 
	private String polityIntroducer;
	/** 户籍信息 */ 
	private int registerInfo;
	/** 宗教信息 */ 
	private String religion;
	/** 入学成绩语文 */ 
	private double rxArtResult;
	/** 入学成绩英语 */ 
	private double rxEnglishResult;
	/** 入学成绩文理综 */ 
	private double rxIntegratedResult;
	/** 入学成绩数学 */ 
	private double rxMathResult;
	/** 入学成绩总分 */ 
	private double rxResult;
	/** 特长 */ 
	private String strong;
	/** 文理分班意向 */ 
	private String wlClassIntention;
	/** 中考成绩语文 */ 
	private double zkArtResult;
	/** 中考证号 */ 
	private String zkCode;
	/** 中考成绩计算机 */ 
	private double zkComputerResult;
	/** 中考成绩英语 */ 
	private double zkEnglishResult;
	/** 中考成绩实验 */ 
	private double zkExperimentResult;
	/** 中考成绩理综 */ 
	private String zkExtro;
	/** 中考成绩文综 */ 
	private double zkIntegratedResult;
	/** 中考成绩数学 */ 
	private double zkMathResult;
	/** 中考成绩体育 */ 
	private double zkPeResult;
	/** 中考成绩 */ 
	private double zkResult;
	/** 中考成绩特长 */ 
	private String zkStrong;
	/** 上下学距离 */ 
	private String distance;
	/** 上下学交通方式 */ 
	private String trafficWay;
	/** 是否乘坐校车 */ 
	private int isNeedBus;
	/** 是否由政府购买学位 */ 
	private int isGovernmentBear;
	/** 是否需要申请资助 */ 
	private int isNeedAssistance;
	/** 是否孤儿 */ 
	private int isOrphan;
	/** 是否享受一补 */ 
	private int isEnjoyAssistance;
	/** 是否烈士或优抚子女 */ 
	private int isMartyrChild;
	/** 身份证件有效期限 */ 
	private String identitycardValid;
	/** 非农业户口类型  DM-FNYHKLX*/
	private String urbanRegisterType;

	// ===========================辅助字段===============
	private String className;// 班级名称
	private String groupName;// 小组名称，综合素质使用
	private String teachClassId;// 教学班id，高中新课改使用
	private String teachClassStuNo;// 教学班学生编号，高中新课改使用
	private String teacherName;

	public SimpleStudent() {

	}





	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTeachClassId() {
		return teachClassId;
	}

	public void setTeachClassId(String teachClassId) {
		this.teachClassId = teachClassId;
	}

	public String getTeachClassStuNo() {
		return teachClassStuNo;
	}

	public void setTeachClassStuNo(String teachClassStuNo) {
		this.teachClassStuNo = teachClassStuNo;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}

	public Integer getLeavesign() {
		return leavesign;
	}

	public void setLeavesign(Integer leavesign) {
		this.leavesign = leavesign;
	}

	public String getSchid() {
		return schid;
	}

	public void setSchid(String schid) {
		this.schid = schid;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getStucode() {
		return stucode;
	}

	public void setStucode(String stucode) {
		this.stucode = stucode;
	}

	public String getStuname() {
		return stuname;
	}

	public void setStuname(String stuname) {
		this.stuname = stuname;
	}

	public String getUnitivecode() {
		return unitivecode;
	}

	public void setUnitivecode(String unitivecode) {
		this.unitivecode = unitivecode;
	}

	public String getClassorderid() {
		return classorderid;
	}

	public void setClassorderid(String classorderid) {
		this.classorderid = classorderid;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getNativePlace() {
		return nativePlace;
	}

	public void setNativePlace(String nativePlace) {
		this.nativePlace = nativePlace;
	}

	public Date getPolityJoinDate() {
		return polityJoinDate;
	}

	public void setPolityJoinDate(Date polityJoinDate) {
		this.polityJoinDate = polityJoinDate;
	}

	/**
	 * @deprecated 不再使用，参考getMobilePhone
	 */
	public String getMobilephone() {
	    if(StringUtils.isBlank(mobilephone))
	        mobilephone = getMobilePhone();
		return mobilephone;
	}

	/**
	 * 不在使用，参考setMobilePhone
	 * @deprecated
	 * @param mobilephone
	 */
	public void setMobilephone(String mobilephone) {
	    if(StringUtils.isBlank(getMobilePhone()))
	        setMobilePhone(mobilephone);
		this.mobilephone = mobilephone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getIdentitycardType() {
		return identitycardType;
	}

	public void setIdentitycardType(String identitycardType) {
		this.identitycardType = identitycardType;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getEnrollYear() {
		return enrollYear;
	}

	public void setEnrollYear(String enrollYear) {
		this.enrollYear = enrollYear;
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

	public int getIsSingleton() {
		return isSingleton;
	}

	public void setIsSingleton(int isSingleton) {
		this.isSingleton = isSingleton;
	}

	public int getStayin() {
		return stayin;
	}

	public void setStayin(int stayin) {
		this.stayin = stayin;
	}

	public int getBoorish() {
		return boorish;
	}

	public void setBoorish(int boorish) {
		this.boorish = boorish;
	}

	public int getIsLocalSource() {
		return isLocalSource;
	}

	public void setIsLocalSource(int isLocalSource) {
		this.isLocalSource = isLocalSource;
	}
	public int getStudyMode() {
		return studyMode;
	}

	public void setStudyMode(int studyMode) {
		this.studyMode = studyMode;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
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

	public int getIsFreshman() {
		return isFreshman;
	}

	public void setIsFreshman(int isFreshman) {
		this.isFreshman = isFreshman;
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

	public int getIsSpecialid() {
		return isSpecialid;
	}

	public void setIsSpecialid(int isSpecialid) {
		this.isSpecialid = isSpecialid;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
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

	public String getEnterScore() {
		return enterScore;
	}

	public void setEnterScore(String enterScore) {
		this.enterScore = enterScore;
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

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getIsDuplexClass() {
		return isDuplexClass;
	}

	public void setIsDuplexClass(String isDuplexClass) {
		this.isDuplexClass = isDuplexClass;
	}

	public String getDuplexClassGradeId() {
		return duplexClassGradeId;
	}

	public void setDuplexClassGradeId(String duplexClassGradeId) {
		this.duplexClassGradeId = duplexClassGradeId;
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

	public int getRecruitMode() {
		return recruitMode;
	}

	public void setRecruitMode(int recruitMode) {
		this.recruitMode = recruitMode;
	}

	public int getLearnMode() {
		return learnMode;
	}

	public void setLearnMode(int learnMode) {
		this.learnMode = learnMode;
	}

	public int getCooperateType() {
		return cooperateType;
	}

	public void setCooperateType(int cooperateType) {
		this.cooperateType = cooperateType;
	}

	public String getCooperateSchCode() {
		return cooperateSchCode;
	}

	public void setCooperateSchCode(String cooperateSchCode) {
		this.cooperateSchCode = cooperateSchCode;
	}

	public String getRegisterPlace() {
		return registerPlace;
	}

	public void setRegisterPlace(String registerPlace) {
		this.registerPlace = registerPlace;
	}

	public int getRegisterType() {
		return registerType;
	}

	public void setRegisterType(int registerType) {
		this.registerType = registerType;
	}

	public int getIsLowAllowance() {
		return isLowAllowance;
	}

	public void setIsLowAllowance(int isLowAllowance) {
		this.isLowAllowance = isLowAllowance;
	}

	public int getIsEnjoyState() {
		return isEnjoyState;
	}

	public void setIsEnjoyState(int isEnjoyState) {
		this.isEnjoyState = isEnjoyState;
	}

	public double getGrantStandard() {
		return grantStandard;
	}

	public void setGrantStandard(double grantStandard) {
		this.grantStandard = grantStandard;
	}

	public String getStudentType() {
		return studentType;
	}

	public void setStudentType(String studentType) {
		this.studentType = studentType;
	}

	public int getSourceType() {
		return sourceType;
	}

	public void setSourceType(int sourceType) {
		this.sourceType = sourceType;
	}

	public String getTeachPointsName() {
		return teachPointsName;
	}

	public void setTeachPointsName(String teachPointsName) {
		this.teachPointsName = teachPointsName;
	}

	public String getStudentCategory() {
		return studentCategory;
	}

	public void setStudentCategory(String studentCategory) {
		this.studentCategory = studentCategory;
	}

	public int getCompatriots() {
		return compatriots;
	}

	public void setCompatriots(int compatriots) {
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

	public double getBenefitMoney() {
		return benefitMoney;
	}

	public void setBenefitMoney(double benefitMoney) {
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

	public String getPlanMode() {
		return planMode;
	}

	public void setPlanMode(String planMode) {
		this.planMode = planMode;
	}

	public int getIsFamilySide() {
		return isFamilySide;
	}

	public void setIsFamilySide(int isFamilySide) {
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

	public String getSchoolinglen() {
		return schoolinglen;
	}

	public void setSchoolinglen(String schoolinglen) {
		this.schoolinglen = schoolinglen;
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

	public String getHomepostalcode() {
		return homepostalcode;
	}

	public void setHomepostalcode(String homepostalcode) {
		this.homepostalcode = homepostalcode;
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

	public String getOldStuCode() {
		return oldStuCode;
	}

	public void setOldStuCode(String oldStuCode) {
		this.oldStuCode = oldStuCode;
	}

	public String getFamilymobile() {
		return familymobile;
	}

	public void setFamilymobile(String familymobile) {
		this.familymobile = familymobile;
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

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
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

	public String getIsPreedu() {
		return isPreedu;
	}

	public void setIsPreedu(String isPreedu) {
		this.isPreedu = isPreedu;
	}

	public String getAccountAttribution() {
		return accountAttribution;
	}

	public void setAccountAttribution(String accountAttribution) {
		this.accountAttribution = accountAttribution;
	}

	public String getFingerprint() {
		return fingerprint;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public String getOldSchcode() {
		return oldSchcode;
	}

	public void setOldSchcode(String oldSchcode) {
		this.oldSchcode = oldSchcode;
	}

	public String getCompulsoryedu() {
		return compulsoryedu;
	}

	public void setCompulsoryedu(String compulsoryedu) {
		this.compulsoryedu = compulsoryedu;
	}

	public String getComputerLevel() {
		return computerLevel;
	}

	public void setComputerLevel(String computerLevel) {
		this.computerLevel = computerLevel;
	}

	public String getEnglishLevel() {
		return englishLevel;
	}

	public void setEnglishLevel(String englishLevel) {
		this.englishLevel = englishLevel;
	}

	public String getFamilyOrigin() {
		return familyOrigin;
	}

	public void setFamilyOrigin(String familyOrigin) {
		this.familyOrigin = familyOrigin;
	}

	public int getFlowingPeople() {
		return flowingPeople;
	}

	public void setFlowingPeople(int flowingPeople) {
		this.flowingPeople = flowingPeople;
	}

	public String getFormerClassLeader() {
		return formerClassLeader;
	}

	public void setFormerClassLeader(String formerClassLeader) {
		this.formerClassLeader = formerClassLeader;
	}

	public String getFormerClassTeacher() {
		return formerClassTeacher;
	}

	public void setFormerClassTeacher(String formerClassTeacher) {
		this.formerClassTeacher = formerClassTeacher;
	}

	public String getIsLocalSchoolEnrollment() {
		return isLocalSchoolEnrollment;
	}

	public void setIsLocalSchoolEnrollment(String isLocalSchoolEnrollment) {
		this.isLocalSchoolEnrollment = isLocalSchoolEnrollment;
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

	public int getRegisterInfo() {
		return registerInfo;
	}

	public void setRegisterInfo(int registerInfo) {
		this.registerInfo = registerInfo;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public double getRxArtResult() {
		return rxArtResult;
	}

	public void setRxArtResult(double rxArtResult) {
		this.rxArtResult = rxArtResult;
	}

	public double getRxEnglishResult() {
		return rxEnglishResult;
	}

	public void setRxEnglishResult(double rxEnglishResult) {
		this.rxEnglishResult = rxEnglishResult;
	}

	public double getRxIntegratedResult() {
		return rxIntegratedResult;
	}

	public void setRxIntegratedResult(double rxIntegratedResult) {
		this.rxIntegratedResult = rxIntegratedResult;
	}

	public double getRxMathResult() {
		return rxMathResult;
	}

	public void setRxMathResult(double rxMathResult) {
		this.rxMathResult = rxMathResult;
	}

	public double getRxResult() {
		return rxResult;
	}

	public void setRxResult(double rxResult) {
		this.rxResult = rxResult;
	}

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}

	public String getWlClassIntention() {
		return wlClassIntention;
	}

	public void setWlClassIntention(String wlClassIntention) {
		this.wlClassIntention = wlClassIntention;
	}

	public double getZkArtResult() {
		return zkArtResult;
	}

	public void setZkArtResult(double zkArtResult) {
		this.zkArtResult = zkArtResult;
	}

	public String getZkCode() {
		return zkCode;
	}

	public void setZkCode(String zkCode) {
		this.zkCode = zkCode;
	}

	public double getZkComputerResult() {
		return zkComputerResult;
	}

	public void setZkComputerResult(double zkComputerResult) {
		this.zkComputerResult = zkComputerResult;
	}

	public double getZkEnglishResult() {
		return zkEnglishResult;
	}

	public void setZkEnglishResult(double zkEnglishResult) {
		this.zkEnglishResult = zkEnglishResult;
	}

	public double getZkExperimentResult() {
		return zkExperimentResult;
	}

	public void setZkExperimentResult(double zkExperimentResult) {
		this.zkExperimentResult = zkExperimentResult;
	}

	public String getZkExtro() {
		return zkExtro;
	}

	public void setZkExtro(String zkExtro) {
		this.zkExtro = zkExtro;
	}

	public double getZkIntegratedResult() {
		return zkIntegratedResult;
	}

	public void setZkIntegratedResult(double zkIntegratedResult) {
		this.zkIntegratedResult = zkIntegratedResult;
	}

	public double getZkMathResult() {
		return zkMathResult;
	}

	public void setZkMathResult(double zkMathResult) {
		this.zkMathResult = zkMathResult;
	}

	public double getZkPeResult() {
		return zkPeResult;
	}

	public void setZkPeResult(double zkPeResult) {
		this.zkPeResult = zkPeResult;
	}

	public double getZkResult() {
		return zkResult;
	}

	public void setZkResult(double zkResult) {
		this.zkResult = zkResult;
	}

	public String getZkStrong() {
		return zkStrong;
	}

	public void setZkStrong(String zkStrong) {
		this.zkStrong = zkStrong;
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

	public int getIsNeedBus() {
		return isNeedBus;
	}

	public void setIsNeedBus(int isNeedBus) {
		this.isNeedBus = isNeedBus;
	}

	public int getIsGovernmentBear() {
		return isGovernmentBear;
	}

	public void setIsGovernmentBear(int isGovernmentBear) {
		this.isGovernmentBear = isGovernmentBear;
	}

	public int getIsNeedAssistance() {
		return isNeedAssistance;
	}

	public void setIsNeedAssistance(int isNeedAssistance) {
		this.isNeedAssistance = isNeedAssistance;
	}

	public int getIsOrphan() {
		return isOrphan;
	}

	public void setIsOrphan(int isOrphan) {
		this.isOrphan = isOrphan;
	}

	public int getIsEnjoyAssistance() {
		return isEnjoyAssistance;
	}

	public void setIsEnjoyAssistance(int isEnjoyAssistance) {
		this.isEnjoyAssistance = isEnjoyAssistance;
	}

	public int getIsMartyrChild() {
		return isMartyrChild;
	}

	public void setIsMartyrChild(int isMartyrChild) {
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

	public String getSocialRecruitmentType() {
		return socialRecruitmentType;
	}

	public void setSocialRecruitmentType(String socialRecruitmentType) {
		this.socialRecruitmentType = socialRecruitmentType;
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

	@Override
	public String fetchCacheEntitName() {
		return "SimpleStudent";
	}
}