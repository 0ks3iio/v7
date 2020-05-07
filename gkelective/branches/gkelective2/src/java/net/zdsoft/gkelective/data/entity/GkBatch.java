package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *批次
 *@author zhouyz
 */
@Entity
@Table(name="gkelective_batch")
public class GkBatch extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roundsId;
	private String groupClassId;//单科开班 默认32个0
	private String teachClassId;//修改为保存是临时教学班id
	private int batch;
	private String placeId;
	private Date creationTime;
	private Date modifyTime;
	private String classType;//选考与学考
	
	@Transient
	private String className;
	@Transient
	private String subjectId; 
	@Transient
	private String subjectName;
	@Transient
	private int number;//总人数
	@Transient
	private int manNumber;//男人数
	@Transient
	private int womanNumber;//女人数
	@Transient
	private Double averageScore;//平均分
	@Transient
	private String teacherId;//教师
	
	@Transient
	private String teacherName;//教师
	@Transient
	private String placeName;//场地
	
	
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getManNumber() {
		return manNumber;
	}
	public void setManNumber(int manNumber) {
		this.manNumber = manNumber;
	}
	public int getWomanNumber() {
		return womanNumber;
	}
	public void setWomanNumber(int womanNumber) {
		this.womanNumber = womanNumber;
	}
	public Double getAverageScore() {
		return averageScore;
	}
	public void setAverageScore(Double averageScore) {
		this.averageScore = averageScore;
	}
	public String getRoundsId() {
		return roundsId;
	}
	public void setRoundsId(String roundsId) {
		this.roundsId = roundsId;
	}
	public String getGroupClassId() {
		return groupClassId;
	}
	public void setGroupClassId(String groupClassId) {
		this.groupClassId = groupClassId;
	}
	public String getClassType() {
		return classType;
	}
	public void setClassType(String classType) {
		this.classType = classType;
	}
	public String getTeachClassId() {
		return teachClassId;
	}
	public void setTeachClassId(String teachClassId) {
		this.teachClassId = teachClassId;
	}
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
	}
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
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
	@Override
	public String fetchCacheEntitName() {
		return "gkBatch";
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

}
