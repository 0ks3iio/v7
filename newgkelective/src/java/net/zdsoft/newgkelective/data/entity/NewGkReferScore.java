package net.zdsoft.newgkelective.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 *  参考成绩
 */
@Entity
@Table(name = "newgkelective_refer_score")
public class NewGkReferScore extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String unitId;
	private String gradeId;
	private String name; // 显示名称
	private Integer times;
	private Integer isDeleted;
	private Date creationTime;
	private Date modifyTime;
	/**
	 * dataList [0]:[学生人数,数值]
	 * [1]:[科目数量,数值]
	 * 之后:[科目,均分]
	 */
	@Transient
	private List<String[]> dataList;
	/**
	 * 新加字段isDefault
	 * 此处由isUsed改为isDefault
	 * 移除Transient注解
	 */
	private Integer isDefault;

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public List<String[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<String[]> dataList) {
		this.dataList = dataList;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
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

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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
		return "newGkReferScore";
	}

}
