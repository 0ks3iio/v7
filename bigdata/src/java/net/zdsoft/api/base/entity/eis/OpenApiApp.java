package net.zdsoft.api.base.entity.eis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.File;
import java.util.Date;

/**
 * 第三方应用信息
 * @author shenke
 * @since 2019/5/21 上午10:22
 */
@Entity
@Table(name = "bg_openapi_ap")
public class OpenApiApp {
	public static final String APP_DOCKING_NORMAL  = "1";
	public static final String APP_APPLYAPI_NORMAL = "2";
    public static final String SEPARATOR = ",";

    public static String getIconPath() {
        return "bigdata" + File.separator + "app" + File.separator + "icon";
    }

    @Id
    private String id;

    /**
     * 开发者ID
     */
    private String developerId;
    /**
     * 名称
     */
    private String name;
    /**
     * 图标地址
     */
    private String iconUrl;
    /**
     * 首页地址
     */
    private String indexUrl;
    /**
     * 退出回调地址
     */
    private String invalidateUrl;
    /**
     * 登录回调地址
     */
    private String verifyUrl;
    /**
     * 适用单位类型
     */
    private String unitClasses;
    /**
     * 适用用户类型
     */
    private String userTypes;
    /**
     * 学段
     */
    private String sections;

    /**
     * 说明
     */
    private String description;


    /**
     * 是否可见
     */
    @Column(name = "is_visible")
    private Integer visible;

    /**
     * 应用状态
     */
    private Integer status;
    
    /**
     * 应用类型
     */
    private String appTypes;


    @Column(name = "is_deleted")
    private Integer deleted;

    private Date modifyTime;

    @Column(updatable = false)
    private Date creationTime;

    public String getSections() {
        return sections;
    }

    public void setSections(String sections) {
        this.sections = sections;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeveloperId() {
        return developerId;
    }

    public void setDeveloperId(String developerId) {
        this.developerId = developerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public String getInvalidateUrl() {
        return invalidateUrl;
    }

    public void setInvalidateUrl(String invalidateUrl) {
        this.invalidateUrl = invalidateUrl;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl;
    }

    public String getUnitClasses() {
        return unitClasses;
    }

    public void setUnitClasses(String unitClasses) {
        this.unitClasses = unitClasses;
    }

    public String getUserTypes() {
        return userTypes;
    }

    public void setUserTypes(String userTypes) {
        this.userTypes = userTypes;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public String getAppTypes() {
		return appTypes;
	}

	public void setAppTypes(String appTypes) {
		this.appTypes = appTypes;
	}

	@Override
    public String toString() {
        return "OpenApiApp{" +
                "id='" + id + '\'' +
                ", developerId='" + developerId + '\'' +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", indexUrl='" + indexUrl + '\'' +
                ", invalidateUrl='" + invalidateUrl + '\'' +
                ", verifyUrl='" + verifyUrl + '\'' +
                ", unitClasses='" + unitClasses + '\'' +
                ", userTypes='" + userTypes + '\'' +
                ", visible=" + visible +
                ", status=" + status +
                ", deleted=" + deleted +
                ", modifyTime=" + modifyTime +
                ", creationTime=" + creationTime +
                '}';
    }
}
