package net.zdsoft.diathesis.data.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DiathesisRecordDto {
	
	private String id;//记录id
	private String projectId;//项目id
	private String projectName;//项目名称
	private String stuId;//学生id
	private String stuName;//学生姓名
	private String className;//班级名称
	private String creationTime;//录入人
	private String operator;//录入时间
	private String auditOpinion;//审核意见
	private String status;//审核状态
	private String auditTime;//审核时间
	private String auditor;//审核人
	private String acadyear;//学年
	private Integer semester;//学期
	private List<DiathesisRecordInfoDto> recordInfoList = new ArrayList<DiathesisRecordInfoDto>();//自定义字段集合
	private Map<String ,List<Object>> recordInfoMap = new HashMap<String, List<Object>>();//自定义字段map
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getStuId() {
		return stuId;
	}
	public void setStuId(String stuId) {
		this.stuId = stuId;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(String creationTime) {
		this.creationTime = creationTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getAuditOpinion() {
		return auditOpinion;
	}
	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(String auditTime) {
		this.auditTime = auditTime;
	}
	public String getAuditor() {
		return auditor;
	}
	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}
	public String getAcadyear() {
		return acadyear;
	}
	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}
	public Integer getSemester() {
		return semester;
	}
	public void setSemester(Integer semester) {
		this.semester = semester;
	}
	public List<DiathesisRecordInfoDto> getRecordInfoList() {
		return recordInfoList;
	}
	public void setRecordInfoList(List<DiathesisRecordInfoDto> recordInfoList) {
		this.recordInfoList = recordInfoList;
	}

	public Map<String, List<Object>> getRecordInfoMap() {
		return recordInfoMap;
	}

	public void setRecordInfoMap(Map<String, List<Object>> recordInfoMap) {
		this.recordInfoMap = recordInfoMap;
	}
}
