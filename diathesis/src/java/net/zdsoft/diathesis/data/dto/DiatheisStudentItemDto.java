package net.zdsoft.diathesis.data.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 每一项详细信息
 */
public class DiatheisStudentItemDto {
	private String projectId;
	private String projectName;
	//统计标题
	private List<String> countTitleList;
	//统计数据或者列表数据
	private List<List<String>> countList;
	//各子类数据显示
	private List<DiatheisStudentItemDto> itemDtoList=new ArrayList<DiatheisStudentItemDto>();
	
	//思想品德特殊 是否有犯罪记录 0无 1有
	private int countCrime;
	//学业水平
	//必修
	private DiatheisStudentItemDto bjScoreDto;
	//学业水平考试
	private DiatheisStudentItemDto xyspScoreDto;
	//选修I
	private DiatheisStudentItemDto xj1ScoreDto;
	//选修II
	private DiatheisStudentItemDto xj2ScoreDto;
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public List<String> getCountTitleList() {
		return countTitleList;
	}
	public void setCountTitleList(List<String> countTitleList) {
		this.countTitleList = countTitleList;
	}
	public List<List<String>> getCountList() {
		return countList;
	}
	public void setCountList(List<List<String>> countList) {
		this.countList = countList;
	}
	public List<DiatheisStudentItemDto> getItemDtoList() {
		return itemDtoList;
	}
	public void setItemDtoList(List<DiatheisStudentItemDto> itemDtoList) {
		this.itemDtoList = itemDtoList;
	}
	public int getCountCrime() {
		return countCrime;
	}
	public void setCountCrime(int countCrime) {
		this.countCrime = countCrime;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public DiatheisStudentItemDto getBjScoreDto() {
		return bjScoreDto;
	}
	public void setBjScoreDto(DiatheisStudentItemDto bjScoreDto) {
		this.bjScoreDto = bjScoreDto;
	}
	public DiatheisStudentItemDto getXyspScoreDto() {
		return xyspScoreDto;
	}
	public void setXyspScoreDto(DiatheisStudentItemDto xyspScoreDto) {
		this.xyspScoreDto = xyspScoreDto;
	}
	public DiatheisStudentItemDto getXj1ScoreDto() {
		return xj1ScoreDto;
	}
	public void setXj1ScoreDto(DiatheisStudentItemDto xj1ScoreDto) {
		this.xj1ScoreDto = xj1ScoreDto;
	}
	public DiatheisStudentItemDto getXj2ScoreDto() {
		return xj2ScoreDto;
	}
	public void setXj2ScoreDto(DiatheisStudentItemDto xj2ScoreDto) {
		this.xj2ScoreDto = xj2ScoreDto;
	}
	
}
