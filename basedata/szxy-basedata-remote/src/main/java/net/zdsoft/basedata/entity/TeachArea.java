package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_teach_area")
public class TeachArea extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	@Column(updatable=false)
	@ColumnInfo(displayName = "单位", hide=true, nullable = false)
	private String unitId;
	@ColumnInfo(displayName = "校区名称", nullable = false,maxLength=80)
	private String areaName;
	@ColumnInfo(displayName = "校区编号", nullable = false,maxLength=20,regex="/^\\d+$/",regexTip="只能输入数字")
	private String areaCode;
	@ColumnInfo(displayName = "校区地址", nullable = true,maxLength=80)
	private String address;
	@ColumnInfo(displayName = "创建时间", disabled = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@ColumnInfo(displayName = "修改时间", disabled = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	@ColumnInfo(displayName = "是否删除", hide = true, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
	private int isDeleted;
	@ColumnInfo(displayName = "负责人", nullable = true,maxLength=20)
	private String supervisor;
	@ColumnInfo(displayName = "联系电话", nullable = true,maxLength=20,regex="/^\\d+$/",regexTip="只能输入20位数字")
	private String linkPhone;
	@ColumnInfo(displayName = "Email地址", nullable = true,maxLength=30)
	private String email;
	@ColumnInfo(displayName = "备注", nullable = true,maxLength=250)
	private String remark;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "teachArea";
	}

}
