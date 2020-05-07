package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkArrayItem;
import net.zdsoft.newgkelective.data.entity.NewGkChoRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassCombineRelation;
import net.zdsoft.newgkelective.data.entity.NewGkClassSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTime;
import net.zdsoft.newgkelective.data.entity.NewGkLessonTimeEx;
import net.zdsoft.newgkelective.data.entity.NewGkRelateSubtime;
import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;
import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlanEx;

public class ArrayFeaturesDto {
	private NewGkArrayItem arrayItem;
	private List<NewGkLessonTime> lessonTimeList ;
	private List<NewGkLessonTimeEx> lessonTimeExList;
	private List<NewGkSubjectTime> subjectTimes;
	private List<NewGkChoRelation> relationList;
	
	private List<NewGkTeacherPlan> teacherPlanList;
	private List<NewGkTeacherPlanEx> teacherPlanExList;
	
	private List<NewGkRelateSubtime> relateSubTimeList;
	
	private List<NewGkClassSubjectTime> newClasSubjTimes;
	private List<NewGkClassCombineRelation> newCombineList;
	
	public NewGkArrayItem getArrayItem() {
		return arrayItem;
	}
	public void setArrayItem(NewGkArrayItem arrayItem) {
		this.arrayItem = arrayItem;
	}
	public List<NewGkLessonTime> getLessonTimeList() {
		return lessonTimeList;
	}
	public void setLessonTimeList(List<NewGkLessonTime> lessonTimeList) {
		this.lessonTimeList = lessonTimeList;
	}
	public List<NewGkLessonTimeEx> getLessonTimeExList() {
		return lessonTimeExList;
	}
	public void setLessonTimeExList(List<NewGkLessonTimeEx> lessonTimeExList) {
		this.lessonTimeExList = lessonTimeExList;
	}
	public List<NewGkSubjectTime> getSubjectTimes() {
		return subjectTimes;
	}
	public void setSubjectTimes(List<NewGkSubjectTime> subjectTimes) {
		this.subjectTimes = subjectTimes;
	}
	public List<NewGkChoRelation> getRelationList() {
		return relationList;
	}
	public void setRelationList(List<NewGkChoRelation> relationList) {
		this.relationList = relationList;
	}
	public List<NewGkTeacherPlan> getTeacherPlanList() {
		return teacherPlanList;
	}
	public void setTeacherPlanList(List<NewGkTeacherPlan> teacherPlanList) {
		this.teacherPlanList = teacherPlanList;
	}
	public List<NewGkTeacherPlanEx> getTeacherPlanExList() {
		return teacherPlanExList;
	}
	public void setTeacherPlanExList(List<NewGkTeacherPlanEx> teacherPlanExList) {
		this.teacherPlanExList = teacherPlanExList;
	}
	public List<NewGkRelateSubtime> getRelateSubTimeList() {
		return relateSubTimeList;
	}
	public void setRelateSubTimeList(List<NewGkRelateSubtime> relateSubTimeList) {
		this.relateSubTimeList = relateSubTimeList;
	}
	public List<NewGkClassSubjectTime> getNewClasSubjTimes() {
		return newClasSubjTimes;
	}
	public void setNewClasSubjTimes(List<NewGkClassSubjectTime> newClasSubjTimes) {
		this.newClasSubjTimes = newClasSubjTimes;
	}
	public List<NewGkClassCombineRelation> getNewCombineList() {
		return newCombineList;
	}
	public void setNewCombineList(List<NewGkClassCombineRelation> newCombineList) {
		this.newCombineList = newCombineList;
	}
}
