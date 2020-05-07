package net.zdsoft.newgkelective.data.dto;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Course;
import net.zdsoft.newgkelective.data.entity.NewGkClassBatch;
import net.zdsoft.newgkelective.data.entity.NewGkDivideClass;

public class BatchClassDto {
	private String batch;
	private Course course;
	private Integer stuNum;
	private Integer freeStuNum;
	// 已安排班级
	private List<NewGkDivideClass> devideClassList;
	// 与组合班关联的教学班
	private List<NewGkDivideClass> combinationDivideClassList;
	private String classId;
	// 开班数
	private Integer classNum;
	// 走班学生分布使用
	private List<NewGkClassBatch> clsBatchs;
	private NewGkDivideClass devideClass;
	private Map<String,Map<String,Integer>> map;
	private Integer maxCourseNum;
	// 不便于在FTL组装的数据
	private String otherInfo;
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public Integer getStuNum() {
		return stuNum;
	}
	public void setStuNum(Integer stuNum) {
		this.stuNum = stuNum;
	}
	public Integer getFreeStuNum() {
		return freeStuNum;
	}
	public void setFreeStuNum(Integer freeStuNum) {
		this.freeStuNum = freeStuNum;
	}
	public List<NewGkDivideClass> getDevideClassList() {
		return devideClassList;
	}
	public void setDevideClassList(List<NewGkDivideClass> devideClassList) {
		this.devideClassList = devideClassList;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public Integer getClassNum() {
		return classNum;
	}
	public void setClassNum(Integer classNum) {
		this.classNum = classNum;
	}
	public List<NewGkClassBatch> getClsBatchs() {
		return clsBatchs;
	}
	public void setClsBatchs(List<NewGkClassBatch> clsBatchs) {
		this.clsBatchs = clsBatchs;
	}
	public NewGkDivideClass getDevideClass() {
		return devideClass;
	}
	public void setDevideClass(NewGkDivideClass devideClass) {
		this.devideClass = devideClass;
	}
	public List<NewGkDivideClass> getCombinationDivideClassList() {
		return combinationDivideClassList;
	}
	public void setCombinationDivideClassList(List<NewGkDivideClass> combinationDivideClassList) {
		this.combinationDivideClassList = combinationDivideClassList;
	}
	public Map<String, Map<String, Integer>> getMap() {
		return map;
	}
	public void setMap(Map<String, Map<String, Integer>> map) {
		this.map = map;
	}
	public Integer getMaxCourseNum() {
		return maxCourseNum;
	}
	public void setMaxCourseNum(Integer maxCourseNum) {
		this.maxCourseNum = maxCourseNum;
	}
	public String getOtherInfo() {
		return otherInfo;
	}
	public void setOtherInfo(String otherInfo) {
		this.otherInfo = otherInfo;
	}
}
