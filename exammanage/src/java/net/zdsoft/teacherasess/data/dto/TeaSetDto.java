package net.zdsoft.teacherasess.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.teacherasess.data.entity.TeacherAsessRank;
import net.zdsoft.teacherasess.data.entity.TeacherAsessSet;

public class TeaSetDto {
	
	private List<TeacherAsessSet> teaSetlist = new ArrayList<>();
	private List<TeacherAsessRank> teaRankList=new ArrayList<>();
	private String unitId;
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public List<TeacherAsessSet> getTeaSetlist() {
		return teaSetlist;
	}

	public void setTeaSetlist(List<TeacherAsessSet> teaSetlist) {
		this.teaSetlist = teaSetlist;
	}

	public List<TeacherAsessRank> getTeaRankList() {
		return teaRankList;
	}

	public void setTeaRankList(List<TeacherAsessRank> teaRankList) {
		this.teaRankList = teaRankList;
	}

	
}
