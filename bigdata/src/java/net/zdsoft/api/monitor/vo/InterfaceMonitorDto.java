package net.zdsoft.api.monitor.vo;

public class InterfaceMonitorDto {

	private String description;
	private int findCount;
	private int findNum;
	private int saveCount;
	private int saveNum;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getFindCount() {
		return findCount;
	}
	public void setFindCount(int findCount) {
		this.findCount = findCount;
	}
	public int getFindNum() {
		return findNum;
	}
	public void setFindNum(int findNum) {
		this.findNum = findNum;
	}
	public int getSaveCount() {
		return saveCount;
	}
	public void setSaveCount(int saveCount) {
		this.saveCount = saveCount;
	}
	public int getSaveNum() {
		return saveNum;
	}
	public void setSaveNum(int saveNum) {
		this.saveNum = saveNum;
	}
}
