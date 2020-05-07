package net.zdsoft.eclasscard.data.dto.cache;

import java.util.List;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Student;

public class StudentCacheDto {

	//常用字段缓存
	private String id;
	private String schoolId;
	private String classId;
	private String studentName;
	private String studentCode;
	private Integer sex;
	
	
	//以下非缓存字段
	private String className;
	private Integer failTimes = 0;
	private String showPictrueUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getShowPictrueUrl() {
		return showPictrueUrl;
	}

	public void setShowPictrueUrl(String showPictrueUrl) {
		this.showPictrueUrl = showPictrueUrl;
	}
	
	public Integer getFailTimes() {
		return failTimes;
	}

	public void setFailTimes(Integer failTimes) {
		this.failTimes = failTimes;
	}

	public static List<StudentCacheDto> toStudentCacheDto(List<Student> students){
		List<StudentCacheDto> studtos = Lists.newArrayList();
		for(Student stu:students){
			StudentCacheDto dto = new StudentCacheDto();
			dto.setClassId(stu.getClassId());
			dto.setId(stu.getId());
			dto.setSchoolId(stu.getSchoolId());
			dto.setSex(stu.getSex());
			dto.setStudentCode(stu.getStudentCode());
			dto.setStudentName(stu.getStudentName());
			studtos.add(dto);
		}
		return studtos;
	}
}
