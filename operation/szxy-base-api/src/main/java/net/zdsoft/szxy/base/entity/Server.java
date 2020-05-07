package net.zdsoft.szxy.base.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 基础数据： 子系统
 * @author shenke
 * @since 2019/3/22 上午11:43
 */
@Data
@Entity
@Table(name = "base_server")
public class Server implements Serializable {

    @Id
    private Integer id;

    /**
     * 第三方应用申请时的UUID
     */
    private String systemId;
    /**
     * 应用名称
     */
    private String name;
    /**
     *  内部应用状态
     * @see net.zdsoft.szxy.base.enu.ServerStatus
     */
    private Integer status;
    /**
     * 产品编码 例：系统管理 eis_basedata
     */
    private String code;
    /**
     * 基础数据同步方式
     */
    private Integer baseSyncType;
    /**
     * 同步数据接口方式
     */
    private Integer interfaceType;
    /**
     * 功能页面
     */
    @Column(name = "capabilityurl")
    private String capabilityUrl;
    /**
     * 介绍页面
     */
    @Column(name = "introduceurl")
    private String introduceUrl;
    /**
     * 首页地址
     */
    private String indexUrl;
    /**
     * 联系电话
     */
    private String linkPhone;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 约定字符串
     */
    private String appoint;
    /**
     * 和passport对接时的VerifyKey
     */
    private String serverKey;
    /**
     * 协议
     */
    private String protocol;
    /**
     * 域名
     */
    private String domain;
    /**
     * 端口号
     */
    private Integer port;
    /**
     * 是否调用 passport
     *
     */
    private Integer isPassport;

    private String serverCode;
    /**
     * 对应passport的Sys_server_type的ID
     */
    private Integer serverTypeId;
    /**
     * 软删标记 0.正常 1.软删
     */
    private Integer isDeleted;
    private Integer eventSource;
    private String context;
    /**
     * 服务类型 1 、公司内置产品      2 、第三方AP
     */
    private Integer serverClass;
    private String secondDomain;
    /**
     * 适用类型(教师，家长，学生) 针对第三方AP，适用多个的话用，隔开
     */
    @Column(name = "usertype")
    private String userType;
    /**
     * 订阅类型 1、系统订阅 2、单位订阅个人授权 3、单位订阅个人免费
     */
    private Integer orderType;
    /**
     * 单位类型 不限　1教育局　２学校　适用多个的话用，隔开
     */
    @Column(name = "unittype")
    private String unitType;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 应用图标名称
     */
    private String icon;
    /**
     * 应用图标相对路径
     */
    private String iconUrl;
    /**
     * 通知登录url
     */
    private String verifyUrl;
    /**
     * 退出登录url
     */
    private String invalidateUrl;
    /**
     * 开发者ID
     */
    private String devId;
    /**
     * 创建时间
     */
    private Date creationTime;
    /**
     * 应用修改时间
     */
    private Date modifyTime;
    /**
     * 审核时间
     */
    private Date auditTime;
    /**
     * 上线时间
     */
    private Date onlineTime;
    /**
     * 申请时间
     */
    private Date applyTime;
    /**
     * 学段
     */
    private String sections;
    /**
     * 原sys_subsystem.id
     */
    private Integer subId;
    /**
     * 排序号
     */
    private Integer orderId;
    /**
     * 是否可见1、可见 0、不可见
     */
    private Integer isVisible;
    /**
     * 打开方式
     */
    private Integer openType;
    /**
     * 应用详情页地址
     */
    private String introductionPage;
}
