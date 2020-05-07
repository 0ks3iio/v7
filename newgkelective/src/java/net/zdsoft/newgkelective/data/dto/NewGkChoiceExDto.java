package net.zdsoft.newgkelective.data.dto;

import net.zdsoft.newgkelective.data.optaplanner.util.ExcelAttribute;

public class NewGkChoiceExDto {
	@ExcelAttribute(column = "A", name = "")
	private String name;
	@ExcelAttribute(column = "B", name = "")
	private String stuCode;
	@ExcelAttribute(column = "C", name = "")
	private String subjectId;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStuCode() {
		return stuCode;
	}
	public void setStuCode(String stuCode) {
		this.stuCode = stuCode;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
}
