package net.zdsoft.stuwork.data.dto;

import java.util.List;
import java.util.Map;

import net.zdsoft.stuwork.data.entity.DyStuHealthProjectItem;
import net.zdsoft.stuwork.data.entity.DyStuHealthResult;

/**
 * @author yangsj  2017年9月27日下午4:39:45
 */
public class DyStuHealthInputDto {
  
	private String studentName;
	private List<DyStuHealthProjectItem> listDSHPI;
	private String sex;
	private String className;
	private String schoolYearTeam;  //学年学期
	private String studentId;
	private String acadyear;
	private String semester;
	private Map<String, String> itemIdMap;
	
	
	
	public Map<String, String> getItemIdMap() {
		return itemIdMap;
	}
	public void setItemIdMap(Map<String, String> itemIdMap) {
		this.itemIdMap = itemIdMap;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public String getSemester() {
		return semester;
	}
	public void setSemester(String semester) {
		this.semester = semester;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getSchoolYearTeam() {
		return schoolYearTeam;
	}
	public void setSchoolYearTeam(String schoolYearTeam) {
		this.schoolYearTeam = schoolYearTeam;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public List<DyStuHealthProjectItem> getListDSHPI() {
		return listDSHPI;
	}
	public void setListDSHPI(List<DyStuHealthProjectItem> listDSHPI) {
		this.listDSHPI = listDSHPI;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	
}
