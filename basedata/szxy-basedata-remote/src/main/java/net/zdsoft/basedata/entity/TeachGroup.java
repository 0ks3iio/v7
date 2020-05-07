package net.zdsoft.basedata.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 教研组
 */
@Entity
@Table(name = "base_teach_group")
public class TeachGroup extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String schoolId; // 学校id
	private String teachGroupName;
	private String subjectId; // 科目，最少关联一个科目
	private Date creationTime;// 创建日期
	private Date ModifyTime;// 修改日期
	private Integer isDeleted;// 软删标志
	@Transient
	private Set<String> teacherIdSet;
	
	private Integer orderId;

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getTeachGroupName() {
		return teachGroupName;
	}

	public void setTeachGroupName(String teachGroupName) {
		this.teachGroupName = teachGroupName;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getModifyTime() {
		return ModifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		ModifyTime = modifyTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String fetchCacheEntitName() {
		return "baseTeachGroup";
	}

	public Set<String> getTeacherIdSet() {
		return teacherIdSet;
	}

	public void setTeacherIdSet(Set<String> teacherIdSet) {
		this.teacherIdSet = teacherIdSet;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

}
