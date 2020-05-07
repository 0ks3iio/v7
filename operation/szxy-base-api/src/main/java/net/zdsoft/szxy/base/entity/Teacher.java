package net.zdsoft.szxy.base.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.zdsoft.szxy.base.enu.TeacherLeavingSchoolCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author shenke
 * @since 2019/3/19 上午11:45
 */
@ToString(exclude = "users")
@EqualsAndHashCode(exclude = "users")
@Entity
@Table(name = "base_teacher")
public class Teacher implements Serializable {

    @Id
    @Column(length = 32, unique = true, nullable = false)
    private @Getter @Setter String id;
    /**
     * 所在部门id
     */
    private @Getter @Setter  String deptId;
    /**
     * 所在单位 id
     */
    private @Getter @Setter  String unitId;
    /**
     * 编号
     */
    @Column(length = 32)
    private @Getter @Setter  String teacherCode;
    /**
     * 姓名
     */
    private @Getter @Setter  String teacherName;
    /**
     * 曾用名
     */
    @Column(length = 32)
    private @Getter @Setter  String oldName;
    /**
     * 性别 DM-XB
     */
    private @Getter @Setter  Integer sex;
    /**
     * 出生日期
     */
    private @Getter @Setter  Date birthday;
    /**
     * 籍贯 regionCode
     */

    private @Getter @Setter  String nativePlace;
    /**
     * 民族 DM-MZ
     */

    private @Getter @Setter  String nation;
    /**
     * 政治面貌 DM-ZZMM
     */

    private @Getter @Setter  String polity;
    /**
     * 入党时间
     */
    private @Getter @Setter  Date polityJoin;
    /**
     * 学历 DM-XL
     */

    private @Getter @Setter  String academicQualification;
    /**
     * 毕业时间
     */
    private @Getter @Setter  Date graduateTime;
    /**
     * 毕业学校
     */
    @Column(length = 100)
    private @Getter @Setter  String graduateSchool;
    /**
     * 毕业专业
     */
    @Column(length = 100)
    private @Getter @Setter  String major;
    /**
     * 参加工作时间
     */
    private @Getter @Setter  Date workDate;
    /**
     * 在职标志 DM-JSZZBJ
     */

    private @Getter @Setter  String incumbencySign;
    /**
     * 职务 职务
     */

    private @Getter @Setter  String title;
    /**
     * 证件类型 DM-SFZJLX
     */

    private @Getter @Setter  String identityType;
    /**
     * 身份证件号码
     */

    private @Getter @Setter  String identityCard;
    /**
     * 手机号码
     */
    @Column(length = 50)
    private @Getter @Setter  String mobilePhone;
    /**
     * 办公室电话
     */
    @Column(length = 50)
    private @Getter @Setter  String officeTel;
    /**
     * 户口类别 DM-HKXZ
     */

    private @Getter @Setter  Integer registerType;
    /**
     * 户口所在地
     */

    private @Getter @Setter  String registerPlace;
    /**
     * 电子信箱
     */
    @Column(length = 100)
    private @Getter @Setter  String email;
    /**
     * 备注
     */
    @Column(length = 500)
    private @Getter @Setter  String remark;
    /**
     * 卡号
     */
    @Column(length = 50)
    private @Getter @Setter  String cardNumber;
    /**
     * 个人主页
     */
    @Column(length = 100)
    private @Getter @Setter  String homepage;
    /**
     * 联系电话
     */
    @Column(length = 50)
    private @Getter @Setter  String linkPhone;
    /**
     * 联系地址
     */
    @Column(length = 100)
    private @Getter @Setter  String linkAddress;
    /**
     * 邮政编码
     */
    @Column(length = 50)
    private @Getter @Setter  String postalcode;
    /**
     * 行政区划码
     */
    @Column(length = 50)
    private @Getter @Setter  String regionCode;
    /**
     * 排序号
     */
    private @Getter @Setter  Integer displayOrder;
    /**
     * 卡类型
     */
    private @Getter @Setter  Integer chargeNumberType;
    /**
     * 卡号
     */
    @Column(length = 50)
    private @Getter @Setter  String chargeNumber;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false, nullable = false)
    private @Getter @Setter  Date creationTime;
    /**
     * 修改时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private @Getter @Setter  Date modifyTime;
    /**
     * 软删标记
     */
    private @Getter @Setter  Integer isDeleted;
    /**
     * 数据来源标记
     */
    private @Getter @Setter  Integer eventSource;
    /**
     * 照片目录
     */
    @Column(length = 32)
    private @Getter @Setter  String dirId;
    /**
     * 照片相对路径
     */
    @Column(length = 100)
    private @Getter @Setter  String filePath;
    /**
     * 学段
     */
    private @Getter @Setter  Integer section;
    /**
     * 国家/地区
     */

    private @Getter @Setter  String country;
    /**
     * 是否华侨
     */

    private @Getter @Setter  String returnedChinese;
    /**
     * 编制类型 DM-BZLX
     */

    private @Getter @Setter  String weaveType;
    /**
     * 是否双师型， 不再使用
     */
    @Deprecated
    private @Getter @Setter  Integer multiTitle;
    /**
     * 授课状态 DM-JSSKZT
     */

    private @Getter @Setter  String teachStatus;
    /**
     * 院系ID
     */
    @Column(length = 32)
    private @Getter @Setter  String instituteId;
    /**
     * 参加教育工作年月
     */
    private @Getter @Setter  Date workTeachDate;
    /**
     * 原有学历（全日制）
     */

    private @Getter @Setter  String oldAcademicQualification;
    /**
     * 专业技术评审年月
     */
    private @Getter @Setter  Date specTechnicalDutyDate;
    /**
     * 家庭地址
     */
    @Column(length = 100)
    private @Getter @Setter  String homeAddress;
    /**
     * 一卡通
     */
    @Column(length = 100)
    private @Getter @Setter  String generalCard;
    /**
     * 专业技术职务
     */

    private @Getter @Setter  String specTechnicalDuty;
    /**
     * 最高学位
     */
    @Column(length = 3)
    private @Getter @Setter  String highestDegree;
    /**
     * 获得最高学历的院校或机构
     */
    @Column(length = 100)
    private @Getter @Setter  String highestDegreeInstitutions;
    /**
     * 最高学历 DM-XL
     */
    private @Getter @Setter  String highestDiploma;
    /**
     * 最高学历的院系或机构
     */
    private @Getter @Setter  String highestDiplomaInstitutions;
    /**
     * 合同签订情况 DM-QDHTQK
     */
    private @Getter @Setter  String laborContractSituation;
    /**
     * 无限一金情况
     */
    private @Getter @Setter  String fiveInsurancePayments;
    /**
     * 教师资格证号码
     */
    private @Getter @Setter  String certificationCode;
    /**
     * 近一年教师接受培训状况
     */
    private @Getter @Setter  String oneYearTraining;
    /**
     * 主要任课课程
     */
    private @Getter @Setter  String mainCourse;
    /**
     * 是否接受过特殊教育专业培养培训
     */
    private @Getter @Setter  String specialEduTraining;
    /**
     * 是否学前教育专业
     */
    private @Getter @Setter  String preschoolSpecialty;
    /**
     * 任课状况
     */
    private @Getter @Setter  String courseSituation;
    /**
     * 任课学科类别
     */
    private @Getter @Setter  String courseSubjectCategory;
    /**
     * 是否双教师
     */
    private @Getter @Setter  String isDoubleCertification;
    /**
     * 获得的其他职业资格证书
     */
    private @Getter @Setter  String otherCertification;
    /**
     * 获得的其他职业资格证书等级
     */
    private @Getter @Setter  String otherCertificationLevel;
    /**
     * 企业工作（实践）时长
     */
    private @Getter @Setter  String enterpriseWorkTime;
    /**
     * 导师类别
     */
    private @Getter @Setter  String tutorType;
    /**
     * 现主要从事学科领域
     */
    private @Getter @Setter  String subjectAreas;
    /**
     * 专家类别
     */
    private @Getter @Setter  String expertType;
    /**
     * 重要科研教学获奖情况
     */
    private @Getter @Setter  String researchAwards;
    /**
     * 获得海外学位情况
     */
    private @Getter @Setter  String overseasDegree;
    /**
     * 海外研修经历
     */
    private @Getter @Setter  String overseasTraining;
    /**
     * 学术结构
     */
    private @Getter @Setter  String academicStructure;
    /**
     * 个人信息标识码
     */
    private @Getter @Setter  String infoPin;
    /**
     * 职务 学校的：DM-XXZW 教育局：DM-JYJZW
     */
    private @Getter @Setter  String duty;
    /**
     * 所属校区（附设校区）
     */
    @Column(length = 32, name = "subschool_id")
    private @Getter @Setter  String subSchoolId;
    /**
     * 骨干教师等级 DM-TZ-GGJSDJ
     */
    private @Getter @Setter  String backboneTeacherLevel;
    /**
     * 岗位类型分组 DM-TZ-GWLBFZ
     */
    private @Getter @Setter  String jobGroup;
    /**
     * 党内职务
     */
    private @Getter @Setter  String partyPositions;
    /**
     * 在职学历 DM-ZZXL
     */
    private @Getter @Setter  String onJobDiploma;
    /**
     * 任现职务级别时间
     */
    private @Getter @Setter  Date presentDutyLevelDate;
    /**
     * 教职工类别 DM-JZGLB
     */
    private @Getter @Setter  String employeeType;
    /**
     * 婚姻状况
     */
    private @Getter @Setter  String maritalStatus;
    /**
     * 身份
     */
    @Column(length = 2)
    private @Getter @Setter  String identity;
    /**
     * 档案身份
     */
    private @Getter @Setter  String archivesIdentity;
    /**
     * 转正工作时间
     */
    private @Getter @Setter  Date positiveTime;
    /**
     * 加入本市工作时间
     */
    private @Getter @Setter  Date thisCityWorkDate;
    /**
     * 加入当前单位工作时间
     */
    private @Getter @Setter  Date thisUnitWorkDate;
    /**
     * 离退休时间
     */
    private @Getter @Setter  Date retiredTime;
    /**
     * 任教年级
     */
    private @Getter @Setter  String teachGrade;
    /**
     * 全日制普通学历
     */
    private @Getter @Setter  String fullTimeEducation;
    /**
     * 是否有统计从业资格证
     */
    private @Getter @Setter  String isHaveCountCertification;
    /**
     * 统计从业资格证号
     */
    @Column(length = 100)
    private @Getter @Setter  String countCertificationCode;
    /**
     * 是否学前教育专业毕业
     */
    private @Getter @Setter  String isPreeEduGraduate;
    /**
     * 是否参加过交流
     */
    private @Getter @Setter  String joinCommunication;
    /**
     * 是否参加过支教
     */
    private @Getter @Setter  String joinTeaching;
    /**
     * 连续工龄
     */
    @Column(length = 3)
    private @Getter @Setter  Integer continuationSeniority;
    /**
     * 是否兼岗
     */
    @Column(length = 1)
    private @Getter @Setter  String partTimePosition;
    /**
     * 是否在编 DM-BOOLEAN
     */
    private @Getter @Setter  String inPreparation;
    /**
     * 是否属于免费师范生 DM-MFSFS
     */
    private @Getter @Setter  String freeNormal;
    /**
     * 是否特岗教师 DM-TGLS
     */
    private @Getter @Setter  String specialTeacher;
    /**
     * 教师资格证种类 DM-JSZGZZL
     */
    private @Getter @Setter  String certificationType;
    /**
     * 编制单位ID
     */
    private @Getter @Setter  String weaveUnitId;
    /**
     * 普通话等级 DM-PTHDJ
     */
    @Column(name = "putonghua_grade")
    private @Getter @Setter  String mandarinLevel;
    /**
     * 主要任课学段 DM-ZYRKXD
     */
    private @Getter @Setter  String learningPeriod;
    /**
     * 是否全日制师范类专业毕业
     */
    private @Getter @Setter  String normalGraduated;
    /**
     * 从事教学工作时间（结束时间）
     */
    private @Getter @Setter  Date educationWorkDate;
    /**
     * 工作岗位 DM-GZGW
     */
    private @Getter @Setter  String job;
    /**
     * 是否特殊教育专业毕业
     */
    private @Getter @Setter  String isSpecialEduGraduate;
    /**
     * 是否聘任制教师
     */
    private @Getter @Setter  String isAppointmentTeacher;
    /**
     * 是否授课
     */
    private @Getter @Setter  String isGiveLessons;
    /**
     * 是否双师型 废弃
     */
    @Deprecated
    private @Getter @Setter  String isDoubleTeachers;
    /**
     * 分科情况
     */
    private @Getter @Setter  String subjectBranch;
    /**
     * 分科情况大类
     */
    private @Getter @Setter  String subjectBranchGroup;
    /**
     * 不授课原因
     */
    private @Getter @Setter  String notGivelessonReason;
    /**
     * 年龄
     */
    private @Getter @Setter  Integer age;
    /**
     * 专业技术职务（新标准）
     */
    private @Getter @Setter  String dutyNew;
    /**
     * 教师离职标记
     * @see TeacherLeavingSchoolCode
     */
    private @Getter @Setter  String isLeaveSchool;
    /**
     * 教师当前状态
     * 中小学：  DM-DC-JSYDLB 中职：DM-DC-JSYDLBZZ
     */
    private @Getter @Setter  String nowState;
    /**
     * 档案号
     */
    private @Getter @Setter  String fileNumber;
    /**
     * 其他职务
     */
    private @Getter @Setter  String otherDuty;
    /**
     * 任职时间
     */
    private @Getter @Setter  Date dutyDate;
    /**
     * 职务级别 DM-ZWJB
     */
    private @Getter @Setter  String dutyLevel;
    /**
     * 其他职务级别
     */
    private @Getter @Setter  String otherDutyLevel;
    /**
     * 其他职务
     */
    private @Getter @Setter  String dutyEx;
    /**
     * 其他职务所在单位
     */
    private @Getter @Setter  String dutyExUnit;
    /**
     * 其他职务任职时间
     */
    private @Getter @Setter  Date dutyExDate;
    /**
     * 岗位级别 DM-GLG / DM-PRGW DM-GQG
     */
    private @Getter @Setter  String jobLevel;
    /**
     * 其他岗位级别 DM-TZ-GWDJ
     */
    private @Getter @Setter  String otherJobLevel;
    /**
     * 工龄中断起始日期
     */
    private @Getter @Setter  Date interruptionStartDate;
    /**
     * 工龄中断结束日期
     */
    private @Getter @Setter  Date interruptionEndDate;
    /**
     * 视作参加工作时间
     */
    private @Getter @Setter  Date reallyWorkDate;
    /**
     * 是否双肩挑
     */
    private @Getter @Setter  String doubleShoulder;
    /**
     * 月平均工资
     */
    private @Getter @Setter  Integer salaryLevel;
    /**
     * 进入编制单位时间
     */
    private @Getter @Setter  Date weaveUnitDate;
    /**
     * 进入编制单位形式
     */
    private @Getter @Setter  String weaveUnitType;
    /**
     * 工作所在单位时间
     */
    private @Getter @Setter  Date workUnitDate;
    /**
     * 工作所在单位形式
     */
    private @Getter @Setter  String workUnitType;
    /**
     * 聘用合同到期时间
     */
    private @Getter @Setter  Date contractFinishDate;
    /**
     * 工作证号
     */
    private @Getter @Setter  String workNumber;
    /**
     * 签订合同情况 DM-QDHTQK
     */
    private @Getter @Setter  String signContract;
    /**
     * 近三年专任教师接受培训情况
     */
    private @Getter @Setter  String threeYearsTraining;
    /**
     * 隐藏手机号
     */
    private @Getter @Setter  Integer hidePhone;

    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "ownerId", referencedColumnName = "id")
    private List<User> users;


}
