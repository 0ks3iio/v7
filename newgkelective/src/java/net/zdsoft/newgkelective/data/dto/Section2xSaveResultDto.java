package net.zdsoft.newgkelective.data.dto;

import java.util.ArrayList;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkSectionEnd;
import net.zdsoft.newgkelective.data.entity.NewGkSectionResult;

public class Section2xSaveResultDto {
	
	private String sectionId;
	private List<NewGkSectionResult> sectionResultList=new ArrayList<>();
	
	private List<NewGkSectionEnd> sectionEndList=new ArrayList<>();

	public List<NewGkSectionResult> getSectionResultList() {
		return sectionResultList;
	}

	public void setSectionResultList(List<NewGkSectionResult> sectionResultList) {
		this.sectionResultList = sectionResultList;
	}

	public List<NewGkSectionEnd> getSectionEndList() {
		return sectionEndList;
	}

	public void setSectionEndList(List<NewGkSectionEnd> sectionEndList) {
		this.sectionEndList = sectionEndList;
	}

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	
}
