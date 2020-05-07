package net.zdsoft.eclasscard.data.dto;

import java.util.List;

public class EccStuQualityDto {
	private String itemkey;
	private String mainTitle;
	private String score;
	private String starScore;
	private String fiveBetter;
	private List<EccStuQualityDto> partList1;
	private List<EccStuQualityDto> partList2;
	
	 //获奖情况
    private String timeStr;
    //获奖情况
    private String description;
    
	public String getMainTitle() {
		return mainTitle;
	}
	public void setMainTitle(String mainTitle) {
		this.mainTitle = mainTitle;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getStarScore() {
		return starScore;
	}
	public void setStarScore(String starScore) {
		this.starScore = starScore;
	}
	public List<EccStuQualityDto> getPartList1() {
		return partList1;
	}
	public void setPartList1(List<EccStuQualityDto> partList1) {
		this.partList1 = partList1;
	}
	public List<EccStuQualityDto> getPartList2() {
		return partList2;
	}
	public void setPartList2(List<EccStuQualityDto> partList2) {
		this.partList2 = partList2;
	}
	public String getItemkey() {
		return itemkey;
	}
	public void setItemkey(String itemkey) {
		this.itemkey = itemkey;
	}
	public String getFiveBetter() {
		return fiveBetter;
	}
	public void setFiveBetter(String fiveBetter) {
		this.fiveBetter = fiveBetter;
	}
	public String getTimeStr() {
		return timeStr;
	}
	public void setTimeStr(String timeStr) {
		this.timeStr = timeStr;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
