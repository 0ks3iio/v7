package net.zdsoft.newgkelective.data.dto;

import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGKStudentRangeEx;

public class NewGKStudentRangeExDto {
	
	private List<NewGKStudentRangeEx> exList;
	private Integer batchCountTypea;
	private Integer batchCountTypeb;
	// 3+1+2 学考是否跟随选考
	private String followType;
	
	public Integer getBatchCountTypea() {
		return batchCountTypea;
	}
	public void setBatchCountTypea(Integer batchCountTypea) {
		this.batchCountTypea = batchCountTypea;
	}
	public Integer getBatchCountTypeb() {
		return batchCountTypeb;
	}
	public void setBatchCountTypeb(Integer batchCountTypeb) {
		this.batchCountTypeb = batchCountTypeb;
	}
	public List<NewGKStudentRangeEx> getExList() {
		return exList;
	}
	public void setExList(List<NewGKStudentRangeEx> exList) {
		this.exList = exList;
	}
	public String getFollowType() {
		return followType;
	}
	public void setFollowType(String followType) {
		this.followType = followType;
	}
}
