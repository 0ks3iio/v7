package net.zdsoft.newstusys.businessimport.entity;

import java.io.Serializable;

/**
 * STUDENT_INFORMAL_TEMP
 * 
 * @author
 * 
 */
@SuppressWarnings("serial")
public class StudentImport implements Serializable {
	// 学生基本字段
	private String stuname; // 姓名
	private String sex; // 性别
	private String birthday; // 出生日期
	private String sourcetype; // 学生来源
	private String identitycardType;// 身份证件类型
	private String identitycard; // 身份证号码
	private String unitivecode; // 学籍号
	private String stucode; // 学号
	private String classid; // 班级id
	private String boarder; // 就读方式
	private String registerinfo; // 户籍信息
	private String cardnumber; // 点到卡号
	private String classorderid; // 班内编号

	private String islocalsource; // 是否本地生源
	private String flowingpeople; // 是否流动人口
	private String background; // 政治面貌
	private String polityJoinDate; // 加入时间（政治面貌）
	private String nation; // 民族
	private String oldname; // 曾用名
	private String spellname; // 姓名拼音
	private String bloodtype; // 血型
	private String religion; // 宗教信仰
	private String homeaddress; // 家庭地址
	private String registerplace; // 户口所在地
	private String registerStreet;// 户籍镇街
	private String rewardRemark;
	private String resumeStr;

	private String linkphone; // 联系电话
	private String postalcode; // 邮政编码
	private String email; // 电子邮件
	private String toschooltype; // 入学方式
	private String toschooldate; // 入学年月
	private String oldSchoolName; // 毕业学校名称--来源学校
	private String oldSchoolDate; // 毕业学校名称--来源学校入学日期
	private String nativeplace; // 籍贯
	private String health; // 健康状况

	private String country; // 国别
	private String sourceplace; // 来源地区
	private String preeduflag; // 是否接受学前教育
	private String mobilephone; // 手机
	private String computerlevel; // 计算机等级
	private String familyorigin; // 家庭出身
	private String strong; // 特长
	private String remark; // 备注
	private String examcode; // 考试证号

	// 2009-12-21 新增字段
	private String linkaddress; // 联系地址
	private String homeplace; // 出生地
	private String economystate; // 经济状况
	private String homepage; // 个人主页
	private String returnedchinese; // 港澳台侨
	private String polityintroducer; // 入团介绍人
	private String politydate; // 入团时间
	private String englishlevel; // 英语等级
	private String singleton; // 独生子女
	private String stayin; // 留守儿童
	private String boorish; // 农民工子女

	// 2012-02-03
	private String recruitMode;// 招生模式
	private String learnMode;// 学习模式
	private String cooperateType;// 联招合作类型
	private String cooperateSchCode;// 联招合作学校机构代码
	private String registerPlace;// 户籍所在地
	private String registerType;// 户籍性质
	private String isLowAllowance;// 是否低保
	private String isEnjoyState;// 是否享受国家助学金
	private String grantStandard;// 助学金月发送标准（元）
	private String studentType;// 学生类型
	private String sourceType;// 生源类别--招生对象
	private String teachPointsName;// 教学点名称
	private String studentCategory;// 学生类别
	private String compatriots;// 港澳台侨胞
	private String graduateSchool;// 毕业学校
	private String bankCardNo;// 银行卡号
	private String familyName;// 家庭（监护人）姓名
	private String familyPhone;// 家长（监护人）电话

	// 2012-09-20
	private String registerCode;// 注册序号
	private String spellName;// 姓名拼音
	private String homePlace;// 家庭所在地
	private String lastDegree;// 学历层次 DM-XL
	private String registerAddress;// 户口所在详细地址
	private String bloodType;// 血型 DM-XX
	private String examCode;// 准考证号
	// private String credentialType;// 证件类型
	// private String credentialCode;// 证件号
	private String photoNo;// 拍照号
	private String benefitMoney;// 普惠金
	private String studentMode;// 学生属性 DM-XSSX
	private String rewardSpecId;// 奖励专业
	private String schLengthType;// 学制类型 DM-XZLX
	private String planMode;// 计划性质 DM-JHXZ

	// 2014-09-01
	private String isFamilySide;// 监护人是否在身边
	private String studentQq;// 学生QQ号
	private String oneCardNumber;// 一卡通卡号
	private String seatNumber;// 座位编号

	// 学生入学成绩
	private String chineseScore;// 语文成绩
	private String mathScore;// 数学成绩
	private String englishScore;// 英语成绩
	private String scienceScore;// 科学成绩
	private String chineseRanking;// 语文排名
	private String mathRanking;// 数学排名
	private String englishRanking;// 英语排名
	private String scienceRanking;// 科学排名
	private String allScoreRanking;// 总分排名
	private String averageScoreRanking;// 平均分排名

	// 考试分数信息
	private String subj1; // 科目1
	private String subj2; // 科目2
	private String subj3; // 科目3
	private String subj4; // 科目4
	private String subj5; // 科目5
	private String subj6; // 科目6
	private String subj7; // 科目7
	private String subj8; // 科目8
	private String subj9; // 科目9

	// 学生数据导入新增字段
	private String zkcode; // 中考证号
	private String zkresult; // 中考成绩

	private String dorm;
	private String dormTel;
	private String enterScore;
	private String specialtyId;
	private String specialtyPointId;

	/**
	 * 隐藏字段
	 */
	private String stuid; // 学生id
	private String schid; // 学校id
	private String enrollyear; // 入学学年
	private String section; // 学段(对未入学学生而言，是非隐藏字段)
	private String regioncode; // 地区码
	private String oldschcode; // 毕业学校代码
	private String schcode; // 学校代码
	private String schname; // 学校名称

	// 教育局审核学校导入专用
	private String allocid; // 招生分派id
	private String scoreid; // 招生成绩id
	private Integer infotype; // 同recruit_allocate表中的informal_type
	private Integer state; // 同recruit_allocate表中的state

	// 下面的四个字段用在student_normalflow
	private String operator; // 操作人
	private String operateunit; // 操作人单位
	private String flowtype; // 流动类型
	private String acadyear;
	private String semester;

	// 学籍状态
	private Integer leavesign; // 注意：上面的flowtype和now_state同义

	// 中策 全国模板 2014-12-1 add by like
	// --------start----------
	private String organizationcode;// 学校标识码
	private String englishName;// 英文姓名
	private String specialtyCatalog;// 专业目录版本
	private String schoolinglen;// 学制
	private String marriage;// 婚姻情况 DM-CHYZK
	private String nativeType;// 户口性质
	private String localPoliceStation;// 所属派出所
	private String nowaddress;// 现住址
	private String homepostalcode;// 家庭邮政编码
	private String source;// 学生来源
	private String cooperateForm;// 联招合作办学形式
	private String trainStation;// 离家最近火车站
	private String oldStuCode;// 原学号
	private String familymobile;// 家庭电话 非监护人电话

	private String gradeId;
	private String nowState;

	private String fuRealName;// 父亲姓名
	private String muRealName;// 母亲姓名

	private String realname1;// 成员1姓名
	private String relation1;// 成员1关系
	private String isguardian1;// 成员1是否监护人
	private String linkphone1;// 成员1联系电话
	private String birthday1;// 成员1出生年月
	private String identitycardType1;// 成员1身份证件类型
	private String identitycard1;// 成员1身份证件号
	private String nation1;// 成员1民族
	private String politicalstatus1;// 成员1政治面貌
	private String health1;// 成员1健康状况
	private String company1;// 成员1工作或学习单位
	private String duty1;// 成员1职务
	private String culture1;// 成员1文化程度
	private String background1;// 成员1政治面貌
	private String country1;

	private String realname2;// 成员2姓名
	private String relation2;// 成员2关系
	private String isguardian2;// 成员2是否监护人
	private String linkphone2;// 成员2联系电话
	private String birthday2;// 成员2出生年月
	private String identitycardType2;// 成员2身份证件类型
	private String identitycard2;// 成员2身份证件号
	private String nation2;// 成员2民族
	private String politicalstatus2;// 成员2政治面貌
	private String health2;// 成员2健康状况
	private String company2;// 成员2工作或学习单位
	private String duty2;// 成员2职务
	private String culture2;// 成员2文化程度
	private String background2;// 成员2政治面貌
	private String country2;
	// --------end----------

	// 20180110 增加学生和家长的字段
	private String stuUserName;
	private String stuUserPassword;
	private String familyUserName;
	private String familyUserPassword;
	private String familyRealName;
	private String familyMobilephone;
	private String relation;
	
	private String oriDataRowNum;

	public String getStuname() {
		return stuname;
	}

	public void setStuname(String stuname) {
		this.stuname = stuname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSourcetype() {
		return sourcetype;
	}

	public void setSourcetype(String sourcetype) {
		this.sourcetype = sourcetype;
	}

	public String getIdentitycardType() {
		return identitycardType;
	}

	public void setIdentitycardType(String identitycardType) {
		this.identitycardType = identitycardType;
	}

	public String getIdentitycard() {
		return identitycard;
	}

	public void setIdentitycard(String identitycard) {
		this.identitycard = identitycard;
	}

	public String getUnitivecode() {
		return unitivecode;
	}

	public void setUnitivecode(String unitivecode) {
		this.unitivecode = unitivecode;
	}

	public String getStucode() {
		return stucode;
	}

	public void setStucode(String stucode) {
		this.stucode = stucode;
	}

	public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}

	public String getBoarder() {
		return boarder;
	}

	public void setBoarder(String boarder) {
		this.boarder = boarder;
	}

	public String getRegisterinfo() {
		return registerinfo;
	}

	public void setRegisterinfo(String registerinfo) {
		this.registerinfo = registerinfo;
	}

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getClassorderid() {
		return classorderid;
	}

	public void setClassorderid(String classorderid) {
		this.classorderid = classorderid;
	}

	public String getIslocalsource() {
		return islocalsource;
	}

	public void setIslocalsource(String islocalsource) {
		this.islocalsource = islocalsource;
	}

	public String getFlowingpeople() {
		return flowingpeople;
	}

	public void setFlowingpeople(String flowingpeople) {
		this.flowingpeople = flowingpeople;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getPolityJoinDate() {
		return polityJoinDate;
	}

	public void setPolityJoinDate(String polityJoinDate) {
		this.polityJoinDate = polityJoinDate;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getOldname() {
		return oldname;
	}

	public void setOldname(String oldname) {
		this.oldname = oldname;
	}

	public String getSpellname() {
		return spellname;
	}

	public void setSpellname(String spellname) {
		this.spellname = spellname;
	}

	public String getBloodtype() {
		return bloodtype;
	}

	public void setBloodtype(String bloodtype) {
		this.bloodtype = bloodtype;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getRegisterplace() {
		return registerplace;
	}

	public void setRegisterplace(String registerplace) {
		this.registerplace = registerplace;
	}

	public String getLinkphone() {
		return linkphone;
	}

	public void setLinkphone(String linkphone) {
		this.linkphone = linkphone;
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

	public String getToschooltype() {
		return toschooltype;
	}

	public void setToschooltype(String toschooltype) {
		this.toschooltype = toschooltype;
	}

	public String getToschooldate() {
		return toschooldate;
	}

	public void setToschooldate(String toschooldate) {
		this.toschooldate = toschooldate;
	}

	public String getOldSchoolName() {
		return oldSchoolName;
	}

	public void setOldSchoolName(String oldSchoolName) {
		this.oldSchoolName = oldSchoolName;
	}

	public String getOldSchoolDate() {
		return oldSchoolDate;
	}

	public void setOldSchoolDate(String oldSchoolDate) {
		this.oldSchoolDate = oldSchoolDate;
	}

	public String getNativeplace() {
		return nativeplace;
	}

	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}

	public String getHealth() {
		return health;
	}

	public void setHealth(String health) {
		this.health = health;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSourceplace() {
		return sourceplace;
	}

	public void setSourceplace(String sourceplace) {
		this.sourceplace = sourceplace;
	}

	public String getPreeduflag() {
		return preeduflag;
	}

	public void setPreeduflag(String preeduflag) {
		this.preeduflag = preeduflag;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getComputerlevel() {
		return computerlevel;
	}

	public void setComputerlevel(String computerlevel) {
		this.computerlevel = computerlevel;
	}

	public String getFamilyorigin() {
		return familyorigin;
	}

	public void setFamilyorigin(String familyorigin) {
		this.familyorigin = familyorigin;
	}

	public String getStrong() {
		return strong;
	}

	public void setStrong(String strong) {
		this.strong = strong;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExamcode() {
		return examcode;
	}

	public void setExamcode(String examcode) {
		this.examcode = examcode;
	}

	public String getLinkaddress() {
		return linkaddress;
	}

	public void setLinkaddress(String linkaddress) {
		this.linkaddress = linkaddress;
	}

	public String getHomeplace() {
		return homeplace;
	}

	public void setHomeplace(String homeplace) {
		this.homeplace = homeplace;
	}

	public String getEconomystate() {
		return economystate;
	}

	public void setEconomystate(String economystate) {
		this.economystate = economystate;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getReturnedchinese() {
		return returnedchinese;
	}

	public void setReturnedchinese(String returnedchinese) {
		this.returnedchinese = returnedchinese;
	}

	public String getPolityintroducer() {
		return polityintroducer;
	}

	public void setPolityintroducer(String polityintroducer) {
		this.polityintroducer = polityintroducer;
	}

	public String getPolitydate() {
		return politydate;
	}

	public void setPolitydate(String politydate) {
		this.politydate = politydate;
	}

	public String getEnglishlevel() {
		return englishlevel;
	}

	public void setEnglishlevel(String englishlevel) {
		this.englishlevel = englishlevel;
	}

	public String getSingleton() {
		return singleton;
	}

	public void setSingleton(String singleton) {
		this.singleton = singleton;
	}

	public String getStayin() {
		return stayin;
	}

	public void setStayin(String stayin) {
		this.stayin = stayin;
	}

	public String getBoorish() {
		return boorish;
	}

	public void setBoorish(String boorish) {
		this.boorish = boorish;
	}

	public String getRecruitMode() {
		return recruitMode;
	}

	public void setRecruitMode(String recruitMode) {
		this.recruitMode = recruitMode;
	}

	public String getLearnMode() {
		return learnMode;
	}

	public void setLearnMode(String learnMode) {
		this.learnMode = learnMode;
	}

	public String getCooperateType() {
		return cooperateType;
	}

	public void setCooperateType(String cooperateType) {
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

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public String getIsLowAllowance() {
		return isLowAllowance;
	}

	public void setIsLowAllowance(String isLowAllowance) {
		this.isLowAllowance = isLowAllowance;
	}

	public String getIsEnjoyState() {
		return isEnjoyState;
	}

	public void setIsEnjoyState(String isEnjoyState) {
		this.isEnjoyState = isEnjoyState;
	}

	public String getGrantStandard() {
		return grantStandard;
	}

	public void setGrantStandard(String grantStandard) {
		this.grantStandard = grantStandard;
	}

	public String getStudentType() {
		return studentType;
	}

	public void setStudentType(String studentType) {
		this.studentType = studentType;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
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

	public String getCompatriots() {
		return compatriots;
	}

	public void setCompatriots(String compatriots) {
		this.compatriots = compatriots;
	}

	public String getGraduateSchool() {
		return graduateSchool;
	}

	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFamilyPhone() {
		return familyPhone;
	}

	public void setFamilyPhone(String familyPhone) {
		this.familyPhone = familyPhone;
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

	public String getBenefitMoney() {
		return benefitMoney;
	}

	public void setBenefitMoney(String benefitMoney) {
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

	public String getIsFamilySide() {
		return isFamilySide;
	}

	public void setIsFamilySide(String isFamilySide) {
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

	public String getChineseScore() {
		return chineseScore;
	}

	public void setChineseScore(String chineseScore) {
		this.chineseScore = chineseScore;
	}

	public String getMathScore() {
		return mathScore;
	}

	public void setMathScore(String mathScore) {
		this.mathScore = mathScore;
	}

	public String getEnglishScore() {
		return englishScore;
	}

	public void setEnglishScore(String englishScore) {
		this.englishScore = englishScore;
	}

	public String getScienceScore() {
		return scienceScore;
	}

	public void setScienceScore(String scienceScore) {
		this.scienceScore = scienceScore;
	}

	public String getChineseRanking() {
		return chineseRanking;
	}

	public void setChineseRanking(String chineseRanking) {
		this.chineseRanking = chineseRanking;
	}

	public String getMathRanking() {
		return mathRanking;
	}

	public void setMathRanking(String mathRanking) {
		this.mathRanking = mathRanking;
	}

	public String getEnglishRanking() {
		return englishRanking;
	}

	public void setEnglishRanking(String englishRanking) {
		this.englishRanking = englishRanking;
	}

	public String getScienceRanking() {
		return scienceRanking;
	}

	public void setScienceRanking(String scienceRanking) {
		this.scienceRanking = scienceRanking;
	}

	public String getAllScoreRanking() {
		return allScoreRanking;
	}

	public void setAllScoreRanking(String allScoreRanking) {
		this.allScoreRanking = allScoreRanking;
	}

	public String getAverageScoreRanking() {
		return averageScoreRanking;
	}

	public void setAverageScoreRanking(String averageScoreRanking) {
		this.averageScoreRanking = averageScoreRanking;
	}

	public String getSubj1() {
		return subj1;
	}

	public void setSubj1(String subj1) {
		this.subj1 = subj1;
	}

	public String getSubj2() {
		return subj2;
	}

	public void setSubj2(String subj2) {
		this.subj2 = subj2;
	}

	public String getSubj3() {
		return subj3;
	}

	public void setSubj3(String subj3) {
		this.subj3 = subj3;
	}

	public String getSubj4() {
		return subj4;
	}

	public void setSubj4(String subj4) {
		this.subj4 = subj4;
	}

	public String getSubj5() {
		return subj5;
	}

	public void setSubj5(String subj5) {
		this.subj5 = subj5;
	}

	public String getSubj6() {
		return subj6;
	}

	public void setSubj6(String subj6) {
		this.subj6 = subj6;
	}

	public String getSubj7() {
		return subj7;
	}

	public void setSubj7(String subj7) {
		this.subj7 = subj7;
	}

	public String getSubj8() {
		return subj8;
	}

	public void setSubj8(String subj8) {
		this.subj8 = subj8;
	}

	public String getSubj9() {
		return subj9;
	}

	public void setSubj9(String subj9) {
		this.subj9 = subj9;
	}

	public String getZkcode() {
		return zkcode;
	}

	public void setZkcode(String zkcode) {
		this.zkcode = zkcode;
	}

	public String getZkresult() {
		return zkresult;
	}

	public void setZkresult(String zkresult) {
		this.zkresult = zkresult;
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

	public String getSpecialtyId() {
		return specialtyId;
	}

	public void setSpecialtyId(String specialtyId) {
		this.specialtyId = specialtyId;
	}

	public String getSpecialtyPointId() {
		return specialtyPointId;
	}

	public void setSpecialtyPointId(String specialtyPointId) {
		this.specialtyPointId = specialtyPointId;
	}

	public String getStuid() {
		return stuid;
	}

	public void setStuid(String stuid) {
		this.stuid = stuid;
	}

	public String getSchid() {
		return schid;
	}

	public void setSchid(String schid) {
		this.schid = schid;
	}

	public String getEnrollyear() {
		return enrollyear;
	}

	public void setEnrollyear(String enrollyear) {
		this.enrollyear = enrollyear;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}

	public String getOldschcode() {
		return oldschcode;
	}

	public void setOldschcode(String oldschcode) {
		this.oldschcode = oldschcode;
	}

	public String getSchcode() {
		return schcode;
	}

	public void setSchcode(String schcode) {
		this.schcode = schcode;
	}

	public String getSchname() {
		return schname;
	}

	public void setSchname(String schname) {
		this.schname = schname;
	}

	public String getAllocid() {
		return allocid;
	}

	public void setAllocid(String allocid) {
		this.allocid = allocid;
	}

	public String getScoreid() {
		return scoreid;
	}

	public void setScoreid(String scoreid) {
		this.scoreid = scoreid;
	}

	public Integer getInfotype() {
		return infotype;
	}

	public void setInfotype(Integer infotype) {
		this.infotype = infotype;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperateunit() {
		return operateunit;
	}

	public void setOperateunit(String operateunit) {
		this.operateunit = operateunit;
	}

	public String getFlowtype() {
		return flowtype;
	}

	public void setFlowtype(String flowtype) {
		this.flowtype = flowtype;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public Integer getLeavesign() {
		return leavesign;
	}

	public void setLeavesign(Integer leavesign) {
		this.leavesign = leavesign;
	}

	public String getOrganizationcode() {
		return organizationcode;
	}

	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public String getSpecialtyCatalog() {
		return specialtyCatalog;
	}

	public void setSpecialtyCatalog(String specialtyCatalog) {
		this.specialtyCatalog = specialtyCatalog;
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

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getNowState() {
		return nowState;
	}

	public void setNowState(String nowState) {
		this.nowState = nowState;
	}

	public String getFuRealName() {
		return fuRealName;
	}

	public void setFuRealName(String fuRealName) {
		this.fuRealName = fuRealName;
	}

	public String getMuRealName() {
		return muRealName;
	}

	public void setMuRealName(String muRealName) {
		this.muRealName = muRealName;
	}

	public String getRealname1() {
		return realname1;
	}

	public void setRealname1(String realname1) {
		this.realname1 = realname1;
	}

	public String getRelation1() {
		return relation1;
	}

	public void setRelation1(String relation1) {
		this.relation1 = relation1;
	}

	public String getIsguardian1() {
		return isguardian1;
	}

	public void setIsguardian1(String isguardian1) {
		this.isguardian1 = isguardian1;
	}

	public String getLinkphone1() {
		return linkphone1;
	}

	public void setLinkphone1(String linkphone1) {
		this.linkphone1 = linkphone1;
	}

	public String getBirthday1() {
		return birthday1;
	}

	public void setBirthday1(String birthday1) {
		this.birthday1 = birthday1;
	}

	public String getIdentitycardType1() {
		return identitycardType1;
	}

	public void setIdentitycardType1(String identitycardType1) {
		this.identitycardType1 = identitycardType1;
	}

	public String getIdentitycard1() {
		return identitycard1;
	}

	public void setIdentitycard1(String identitycard1) {
		this.identitycard1 = identitycard1;
	}

	public String getNation1() {
		return nation1;
	}

	public void setNation1(String nation1) {
		this.nation1 = nation1;
	}

	public String getPoliticalstatus1() {
		return politicalstatus1;
	}

	public void setPoliticalstatus1(String politicalstatus1) {
		this.politicalstatus1 = politicalstatus1;
	}

	public String getHealth1() {
		return health1;
	}

	public void setHealth1(String health1) {
		this.health1 = health1;
	}

	public String getCompany1() {
		return company1;
	}

	public void setCompany1(String company1) {
		this.company1 = company1;
	}

	public String getDuty1() {
		return duty1;
	}

	public void setDuty1(String duty1) {
		this.duty1 = duty1;
	}

	public String getRealname2() {
		return realname2;
	}

	public void setRealname2(String realname2) {
		this.realname2 = realname2;
	}

	public String getRelation2() {
		return relation2;
	}

	public void setRelation2(String relation2) {
		this.relation2 = relation2;
	}

	public String getIsguardian2() {
		return isguardian2;
	}

	public void setIsguardian2(String isguardian2) {
		this.isguardian2 = isguardian2;
	}

	public String getLinkphone2() {
		return linkphone2;
	}

	public void setLinkphone2(String linkphone2) {
		this.linkphone2 = linkphone2;
	}

	public String getBirthday2() {
		return birthday2;
	}

	public void setBirthday2(String birthday2) {
		this.birthday2 = birthday2;
	}

	public String getIdentitycardType2() {
		return identitycardType2;
	}

	public void setIdentitycardType2(String identitycardType2) {
		this.identitycardType2 = identitycardType2;
	}

	public String getIdentitycard2() {
		return identitycard2;
	}

	public void setIdentitycard2(String identitycard2) {
		this.identitycard2 = identitycard2;
	}

	public String getNation2() {
		return nation2;
	}

	public void setNation2(String nation2) {
		this.nation2 = nation2;
	}

	public String getPoliticalstatus2() {
		return politicalstatus2;
	}

	public void setPoliticalstatus2(String politicalstatus2) {
		this.politicalstatus2 = politicalstatus2;
	}

	public String getHealth2() {
		return health2;
	}

	public void setHealth2(String health2) {
		this.health2 = health2;
	}

	public String getCompany2() {
		return company2;
	}

	public void setCompany2(String company2) {
		this.company2 = company2;
	}

	public String getDuty2() {
		return duty2;
	}

	public void setDuty2(String duty2) {
		this.duty2 = duty2;
	}

	public String getStuUserName() {
		return stuUserName;
	}

	public void setStuUserName(String stuUserName) {
		this.stuUserName = stuUserName;
	}

	public String getStuUserPassword() {
		return stuUserPassword;
	}

	public void setStuUserPassword(String stuUserPassword) {
		this.stuUserPassword = stuUserPassword;
	}

	public String getFamilyUserName() {
		return familyUserName;
	}

	public void setFamilyUserName(String familyUserName) {
		this.familyUserName = familyUserName;
	}

	public String getFamilyUserPassword() {
		return familyUserPassword;
	}

	public void setFamilyUserPassword(String familyUserPassword) {
		this.familyUserPassword = familyUserPassword;
	}

	public String getFamilyRealName() {
		return familyRealName;
	}

	public void setFamilyRealName(String familyRealName) {
		this.familyRealName = familyRealName;
	}

	public String getFamilyMobilephone() {
		return familyMobilephone;
	}

	public void setFamilyMobilephone(String familyMobilephone) {
		this.familyMobilephone = familyMobilephone;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getOriDataRowNum() {
		return oriDataRowNum;
	}

	public void setOriDataRowNum(String oriDataRowNum) {
		this.oriDataRowNum = oriDataRowNum;
	}

	public String getRegisterStreet() {
		return registerStreet;
	}

	public void setRegisterStreet(String registerStreet) {
		this.registerStreet = registerStreet;
	}

	public String getRewardRemark() {
		return rewardRemark;
	}

	public void setRewardRemark(String rewardRemark) {
		this.rewardRemark = rewardRemark;
	}

	public String getResumeStr() {
		return resumeStr;
	}

	public void setResumeStr(String resumeStr) {
		this.resumeStr = resumeStr;
	}

	public String getCulture1() {
		return culture1;
	}

	public void setCulture1(String culture1) {
		this.culture1 = culture1;
	}

	public String getCulture2() {
		return culture2;
	}

	public void setCulture2(String culture2) {
		this.culture2 = culture2;
	}

	public String getBackground1() {
		return background1;
	}

	public void setBackground1(String background1) {
		this.background1 = background1;
	}

	public String getBackground2() {
		return background2;
	}

	public void setBackground2(String background2) {
		this.background2 = background2;
	}

	public String getCountry1() {
		return country1;
	}

	public void setCountry1(String country1) {
		this.country1 = country1;
	}

	public String getCountry2() {
		return country2;
	}

	public void setCountry2(String country2) {
		this.country2 = country2;
	}

}