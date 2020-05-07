package net.zdsoft.newgkelective.data.optaplanner.shuff.domain;

import java.util.List;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@PlanningEntity
public class GroupSubject extends AbstractPersistable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Group group;
	private Subject subject;
	private int stuNum;
	
	TimeSlot timeSlot;
	List<TimeSlot> timeSlotDomain;

	@PlanningVariable(valueRangeProviderRefs = {"timeSlotRange"})
	public TimeSlot getTimeSlot() {
		return this.timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	@ValueRangeProvider(id = "timeSlotRange")
	public List<TimeSlot> getTimeSlotDomain() {
		return this.timeSlotDomain;
	}

	public void setTimeSlotDomain(List<TimeSlot> timeSlotDomain) {
		this.timeSlotDomain = timeSlotDomain;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public int getStuNum() {
		return stuNum;
	}

	public void setStuNum(int stuNum) {
		this.stuNum = stuNum;
	}
	
	
}
