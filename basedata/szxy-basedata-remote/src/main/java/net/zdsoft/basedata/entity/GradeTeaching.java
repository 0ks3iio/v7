package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.*;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_grade_teaching")
public class GradeTeaching extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	@ColumnInfo(displayName = "学年")
	private String acadyear;
	@ColumnInfo(displayName = "学期")
	private String semester;
	@ColumnInfo(displayName = "单位")
	private String unitId;
	@ColumnInfo(displayName = "年级id")
	private String gradeId;
	@ColumnInfo(displayName = "科目Id")
	private String subjectId;
	@ColumnInfo(displayName = "创建时间", disabled = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	@ColumnInfo(displayName = "修改时间", disabled = true)
	private Date modifyTime;
	// @ColumnInfo(displayName = "分层")
	// private Integer layered;
	@ColumnInfo(displayName = "科目类型")
	private String subjectType;
	@ColumnInfo(displayName = "是否软删", disabled = true)
	private Integer isDeleted;
	@ColumnInfo(displayName = "是否以教学班形式教学")
	private Integer isTeaCls;
	@ColumnInfo(displayName = "学分")
	private Integer credit;

	public Integer getCredit() {
		return credit;
	}

	public void setCredit(Integer credit) {
		this.credit = credit;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gradeTeaching";
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
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

	// public Integer getLayered() {
	// return layered;
	// }
	//
	// public void setLayered(Integer layered) {
	// this.layered = layered;
	// }

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getIsTeaCls() {
		return isTeaCls;
	}

	public void setIsTeaCls(Integer isTeaCls) {
		this.isTeaCls = isTeaCls;
	}

}
