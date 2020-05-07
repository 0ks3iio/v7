package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author shenke
 * @since 2019/3/19 上午11:28
 */
@Table(name = "base_school")
@Entity
@Data
public class School implements Serializable {
    @Id
    private String id;

    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 学校英文名称
     */
    private String englishName;
    /**
     * 学校简称
     */
    private String shortName;
    /**
     * 学校统一编号
     */
    private String schoolCode;
    /**
     * 学校地址
     */
    private String address;
    /**
     * 统计用行政区划（标准国家行政区划码）
     */
    private String regionCode;
    /**
     * 学校校长
     */
    private String schoolmaster;
    /**
     * 党组织负责人
     */
    private String partyMaster;
    /**
     * 创建日期
     */
    private Date buildDate;
    /**
     * 校庆日
     */
    private String anniversary;
    /**
     * 学校办别码
     * 微代码 DM-XXBB
     */
    private Integer runSchoolType;
    /**
     * 学校类别码
     * 微代码 DM-XXLB
     */
    private String schoolType;
    /**
     * 地区类别码
     * 微代码 DM-SZDQLB
     */
    private Integer regionType;
    /**
     * 地区经济属性码
     * 微代码 DM-SZDJJSX
     */
    private Integer regionEconomy;
    /**
     * 地区民族属性码
     * 微代码 DM-XDMZ
     */
    private Integer regionNation;
    /**
     * 小学规定年制
     *
     */
    private Integer gradeYear;
    /**
     * 小学入学年龄
     */
    private Integer gradeAge;
    /**
     * 初中规定学制
     */
    private Integer juniorYear;
    /**
     * 初中入学年龄
     */
    private Integer juniorAge;
    /**
     * 高中规定年制
     */
    private Integer seniorYear;
    /**
     * 幼儿园学制
     */
    private Integer infantYear;
    /**
     * 幼儿园入学年龄
     */
    private Integer infantAge;
    /**
     * 主教学语言码
     * 微代码 DM-ZGYZ
     */
    private String primaryLang;
    /**
     * 辅教育语言码
     * 微代码 DM-ZGYZ
     */
    private String secondaryLang;
    /**
     * 招生区域
     */
    private String recruitRegion;
    /**
     * 组织机构代码
     */
    private String organizationCode;
    /**
     * 历史沿革
     */
    private String introduction;
    /**
     * 学校面积
     */
    private String area;

    @Column(updatable = false)
    private String serialNumber;
    /**
     * 学段
     * 根据base_schtype_section学校类别码生成
     */
    private String sections;
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
     * 软删除
     */
    private int isDeleted;
    /**
     * 时间来源
     */
    private int eventSource;
    /**
     * 学区
     */
    private String schoolDistrictId;
    /**
     * 学校主管部门
     */
    private String governor;
    /**
     * 邮政编码
     */
    @Column(name = "postalcode")
    private String postalCode;
    /**
     * 联系电话
     */
    private String linkPhone;
    /**
     * 传真电话
     */
    private String fax;
    /**
     * 电子信箱
     */
    private String email;
    /**
     * 主页地址
     */
    private String homepage;
    /**
     * 重点级别
     */
    private Integer emphases;
    /**
     * 是否农名工子女定点学校
     */
    private Integer isBoorish;
    /**
     * 示范级别
     */
    private Integer demonstration;
    /**
     * 学校特色
     */
    private String feature;
    /**
     * 是否公办民助
     */
    private Integer publicAssist;
    /**
     * 统计用学校性质
     */
    private String schoolPropStat;
    /**
     * 学校性质分组
     */
    private String schoolTypeGroup;
    /**
     * 台帐用学校类型码
     */
    private String ledgerSchoolType;
    /**
     * 学校（机构）属地管理教育行政部门
     */
    private String regionManageDept;
    /**
     * 学校（机构）属地管理教育行政部门代码
     */
    private String regionManageDeptCode;
    /**
     * 学校地址码
     */
    private String addressCode;
    /**
     * 法人登记证号
     */
    private String legaRegistrationNumber;
    /**
     * 学校产权土地证号
     */
    private String landCertificateNo;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 填表人电子邮箱
     */
    private String fillEmail;
    /**
     * 学校统计人员姓名
     */
    private String statStuffName;
    /**
     * 学校统计人员联系方式
     */
    private String statStuffContact;
    /**
     * 是否学区长学校
     */
    private String isSchDistrictChief;
    /**
     * 本地自建管理行政区域(业务用行政区划码)
     */
    private String statRegionCode;
    /**
     * 幼儿园级别
     */
    private String kgLevel;
    /**
     * 手机号码
     */
    private String mobilePhone;
    /**
     * 区域属性码
     */
    private String regionPropertyCode;
    /**
     * 性质类别码
     */
    private String natureType;
    /**
     * 是否独立设置少数民族学校
     */
    private Integer isMinoritySchool;
    /**
     * 学校举办者名称
     */
    private String schoolSetupName;
    /**
     * 本地自建(特色)统计区域(特色行政区划码)
     */
    private String specialRegionCode;
    /**
     * 建筑面积
     */
    private Double builtupArea;
    /**
     * 绿化面积
     */
    private Double greenArea;
    /**
     * 运动场面积
     */
    private Double sportsArea;
    /**
     * 法人
     */
    private String legalPerson;
    /**
     * 行业
     */
    private String industry;
    /**
     * 备注
     */
    private String remark;



}
