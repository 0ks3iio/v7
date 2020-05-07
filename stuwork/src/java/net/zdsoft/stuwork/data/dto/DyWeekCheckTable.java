package net.zdsoft.stuwork.data.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DyWeekCheckTable {
	private Date weekDate;
	private Date beginDate;
	private Date endDate;
	private String sectionStr;
	private Set<String> sections = new HashSet<String>();
	private int state;//0未开始 1未提交 2已提交
	private int week;
	private String sec;
	
	public String getSec() {
		return sec;
	}
	public void setSec(String sec) {
		this.sec = sec;
	}
	public Set<String> getSections() {
		return sections;
	}
	public void setSections(Set<String> sections) {
		this.sections = sections;
	}
	public String getSectionStr() {
		return sectionStr;
	}
	public void setSectionStr(String sectionStr) {
		this.sectionStr = sectionStr;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public Date getWeekDate() {
		return weekDate;
	}
	public void setWeekDate(Date weekDate) {
		this.weekDate = weekDate;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	;
}
