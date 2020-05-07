package net.zdsoft.gkelective.data.dto;

import java.util.List;

import net.zdsoft.gkelective.data.entity.GkSubject;

public class GkTeachPlacePlanDto {
	private List<GkSubject> gkSubjectList;
	private String planId;
	private String[] placeIds;
	public List<GkSubject> getGkSubjectList() {
		return gkSubjectList;
	}
	public void setGkSubjectList(List<GkSubject> gkSubjectList) {
		this.gkSubjectList = gkSubjectList;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String[] getPlaceIds() {
		return placeIds;
	}
	public void setPlaceIds(String[] placeIds) {
		this.placeIds = placeIds;
	}
	
	

}
