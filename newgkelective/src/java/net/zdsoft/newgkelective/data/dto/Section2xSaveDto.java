package net.zdsoft.newgkelective.data.dto;

public class Section2xSaveDto {
	private String sectionId;
	private int meanSize;
	private int openClassNum;
	private int marginSize;
	
	private String[] sectionBeginId;

	public String getSectionId() {
		return sectionId;
	}

	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}

	public int getMeanSize() {
		return meanSize;
	}

	public void setMeanSize(int meanSize) {
		this.meanSize = meanSize;
	}

	public int getOpenClassNum() {
		return openClassNum;
	}

	public void setOpenClassNum(int openClassNum) {
		this.openClassNum = openClassNum;
	}

	public int getMarginSize() {
		return marginSize;
	}

	public void setMarginSize(int marginSize) {
		this.marginSize = marginSize;
	}

	public String[] getSectionBeginId() {
		return sectionBeginId;
	}

	public void setSectionBeginId(String[] sectionBeginId) {
		this.sectionBeginId = sectionBeginId;
	}
	
	
}
