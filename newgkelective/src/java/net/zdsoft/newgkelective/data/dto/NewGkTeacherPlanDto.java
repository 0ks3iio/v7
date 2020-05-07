package net.zdsoft.newgkelective.data.dto;

import java.util.Date;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkTeacherPlan;

public class NewGkTeacherPlanDto {

	private String itemId;
	private String itemName;
	private Date creationTime;
	private List<NewGkTeacherPlan> subjectList;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public List<NewGkTeacherPlan> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<NewGkTeacherPlan> subjectList) {
		this.subjectList = subjectList;
	}

}
