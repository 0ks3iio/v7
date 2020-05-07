package net.zdsoft.tutor.data.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.tutor.data.entity.TutorRecordDetailed;

/**
 * @author yangsj  2017年11月20日上午10:40:42
 */
public class TutorShowRecordDto {
   
	private TutorRecordDetailed tutorRecordDetailed;
	private String   gcName;  //行政班
	private String  stuNames;
	private String teacherName;
	
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public TutorRecordDetailed getTutorRecordDetailed() {
		return tutorRecordDetailed;
	}
	public void setTutorRecordDetailed(TutorRecordDetailed tutorRecordDetailed) {
		this.tutorRecordDetailed = tutorRecordDetailed;
	}
	public String getGcName() {
		return gcName;
	}
	public void setGcName(String gcName) {
		this.gcName = gcName;
	}
	public String getStuNames() {
		return stuNames;
	}
	public void setStuNames(List<Student> students) {
		StringBuilder studentNames = new StringBuilder();
		String stuNames = null ;
		if(!CollectionUtils.isEmpty(students)) {
			
			for (Student student : students) {
				studentNames.append(student.getStudentName()+",");
			}
			stuNames = StringUtils.removeEnd(studentNames.toString(), ",");
		}
		this.stuNames = stuNames;
	}
	
}
