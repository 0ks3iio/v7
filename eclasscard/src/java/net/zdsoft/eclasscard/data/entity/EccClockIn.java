package net.zdsoft.eclasscard.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name="eclasscard_clock_in")
public class EccClockIn  extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String ownerId;
	private String unitId;
	private String eccInfoId;
	private String type;
	private Integer status;
	private String ownerType;
	private Integer clockType;
	private String remark;
	
	private Integer businessType;//用于区分行政班 电子班牌 上课考勤与上下学

	@Temporal(TemporalType.TIMESTAMP)
	private Date clockInTime;
	@Transient
	private int orderNumber;
	@Transient
	private String placeName;
	@Transient	
	private String studentName;
	@Transient
	private String studentCode;
	@Transient
	private String className;
	@Transient
	private String roomName;
	@Transient
	private String teacherName;
	@Transient
	private String subjectName;
		
	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

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

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getOwnerType() {
		return ownerType;
	}

	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardClockIn";
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getEccInfoId() {
		return eccInfoId;
	}

	public void setEccInfoId(String eccInfoId) {
		this.eccInfoId = eccInfoId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getClockInTime() {
		return clockInTime;
	}

	public void setClockInTime(Date clockInTime) {
		this.clockInTime = clockInTime;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Integer getClockType() {
		return clockType;
	}

	public void setClockType(Integer clockType) {
		this.clockType = clockType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	
	
}
