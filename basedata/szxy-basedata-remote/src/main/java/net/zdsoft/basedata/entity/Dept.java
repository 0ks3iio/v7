package net.zdsoft.basedata.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.alibaba.fastjson.TypeReference;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.framework.utils.SUtils;

@Entity
@Table(name = "base_dept")
public class Dept extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;
	
	public enum DeptStateEnum{
		VALIDATE(1),
		INVALIDATE(0);
		
		private int state;
		private DeptStateEnum(int state){
			this.state = state;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
	}
	
	public static List<Dept> dt(String data) {
		List<Dept> ts = SUtils.dt(data, new TypeReference<List<Dept>>() {
		});
		if (ts == null)
			ts = new ArrayList<Dept>();
		return ts;
	}
	
	public static Dept dc(String data) {
		return SUtils.dc(data, Dept.class);
	}
	
	

	@ColumnInfo(displayName = "单位ID", hide = true)
	private String unitId;
	@ColumnInfo(displayName = "名称", nullable = false, displayOrder = 1)
	private String deptName;
	@ColumnInfo(displayName = "编号", displayOrder = 2,maxLength = 6)
	private String deptCode;
	@ColumnInfo(displayName = "部门简称", displayOrder=3)
	private String deptShortName;
	@ColumnInfo(displayName = "上级部门", displayOrder = 4, nullable = false, vtype = ColumnInfo.VTYPE_SELECT, vsql = "select '00000000000000000000000000000000', '一级部门' from dual union select id, dept_name from base_dept where is_deleted = '0' and unit_id = {unitId} and id <> {id}")
	private String parentId;
	@ColumnInfo(displayName = "办公电话", displayOrder = 5)
	private String deptTel;
	@ColumnInfo(displayName = "排序号", vtype = ColumnInfo.VTYPE_INT, displayOrder = 6)
	private Integer displayOrder;
	@ColumnInfo(displayName = "类型", displayOrder = 7, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-JYBZ")
	private Integer deptType;
	@ColumnInfo(displayName = "负责人", displayOrder = 8, vtype = ColumnInfo.VTYPE_SELECT, vsql = "select id, teacher_name from base_teacher where is_deleted = 0 and unit_id = {unitId}")
	private String teacherId;
	@ColumnInfo(displayName = "分管领导", displayOrder = 9, vtype = ColumnInfo.VTYPE_SELECT, vsql = "select id, teacher_name from base_teacher where is_deleted = 0 and unit_id = {unitId}")
	private String leaderId;
	@ColumnInfo(displayName = "分管校长", displayOrder = 10, vtype = ColumnInfo.VTYPE_SELECT, vsql = "select id, teacher_name from base_teacher where is_deleted = 0 and unit_id = {unitId}")
	private String deputyHeadId;
	@ColumnInfo(displayName = "备注")
	private String remark;
	private Integer isDefault;
	@ColumnInfo(displayName = "创建时间", disabled = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable=false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "修改时间", disabled = true)
	private Date modifyTime;
	@ColumnInfo(displayName = "是否删除", hide = true, vtype = ColumnInfo.VTYPE_RADIO, mcodeId = "DM-BOOLEAN")
	private Integer isDeleted;
	private Integer eventSource;
//	@Transient
	private String dingdingId;
	@ColumnInfo(displayName = "所属学院", fitForSchoolType="^[40,41]$")
	private String instituteId;
	@ColumnInfo(displayName = "所属校区")
	private String areaId;
	
	@Transient
	@ColumnInfo(disabled = true)
	private Long sequenceIntId;
	@Transient
	@ColumnInfo(displayName = "部门状态", hide=true)
	private Integer deptState;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Integer getIsDeleted() {
		if (isDeleted == null)
			isDeleted = 0;
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDeptType() {
		return deptType;
	}

	public void setDeptType(Integer deptType) {
		this.deptType = deptType;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
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

	public String getDeptShortName() {
		return deptShortName;
	}

	public void setDeptShortName(String deptShortName) {
		this.deptShortName = deptShortName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "dept";
	}

	public String getDeptTel() {
		return deptTel;
	}

	public void setDeptTel(String deptTel) {
		this.deptTel = deptTel;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getEventSource() {
		return eventSource;
	}

	public void setEventSource(Integer eventSource) {
		this.eventSource = eventSource;
	}

	public Integer getDeptState() {
		return deptState;
	}

	public void setDeptState(Integer deptState) {
		this.deptState = deptState;
	}

	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}

	public String getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(String instituteId) {
		this.instituteId = instituteId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getDeputyHeadId() {
		return deputyHeadId;
	}

	public void setDeputyHeadId(String deputyHeadId) {
		this.deputyHeadId = deputyHeadId;
	}

	public Long getSequenceIntId() {
		return sequenceIntId;
	}

	public void setSequenceIntId(Long sequenceIntId) {
		this.sequenceIntId = sequenceIntId;
	}

	public String getDingdingId() {
		return dingdingId;
	}

	public void setDingdingId(String dingdingId) {
		this.dingdingId = dingdingId;
	}
}
