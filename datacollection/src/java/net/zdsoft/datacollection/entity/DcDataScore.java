package net.zdsoft.datacollection.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "dc_data_score")
public class DcDataScore extends BaseEntity<String> {

	private Integer score;
	private String teacherId;
	private String description;
	private String operationUserId;
	private String unitId;
	private String acadyear;
	private String semester;
	private String createUserId;
	private Date creationTime;
	private String woDate;
	private Integer state;
	private String deptId;
	private String finalOperationUserId;
	private Integer scoreType;

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOperationUserId() {
		return operationUserId;
	}

	public void setOperationUserId(String operationUserId) {
		this.operationUserId = operationUserId;
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

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getFinalOperationUserId() {
		return finalOperationUserId;
	}

	public void setFinalOperationUserId(String finalOperationUserId) {
		this.finalOperationUserId = finalOperationUserId;
	}

	public Integer getScoreType() {
		return scoreType;
	}

	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}

	@Override
	public String fetchCacheEntitName() {
		return null;
	}

	public String getWoDate() {
		return woDate;
	}

	public void setWoDate(String woDate) {
		this.woDate = woDate;
	}

}
