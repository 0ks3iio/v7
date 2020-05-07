package net.zdsoft.activity.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name="famdear_threeInTwoStu_member")
public class FamDearThreeInTwoStuMember extends BaseEntity<String>{
	
	private String stuId;
	private String name;
	private String relation;
	private String company;
	private String birthday;
	private String politicCountenance;//政治面貌
	private String linkPhone;

	public String getStuId() {
		return stuId;
	}

	public void setStuId(String stuId) {
		this.stuId = stuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPoliticCountenance() {
		return politicCountenance;
	}

	public void setPoliticCountenance(String politicCountenance) {
		this.politicCountenance = politicCountenance;
	}

	public String getLinkPhone() {
		return linkPhone;
	}

	public void setLinkPhone(String linkPhone) {
		this.linkPhone = linkPhone;
	}

	@Override
	public String fetchCacheEntitName() {
		return "famDearThreeInTwoStuMember";
	}

}
