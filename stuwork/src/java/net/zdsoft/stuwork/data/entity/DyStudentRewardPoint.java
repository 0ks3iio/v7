package net.zdsoft.stuwork.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "dy_student_reward_point")
public class DyStudentRewardPoint extends BaseEntity<String>{


	private static final long serialVersionUID = 1L;
    
	private String projectId;                  //CHAR(32)  not null,
	private String settingId;               //  CHAR(32)  not null,
	private String studentId;                //  CHAR(32)  not null,
	private String unitId;                  //   CHAR(32)  not null,
	private float rewardPoint;            //   NUMBER(6,2),   ---实际分数
	private Date creationTime;            // DATE,        
	private Date modifyTime;                // DATE,
	private boolean isDeleted;               //   NUMBER(1),
	private String acadyear;                 //   CHAR(9),     --学年
	private String semester;                 //   NUMBER(1),   --学期
	private String remark;					//
	private String rewardCountPoint;       //   VARCHAR(9)  ---- 计算方式 all 保存 all  acadyear 保存实际的学年 period 保存实际的借此    
	
    
	
	@Transient
	private String studentName;
	@Transient
	private String projectName;//项目名称
	@Transient
	private String projectRemark;//项目分类
	@Transient
	private String rewardClasses;//类型
	@Transient
	private String rewardGrade;	//级别
	@Transient
	private String rewardLevel;//奖级
	@Transient
	private int rewardPeriod; //届次---节假日使用
	@Transient
	private String stucode;
	@Transient
	private int orderNum;
	@Transient
	private String className;
	@Transient
	private String classId;
	
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getSettingId() {
		return settingId;
	}

	public void setSettingId(String settingId) {
		this.settingId = settingId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public float getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(float rewardPoint) {
		this.rewardPoint = rewardPoint;
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

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getRewardCountPoint() {
		return rewardCountPoint;
	}

	public void setRewardCountPoint(String rewardCountPoint) {
		this.rewardCountPoint = rewardCountPoint;
	}
	
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getRewardClasses() {
		return rewardClasses;
	}

	public void setRewardClasses(String rewardClasses) {
		this.rewardClasses = rewardClasses;
	}

	public String getRewardGrade() {
		return rewardGrade;
	}

	public void setRewardGrade(String rewardGrade) {
		this.rewardGrade = rewardGrade;
	}

	public String getRewardLevel() {
		return rewardLevel;
	}

	public void setRewardLevel(String rewardLevel) {
		this.rewardLevel = rewardLevel;
	}

	public int getRewardPeriod() {
		return rewardPeriod;
	}

	public void setRewardPeriod(int rewardPeriod) {
		this.rewardPeriod = rewardPeriod;
	}

	public String getStucode() {
		return stucode;
	}

	public void setStucode(String stucode) {
		this.stucode = stucode;
	}

	@Override
	public String fetchCacheEntitName() {
		return "dyStudentRewardPoint";
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getProjectRemark() {
		return projectRemark;
	}

	public void setProjectRemark(String projectRemark) {
		this.projectRemark = projectRemark;
	}
	
	
	
}
