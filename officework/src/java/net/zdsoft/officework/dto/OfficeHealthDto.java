package net.zdsoft.officework.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.zdsoft.basedata.entity.Student;

public class OfficeHealthDto {
	
	private String url;
	private String username;
	private String password;
	private Map<String, Set<String>> studentCodes = new HashMap<String, Set<String>>();//unitId--studentcodes
	private Map<String, Student> studentMap = new HashMap<String, Student>();//unitId_studentcode--student
	private Map<String,String> devSnMap = new HashMap<String, String>();//devsn--unitId
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Map<String, Set<String>> getStudentCodes() {
		return studentCodes;
	}
	public void setStudentCodes(Map<String, Set<String>> studentCodes) {
		this.studentCodes = studentCodes;
	}
	public Map<String, Student> getStudentMap() {
		return studentMap;
	}
	public void setStudentMap(Map<String, Student> studentMap) {
		this.studentMap = studentMap;
	}
	public Map<String, String> getDevSnMap() {
		return devSnMap;
	}
	public void setDevSnMap(Map<String, String> devSnMap) {
		this.devSnMap = devSnMap;
	}
	
}
