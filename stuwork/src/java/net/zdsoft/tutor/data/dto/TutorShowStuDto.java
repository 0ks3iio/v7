package net.zdsoft.tutor.data.dto;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.tutor.data.entity.TutorRecord;

/**
 * @author yangsj  2017年9月15日下午2:26:16
 * 
 * 当前导师下的学生列表数据
 */
public class TutorShowStuDto {
      
	private Student student;
	private TutorRecord tutorRecord;
	private String   gcName;  //行政班
	private String   classMaster;  //班主任
	public Student getStudent() {
		return student;
	}
	public void setStudent(Student student) {
		this.student = student;
	}
	public TutorRecord getTutorRecord() {
		return tutorRecord;
	}
	public void setTutorRecord(TutorRecord tutorRecord) {
		this.tutorRecord = tutorRecord;
	}
	public String getGcName() {
		return gcName;
	}
	public void setGcName(String gcName) {
		this.gcName = gcName;
	}
	public String getClassMaster() {
		return classMaster;
	}
	public void setClassMaster(String classMaster) {
		this.classMaster = classMaster;
	}
	
	
}
