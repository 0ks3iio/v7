package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "stusys_subschool")
public class SubSchool extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	private String schoolId;
    private String name;
    private String address;
    // @Temporal(TemporalType.TIMESTAMP)
    // @ColumnInfo(displayName = "创建时间", disabled = true)
    // private Date creationTime;
    // @ColumnInfo(displayName = "修改时间", disabled = true)
    // @Temporal(TemporalType.TIMESTAMP)
    private Long updatestamp;
    @ColumnInfo(displayName = "是否删除", hide = true, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
    private Integer isDeleted;
    
    @ColumnInfo(displayName = "校区编号", nullable = false,maxLength=20,regex="/^\\d+$/",regexTip="只能输入数字")
	private String areaCode;

    @Override
    public String fetchCacheEntitName() {
        return "subSchool";
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

	public Long getUpdatestamp() {
		return updatestamp;
	}

	public void setUpdatestamp(Long updatestamp) {
		this.updatestamp = updatestamp;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	
	
}
