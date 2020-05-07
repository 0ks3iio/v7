package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "newgkelective_section_result")
public class NewGkSectionResult extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private String sectionId;
    private String groupName;//名称
    //subjectId_subjectId 或者 subjectId_subjectId_subjectId
    private String subjectIds;
    private String subjectType;
    //算法前 0 或者算法后安排1
    private String arrangeType;
    //subjectId_subjectId_subjectId-10,subjectId_subjectId_subjectId-20
    private String studentScource;//来源各个选课结果人数
    private String groupClassId;// arrangeType:0时 组合id(正式组合班id)  arrangeType:1时 endId(算法后endid)  
    private Date creationTime;
 	private Date modifyTime;
 	
 	@Transient
 	private String[] studentScourceSub;
 	@Transient
 	private String[] studentScourceNum;
	@Override
	public String fetchCacheEntitName() {
		return "newGkSectionResult";
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getArrangeType() {
		return arrangeType;
	}
	public void setArrangeType(String arrangeType) {
		this.arrangeType = arrangeType;
	}
	public String getStudentScource() {
		return studentScource;
	}
	public void setStudentScource(String studentScource) {
		this.studentScource = studentScource;
	}
	public String getGroupClassId() {
		return groupClassId;
	}
	public void setGroupClassId(String groupClassId) {
		this.groupClassId = groupClassId;
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
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String[] getStudentScourceSub() {
		return studentScourceSub;
	}
	public void setStudentScourceSub(String[] studentScourceSub) {
		this.studentScourceSub = studentScourceSub;
	}
	public String[] getStudentScourceNum() {
		return studentScourceNum;
	}
	public void setStudentScourceNum(String[] studentScourceNum) {
		this.studentScourceNum = studentScourceNum;
	}
	

	
}
