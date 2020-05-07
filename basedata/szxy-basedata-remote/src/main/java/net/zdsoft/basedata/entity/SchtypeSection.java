package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="base_schtype_section")
public class SchtypeSection extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;
	private String schoolType;
	private String section;
	private int isDeleted;
	private int eventSource;
	
	public String getSchoolType() {
		return schoolType;
	}
	public void setSchoolType(String schoolType) {
		this.schoolType = schoolType;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	public int getEventSource() {
		return eventSource;
	}
	public void setEventSource(int eventSource) {
		this.eventSource = eventSource;
	}
	@Override
	public String fetchCacheEntitName() {
		return "schtypeSection";
	}
	
}
