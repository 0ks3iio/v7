package net.zdsoft.system.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_permission")
public class Permission extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	@ColumnInfo(displayName = "名称")
	private String name;
	@ColumnInfo(displayName = "地址")
	private String url;
	@ColumnInfo(displayName = "上级", vtype=ColumnInfo.VTYPE_SELECT, vsql="select id, name from base_permission where type = 1 and id <> {id}")
	private String parentId;
	@ColumnInfo(displayName = "类型", vtype=ColumnInfo.VTYPE_SELECT, vselect= {"1:教育局", "2:学校"})
	private Integer type;
	@ColumnInfo(displayName = "所属系统", vtype=ColumnInfo.VTYPE_SELECT, vsql="select code, name from sys_subsystem")
	private String subsystemCode;
	@ColumnInfo(displayName = "扩展内容")
	private String extendId;

	@Override
	public String fetchCacheEntitName() {
		return "permission";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

    public String getSubsystemCode() {
        return subsystemCode;
    }

    public void setSubsystemCode(String subsystemCode) {
        this.subsystemCode = subsystemCode;
    }

    public String getExtendId() {
        return extendId;
    }

    public void setExtendId(String extendId) {
        this.extendId = extendId;
    }
}
