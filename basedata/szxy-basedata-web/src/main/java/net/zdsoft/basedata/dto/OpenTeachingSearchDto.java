package net.zdsoft.basedata.dto;
/**
 * 开课计划查询dto
 *
 */
public class OpenTeachingSearchDto {
	private String unitId;
	private String acadyear;
	private String semester;
	private Integer isDeleted;
	
	private String[] gradeIds;
	private String subjectType;//1必修 2选修
	
	private String subjectId;
	private String[] subjectIds;
	private Integer isTeaCls;//是否以教学班形式
	private String[] classIds;
	private String[] subjectTypes;
	
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
	public String[] getGradeIds() {
		return gradeIds;
	}
	public void setGradeIds(String[] gradeIds) {
		this.gradeIds = gradeIds;
	}
	public Integer getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public Integer getIsTeaCls() {
		return isTeaCls;
	}
	public void setIsTeaCls(Integer isTeaCls) {
		this.isTeaCls = isTeaCls;
	}
	public String[] getClassIds() {
		return classIds;
	}
	public void setClassIds(String[] classIds) {
		this.classIds = classIds;
	}
	public String[] getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String[] subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String[] getSubjectTypes() {
		return subjectTypes;
	}
	public void setSubjectTypes(String[] subjectTypes) {
		this.subjectTypes = subjectTypes;
	}
	

}
