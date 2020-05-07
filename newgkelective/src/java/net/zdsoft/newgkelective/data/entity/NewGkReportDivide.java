package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="newgkelective_report_divide")
public class NewGkReportDivide extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String unitId;
	private String reportId;
	private String subjectId;
	private String subjectType;
	private int xzbNumber;
	private int jxbNumber;
	private int studentNumber;
	private Date creationTime;
	private Date modifyTime;
	@Override
	public String fetchCacheEntitName() {
		return "newGkReportDivide";
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public int getXzbNumber() {
		return xzbNumber;
	}
	public void setXzbNumber(int xzbNumber) {
		this.xzbNumber = xzbNumber;
	}
	public int getJxbNumber() {
		return jxbNumber;
	}
	public void setJxbNumber(int jxbNumber) {
		this.jxbNumber = jxbNumber;
	}
	public int getStudentNumber() {
		return studentNumber;
	}
	public void setStudentNumber(int studentNumber) {
		this.studentNumber = studentNumber;
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

}
