package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 分班开设学科
 */
@Entity
@Table(name="newgkelective_open_subject")
public class NewGkOpenSubject extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * newgkelective_divide.id
	 */
	private String divideId;
	
	private String subjectId;
	/**
	 * O:行政班课程 A:选考 B:学考 J:教学班
	 */
	private String subjectType;
	
	private Date creationTime;
	
	private Date modifyTime;
	/**
	 * 1:7选3或者6选3(物化生历地政技)  2:理科(物化生)3:文科(历地政)4:语数英
	 */
	private String groupType;
	
	
	@Transient
	private String subjectName;
	
	@Transient
	private String placeIds;
	@Transient
	private String isNeed;
	
	

	public String getIsNeed() {
		return isNeed;
	}



	public void setIsNeed(String isNeed) {
		this.isNeed = isNeed;
	}



	public String getPlaceIds() {
		return placeIds;
	}



	public void setPlaceIds(String placeIds) {
		this.placeIds = placeIds;
	}



	public String getSubjectName() {
		return subjectName;
	}



	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}



	public String getDivideId() {
		return divideId;
	}



	public void setDivideId(String divideId) {
		this.divideId = divideId;
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
		return "newGkOpenSubject";
	}



	public String getGroupType() {
		return groupType;
	}



	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	
}
