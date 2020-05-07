package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "newgkelective_subj_group_color")
public class NewGkSubjectGroupColor extends BaseEntity<String> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String groupType;   // 用来区分是 几门科目的组合 三科用3  两科用 2
	private String subjectGroup;  // 科目组合 的id，如 物化组合： 物理Id,化学Id 也可以三科或者一科 组合 ；按照id字符串排序后传入 
	private String color;
	private Date creationTime;
	private Date modifyTime;

	@Override
	public String fetchCacheEntitName() {
		return "newGkSubjectGroupColor";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getSubjectGroup() {
		return subjectGroup;
	}

	public void setSubjectGroup(String subjectGroup) {
		this.subjectGroup = subjectGroup;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
