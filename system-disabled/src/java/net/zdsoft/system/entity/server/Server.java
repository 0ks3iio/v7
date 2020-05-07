package net.zdsoft.system.entity.server;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_server")
public class Server extends BaseEntity<Integer> {
    private static final long serialVersionUID = 1L;

    private String systemId;

    private String name;// 应用名称
    private Integer status;// 内部应用状态（停用（0）、上线（1））第三方应用状态（停用（0）、上线（1）、下线（2）、未提交（3）、审核中（4）、未通过（5）)
    private String code;
    private Integer baseSyncType;
    private Integer interfaceType;
    private String capabilityurl;
    private String introduceurl;
    private String indexUrl;
    private String linkPhone;
    private String linkMan;
    private String appoint;
    private String serverKey;
    private String protocol;
    private String domain;
    private Integer port;
    private Integer isPassport;
    private String serverCode;
    private Integer serverTypeId;
    private Integer isDeleted;// 软删标记 0.正常 1.软删
    private Integer eventSource;
    private String context;
    private Integer serverClass;// 服务类型 1 、公司内置产品      2 、第三方AP
    private String secondDomain;
    private String usertype;// 适用类型(教师，家长，学生) 针对第三方AP，适用多个的话用，隔开
    private Integer orderType;// 订阅类型 1、系统订阅 2、单位订阅个人授权 3、单位订阅个人免费
    private String unittype;// 单位类型 不限　1教育局　２学校　适用多个的话用，隔开
    private String description;// 应用描述
    private String icon;// 应用图标名称
    private String iconUrl;// 应用图标相对路径
    private String verifyUrl;// 通知登录url
    private String invalidateUrl;// 退出登录url
    private String devId;// 开发者ID
    private Date creationTime;// 创建时间
    private Date modifyTime;// 应用修改时间
    private Date auditTime;// 审核时间
    private Date onlineTime;// 上线时间
    private Date applyTime;// 申请时间
    private String sections;// 学段
    private Integer subId;// 原sys_subsystem.id
    private Integer orderId; // 排序号
    private Integer isVisible;// 是否可见1、可见 0、不可见
    private Integer openType;// 打开方式

    private String introductionPage; // 应用详情页地址

    @Transient
    private String devName;// 开发者姓名
    @Transient
    private String statusName;// 状态名
    @Transient
    private String statusColor;// 状态颜色
    @Transient
    private String timeStr;// 时间的字符串形式
    @Transient
    private String fullIcon;// 完整路径
    @Transient
    private String appSource;// 应用来源
    @Transient
    private String[] unitTypeArray;// 单位类型数组
    @Transient
    private String[] userTypeArray;// 用户类型数组
    @Transient
    private String[] sectionsArray;// 学段类型数组
    @Transient
    private String userTypeName;// 适用对象
    @Transient
    private String orderTypeName;// 订阅类型
    @Transient
    private String unitTypeName;// 单位类型
    @Transient
    private String sectionsName;// 学段

    @Transient
    private Integer subscribeCount; // 订阅数量
    @Transient
    private String serverRegionId;
    /**
     * 状态：启用
     */
    public static final int STATUS_TURNON = 1;

    /**
     * 内部server
     */
    public static final int SERVER_INNER = 1;

    /**
     * 外部server
     */
    public static final int SERVER_OUTER = 2;

    public String getUrl() {
        // return "http://192.168.22.13:96/eis";
        if (StringUtils.isEmpty(context)) {
            return protocol + "://" + domain + ":" + port;
        }
        else {
            if (context.substring(0, 1).equals("/")) {
                return protocol + "://" + domain + ":" + port + context;
            }
            else {
                return protocol + "://" + domain + ":" + port + "/" + context;
            }
        }
    }

    public String getUrlIndex(boolean isSecondUrl) {
        if (StringUtils.startsWith(indexUrl, "http")) {
            return indexUrl;
        }
        if (StringUtils.isNotBlank(indexUrl) && !StringUtils.equals("/", indexUrl)) {
            return isSecondUrl ? getSecondUrl() : getUrl() /* + "/" */+ indexUrl;
        }
        else {
            return isSecondUrl ? getSecondUrl() : getUrl();
        }
    }

    public String getSecondUrl() {
        if (StringUtils.isEmpty(context)) {
            return protocol + "://" + secondDomain + ":" + port;
        }
        else {
            if (context.substring(0, 1).equals("/")) {
                return protocol + "://" + secondDomain + ":" + port + context;
            }
            else {
                return protocol + "://" + secondDomain + ":" + port + "/" + context;
            }
        }

    }

    public String getServerRegionId() {
        return serverRegionId;
    }

    public void setServerRegionId(String serverRegionId) {
        this.serverRegionId = serverRegionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getBaseSyncType() {
        return baseSyncType;
    }

    public void setBaseSyncType(Integer baseSyncType) {
        this.baseSyncType = baseSyncType;
    }

    public Integer getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Integer interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getCapabilityurl() {
        return capabilityurl;
    }

    public void setCapabilityurl(String capabilityurl) {
        this.capabilityurl = capabilityurl;
    }

    public String getIntroduceurl() {
        return introduceurl;
    }

    public void setIntroduceurl(String introduceurl) {
        this.introduceurl = introduceurl;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getAppoint() {
        return appoint;
    }

    public void setAppoint(String appoint) {
        this.appoint = appoint;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getIsPassport() {
        return isPassport;
    }

    public void setIsPassport(Integer isPassport) {
        this.isPassport = isPassport;
    }

    public String getServerCode() {
        return serverCode;
    }

    public void setServerCode(String serverCode) {
        this.serverCode = serverCode;
    }

    public Integer getServerTypeId() {
        return serverTypeId;
    }

    public void setServerTypeId(Integer serverTypeId) {
        this.serverTypeId = serverTypeId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getEventSource() {
    	if(eventSource == null)
    		eventSource = 1;
        return eventSource;
    }

    public void setEventSource(Integer eventSource) {
        this.eventSource = eventSource;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public Integer getServerClass() {
        return serverClass;
    }

    public void setServerClass(Integer serverClass) {
        this.serverClass = serverClass;
    }

    public String getSecondDomain() {
        return secondDomain;
    }

    public void setSecondDomain(String secondDomain) {
        this.secondDomain = secondDomain;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getUnittype() {
        return unittype;
    }

    public void setUnittype(String unittype) {
        this.unittype = unittype;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getInvalidateUrl() {
        return invalidateUrl;
    }

    public void setInvalidateUrl(String invalidateUrl) {
        this.invalidateUrl = invalidateUrl;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public Date getOnlineTime() {
        return onlineTime;
    }

    public void setOnlineTime(Date onlineTime) {
        this.onlineTime = onlineTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public String getFullIcon() {
        return fullIcon;
    }

    public void setFullIcon(String fullIcon) {
        this.fullIcon = fullIcon;
    }

    public String[] getUnitTypeArray() {
        return unitTypeArray;
    }

    public void setUnitTypeArray(String[] unitTypeArray) {
        this.unitTypeArray = unitTypeArray;
    }

    public String[] getUserTypeArray() {
        return userTypeArray;
    }

    public void setUserTypeArray(String[] userTypeArray) {
        this.userTypeArray = userTypeArray;
    }

    public String[] getSectionsArray() {
        return sectionsArray;
    }

    public void setSectionsArray(String[] sectionsArray) {
        this.sectionsArray = sectionsArray;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getAppSource() {
        return appSource;
    }

    public void setAppSource(String appSource) {
        this.appSource = appSource;
    }

    @Override
    public String fetchCacheEntitName() {
        return "server";
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {

        this.subId = subId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(Integer isVisible) {
        this.isVisible = isVisible;
    }

    public Integer getOpenType() {
        return openType;
    }

    public void setOpenType(Integer openType) {
        this.openType = openType;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public String getUnitTypeName() {
        return unitTypeName;
    }

    public void setUnitTypeName(String unitTypeName) {
        this.unitTypeName = unitTypeName;
    }

    public String getSectionsName() {
        return sectionsName;
    }

    public void setSectionsName(String sectionsName) {
        this.sectionsName = sectionsName;
    }

    public Integer getSubscribeCount() {
        return subscribeCount;
    }

    public void setSubscribeCount(Integer subscribeCount) {
        this.subscribeCount = subscribeCount;
    }

    public String getIntroductionPage() {
        return introductionPage;
    }

    public void setIntroductionPage(String introductionPage) {
        this.introductionPage = introductionPage;
    }

}
