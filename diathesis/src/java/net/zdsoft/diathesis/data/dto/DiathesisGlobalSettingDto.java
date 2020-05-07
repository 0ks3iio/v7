package net.zdsoft.diathesis.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 * 全局设置
 */
public class DiathesisGlobalSettingDto {
	private String id;
    private String unitId;
    @NotEmpty(message = "等第不能为空")
	private List<String> rankItems;
    @NotEmpty(message = "等第对应值不能为空")
	private List<String> rankValues;
    @Pattern(regexp = "^[SG]$",message = "没有这个评价录入形式")
	@NotBlank(message = "评价录入形式不能为空")
	private String inputValueType;
    @Pattern(regexp = "^[SP]$",message = "没有这个分数换算制度")
	@NotBlank(message = "评分制度不能为空, S:分数范围制度 P:分数比例制度")
	private String scoreType;
	private List<String> inputTypes;
	private List<String> auditorTypes;
	private Map<String,String> peopleTypesMap;
	//分数范围
	private List<String> scoreScopes;
	private Map<String,List<String>> educationScoreScopesMap;
	private String operator;
	private String mutualType;
	private List<Integer> author;
	private List<String> evaluation;
	/**
	 * 0:否  1 :是
	 */
	private String isAssignPoint;

	/**
	 * 学期分数占比
	 */
	private List<String> semesterScoreProp;
	/**
	 * 必修
	 */
	private List<DiathesisFieldDto> compulsoryField;
	/**
	 * 选修
	 */
	private List<DiathesisFieldDto> electiveField;
	/**
	 * 学业
	 */
	private List<DiathesisFieldDto> academicField;


	public List<String> getSemesterScoreProp() {
		return semesterScoreProp;
	}

	public void setSemesterScoreProp(List<String> semesterScoreProp) {
		this.semesterScoreProp = semesterScoreProp;
	}

	public String getIsAssignPoint() {
		return isAssignPoint;
	}

	public void setIsAssignPoint(String isAssignPoint) {
		this.isAssignPoint = isAssignPoint;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public List<String> getRankItems() {
		return rankItems;
	}

	public void setRankItems(List<String> rankItems) {
		this.rankItems = rankItems;
	}

	public List<String> getRankValues() {
		return rankValues;
	}

	public void setRankValues(List<String> rankValues) {
		this.rankValues = rankValues;
	}

	public String getInputValueType() {
		return inputValueType;
	}

	public void setInputValueType(String inputValueType) {
		this.inputValueType = inputValueType;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	public List<String> getInputTypes() {
		return inputTypes;
	}

	public void setInputTypes(List<String> inputTypes) {
		this.inputTypes = inputTypes;
	}

	public List<String> getAuditorTypes() {
		return auditorTypes;
	}

	public void setAuditorTypes(List<String> auditorTypes) {
		this.auditorTypes = auditorTypes;
	}

	public Map<String, String> getPeopleTypesMap() {
		return peopleTypesMap;
	}

	public void setPeopleTypesMap(Map<String, String> peopleTypesMap) {
		this.peopleTypesMap = peopleTypesMap;
	}

	public List<String> getScoreScopes() {
		return scoreScopes;
	}

	public void setScoreScopes(List<String> scoreScopes) {
		this.scoreScopes = scoreScopes;
	}

	public Map<String, List<String>> getEducationScoreScopesMap() {
		return educationScoreScopesMap;
	}

	public void setEducationScoreScopesMap(Map<String, List<String>> educationScoreScopesMap) {
		this.educationScoreScopesMap = educationScoreScopesMap;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getMutualType() {
		return mutualType;
	}

	public void setMutualType(String mutualType) {
		this.mutualType = mutualType;
	}

	public List<Integer> getAuthor() {
		return author;
	}

	public void setAuthor(List<Integer> author) {
		this.author = author;
	}

	public List<String> getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(List<String> evaluation) {
		this.evaluation = evaluation;
	}

	public List<DiathesisFieldDto> getCompulsoryField() {
		return compulsoryField;
	}

	public void setCompulsoryField(List<DiathesisFieldDto> compulsoryField) {
		this.compulsoryField = compulsoryField;
	}

	public List<DiathesisFieldDto> getElectiveField() {
		return electiveField;
	}

	public void setElectiveField(List<DiathesisFieldDto> electiveField) {
		this.electiveField = electiveField;
	}

	public List<DiathesisFieldDto> getAcademicField() {
		return academicField;
	}

	public void setAcademicField(List<DiathesisFieldDto> academicField) {
		this.academicField = academicField;
	}
}
