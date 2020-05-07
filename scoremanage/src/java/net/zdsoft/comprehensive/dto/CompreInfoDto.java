package net.zdsoft.comprehensive.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.comprehensive.entity.CompreInfo;

/**
 * 总评dto
 * @author niuchao
 * @date 2018-12-11
 *
 */
public class CompreInfoDto {
	
	private String gradeId;
	private String gradeName;
	private String openAcadyear;//入学年份
	private String curkey;//当前，如332
	private List<CompreInfo> infoList = new ArrayList<CompreInfo>();
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getOpenAcadyear() {
		return openAcadyear;
	}
	public void setOpenAcadyear(String openAcadyear) {
		this.openAcadyear = openAcadyear;
	}
	public String getCurkey() {
		return curkey;
	}
	public void setCurkey(String curkey) {
		this.curkey = curkey;
	}
	public List<CompreInfo> getInfoList() {
		return infoList;
	}
	public void setInfoList(List<CompreInfo> infoList) {
		this.infoList = infoList;
	}
	
}
