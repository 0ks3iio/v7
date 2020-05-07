package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "newgkelective_section_end")
public class NewGkSectionEnd extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sectionId;
	//subjectId_subjectId
	private String subjectIds;
	private Integer openClassNum;//开设班级数
	//subjectId_subjectId_subjectId-10,subjectId_subjectId_subjectId-20
	private String studentScource;//来源各个选课结果人数
	private Date creationTime;
	private Date modifyTime;
	@Transient
 	private String[] studentScourceSub;
 	@Transient
 	private String[] studentScourceNum;
	
	@Override
	public String fetchCacheEntitName() {
		return "newGkSectionEnd";
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

	public Integer getOpenClassNum() {
		return openClassNum;
	}

	public void setOpenClassNum(Integer openClassNum) {
		this.openClassNum = openClassNum;
	}

	public String getStudentScource() {
		return studentScource;
	}

	public void setStudentScource(String studentScource) {
		this.studentScource = studentScource;
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
