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
 * @since 2019/3/19 下午6:08
 */
@EqualsAndHashCode(exclude = "users")
@ToString(exclude = "users")
@Entity
@Table(name = "base_family")
public class Family implements Serializable {

    @Id
    private @Getter @Setter  String id;
    /**
     * 学生id
     */
    @Column(updatable=false)
    private @Getter @Setter  String studentId;
    /**
     * 学校id
     */
    private @Getter @Setter  String schoolId;
    /**
     * 关系码
     */
    private @Getter @Setter String relation;
    /**
     * 真实姓名
     */
    private @Getter @Setter  String realName;
    /**
     * 是否监护人
     * @see net.zdsoft.szxy.base.enu.FamilyGuardianCode
     */
    private @Getter @Setter  Integer isGuardian;
    /**
     * 单位名称
     */
    private @Getter @Setter  String company;
    /**
     * 职务
     */
    private @Getter @Setter  String duty;
    /**
     * 联系电话
     */
    private @Getter @Setter  String linkPhone;
    /**
     * 职业码
     */
    private @Getter @Setter  String workCode;
    /**
     * 专业技术职务码
     */
    private @Getter @Setter  String professionCode;
    /**
     * 职务级别码
     */
    private @Getter @Setter  String dutyLevel;
    /**
     * 政治面貌码
     */
    private @Getter @Setter  String politicalStatus;
    /**
     * 婚姻状况码
     */
    private @Getter @Setter  String maritalStatus;
    /**
     * 侨居地码
     */
    private @Getter @Setter  String emigrationPlace;
    /**
     * 出生日期
     */
    private @Getter @Setter  Date birthday;
    /**
     * 文化程度
     */
    private @Getter @Setter  String culture;
    /**
     * 身份证
     */
    private @Getter @Setter  String identityCard;
    /**
     * 民族
     */
    private @Getter @Setter  String nation;
    /**
     * 个人主页
     */
    private @Getter @Setter  String homepage;
    /**
     * 备注
     */
    private @Getter @Setter  String remark;
    /**
     * 邮政编码
     */
    private @Getter @Setter  String postalcode;
    /**
     * 联系地址
     */
    private @Getter @Setter  String linkAddress;
    /**
     * 邮箱
     */
    private @Getter @Setter  String email;
    /**
     * 手机号码
     */
    private @Getter @Setter  String mobilePhone;
    /**
     * 办公电话
     */
    private @Getter @Setter  String officeTel;
    /**
     * 性别
     */
    private @Getter @Setter  Integer sex;
    /**
     * 行政区划
     */
    private @Getter @Setter  String regionCode;
    /**
     * 扣费号码
     */
    private @Getter @Setter  String chargeNumber;
    /**
     * 扣费类型
     */
    private @Getter @Setter  Integer chargeNumberType;
    /**
     * 是否离校
     * @see net.zdsoft.szxy.base.enu.StudentLeavingSchoolCode
     */
    private @Getter @Setter  Integer isLeaveSchool;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable=false)
    private @Getter @Setter  Date creationTime;
    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private @Getter @Setter  Date modifyTime;
    /**
     * 软删除
     */
    private @Getter @Setter  Integer isDeleted;
    /**
     * 事件来源
     * @see net.zdsoft.szxy.base.enu.EventSourceCode
     */
    private @Getter @Setter  Integer eventSource;
    /**
     * 是否接收短信
     */
    private @Getter @Setter  String receiveInfomation;
    /**
     * 是否智能机
     */
    private @Getter @Setter  String smartMobilePhone;
    /**
     * 证件类型
     */
    private @Getter @Setter  String identitycardType;
    /**
     * 健康状况
     */
    private @Getter @Setter  String health;
    /**
     * 户口所在地
     */
    private @Getter @Setter  String registerPlace;
    /**
     * 关系说明
     */
    private @Getter @Setter  String relationRemark;
    /**
     * 订单状态
     */
    private @Getter @Setter  Integer openUserStatus;

    private @Getter @Setter  Long sequenceIntId;


    @Setter
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "ownerId", referencedColumnName = "id")
    private List<User> users;
}
