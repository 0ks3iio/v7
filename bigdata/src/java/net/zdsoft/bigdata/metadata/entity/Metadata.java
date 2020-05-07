package net.zdsoft.bigdata.metadata.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import java.util.Date;

/**
 * 元数据
 * Created by wangdongdong on 2019/1/4 10:36.
 */
@Entity
@Table(name = "bg_metadata")
public class Metadata extends BaseEntity<String> {

	private static final long serialVersionUID = -3947844187144834758L;

	private String name;

    private String mdType;
    
    private String dbId;

    private String dbType;

    private String tableName;

    private String remark;

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date modifyTime;

    private Integer isCustom;

    private Integer isProperty;

    private String propertyTopicId;

    private String dwRankId;

    private Integer isSupportApi;

    private String parentId;

    private Integer isPhoenix;

    private Integer status;

    @Transient
    private String dbName;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMdType() {
        return mdType;
    }

    public void setMdType(String mdType) {
        this.mdType = mdType;
    }

    public String getDbId() {
		return dbId;
	}

	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getIsCustom() {
        return isCustom;
    }

    public void setIsCustom(Integer isCustom) {
        this.isCustom = isCustom;
    }

    public Integer getIsProperty() {
        return isProperty;
    }

    public void setIsProperty(Integer isProperty) {
        this.isProperty = isProperty;
    }

    public String getPropertyTopicId() {
        return propertyTopicId;
    }

    public void setPropertyTopicId(String propertyTopicId) {
        this.propertyTopicId = propertyTopicId;
    }

    public String getDwRankId() {
        return dwRankId;
    }

    public void setDwRankId(String dwRankId) {
        this.dwRankId = dwRankId;
    }

    public Integer getIsSupportApi() {
        return isSupportApi;
    }

    public void setIsSupportApi(Integer isSupportApi) {
        this.isSupportApi = isSupportApi;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Integer getIsPhoenix() {
        return isPhoenix;
    }

    public void setIsPhoenix(Integer isPhoenix) {
        this.isPhoenix = isPhoenix;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String fetchCacheEntitName() {
        return "Metadata";
    }
}
