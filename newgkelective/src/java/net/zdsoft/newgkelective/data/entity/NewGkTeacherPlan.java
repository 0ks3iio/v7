package net.zdsoft.newgkelective.data.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * @author user  教师安排
 *
 */
@Entity
@Table(name="newgkelective_teacher_plan")
public class NewGkTeacherPlan extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private String arrayItemId;
	
	private String subjectId;
	
	private Integer classNum;
	
	private Date creationTime;
	
	private Date modifyTime;
	/**
	 * 科目下的教师
	 */
	@Transient
	private List<String> exTeacherIdList=new ArrayList<String>();
	@Transient
	private List<NewGkTeacherPlanEx> teacherPlanExList = new ArrayList<>();
	@Transient
	private String subjectName;
	/**
	 * 科目下的教师数
	 */
	@Transient
	private String teacherCounts;

	public List<NewGkTeacherPlanEx> getTeacherPlanExList() {
		return teacherPlanExList;
	}

	public void setTeacherPlanExList(List<NewGkTeacherPlanEx> teacherPlanExList) {
		this.teacherPlanExList = teacherPlanExList;
	}

	public String getTeacherCounts() {
		return teacherCounts;
	}

	public void setTeacherCounts(String teacherCounts) {
		this.teacherCounts = teacherCounts;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<String> getExTeacherIdList() {
		return exTeacherIdList;
	}

	public void setExTeacherIdList(List<String> exTeacherIdList) {
		this.exTeacherIdList = exTeacherIdList;
	}

	public String getArrayItemId() {
		return arrayItemId;
	}

	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getClassNum() {
		return classNum;
	}

	public void setClassNum(Integer classNum) {
		this.classNum = classNum;
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
		return "getTeacherArrange";
	}

}
