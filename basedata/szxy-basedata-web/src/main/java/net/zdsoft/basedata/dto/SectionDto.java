package net.zdsoft.basedata.dto;

public class SectionDto {
	/*学段名称*/
	private String sectionName;
	/*学段值*/
	private String sectionValue;
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getSectionValue() {
		return sectionValue;
	}
	public void setSectionValue(String sectionValue) {
		this.sectionValue = sectionValue;
	}
	public SectionDto(String sectionName, String sectionValue) {
		super();
		this.sectionName = sectionName;
		this.sectionValue = sectionValue;
	}
	public SectionDto() {
		super();
		// TODO Auto-generated constructor stub
	}
}
