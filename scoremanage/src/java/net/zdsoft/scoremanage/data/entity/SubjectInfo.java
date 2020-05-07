package net.zdsoft.scoremanage.data.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.annotation.ColumnInfo;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="scoremanage_subject_info")
public class SubjectInfo extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ColumnInfo(displayName="单位id",nullable=false)
	private String unitId;
	@ColumnInfo(displayName="考试id",nullable=false)
	private String examId;
	@ColumnInfo(displayName="科目id",nullable=false)
	private String subjectId;
	@ColumnInfo(displayName="考试方式",nullable=false,mcodeId="DM-KSFS")
	private String examMode;
	@ColumnInfo(displayName="成绩录入方式",nullable=false,mcodeId="DM-CJLRXSFS")
	private String inputType;
	@ColumnInfo(displayName="满分值")
	private Float fullScore;
	@ColumnInfo(displayName="范围")
	private String rangeType;
	@ColumnInfo(displayName="等分",mcodeId="DM-DDMC")
	private String gradeType;
	
	@Transient
	private String[] classIds;
	@Transient
	private String[] teachClassIds;
	@Transient
	private String courseName;
	@Transient
	private Map<String,String> classIdsMap = new HashMap<String, String>();//为页面上判断
	@Transient
	private Map<String,String> teachClassIdsMap = new HashMap<String, String>();//为页面上判断
	@Transient
	private String daytime;
	@Transient
	private String isUsed;
	@Transient
	private int orderId;
		
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	public String getDaytime() {
		return daytime;
	}
	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Map<String, String> getTeachClassIdsMap() {
		return teachClassIdsMap;
	}
	public void setTeachClassIdsMap(Map<String, String> teachClassIdsMap) {
		this.teachClassIdsMap = teachClassIdsMap;
	}
	public String[] getTeachClassIds() {
		return teachClassIds;
	}
	public void setTeachClassIds(String[] teachClassIds) {
		this.teachClassIds = teachClassIds;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExamMode() {
		return examMode;
	}
	public void setExamMode(String examMode) {
		this.examMode = examMode;
	}
	public String getInputType() {
		return inputType;
	}
	public void setInputType(String inputType) {
		this.inputType = inputType;
	}
	public Float getFullScore() {
		return fullScore;
	}
	public void setFullScore(Float fullScore) {
		this.fullScore = fullScore;
	}
	public String getRangeType() {
		return rangeType;
	}
	public void setRangeType(String rangeType) {
		this.rangeType = rangeType;
	}
	public String getGradeType() {
		return gradeType;
	}
	public void setGradeType(String gradeType) {
		this.gradeType = gradeType;
	}
	
	public String[] getClassIds() {
		return classIds;
	}
	public void setClassIds(String[] classIds) {
		this.classIds = classIds;
	}
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	@Override
	public String fetchCacheEntitName() {
		return "subjectInfo";
	}
	public Map<String, String> getClassIdsMap() {
		return classIdsMap;
	}
	public void setClassIdsMap(Map<String, String> classIdsMap) {
		this.classIdsMap = classIdsMap;
	}

}
