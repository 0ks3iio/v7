package net.zdsoft.studevelop.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.studevelop.data.dto.StuSubjectAchiDto;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "studevelop_subject")
public class StuDevelopSubject extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6910162437660388951L;
	private String unitId;
	private String acadyear;
	private String semester;
	private String gradeId;
	private String name;
	private Date creationTime;
	@Transient
	private String categoryNames;
	@Transient
	private String state;//标识该科目是否存在科目类别
	@Transient
	private List<StuDevelopCateGory> cateGoryList;

	@Transient
	private StuSubjectAchiDto stuSubjectAchiDto;

	public StuSubjectAchiDto getStuSubjectAchiDto() {
		return stuSubjectAchiDto;
	}

	public void setStuSubjectAchiDto(StuSubjectAchiDto stuSubjectAchiDto) {
		this.stuSubjectAchiDto = stuSubjectAchiDto;
	}

	public List<StuDevelopCateGory> getCateGoryList() {
		return cateGoryList;
	}

	public void setCateGoryList(List<StuDevelopCateGory> cateGoryList) {
		this.cateGoryList = cateGoryList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategoryNames() {
		return categoryNames;
	}

	public void setCategoryNames(String categoryNames) {
		this.categoryNames = categoryNames;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getGradeId() {
		return gradeId;
	}

	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String fetchCacheEntitName() {
		return "stuDevelopSubject";
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

}
