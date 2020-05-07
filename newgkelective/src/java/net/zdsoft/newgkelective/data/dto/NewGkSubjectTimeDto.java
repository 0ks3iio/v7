package net.zdsoft.newgkelective.data.dto;

import java.util.Date;
import java.util.List;

import net.zdsoft.newgkelective.data.entity.NewGkSubjectTime;

public class NewGkSubjectTimeDto {
	private String itemId;
	private String itemName;
	private Date creationTime;
	private List<NewGkSubjectTime> subjectTimeList;

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

	public List<NewGkSubjectTime> getSubjectTimeList() {
		return subjectTimeList;
	}

	public void setSubjectTimeList(List<NewGkSubjectTime> subjectTimeList) {
		this.subjectTimeList = subjectTimeList;
	}

}
