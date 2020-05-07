package net.zdsoft.newgkelective.data.optaplanner.domain.batch;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;


@XStreamAlias("BatchPeriod")
public class BatchPeriod extends AbstractPersistable{
	private static final long serialVersionUID = 1L;
	
	private Integer day;
	private int timeslotIndex; // 一天之内的第几节课
    private String periodInterval;
    
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public int getTimeslotIndex() {
		return timeslotIndex;
	}
	public void setTimeslotIndex(int timeslotIndex) {
		this.timeslotIndex = timeslotIndex;
	}
	public String getPeriodInterval() {
		return periodInterval;
	}
	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}
    
	public String getPeriodCode() {
		return day+"-"+periodInterval+"-"+timeslotIndex;
	}
}
