package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CGStudent")
public class CGStudent implements Serializable, Comparable<CGStudent>{

	private String studentId;
	private String studentName;
	private String sex;
	private String classId; 	//（可选）原来行政班的ID, 不用于本次排课
	private String className; 	//（可选）原来行政班的名字, 不用于本次排课
	private double lectureCount;   //该学生，一个星期总共有几次课
	private List<CGCourse> courseList; //该学生要上的所有的课
	
	//根据学生的选课情况，返回其组合ID，就是选中的3门的名字的组合
	public String getGroupId() {
		//(Developer o1, Developer o2)->o1.getName().compareTo(o2.getName());
		List<CGCourse> majorCourses = courseList.stream()
			      .filter(e -> e.getType() == CGCourse.CourseType.MAJOR_COURSE)
			      .sorted((CGCourse c1, CGCourse c2) -> c1.getCode().compareTo(c2.getCode()))
			      .collect(Collectors.toList());
		String groupId = "";
		for (CGCourse c : majorCourses) {
			groupId += c.getCode();
		}
		
		return groupId;
	}
	
	
	public void addCourse(CGCourse course) {
		if(courseList == null) {
			courseList = new ArrayList<>();
		}
		courseList.add(course);
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
 
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}

	public double getLectureCount() {
		return lectureCount;
	}
	public void setLectureCount(double lectureCount) {
		this.lectureCount = lectureCount;
	}

	public List<CGCourse> getCourseList() {
		return courseList;
	}

	public void setCourseList(List<CGCourse> courseList) {
		this.courseList = courseList;
	}


	@Override
	public int compareTo(CGStudent sc2) {
		return this.getStudentId().compareTo(sc2.getStudentId());
	}
}
