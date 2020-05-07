package net.zdsoft.activity.dto;

import java.util.Date;

public class FamilyStaDto {

	private int tjCount;//厅级干部数
	private int xjCount;//处级干部数
	private int kjCount;//科级干部数
	private int normalCount;//一般干部数
	private int jisCount;//专业技术人员数
	private int gqCount;//工勤人员数
	private int qyeCount;//企业人员数
	private int otherCount;//其他人员数
	private int totalCount;//小计
	private int meetingCount;//走访见面数
	
	private Date tempDate;
	
	public int getTjCount() {
		return tjCount;
	}
	public void setTjCount(int tjCount) {
		this.tjCount = tjCount;
	}
	public int getXjCount() {
		return xjCount;
	}
	public void setXjCount(int xjCount) {
		this.xjCount = xjCount;
	}
	public int getKjCount() {
		return kjCount;
	}
	public void setKjCount(int kjCount) {
		this.kjCount = kjCount;
	}
	public int getNormalCount() {
		return normalCount;
	}
	public void setNormalCount(int normalCount) {
		this.normalCount = normalCount;
	}
	public int getJisCount() {
		return jisCount;
	}
	public void setJisCount(int jisCount) {
		this.jisCount = jisCount;
	}
	public int getGqCount() {
		return gqCount;
	}
	public void setGqCount(int gqCount) {
		this.gqCount = gqCount;
	}
	public int getQyeCount() {
		return qyeCount;
	}
	public void setQyeCount(int qyeCount) {
		this.qyeCount = qyeCount;
	}
	public int getOtherCount() {
		return otherCount;
	}
	public void setOtherCount(int otherCount) {
		this.otherCount = otherCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getMeetingCount() {
		return meetingCount;
	}
	public void setMeetingCount(int meetingCount) {
		this.meetingCount = meetingCount;
	}
	public Date getTempDate() {
		return tempDate;
	}
	public void setTempDate(Date tempDate) {
		this.tempDate = tempDate;
	}
	
	
}
