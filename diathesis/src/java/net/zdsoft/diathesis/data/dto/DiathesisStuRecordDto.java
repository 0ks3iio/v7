package net.zdsoft.diathesis.data.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiathesisStuRecordDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String typeName;
	public String projectName;
	public String timeId;
	//一条写实记录一个行
	public String textContent;
	//public List<String> textContentList=new ArrayList<>();
	public List<Object[]> fileList=new ArrayList<>();//附件

	//分数
	public String score;
	
	public int typeSort;
	public int projectSort;

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public String getTextContent() {
		return textContent;
	}
	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}
	public List<Object[]> getFileList() {
		return fileList;
	}
	public void setFileList(List<Object[]> fileList) {
		this.fileList = fileList;
	}
	public int getTypeSort() {
		return typeSort;
	}
	public void setTypeSort(int typeSort) {
		this.typeSort = typeSort;
	}
	public int getProjectSort() {
		return projectSort;
	}
	public void setProjectSort(int projectSort) {
		this.projectSort = projectSort;
	}
	
	
}
