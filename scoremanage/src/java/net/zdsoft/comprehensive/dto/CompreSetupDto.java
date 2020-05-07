package net.zdsoft.comprehensive.dto;

import java.util.List;

/**
 * 总评科目来源dto
 * @author niuchao
 * @date 2018-12-11
 *
 */
public class CompreSetupDto {
	
	private String examId;
	private String examName;
	private String subjectNames;
	private List<String> subjectNameList;
	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
	public String getExamName() {
		return examName;
	}
	public void setExamName(String examName) {
		this.examName = examName;
	}
	public String getSubjectNames() {
		return subjectNames;
	}
	public void setSubjectNames(String subjectNames) {
		this.subjectNames = subjectNames;
	}
	public List<String> getSubjectNameList() {
		return subjectNameList;
	}
	public void setSubjectNameList(List<String> subjectNameList) {
		this.subjectNameList = subjectNameList;
	}

}
