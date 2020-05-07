package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
/**
 * 上下学考勤打卡记录
 *
 */
@Entity
@Table(name = "eclasscard_in_out_attance")
public class EccInOutAttance extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String fetchCacheEntitName() {
		return "eccInOutAttance";
	}
	private String periodId;
	private String studentId;
	private String classId;
	private String eccInfoId;
	private String unitId;
	private Integer status;
	private Integer type;
	private Date clockDate;

	public String getPeriodId() {
		return periodId;
	}
	public void setPeriodId(String periodId) {
		this.periodId = periodId;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getEccInfoId() {
		return eccInfoId;
	}
	public void setEccInfoId(String eccInfoId) {
		this.eccInfoId = eccInfoId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getClockDate() {
		return clockDate;
	}
	public void setClockDate(Date clockDate) {
		this.clockDate = clockDate;
	}
	
}
