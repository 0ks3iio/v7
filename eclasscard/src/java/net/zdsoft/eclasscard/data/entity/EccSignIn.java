package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eclasscard_sign_in")
public class EccSignIn extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String ownerId;
	private String classId;
	private String eccInfoId;
	private Date clockInTime;
	
	@Transient
	private String className;
	@Transient
	private String studentName;
	@Transient
	private String placeName;
	@Transient
	private Integer state;
	
	public String getClassName() {
		return className;
	}



	public void setClassName(String className) {
		this.className = className;
	}



	public String getStudentName() {
		return studentName;
	}



	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}



	public String getPlaceName() {
		return placeName;
	}



	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}



	public Integer getState() {
		return state;
	}



	public void setState(Integer state) {
		this.state = state;
	}



	public String getUnitId() {
		return unitId;
	}



	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}



	public String getOwnerId() {
		return ownerId;
	}



	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
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



	public Date getClockInTime() {
		return clockInTime;
	}



	public void setClockInTime(Date clockInTime) {
		this.clockInTime = clockInTime;
	}



	@Override
	public String fetchCacheEntitName() {
		return "eclasscardSignIn";
	}

}
