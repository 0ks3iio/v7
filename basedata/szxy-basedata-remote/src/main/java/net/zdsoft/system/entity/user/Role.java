package net.zdsoft.system.entity.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_role")
public class Role extends BaseEntity<String> {

    private static final long serialVersionUID = 1L;
    private String identifier;
    @Column(name = "unitid")
    @ColumnInfo(displayName = "所在单位", hide = true)
    private String unitId;
    @ColumnInfo(displayName = "名称", nullable = false)
    private String name;
    @Column(name = "modid")
    private String modId;
    @Column(name = "operid")
    private String operId;
    @Column(name = "dynamicdataset")
    private String dynamicDataSet;
    @Column(name = "isactive")
    private String isActive;
    private String description;
    @Column(name = "subsystem")
    private String subSystem;
    @Column(name = "refid")
    private Integer refId;
    @Column(name = "roletype")
    private Integer roleType;
    @Transient
    private Date modifyTime;// 修改时间
    @Transient
    private Integer isSystem;// 是否是系统级权限组 1.是 0.否
    @Transient
    private Integer serverId;
    
    @Override
    public String fetchCacheEntitName() {
        return "role";
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getDynamicDataSet() {
        return dynamicDataSet;
    }

    public void setDynamicDataSet(String dynamicDataSet) {
        this.dynamicDataSet = dynamicDataSet;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(String subSystem) {
        this.subSystem = subSystem;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

	public Integer getServerId() {
		return serverId;
	}

	public void setServerId(Integer serverId) {
		this.serverId = serverId;
	}
}
