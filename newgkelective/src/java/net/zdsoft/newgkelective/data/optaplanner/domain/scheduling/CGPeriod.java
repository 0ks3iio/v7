package net.zdsoft.newgkelective.data.optaplanner.domain.scheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@PlanningEntity
@XStreamAlias("CGPeriod")
public class CGPeriod implements Comparable<CGPeriod>, Serializable{

    private CGDay day;
    private CGTimeslot timeslot;
    @XStreamOmitField
	private List<CGSectionLecture> lectureList = new ArrayList<CGSectionLecture>();

    /** 额外时间点 比如年级禁排的时间点 */
    private boolean isExtra = false;
    
	@InverseRelationShadowVariable(sourceVariableName = "period")
	public List<CGSectionLecture> getLectureList() {
		return lectureList;
	}

	@Override
	public int compareTo(CGPeriod period2) {
		if (day.getDayIndex() > period2.getDay().getDayIndex())
			return 1;
		else if (day.getDayIndex() < period2.getDay().getDayIndex())
			return -1;
		else if (timeslot.getTimeslotIndex() > period2.getTimeslot().getTimeslotIndex())
			return 1;
		else if (timeslot.getTimeslotIndex() < period2.getTimeslot().getTimeslotIndex())
			return -1;
		else
			return 0;
	}
	
	@PlanningId
	public String getPeriodCode(){
		return "PeriodCode:"+day.getDayIndex()+"-"+timeslot.getTimeslotIndex();
	}
	public String getSimpleCode(){
		return "PC:"+day.getDayIndex()+"-"+timeslot.getTimeslotIndex();
	}
	
	public String getSimpleOffSetCode(int timeslotOffset){
		return "PeriodCode:"+day.getDayIndex()+"-"+(timeslot.getTimeslotIndex()+timeslotOffset);
	}
	public String getOffSetPeriodCode(int timeslotOffset){
		return "PeriodCode:"+day.getDayIndex()+"-"+(timeslot.getTimeslotIndex()+timeslotOffset);
	}
	
	public void setLectureList(List<CGSectionLecture> lectureList) {
		this.lectureList = lectureList;
	}

	public CGDay getDay() {
		return day;
	}

	public void setDay(CGDay day) {
		this.day = day;
	}

	public CGTimeslot getTimeslot() {
		return timeslot;
	}

	public void setTimeslot(CGTimeslot timeslot) {
		this.timeslot = timeslot;
	}

	public boolean isContinus(CGPeriod period) {
		int in = Math.abs(period.getTimeslot().getTimeslotIndex() - period.getTimeslot().getTimeslotIndex());
		if(this.day.equals(period.getDay())
				&& this.getTimeslot().getPeriodInterval().equals(period.getTimeslot().getPeriodInterval())
				&& in == 1) {
			return true;
		}
		return false;
	}

	public boolean isExtra() {
		return isExtra;
	}

	public void setExtra(boolean isExtra) {
		this.isExtra = isExtra;
	}

}
