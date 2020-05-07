/*
* Project: v7
* Author : shenke
* @(#) TeachClassDto.java Created on 2016-9-23
* @Copyright (c) 2016 ZDSoft Inc. All rights reserved
*/
package net.zdsoft.basedata.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.constant.BaseConstants;
import net.zdsoft.basedata.entity.TeachClass;

/**
 *  
 * @author: shenke
 * @version: 1.0
 * 2016-9-23下午5:14:54
 */
public class TeachClassDto extends BaseDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private TeachClass teachClass;
	private String teacherName;//任课教师
	private List<String> assistantTeacherNames = new ArrayList<String>();//辅助教师
	private String assistantTeacherName;
	private String subjectName;	//科目名称
	private String classTypeName;	//班级属性
	private String batch;//在那个${PCKC!}；
	private int sum;//总人数
	private int maleNum;//男
	private int femaleNum;//女
	private Double average;//班级平均分
	private int classTypeNameInt;//用作根据班级属性排序
	
	public int getClassTypeNameInt() {
		if(BaseConstants.SUBJECT_TYPE_A.equals(classTypeName)){
			return 1;
		}else if(BaseConstants.SUBJECT_TYPE_B.equals(classTypeName)){
			return 0;
		}
		return -1;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}


	public int getMaleNum() {
		return maleNum;
	}

	public void setMaleNum(int maleNum) {
		this.maleNum = maleNum;
	}

	public int getFemaleNum() {
		return femaleNum;
	}

	public void setFemaleNum(int femaleNum) {
		this.femaleNum = femaleNum;
	}

	public Double getAverage() {
		return average;
	}

	public void setAverage(Double average) {
		this.average = average;
	}

	public TeachClassDto(){
		super();
	}
	
	public TeachClassDto(TeachClass teachClass) {
		super();
		this.teachClass = teachClass;
	}


	public TeachClass getTeachClass() {
		return teachClass;
	}


	public void setTeachClass(TeachClass teachClass) {
		this.teachClass = teachClass;
	}


	public String getTeacherName() {
		return teacherName;
	}


	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}


	public List<String> getAssistantTeacherNames() {
		return assistantTeacherNames;
	}


	public void setAssistantTeacherNames(List<String> assistantTeacherNames) {
		this.assistantTeacherNames = assistantTeacherNames;
	}


	public String getSubjectName() {
		return subjectName;
	}


	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}


	public String getClassTypeName() {
		return classTypeName;
	}


	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public String getAssistantTeacherName() {
		return assistantTeacherName;
	}

	public void setAssistantTeacherName(String assistantTeacherName) {
		this.assistantTeacherName = assistantTeacherName;
	}

}
