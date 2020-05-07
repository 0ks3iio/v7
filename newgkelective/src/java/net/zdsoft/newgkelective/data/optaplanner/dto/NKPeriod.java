package net.zdsoft.newgkelective.data.optaplanner.dto;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("NKPeriod")
public class NKPeriod  implements Serializable{

    private int dayIndex;//星期几
    private int timeslotIndex;//第几节
    //辅助
    private String periodInterval;//时间段
    private int otherIndex;//上午节次或者下午节次
    /**
     * 额外时间点 比如年级禁排时间
     */
    private boolean isExtra = false;
    
    public String getPeriodInterval() {
		return periodInterval;
	}
	public void setPeriodInterval(String periodInterval) {
		this.periodInterval = periodInterval;
	}
	public int getOtherIndex() {
		return otherIndex;
	}
	public void setOtherIndex(int otherIndex) {
		this.otherIndex = otherIndex;
	}
	public int getDayIndex() {
		return dayIndex;
	}
	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}
	public int getTimeslotIndex() {
		return timeslotIndex;
	}
	public void setTimeslotIndex(int timeslotIndex) {
		this.timeslotIndex = timeslotIndex;
	}
	public boolean isExtra() {
		return isExtra;
	}
	public void setExtra(boolean isExtra) {
		this.isExtra = isExtra;
	}

	public String getPeriodCode(){
    	return dayIndex+"_"+periodInterval+"_"+otherIndex;
	}
}
