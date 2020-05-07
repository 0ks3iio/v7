package net.zdsoft.studevelop.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.studevelop.data.dto.StuSubjectAchiDto;

@Entity
@Table(name = "studevelop_category")
public class StuDevelopCateGory extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3014627137106751824L;

	private String subjectId;
	private String categoryName;
	private String state;//是否只取一个成绩
	private Date creationTime;
	@Transient
	private StuSubjectAchiDto stuSubjectAchiDto;

	public StuSubjectAchiDto getStuSubjectAchiDto() {
		return stuSubjectAchiDto;
	}

	public void setStuSubjectAchiDto(StuSubjectAchiDto stuSubjectAchiDto) {
		this.stuSubjectAchiDto = stuSubjectAchiDto;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "stuDevelopCateGory";
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

}
