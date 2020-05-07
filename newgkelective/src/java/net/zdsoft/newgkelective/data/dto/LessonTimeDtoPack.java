package net.zdsoft.newgkelective.data.dto;

import java.util.List;

import net.zdsoft.basedata.entity.Grade;

public class LessonTimeDtoPack {
	private boolean needSource = false;
	private String groupType;
	private String objType;
	private String sourceType;
	private String lessonArrayItemId;
	private List<LessonTimeDto> sourceTimeDto;// 主表信息
	private List<LessonTimeDto> lessonTimeDto;
	private String selGradeId;
	private boolean basicSave = false;
	private Grade gradeDto;

	public List<LessonTimeDto> getLessonTimeDto() {
		return lessonTimeDto;
	}

	public void setLessonTimeDto(List<LessonTimeDto> lessonTimeDto) {
		this.lessonTimeDto = lessonTimeDto;
	}

	public List<LessonTimeDto> getSourceTimeDto() {
		return sourceTimeDto;
	}

	public void setSourceTimeDto(List<LessonTimeDto> sourceTimeDto) {
		this.sourceTimeDto = sourceTimeDto;
	}

	public boolean isNeedSource() {
		return needSource;
	}

	public void setNeedSource(boolean needSource) {
		this.needSource = needSource;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getObjType() {
		return objType;
	}

	public void setObjType(String objType) {
		this.objType = objType;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getLessonArrayItemId() {
		return lessonArrayItemId;
	}

	public void setLessonArrayItemId(String lessonArrayItemId) {
		this.lessonArrayItemId = lessonArrayItemId;
	}

	public String getSelGradeId() {
		return selGradeId;
	}

	public void setSelGradeId(String selGradeId) {
		this.selGradeId = selGradeId;
	}

	public boolean isBasicSave() {
		return basicSave;
	}

	public void setBasicSave(boolean basicSave) {
		this.basicSave = basicSave;
	}

	public Grade getGradeDto() {
		return gradeDto;
	}

	public void setGradeDto(Grade gradeDto) {
		this.gradeDto = gradeDto;
	}
	
}
