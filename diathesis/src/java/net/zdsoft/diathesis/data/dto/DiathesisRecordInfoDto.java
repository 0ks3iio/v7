package net.zdsoft.diathesis.data.dto;

import net.zdsoft.diathesis.data.entity.DiathesisOption;

import java.util.ArrayList;
import java.util.List;

public class DiathesisRecordInfoDto {

	private String id;//infoId
	private String title;//自定义字段名称
	private String structureId;//自定义字段id
	private String dataType;//自定义字段类型
	private String contentTxt;//自定义字段数据库的值
	private String resultTxt;//自定义字段实际展示值

	private Integer isMust; //是否必填
	private List<DiathesisOption> optionList = new ArrayList<DiathesisOption>();//选项集合

	private List<FileDto> fileList=new ArrayList<>();

	public List<FileDto> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileDto> fileList) {
		this.fileList = fileList;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStructureId() {
		return structureId;
	}
	public void setStructureId(String structureId) {
		this.structureId = structureId;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getContentTxt() {
		return contentTxt;
	}
	public void setContentTxt(String contentTxt) {
		this.contentTxt = contentTxt;
	}
	public String getResultTxt() {
		return resultTxt;
	}
	public void setResultTxt(String resultTxt) {
		this.resultTxt = resultTxt;
	}
	public Integer getIsMust() {
		return isMust;
	}
	public void setIsMust(Integer isMust) {
		this.isMust = isMust;
	}
	public List<DiathesisOption> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<DiathesisOption> optionList) {
		this.optionList = optionList;
	}
	
}


