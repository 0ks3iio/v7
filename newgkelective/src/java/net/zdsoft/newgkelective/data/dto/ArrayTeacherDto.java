package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

public class ArrayTeacherDto {
	private String className;// 班级名称
	private String placeName;// 上课教室
	private String haveClassCode;// 上课时间代码(0-1代表星期一第一节等)
	private String haveClassTime;// 上课时间(周三(第一节、第四节))
	private String timetibleId;// 这个是必须带过去的(timetableId)
	private String teacherId;// 老师id
	private String teacherName;// 老师姓名
	private Integer HeapNum;// 所属组别
	private Integer index;// 下标，方便封装list
	private String timetableTeachId;
	
	private String subjectType;
	//时间1-2-1
	private List<String> timeList=new ArrayList<String>();

	public String getTimetableTeachId() {
		return timetableTeachId;
	}

	public void setTimetableTeachId(String timetableTeachId) {
		this.timetableTeachId = timetableTeachId;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public Integer getHeapNum() {
		return HeapNum;
	}

	public void setHeapNum(Integer heapNum) {
		HeapNum = heapNum;
	}

	public String getTimetibleId() {
		return timetibleId;
	}

	public void setTimetibleId(String timetibleId) {
		this.timetibleId = timetibleId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPlaceName() {
		return placeName;
	}

	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}

	public String getHaveClassCode() {
		return haveClassCode;
	}

	public void setHaveClassCode(String haveClassCode) {
		this.haveClassCode = haveClassCode;
	}

	public String getHaveClassTime() {
		return haveClassTime;
	}

	public void setHaveClassTime(String haveClassTime) {
		this.haveClassTime = haveClassTime;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public List<String> getTimeList() {
		return timeList;
	}

	public void setTimeList(List<String> timeList) {
		this.timeList = timeList;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

}
