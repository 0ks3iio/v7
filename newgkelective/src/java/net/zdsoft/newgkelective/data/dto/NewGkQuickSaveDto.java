package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

public class NewGkQuickSaveDto {
	private String subjectType;
	private String subjectIds;
	private String subjectIdsB;
	/**
	 * 开班数
	 */
	private int classNum;
	/**
	 * 每班数量
	 */
	private int classStudentNum;
	/**
	 * 安排人数
	 */
	private int arrangeStudentNum;
	/**
	 * 0:无依据
	   1:按该选科成绩排名  ---3+0 1+x----2+x 混合
	   2:按该选科与语数英成绩之和排名---3+0 1+x----2+x 混合
	 */
	private String openBasis;
	/**
	 * 
	 *  0:随机 ---3+0 1+x
	    1:按顺序分发 ---3+0 1+x
	   	2:按顺序交叉分发 ---3+0 1+x
	 */
	private String basisType;
	
	/**
	 * 以下 2+x
	 */
	private String className;
	
	private List<NewGkQuickGroupDto> dtoList=new ArrayList<>();

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public String getSubjectIds() {
		return subjectIds;
	}

	public void setSubjectIds(String subjectIds) {
		this.subjectIds = subjectIds;
	}

	public String getSubjectIdsB() {
		return subjectIdsB;
	}

	public void setSubjectIdsB(String subjectIdsB) {
		this.subjectIdsB = subjectIdsB;
	}

	public int getClassNum() {
		return classNum;
	}

	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}

	public int getClassStudentNum() {
		return classStudentNum;
	}

	public void setClassStudentNum(int classStudentNum) {
		this.classStudentNum = classStudentNum;
	}

	public String getOpenBasis() {
		return openBasis;
	}

	public void setOpenBasis(String openBasis) {
		this.openBasis = openBasis;
	}

	public String getBasisType() {
		return basisType;
	}

	public void setBasisType(String basisType) {
		this.basisType = basisType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<NewGkQuickGroupDto> getDtoList() {
		return dtoList;
	}

	public void setDtoList(List<NewGkQuickGroupDto> dtoList) {
		this.dtoList = dtoList;
	}

	public int getArrangeStudentNum() {
		return arrangeStudentNum;
	}

	public void setArrangeStudentNum(int arrangeStudentNum) {
		this.arrangeStudentNum = arrangeStudentNum;
	}
	
}
