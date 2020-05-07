package net.zdsoft.gkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * 算法优先级
 */
@Entity
@Table(name = "gkelective_allocation")
public class GkAllocation extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String subjectArrangeId;
	private String name;
	private String type;
	private String isUsing;
	private Integer sort;
	private Date creationTime;
	private Date modifyTime;

	public String getSubjectArrangeId() {
		return subjectArrangeId;
	}

	public void setSubjectArrangeId(String subjectArrangeId) {
		this.subjectArrangeId = subjectArrangeId;
	}

	public String getIsUsing() {
		return isUsing;
	}

	public void setIsUsing(String isUsing) {
		this.isUsing = isUsing;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
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

	@Override
	public String fetchCacheEntitName() {
		return "gkAllocation";
	}
}
