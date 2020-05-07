package net.zdsoft.gkelective.data.dto;

import java.util.List;

import net.zdsoft.gkelective.data.entity.GkRounds;
import net.zdsoft.gkelective.data.entity.GkSubject;
import net.zdsoft.gkelective.data.entity.GkSubjectArrange;

public class GkRoundsDto {
	private GkSubjectArrange gsaEnt;
	private GkRounds gkRounds;
	private List<GkSubject> gksubList;
	
	
	public GkSubjectArrange getGsaEnt() {
		return gsaEnt;
	}
	public void setGsaEnt(GkSubjectArrange gsaEnt) {
		this.gsaEnt = gsaEnt;
	}
	public GkRounds getGkRounds() {
		return gkRounds;
	}
	public void setGkRounds(GkRounds gkRounds) {
		this.gkRounds = gkRounds;
	}
	public List<GkSubject> getGksubList() {
		return gksubList;
	}
	public void setGksubList(List<GkSubject> gksubList) {
		this.gksubList = gksubList;
	}
	
}
