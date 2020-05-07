package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 合班 同时排课数据
 *
 */
@Entity
@Table(name = "newgkelective_clas_comb_rela")
public class NewGkClassCombineRelation  extends BaseEntity<String>{
	
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String arrayItemId;
	private String type; //合班 与 同时排课
	private String classSubjectIds; //两个科目 subject_Ids:classId-subjectId-A,classId-subjectId-O
	
	private Date creationTime;
	private Date modifyTime;
	
	public String getArrayItemId() {
		return arrayItemId;
	}
	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getClassSubjectIds() {
		return classSubjectIds;
	}
	public void setClassSubjectIds(String classSubjectIds) {
		this.classSubjectIds = classSubjectIds;
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
		return "newGkClassCombineRelation";
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
}
