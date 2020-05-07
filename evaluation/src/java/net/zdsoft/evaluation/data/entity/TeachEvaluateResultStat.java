package net.zdsoft.evaluation.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="teach_evaluate_result_stat")
public class TeachEvaluateResultStat extends BaseEntity<String>{
	private static final long serialVersionUID = 1731057636306827193L;
	
	private String unitId;
	private String projectId;
	private String projectName;
	private String evaluateType;
	private String itemId;
	private String itemName;
	private String itemType;
	private String optionId;
	private String optionName;
	private float bfb;
	private float score;
	private String dimension;
	private String statTeacherId;
	private String statTeacherName;
	private String statClassId;
	private String statClassName;
	private String statSubjectId;
	private String statSubjectName;
	private String statGradeId;
	private String statGradeName;
	private Date creationTime;
	
	private int itemNo;
	private int optionNo;
	
	public int getItemNo() {
		return itemNo;
	}

	public void setItemNo(int itemNo) {
		this.itemNo = itemNo;
	}

	public int getOptionNo() {
		return optionNo;
	}

	public void setOptionNo(int optionNo) {
		this.optionNo = optionNo;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getEvaluateType() {
		return evaluateType;
	}

	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getOptionId() {
		return optionId;
	}

	public void setOptionId(String optionId) {
		this.optionId = optionId;
	}

	public String getOptionName() {
		return optionName;
	}

	public void setOptionName(String optionName) {
		this.optionName = optionName;
	}

	public float getBfb() {
		return bfb;
	}

	public void setBfb(float bfb) {
		this.bfb = bfb;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

	public String getStatTeacherId() {
		return statTeacherId;
	}

	public void setStatTeacherId(String statTeacherId) {
		this.statTeacherId = statTeacherId;
	}

	public String getStatTeacherName() {
		return statTeacherName;
	}

	public void setStatTeacherName(String statTeacherName) {
		this.statTeacherName = statTeacherName;
	}

	public String getStatClassId() {
		return statClassId;
	}

	public void setStatClassId(String statClassId) {
		this.statClassId = statClassId;
	}

	public String getStatClassName() {
		return statClassName;
	}

	public void setStatClassName(String statClassName) {
		this.statClassName = statClassName;
	}

	public String getStatSubjectId() {
		return statSubjectId;
	}

	public void setStatSubjectId(String statSubjectId) {
		this.statSubjectId = statSubjectId;
	}

	public String getStatSubjectName() {
		return statSubjectName;
	}

	public void setStatSubjectName(String statSubjectName) {
		this.statSubjectName = statSubjectName;
	}

	public String getStatGradeId() {
		return statGradeId;
	}

	public void setStatGradeId(String statGradeId) {
		this.statGradeId = statGradeId;
	}

	public String getStatGradeName() {
		return statGradeName;
	}

	public void setStatGradeName(String statGradeName) {
		this.statGradeName = statGradeName;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	@Override
	public String fetchCacheEntitName() {
		// TODO Auto-generated method stub
		return "teachEvaluateResultStat";
	}
}
