package net.zdsoft.newgkelective.data.optaplanner.domain.biweekly;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.entity.PlanningPin;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import net.zdsoft.newgkelective.data.optaplanner.util.AbstractPersistable;

@PlanningEntity
public class Section extends AbstractPersistable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PlanningPin
	private boolean pinned = false;
	private String oldId;

	private Integer isBiweekly;
	
	private String teacherCode;

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	@PlanningVariable(valueRangeProviderRefs="weekTypeProvider")
	public Integer getIsBiweekly() {
		return isBiweekly;
	}

	public void setIsBiweekly(Integer isBiweekly) {
		this.isBiweekly = isBiweekly;
	}

	public String getTeacherCode() {
		return teacherCode;
	}

	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}

	public boolean isPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned) {
		this.pinned = pinned;
	}
}
