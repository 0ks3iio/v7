package net.zdsoft.tutor.data.dto;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.basedata.entity.Teacher;

/**
 * @author yangsj  2017年9月14日上午9:40:44
 */
public class TutorAddTeacherDto {
	
	private Teacher teacher;
	private String students;
	private Integer isChooseNum;
	private String param;
	private String isAdd;  //导师是否已添加  1 -- 添加  0 --- 未添加
	private String isFull; //学生数量是否已满  1 -- 已满  0 -- 未满
	public String getIsFull() {
		return isFull;
	}
	public void setIsFull(String isFull) {
		if(isChooseNum >= Integer.valueOf(param)) {
			isFull = "1";
		}else {
			isFull = "0";
		}
		this.isFull = isFull;
	}
	
	public Teacher getTeacher() {
		return teacher;
	}
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	public String getStudents() {
		return students;
	}
	public void setStudents(List<Student> students) {
		StringBuilder studentNames = new StringBuilder();
		String stuNames = null ;
		if(!CollectionUtils.isEmpty(students)) {
			
			for (Student student : students) {
				studentNames.append(student.getStudentName()+",");
			}
			stuNames = StringUtils.removeEnd(studentNames.toString(), ",");
		}
		this.students = stuNames;
	}
	public String getIsAdd() {
		return isAdd;
	}
	public void setIsAdd(String isAdd) {
		this.isAdd = isAdd;
	}
	
	public Integer getIsChooseNum() {
		return isChooseNum;
	}
	public void setIsChooseNum(Integer isChooseNum) {
		this.isChooseNum = isChooseNum;
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
	}
	
	

}
