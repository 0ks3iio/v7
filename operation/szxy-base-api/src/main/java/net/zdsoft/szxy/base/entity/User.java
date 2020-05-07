package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/19 上午11:21
 */
@Data
@Entity
@Table(name = "base_user")
public class User implements Serializable {

    @Id
    private String id;
    /**
     * 单位ID
     */
    private String unitId;
    /**
     * passport 生成之后由IM使用的
     */
    private Integer sequence;
    /**
     * passport sys_account Id
     */
    private String accountId;
    /**
     * 对应base_teacher 、base_student、 base_family
     * 的主键
     */
    @Column
    private String ownerId;
    /**
     * 用户类型 教师、家长、学生
     * @see net.zdsoft.szxy.base.enu.UserOwnerTypeCode
     */
    private Integer ownerType;
    /**
     * 用户名
     */
    @Column(unique = true)
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 用户状态
     * @see net.zdsoft.szxy.base.enu.UserStateCode
     */
    private Integer userState;
    /**
     * 用户所属类型
     * @see net.zdsoft.szxy.base.enu.UserTypeCode
     */
    private Integer userType;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 行政区划
     */
    private String regionCode;
    /**
     * 排序号
     */
    private Integer displayOrder;
    /**
     * 性别
     * @see net.zdsoft.szxy.base.enu.UserSexCode
     */
    private Integer sex;
    /**
     * 扣费号码
     */
    private String chargeNumber;
    /**
     * 扣费类型
     */
    private Integer chargeNumberType;
    /**
     * 订购状态
     */
    private Integer orderStatus;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * 软删标记
     */
    private Integer isDeleted;
    /**
     * 来源
     */
    private Integer eventSource;
    /**
     * 密码
     */
    private String password;
    /**
     *
     */
    private Integer subsystemAdmin;
    /**
     * 部门ID
     */
    private String deptId;
    /**
     *
     */
    private Integer roleType;
    private Integer userRole;
    /**
     * 班级ID
     */
    private String classId;
    /**
     *
     */
    private Integer iconIndex;
    private Integer authProperty;
    private String signature;
    private Date birthday;
    private Integer enrollYear;
    /**
     * 主页
     */
    private String webpage;
    private String summary;
    /**
     * 手机号
     */
    private String mobilePhone;
    /**
     * 地址
     */
    private String address;

    private String zipCode;
    /**
     * 办公室电话
     */
    private String officeTel;
    /**
     * 家庭电话
     */
    private String homeTel;
    private String qq;
    private String msn;
    private String orgVisual;
    /**
     * 背景图片
     */
    private String bgImg;
    @Column(length = 2000)
    private String pinyinAll;
    /**
     * 身份证件号码
     */
    private String identityCard;
    /**
     * 身份证件类型
     */
    private String identityType;
    /**
     * 钉钉ID
     */
    private String dingdingId;
    /**
     * 到期时间
     */
    private Date expireDate;
    private String userIdCode;

}