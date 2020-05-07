package net.zdsoft.basedata.dto;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.TeachClass;

import org.apache.commons.lang3.StringUtils;

public class TeachClassSearchDto {
	private String unitId;
	private String acadyearSearch;
	private String semesterSearch;
	// private String gradeId;
	private String gradeIds;// 多个参数以，隔开
	private String showTabType;// 0:7选3 1:必修课 2:选修课   头部tab值
	private String classType;// 转换成教学班类型

	private String teachClassName;

	private String isUsing;

	private String subjectId;

	private String showView;// 0:查看 1:修改

	private String isUsingMerge;

	private String teacherId;
	
	private String teachClassType;//教学班类型,普通班 用于合并的小班，合并大班
	
	private String studentName;
	
	private String teacherName;
	
	private String relaCourseId;
	
	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getTeachClassType() {
		return teachClassType;
	}

	public void setTeachClassType(String teachClassType) {
		this.teachClassType = teachClassType;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getIsUsingMerge() {
		return isUsingMerge;
	}

	public void setIsUsingMerge(String isUsingMerge) {
		this.isUsingMerge = isUsingMerge;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAcadyearSearch() {
		return acadyearSearch;
	}

	public void setAcadyearSearch(String acadyearSearch) {
		this.acadyearSearch = acadyearSearch;
	}

	public String getSemesterSearch() {
		return semesterSearch;
	}

	public void setSemesterSearch(String semesterSearch) {
		this.semesterSearch = semesterSearch;
	}

	// public String getGradeId() {
	// return gradeId;
	// }
	// public void setGradeId(String gradeId) {
	// this.gradeId = gradeId;
	// }
	public String getShowTabType() {
		return showTabType;
	}

	public void setShowTabType(String showTabType) {
		this.showTabType = showTabType;
	}

	public String getClassType() {
		return classType;
	}

	public void setClassType(String classType) {
		this.classType = classType;
	}

	public void makeClassType() {
		if (StringUtils.isNotBlank(showTabType)) {
			if (String.valueOf(BaseConstants.TYPE_COURSE_DISCIPLINE).equals(
					showTabType)) {
				classType = TeachClass.CLASS_TYPE_SEVEN;
			} else if (BaseConstants.SUBJECT_TYPE_XX.equals(showTabType)) {
				classType = TeachClass.CLASS_TYPE_ELECTIVE;
			} else if (BaseConstants.SUBJECT_TYPE_BX.equals(showTabType)) {
				classType = TeachClass.CLASS_TYPE_REQUIRED;
			}
		}
	}

	public String getGradeIds() {
		return gradeIds;
	}

	public void setGradeIds(String gradeIds) {
		this.gradeIds = gradeIds;
	}

	public String getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(String isUsing) {
		this.isUsing = isUsing;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getTeachClassName() {
		return teachClassName;
	}

	public void setTeachClassName(String teachClassName) {
		this.teachClassName = teachClassName;
	}

	public String getShowView() {
		return showView;
	}

	public void setShowView(String showView) {
		this.showView = showView;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getRelaCourseId() {
		return relaCourseId;
	}

	public void setRelaCourseId(String relaCourseId) {
		this.relaCourseId = relaCourseId;
	}
	
	

}
