package net.zdsoft.basedata.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.basedata.entity.Course;

public class TeachGroupDto {
	private String teachGroupId; // 教研组Id
	private String teachGroupName; // 教研组名字
	private String subjectId; // 科目Id
	private String subjectName; // 科目名字
	private List<TeacherDto> mainTeacherList = new ArrayList<TeacherDto>();
	private List<TeacherDto> memberTeacherList = new ArrayList<TeacherDto>();
	private String mainTeacherIds;
	private String mainTeacherNames;
	private String memberTeacherIds;
	private String memberTeacherName;
	private List<Course> courseList = new ArrayList<Course>();
	//teacherId,teacherName,状态0或者1 默认0
	private List<String[]> showList=new ArrayList<>();
	
	private Integer orderId;

	public List<Course> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<Course> courseList) {
		this.courseList = courseList;
	}

	public String getMainTeacherIds() {
		return mainTeacherIds;
	}

	public void setMainTeacherIds(String mainTeacherIds) {
		this.mainTeacherIds = mainTeacherIds;
	}

	public String getMainTeacherNames() {
		return mainTeacherNames;
	}

	public void setMainTeacherNames(String mainTeacherNames) {
		this.mainTeacherNames = mainTeacherNames;
	}

	public String getMemberTeacherIds() {
		return memberTeacherIds;
	}

	public void setMemberTeacherIds(String memberTeacherIds) {
		this.memberTeacherIds = memberTeacherIds;
	}

	public String getMemberTeacherName() {
		return memberTeacherName;
	}

	public void setMemberTeacherName(String memberTeacherName) {
		this.memberTeacherName = memberTeacherName;
	}

	public String getTeachGroupId() {
		return teachGroupId;
	}

	public void setTeachGroupId(String teachGroupId) {
		this.teachGroupId = teachGroupId;
	}

	public String getTeachGroupName() {
		return teachGroupName;
	}

	public void setTeachGroupName(String teachGroupName) {
		this.teachGroupName = teachGroupName;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<TeacherDto> getMainTeacherList() {
		return mainTeacherList;
	}

	public void setMainTeacherList(List<TeacherDto> mainTeacherList) {
		this.mainTeacherList = mainTeacherList;
	}

	public List<TeacherDto> getMemberTeacherList() {
		return memberTeacherList;
	}

	public void setMemberTeacherList(List<TeacherDto> memberTeacherList) {
		this.memberTeacherList = memberTeacherList;
	}

	public List<String[]> getShowList() {
		return showList;
	}

	public void setShowList(List<String[]> showList) {
		this.showList = showList;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
