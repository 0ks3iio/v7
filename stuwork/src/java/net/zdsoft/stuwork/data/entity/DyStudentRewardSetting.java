package net.zdsoft.stuwork.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "dy_student_reward_setting")
public class DyStudentRewardSetting extends BaseEntity<String>{
	

	private static final long serialVersionUID = 1L;
	
	private String projectId;             //   CHAR(32)  not null,
	private String rewardGrade;             // VARCHAR2(100), ---- 全国决赛 类别
	private String rewardLevel;             // VARCHAR2(100), ---- 第一第二 奖级
	private int rewardLevelOrder;
	private int rewardPeriod;            //  VARCHAR2(6),   -----届次
	private String rewardPoint;           // VARCHAR2(6),   -----分值
	private String remark;                  //  VARCHAR2(200),  -----备注 
	private String unitId;  

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

	public String getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(String rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "dyStudentRewardSetting";
	}

	public int getRewardLevelOrder() {
		return rewardLevelOrder;
	}

	public void setRewardLevelOrder(int rewardLevelOrder) {
		this.rewardLevelOrder = rewardLevelOrder;
	}
	
	
}
