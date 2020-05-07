package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "dy_student_reward_project")
public class DyStudentRewardProject extends BaseEntity<String>{


	private static final long serialVersionUID = 1L;
	
	private String 	rewardClasses;// ---类别 理科
	private String  classesType;//              CHAR(3)   not null;----类别类型 11 21，22，23，24
	private String  projectRemark;//            VARCHAR2(50);--评优先进专用
	private String  projectName; //            VARCHAR2(50);---数学 物理
	private String  projectType;
	private int rewardPeriod;            //  VARCHAR2(6),   -----届次
	private String  rewardPointType;//         CHAR(1)   not null;----累加或者最高 1累加2最高
	private String  rewardCountPointType;//   CHAR(1)   not null;----计算积分单位 1 all 2 acayyear 3 period
	private String acadyear;                 //   CHAR(9),     --学年
	private String semester;                 //   NUMBER(1),   --学期
	private String  unitId; //                  CHAR(32)

	public String getRewardClasses() {
		return rewardClasses;
	}

	public void setRewardClasses(String rewardClasses) {
		this.rewardClasses = rewardClasses;
	}

	public String getClassesType() {
		return classesType;
	}

	public void setClassesType(String classesType) {
		this.classesType = classesType;
	}

	public String getProjectRemark() {
		return projectRemark;
	}

	public void setProjectRemark(String projectRemark) {
		this.projectRemark = projectRemark;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getRewardPointType() {
		return rewardPointType;
	}

	public void setRewardPointType(String rewardPointType) {
		this.rewardPointType = rewardPointType;
	}

	public String getRewardCountPointType() {
		return rewardCountPointType;
	}

	public void setRewardCountPointType(String rewardCountPointType) {
		this.rewardCountPointType = rewardCountPointType;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	
	public int getRewardPeriod() {
		return rewardPeriod;
	}

	public void setRewardPeriod(int rewardPeriod) {
		this.rewardPeriod = rewardPeriod;
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

	@Override
	public String fetchCacheEntitName() {
		return "dyStudentRewardProject";
	}

}
