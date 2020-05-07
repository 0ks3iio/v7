package net.zdsoft.gkelective.data.dto;

import java.util.List;
import java.util.Map;

import net.zdsoft.basedata.entity.Student;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;

public class GkSubjectArrangeDto {
	private GkSubjectArrange gsaEnt;
	//扩展信息
	private String openAcadyear;//年级开设学年使用排序只要前4位
	private String[] subjectIds;//学生选课信息
	private List<GkSubject> gksubList;
	private Map<String,String> teacherMap;//教师信息
	private String gradeName;
	private Student stu;
	private boolean stuXuanKe;//学生选课了就不能修改科目
	
	public boolean isStuXuanKe() {
		return stuXuanKe;
	}
	public void setStuXuanKe(boolean stuXuanKe) {
		this.stuXuanKe = stuXuanKe;
	}
	public Student getStu() {
		return stu;
	}
	public void setStu(Student stu) {
		this.stu = stu;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public Map<String, String> getTeacherMap() {
		return teacherMap;
	}
	public void setTeacherMap(Map<String, String> teacherMap) {
		this.teacherMap = teacherMap;
	}
	public List<GkSubject> getGksubList() {
		return gksubList;
	}
	public void setGksubList(List<GkSubject> gksubList) {
		this.gksubList = gksubList;
	}
	public String[] getSubjectIds() {
		return subjectIds;
	}
	public void setSubjectIds(String[] subjectIds) {
		this.subjectIds = subjectIds;
	}
	public String getOpenAcadyear() {
		return openAcadyear;
	}
	public void setOpenAcadyear(String openAcadyear) {
		this.openAcadyear = openAcadyear;
	}
	public GkSubjectArrange getGsaEnt() {
		return gsaEnt;
	}
	public void setGsaEnt(GkSubjectArrange gsaEnt) {
		this.gsaEnt = gsaEnt;
	}
	
}
