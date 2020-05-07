package net.zdsoft.evaluation.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "teach_evaluate_project")
public class TeachEvaluateProject extends BaseEntity<String> {
	private static final long serialVersionUID = 7305720998575524907L;
	
	private String unitId;
	private String acadyear;
	private String semester;
	private String evaluateType;
	private String projectName;
	private Date beginTime;
	private Date endTime;
	private String remark;
	
	@Transient
	private int submitNum;
	@Transient
	private int noSubmitNum;
	@Transient
	private String status;
	@Transient
	private String containsType;//包涵类别
	@Transient
	private String hasStat;//1有统计结果0无统计结果
	@Transient
	private String hasResult;//1有结果0无结果
	@Transient
	private String gradeIds;
	
	
	public String getGradeIds() {
		return gradeIds;
	}

	public void setGradeIds(String gradeIds) {
		this.gradeIds = gradeIds;
	}

	public String getHasResult() {
		return hasResult;
	}

	public void setHasResult(String hasResult) {
		this.hasResult = hasResult;
	}

	public String getHasStat() {
		return hasStat;
	}

	public void setHasStat(String hasStat) {
		this.hasStat = hasStat;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContainsType() {
		return containsType;
	}

	public void setContainsType(String containsType) {
		this.containsType = containsType;
	}

	public int getSubmitNum() {
		return submitNum;
	}

	public void setSubmitNum(int submitNum) {
		this.submitNum = submitNum;
	}

	public int getNoSubmitNum() {
		return noSubmitNum;
	}

	public void setNoSubmitNum(int noSubmitNum) {
		this.noSubmitNum = noSubmitNum;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
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

	public String getEvaluateType() {
		return evaluateType;
	}

	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String fetchCacheEntitName() {
		return "teachEvaluateProject";
	}

}
