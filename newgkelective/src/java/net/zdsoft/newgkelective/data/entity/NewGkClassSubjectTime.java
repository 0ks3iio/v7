package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.newgkelective.data.constant.NewGkElectiveConstant;

/**
 * 班级特征
 *
 */
@Entity
@Table(name = "newgkelective_clas_subj_time")
public class NewGkClassSubjectTime extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String arrayItemId;
	private String  classId;
	private String subjectId;
	private String subjectType;
	private Integer period;
	/**
	 * 单双周,默认为普通
	 */
	private Integer weekType = NewGkElectiveConstant.WEEK_TYPE_NORMAL;

	private Date creationTime;
	private Date modifyTime;
	
	public String getArrayItemId() {
		return arrayItemId;
	}
	public void setArrayItemId(String arrayItemId) {
		this.arrayItemId = arrayItemId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectType() {
		return subjectType;
	}
	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	@Override
	public String fetchCacheEntitName() {
		return "newGkClassSubjectTime";
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
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Integer getWeekType() {
		return weekType;
	}

	public void setWeekType(Integer weekType) {
		this.weekType = weekType;
	}
}
