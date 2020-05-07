package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 学生分层 扩展
 */
@Entity
@Table(name = "newgkelective_stu_range_ex")
public class NewGKStudentRangeEx extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String divideId;
	private String subjectId;
	private String subjectType;
	private String range;
	private Integer classNum;
	private Integer maximum;//平均值
	private Integer leastNum;//上下浮动人数
	private Date modifyTime;
	
	@Transient
	private String subjectName;
	@Transient
	private Integer stuNum;
	
	public Integer getStuNum() {
		return stuNum;
	}

	public void setStuNum(Integer stuNum) {
		this.stuNum = stuNum;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGKStudentRangeEx";
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

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public Integer getClassNum() {
		return classNum;
	}

	public void setClassNum(Integer classNum) {
		this.classNum = classNum;
	}

	public Integer getMaximum() {
		return maximum;
	}

	public void setMaximum(Integer maximum) {
		this.maximum = maximum;
	}

	public Integer getLeastNum() {
		return leastNum;
	}

	public void setLeastNum(Integer leastNum) {
		this.leastNum = leastNum;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
}
