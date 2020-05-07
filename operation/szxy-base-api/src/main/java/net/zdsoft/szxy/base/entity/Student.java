package net.zdsoft.szxy.base.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/19 下午4:32
 */
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Table(name = "base_student")
@Entity
public class Student implements Serializable {

    @Setter
    @Getter
    @Id
    private String id;
    /**
     * 所在学校ID
     */
    @Getter
    @Setter
    @Column(length = 32)
    private String schoolId;
    /**
     * 所在班级ID
     */
    @Getter
    @Setter
    @Column(length = 32)
    private String classId;
    /**
     * 学生姓名
     */
    @Getter
    @Setter
    private String studentName;
    /**
     * 曾用名
     */
    @Getter
    @Setter
    private String oldName;
    /**
     * 学号
     */
    @Getter
    @Setter
    @Column(length = 20)
    private String studentCode;
    /**
     * 学籍号
     */
    @Getter
    @Setter
    @Column(length = 30)
    private String unitiveCode;
    /**
     * 身份证件号码
     */
    @Getter
    @Setter
    @Column(length = 30)
    private String identityCard;
    /**
     * 性别 DM-XB
     */
    @Getter
    @Setter
    @Column(length = 1)
    private Integer sex;
    /**
     * 出生日期
     */
    @Getter
    @Setter
    private Date birthday;
    /**
     * 手机号码
     */
    @Getter
    @Setter
    @Column(length = 50)
    private String mobilePhone;
    /**
     * 是否离校
     */
    @Getter
    @Setter
    private Integer isLeaveSchool;
    /**
     * 新生入学学年，取班级的入学学年
     */
    @Getter
    @Setter
    private String enrollYear;
    /**
     * 班内编号
     */
    @Getter
    @Setter
    @Column(length = 3)
    private String classInnerCode;
    /**
     * 家庭经济情况 DM-JTJJZK
     */
    @Getter
    @Setter
    @Column(length = 2)
    private String economyState;
    /**
     * 点到卡号
     */
    @Getter
    @Setter
    @Column(length = 20)
    private String cardNumber;
    /**
     * 独生子女 DM-BOOLEAN
     */
    @Getter
    @Setter
    private Integer isSingleton;
    /**
     * 留守儿童 DM-BOOLEAN
     */
    @Getter
    @Setter
    private Integer stayin;
    /**
     * 贫困家庭 DM-BOOLEAN
     */
    @Getter
    @Setter
    private Integer boorish;
    /**
     * 是否本地生源 1是  0否
     */
    @Getter
    @Setter
    private Integer isLocalSource;
    /**
     * 学生来源 DM-XSLY
     */
    @Getter
    @Setter
    private Integer studentRecruitment;
    /**
     * 就读方式 DM-JDFS
     */
    @Getter
    @Setter
    private Integer studyMode;
    /**
     * 籍贯
     */
    @Getter
    @Setter
    @Column(length = 1)
    private String nativePlace;
    /**
     * 个人主页
     */
    @Getter
    @Setter
    @Column(length = 100)
    private String homepage;
    /**
     * 电子信箱
     */
    @Getter
    @Setter
    @Column(length = 40)
    private String email;
    /**
     * 联系电话
     */
    @Getter
    @Setter
    @Column(length = 30)
    private String linkPhone;
    /**
     * 联系地址
     */
    @Getter
    @Setter
    private String linkAddress;
    /**
     * 邮政编码
     */
    @Getter
    @Setter
    private String postalcode;
    /**
     * 行政区划，默认取学校的行政区划
     */
    @Getter
    @Setter
    private String regionCode;
    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    @Getter
    @Setter
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    @Getter
    @Setter
    private Integer isDeleted;
    @Getter
    @Setter
    private Integer eventSource;
    /**
     * 是否新生
     * @see net.zdsoft.szxy.base.enu.StudentFreshmanCode
     */
    @Getter
    @Setter
    private Integer isFreshman;
    /**
     * 密码
     */
    @Getter
    @Setter
    @Column(length = 80)
    private String password;
    /**
     * 当前状态 DM-YDLB
     */
    @Getter
    @Setter
    @Column(length = 2)
    private String nowState;
    /**
     * 学区
     */
    @Getter
    @Setter
    @Column(length = 32)
    private String schoolDistrict;
    /**
     * 会考证号
     */
    @Getter
    @Setter
    private String meetexamCode;
    /**
     * 来源地区
     */
    @Getter
    @Setter
    private String sourcePlace;
    /**
     * 是否特殊身份证号
     */
    @Getter
    @Setter
    private Integer isSpecialid;
    /**
     * 照片目录
     */
    @Getter
    @Setter
    private String dirId;
    /**
     * 照片相对路径
     */
    @Getter
    @Setter
    private String filePath;
    /**
     * 健康状况 DM-JKZK
     */
    @Getter
    @Setter
    private String health;
    /**
     * 民族 DM-MZ
     */
    @Getter
    @Setter
    private String nation;
    /**
     * 政治面貌 DM-ZZMM
     */
    @Getter
    @Setter
    private String background;
    /**
     * 入团时间
     */
    @Getter
    @Setter
    private Date polityJoinDate;
    /**
     * 家庭地址
     */
    @Getter
    @Setter
    private String homeAddress;
    /**
     * 入学时间
     */
    @Getter
    @Setter
    private Date toSchoolDate;
    /**
     * 学生宿舍
     */
    @Getter
    @Setter
    private String dorm;
    /**
     * 学生宿舍电话
     */
    @Getter
    @Setter
    private String dormTel;
    /**
     * 职高专业
     */
    @Getter
    @Setter
    private String specId;
    /**
     * 录取专业方向
     */
    @Getter
    @Setter
    private String specpointId;
    /**
     * 录取成绩
     */
    @Getter
    @Setter
    private String enterScore;
    /**
     * 银行卡号
     */
    @Getter
    @Setter
    private String bankCardNo;
    /**
     * 入学前学校
     */
    @Getter
    @Setter
    private String oldSchoolName;
    /**
     * 入学时间
     */
    @Getter
    @Setter
    private Date oldSchoolDate;
    /**
     * 身份证件类型
     */
    @Getter
    @Setter
    private String identitycardType;
    /**
     * 个人标识码
     */
    @Getter
    @Setter
    private String pin;
    /**
     * 复式班学生所在班级
     */
    @Getter
    @Setter
    private String duplexClassGradeId;
    /**
     * 国家学籍号
     */
    @Getter
    @Setter
    private String stateCode;
    /**
     * 招生模式
     */
    @Getter
    @Setter
    private Integer recruitMode;
    /**
     * 学习属性 DM-XXMS
     */
    @Getter
    @Setter
    private Integer learnMode;
    /**
     * 联招合作类型
     */
    @Getter
    @Setter
    private Integer cooperateType;
    /**
     * 联招合作学校机构代码
     */
    @Getter
    @Setter
    private String cooperateSchoolCode;
    /**
     * 户籍所在地
     */
    @Getter
    @Setter
    private String registerPlace;
    /**
     * 户籍性质
     */
    @Getter
    @Setter
    private Integer registerType;
    /**
     * 是否低保
     */
    @Getter
    @Setter
    private Integer isLowAllowce;
    /**
     * 是否享受国家助学金
     */
    @Getter
    @Setter
    private Integer isEnjoyStateGrants;
    /**
     * 助学金月发标准（元）
     */
    @Getter
    @Setter
    private Double grantStandard;
    /**
     * 生源类别
     */
    @Getter
    @Setter
    private Integer sourceType;
    /**
     * 教学点名称
     */
    @Getter
    @Setter
    private String teachingPointsName;
    /**
     * 学生类别
     */
    @Getter
    @Setter
    private String studentCategory;
    /**
     *
     */
    @Getter
    @Setter
    private Integer compatriots;
    /**
     *
     */
    @Getter
    @Setter
    private String graduateSchool;
    @Getter
    @Setter
    @Column(length = 500)
    private String remark;
    @Getter
    @Setter
    @Column(length = 10)
    private String registerCode;
    @Getter
    @Setter
    @Column(length = 50)
    private String spellName;
    @Getter
    @Setter
    @Column(length = 100)
    private String homePlace;
    @Getter
    @Setter
    @Column(length = 10)
    private String lastDegree;
    @Getter
    @Setter
    @Column(length = 200)
    private String registerAddress;
    @Getter
    @Setter
    @Column(length = 10)
    private String bloodType;
    @Getter
    @Setter
    @Column(length = 10)
    private String examCode;
    @Getter
    @Setter
    @Column(length = 10)
    private String photoNo;
    @Getter
    @Setter
    private Double benefitMoney;
    @Getter
    @Setter
    @Column(length = 10)
    private String studentMode;
    @Getter
    @Setter
    @Column(length = 32)
    private String rewardSpecId;
    @Getter
    @Setter
    @Column(length = 10)
    private String schLengthType;
    @Getter
    @Setter
    private Integer isFamilySide;
    @Getter
    @Setter
    @Column(length = 50)
    private String studentQq;
    @Getter
    @Setter
    @Column(length = 50)
    private String oneCardNumber;
    @Getter
    @Setter
    @Column(length = 10)
    private String seatNumber;
    @Getter
    @Setter
    @Column(length = 50)
    private String englishName;
    @Getter
    @Setter
    @Column(length = 10)
    private String schoolinglen;
    @Getter
    @Setter
    @Column(length = 10)
    private String toschooltype;
    @Getter
    @Setter
    @Column(length = 10)
    private String country;
    @Getter
    @Setter
    @Column(length = 10)
    private String marriage;
    @Getter
    @Setter
    @Column(length = 10)
    private String nativeType;
    @Getter
    @Setter
    @Column(length = 10)
    private String localPoliceStation;
    @Getter
    @Setter
    @Column(length = 100)
    private String nowaddress;
    @Getter
    @Setter
    @Column(length = 10)
    private String homePostalcode;
    /**
     * source与sourceType同样代表学生来源 目前5.0与6.0 使用的是这个字段 这边先开放 后续在调整具体选择
     */
    @Getter
    @Setter
    private String source;

    @Getter
    @Setter
    @Column(length = 10)
    private String cooperateForm;
    @Getter
    @Setter
    @Column(length = 10)
    private String trainStation;
    @Getter
    @Setter
    @Column(length = 50)
    private String oldStudentCode;
    @Getter
    @Setter
    @Column(length = 50)
    private String familyMobile;
    @Getter
    @Setter
    @Column(length = 10)
    private String religion;
    @Getter
    @Setter
    @Column(length = 10)
    private String familyOrigin;
    @Getter
    @Setter
    @Column(length = 10)
    private String compulsoryedu;
    @Getter
    @Setter
    private Integer flowingPeople;
    @Getter
    @Setter
    private Date polityDate;
    @Getter
    @Setter
    @Column(length = 50)
    private String polityIntroducer;
    @Getter
    @Setter
    private Date partyDate;
    @Getter
    @Setter
    @Column(length = 50)
    private String partyIntroducer;
    @Getter
    @Setter
    @Column(length = 50)
    private String zkCode;
    @Getter
    @Setter
    private Double zkResult;
    @Getter
    @Setter
    @Column(length = 10)
    private String englishLevel;
    @Getter
    @Setter
    @Column(length = 10)
    private String computerLevel;
    @Getter
    @Setter
    @Transient
    private String specialty;
    @Getter
    @Setter
    private Integer isPreedu;
    @Getter
    @Setter
    @Column(length = 500)
    private String strong;
    @Getter
    @Setter
    private Integer registerInfo;
    @Getter
    @Setter
    @Column(length = 50)
    private String oldSchcode;
    @Getter
    @Setter
    @Column(length = 100)
    private String wlClassIntention;
    @Getter
    @Setter
    @Column(length = 100)
    private String formerClassTeacher;
    @Getter
    @Setter
    @Column(length = 100)
    private String formerClassLeader;
    @Getter
    @Setter
    private Double zkArtResult;
    @Getter
    @Setter
    private Double zkMathResult;
    @Getter
    @Setter
    private Double zkEnglishResult;
    @Getter
    @Setter
    private Double zkIntegratedResult;
    @Getter
    @Setter
    private Double zkPeResult;
    @Getter
    @Setter
    private Double zkExperimentResult;
    @Getter
    @Setter
    private Double zkComputerResult;
    @Getter
    @Setter
    @Column(length = 100)
    private String zkExtro;
    @Getter
    @Setter
    @Column(length = 1000)
    private String zkStrong;
    @Getter
    @Setter
    private Double rxArtResult;
    @Getter
    @Setter
    private Double rxMathResult;
    @Getter
    @Setter
    private Double rxEnglishResult;
    @Getter
    @Setter
    private Double rxIntegratedResult;
    @Getter
    @Setter
    private Double rxResult;
    @Getter
    @Setter
    @Column(length = 100)
    private String accountAttribution;
    @Getter
    @Setter
    @Column(length = 100)
    private String isLocalSchoolEnrollment;
    @Getter
    @Setter
    @Column(length = 100)
    private String isDuplexClass;
    @Getter
    @Setter
    @Column(length = 100)
    private String isRereading;
    @Getter
    @Setter
    @Column(length = 100)
    private String regularClass;
    @Getter
    @Setter
    private Integer age;
    @Getter
    @Setter
    @Column(length = 100)
    private String degreePlace;
    @Getter
    @Setter
    @Column(length = 10)
    private String disabilityType;
    @Getter
    @Setter
    @Column(length = 10)
    private String isBoarding;
    @Getter
    @Setter
    @Column(length = 10)
    private String isMigration;
    @Getter
    @Setter
    @Column(length = 10)
    private String isFallEnrollment;
    @Getter
    @Setter
    @Column(length = 10)
    private String fingerprint;
    @Getter
    @Setter
    @Column(length = 10)
    private String distance;
    @Getter
    @Setter
    @Column(length = 10)
    private String trafficWay;
    @Getter
    @Setter
    private Integer isNeedBus;
    @Getter
    @Setter
    private Integer isGovernmentBear;
    @Getter
    @Setter
    private Integer isNeedAssistance;
    @Getter
    @Setter
    private Integer isOrphan;
    @Getter
    @Setter
    private Integer isEnjoyAssistance;
    @Getter
    @Setter
    private Integer isMartyrChild;
    @Getter
    @Setter
    @Column(length = 10)
    private String identitycardValid;
    @Getter
    @Setter
    @Column(length = 10)
    private String urbanRegisterType;
    @Getter
    @Setter
    @Column(length = 10)
    private String planMode;
    @Getter
    @Setter
    @Column(length = 10)
    private String socialRecruitmentType;
    @Getter
    @Setter
    @Column(length = 10)
    private String rxqk;
    @Getter
    @Setter
    @Column(length = 10)
    private String isGetCertification;
    @Getter
    @Setter
    @Column(length = 10)
    private String isFiveHigherVocational;
    @Getter
    @Setter
    @Column(length = 10)
    private String belongContinents;
    @Getter
    @Setter
    @Column(length = 10)
    private String foreignerStudyTime;
    @Getter
    @Setter
    @Column(length = 10)
    private String isVocationalTechClass;
    @Getter
    @Setter
    @Column(length = 10)
    private String isRegularClass;
    @Getter
    @Setter
    @Column(length = 10)
    private String fingerprint2;
    @Getter
    @Setter
    @Column(length = 10)
    private String studentType;
    /**
     * 调入调出验证码
     */
    @Getter
    @Setter
    @Column(length = 10)
    private String verifyCode;
    @Getter
    @Setter
    @Column(length = 1000)
    private String rewardRemark;
    @Getter
    @Setter
    @Column(length = 100)
    private String registerStreet;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "ownerId", referencedColumnName = "id")
    private List<User> users;
}
