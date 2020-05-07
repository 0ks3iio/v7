package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CGClass")
public class CGClass implements Serializable {
	private CGCourse course;
	private int jxbIndex;				//同一个Course中几个班级的序号
	private String jxbId;				//唯一
	private CGTeacher teacher;
	private int avgStudentCount;
	private boolean isFixed;
	private int isBiWeekly = CGCourse.WEEK_TYPE_NORMAL;

	private List<CGStudent> studentList;
	
	private String oldId;//行政班拆分成教学班 原行政班id
	private String className;
	
	private String batch;
	private boolean pure3 = false;

	private int lectureCount;
	// 拆分以前的班级
	private String parentId;
	
	public CGClass() {
		super();
	}

	public CGClass(CGCourse course, boolean isFixed, List<CGStudent> studentList) {
		super();
		this.course = course;
		this.isFixed = isFixed;
		this.studentList = studentList;
	}
	public CGCourse getCourse() {
		return course;
	}

	public void setCourse(CGCourse course) {
		this.course = course;
	}

	public int getJxbIndex() {
		return jxbIndex;
	}

	public void setJxbIndex(int jxbIndex) {
		this.jxbIndex = jxbIndex;
	}

	public CGTeacher getTeacher() {
		return teacher;
	}

	public void setTeacher(CGTeacher teacher) {
		this.teacher = teacher;
	}

	public List<CGStudent> getStudentList() {
		return studentList;
	}

	public void setStudentList(List<CGStudent> studentList) {
		this.studentList = studentList;
	}

	public String getJxbId() {
		return jxbId;
	}

	public void setJxbId(String jxbId) {
		this.jxbId = jxbId;
	}

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	public int getAvgStudentCount() {
		return avgStudentCount;
	}

	public void setAvgStudentCount(int avgStudentCount) {
		this.avgStudentCount = avgStudentCount;
	}

	public boolean isFixed() {
		return isFixed;
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public boolean isPure3() {
		return pure3;
	}

	public void setPure3(boolean pure3) {
		this.pure3 = pure3;
	}

	public int getLectureCount() {
		return lectureCount;
	}

	public void setLectureCount(int lectureCount) {
		this.lectureCount = lectureCount;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getIsBiWeekly() {
		return isBiWeekly;
	}

	public void setIsBiWeekly(Integer isBiWeekly) {
		this.isBiWeekly = isBiWeekly;
	}
}
