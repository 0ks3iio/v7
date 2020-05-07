package net.zdsoft.teacherasess.data.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="teacherasess_asess")
public class TeacherAsess extends BaseEntity<String>{

	@ColumnInfo(displayName = "单位id", hide = true)
	@Column(updatable=false)
	private String unitId;
	@ColumnInfo(displayName = "学年", hide = true)
	@Column(updatable=false)
	private String acadyear;
	@ColumnInfo(displayName = "年级id", hide = true)
	@Column(updatable=false)
	private String gradeId;
	@ColumnInfo(displayName = "名称", hide = true)
	@Column(updatable=false)
	private String name;
	@ColumnInfo(displayName = "本次方案id", hide = true)
	@Column(updatable=false)
	private String convertId;
	@ColumnInfo(displayName = "参考方案id", hide = true)
	@Column(updatable=false)
	private String referConvertId;
	@ColumnInfo(displayName = "对比状态", hide = true)
	@Column(updatable=false)
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "创建时间", disabled = true)
	@Column(updatable=false)
	private Date creationTime;
	@ColumnInfo(displayName = "是否删除", hide = true)
	private int isDeleted;
	
	@Transient
	private String gradeName;
	@Transient
	private String convertName;
	@Transient
	private String referConvertName;
	@Transient
	private boolean convert;
	
	private String xuankaoType;//物化生政史地技科目是否设为选考
	
	@Override
	public String fetchCacheEntitName() {
		return "teacherAsess";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConvertId() {
		return convertId;
	}

	public void setConvertId(String convertId) {
		this.convertId = convertId;
	}

	public String getReferConvertId() {
		return referConvertId;
	}

	public void setReferConvertId(String referConvertId) {
		this.referConvertId = referConvertId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getGradeName() {
		return gradeName;
	}

	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}

	public String getConvertName() {
		return convertName;
	}

	public void setConvertName(String convertName) {
		this.convertName = convertName;
	}

	public String getReferConvertName() {
		return referConvertName;
	}

	public void setReferConvertName(String referConvertName) {
		this.referConvertName = referConvertName;
	}

	public boolean isConvert() {
		return convert;
	}

	public void setConvert(boolean convert) {
		this.convert = convert;
	}

	public String getXuankaoType() {
		return xuankaoType;
	}

	public void setXuankaoType(String xuankaoType) {
		this.xuankaoType = xuankaoType;
	}
	
	
}
