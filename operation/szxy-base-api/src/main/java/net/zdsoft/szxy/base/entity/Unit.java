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
 * @since 2019/3/18 下午2:55
 */
@Entity
@Table(name = "base_unit")
@Data
public class Unit implements Serializable {

    @Id
    private String id;

    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 单位分类
     * 微代码 DM-DWFL
     */
    private Integer unitClass;
    /**
     * 单位类型
     * 微代码 DM-DWLX
     */
    private Integer unitType;
    /**
     * 行政级别
     */
    private Integer regionLevel;
    /**
     * 行政区划
     */
    private String regionCode;

    @Column(updatable = false)
    private String serialNumber;
    /**
     * 二级域名
     */
    private String secondLevelDomain;
    /**
     * 上级单位ID
     */
    private String parentId;
    /**
     * 排序号
     */
    private String displayOrder;
    /**
     * 邮政编码
     */
    @Column(name = "postalcode")
    private String postalCode;
    /**
     * 邮件
     */
    private String email;
    /**
     * 传真
     */
    private String fax;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 联系电话
     */
    private String linkPhone;
    /**
     * 手机号码
     */
    private String mobilePhone;
    /**
     * 主页地址
     */
    private String homepage;
    /**
     * 地址
     */
    private String address;
    /**
     * 是否允许教师发送短信
     */
    private Integer isTeacherSms;
    /**
     * 是否留言短信发送
     */
    private Integer isGuestbookSms;
    /**
     * 剩余短信数
     */
    private Integer balance;
    /**
     * 费用类型
     */
    private Integer feeType;
    /**
     * 教师人数限制
     */
    private Integer limitTeacher;
    /**
     * 是否允许自由发送短信
     */
    private Integer isSmsFree;
    /**
     * 创建日期
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;
    /**
     * 变更时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;
    /**
     * 软删标记
     */
    private Integer isDeleted;
    /**
     * 时间来源
     */
    private Integer eventSource;
    /**
     * 单位编号
     */
    private String unionCode;
    /**
     * 注册码
     */
    private String pollCode;
    /**
     * 单位状态 DM-DWZT
     */
    private Integer unitState;
    /**
     * 单位类型
     * TODO
     */
    private Integer useType;
    /**
     * 是否授权
     */
    private Integer authorized;
    /**
     * 单位使用类型
     */
    private String unitUseType;
    /**
     * 分区号
     */
    private Integer unitPartitionNum;
    /**
     * 分级显示
     */
    private String sha1;
    /**
     * im数据更新值
     */
    private Integer orgVersion;
    /**
     * DM-TZ-DWLX
     */
    private String orgVisual;
    /**
     * 单位的教育类型
     */
    private String unitEducationType;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 单位负责人
     */
    private String unitHeader;
    /**
     * 单位组织机构码
     */
    private String organizationCode;
    /**
     * 单位性质
     */
    private String unitProperty;
    /**
     * 学校办别码 DM-XXBB
     */
    private Integer runSchoolType;
    /**
     * 学校类别码 DM-XXLB
     */
    private String schoolType;
    /**
     * 顶级单位ID
     */
    private String rootUnitId;
    /**
     * 备注
     */
    private String remark;
}