package net.zdsoft.gkelective.data.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public class SingleOpenResultDto {
	
	private int batch;
	private int classNum;
	private Map<String,Integer> subjectNum = new LinkedHashMap<String, Integer>();
	private Map<String,Integer> subjectNoTeaNum = new LinkedHashMap<String, Integer>();
	
	private String[] subjectNames;
	private Integer[] subjectNums;
	private Integer[] subjectNoTeaNums;
	
	public int getClassNum() {
		return classNum;
	}
	public void setClassNum(int classNum) {
		this.classNum = classNum;
	}
	public int getBatch() {
		return batch;
	}
	public void setBatch(int batch) {
		this.batch = batch;
	}
	public Map<String, Integer> getSubjectNum() {
		return subjectNum;
	}
	public void setSubjectNum(Map<String, Integer> subjectNum) {
		this.subjectNum = subjectNum;
	}
	public Map<String, Integer> getSubjectNoTeaNum() {
		return subjectNoTeaNum;
	}
	public void setSubjectNoTeaNum(Map<String, Integer> subjectNoTeaNum) {
		this.subjectNoTeaNum = subjectNoTeaNum;
	}
	public String[] getSubjectNames() {
		return subjectNames;
	}
	public void setSubjectNames(String[] subjectNames) {
		this.subjectNames = subjectNames;
	}
	public Integer[] getSubjectNums() {
		return subjectNums;
	}
	public void setSubjectNums(Integer[] subjectNums) {
		this.subjectNums = subjectNums;
	}
	public Integer[] getSubjectNoTeaNums() {
		return subjectNoTeaNums;
	}
	public void setSubjectNoTeaNums(Integer[] subjectNoTeaNums) {
		this.subjectNoTeaNums = subjectNoTeaNums;
	}

}
