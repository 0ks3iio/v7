package net.zdsoft.scoremanage.data.dto;

import java.util.List;

import net.zdsoft.scoremanage.data.entity.Borderline;

public class BorderlineDto {
	private List<Borderline> borderlineList;
	private List<Borderline> gardeSectionList;

	public List<Borderline> getGardeSectionList() {
		return gardeSectionList;
	}

	public void setGardeSectionList(List<Borderline> gardeSectionList) {
		this.gardeSectionList = gardeSectionList;
	}

	public List<Borderline> getBorderlineList() {
		return borderlineList;
	}

	public void setBorderlineList(List<Borderline> borderlineList) {
		this.borderlineList = borderlineList;
	}
	
}
