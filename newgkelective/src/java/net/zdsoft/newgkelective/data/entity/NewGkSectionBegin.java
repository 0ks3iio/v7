package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "newgkelective_section_begin")
public class NewGkSectionBegin extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String sectionId;
	private String groupName;
	//subjectId_subjectId_subjectId
	private String subjectIds;//3科科目 或者2科
	private String subjectType;
	private Integer studentNum;//学生总人数
	private Integer arrangeNum;//已安排人数
	private Integer noJoin;//是否参与 (2科 如果不参与意味着不能组这个2+x;3科 如果不参与 意味着这批人不参与算法分班)
	private Date creationTime;
	private Date modifyTime;
	
	//算法中安排人数
	@Transient
	private Integer leftArrangeNum=0;
	
	@Override
	public String fetchCacheEntitName() {
		return "newGkSectionBegin";
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

	public Integer getStudentNum() {
		return studentNum;
	}

	public void setStudentNum(Integer studentNum) {
		this.studentNum = studentNum;
	}

	public Integer getArrangeNum() {
		return arrangeNum;
	}

	public void setArrangeNum(Integer arrangeNum) {
		this.arrangeNum = arrangeNum;
	}
	
	public Integer getNoJoin() {
		return noJoin;
	}

	public void setNoJoin(Integer noJoin) {
		this.noJoin = noJoin;
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

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getLeftArrangeNum() {
		return leftArrangeNum;
	}

	public void setLeftArrangeNum(Integer leftArrangeNum) {
		this.leftArrangeNum = leftArrangeNum;
	}

}
