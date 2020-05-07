package net.zdsoft.newgkelective.data.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 教师组，用于智能排课-基础条件 统一设置禁排
 * @author user
 *
 */
@Entity
@Table(name = "newgkelective_teacher_group")
public class NewGkTeacherGroup extends BaseEntity<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String unitId; // 单位id
	private String objectId; // 暂时为 gradeId/ 或可为  其他id
	private String teacherGroupName;
	private Date creationTime;// 创建日期
	private Date modifyTime;// 修改日期
	
	@Transient
	private Set<String> teacherIdSet = new HashSet<>();
	@Transient
	private String teacherNameStr;
	@Transient
	private String noTimeStr;
	
	@Override
	public String fetchCacheEntitName() {
		return "newGkTeacherGroup";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getTeacherGroupName() {
		return teacherGroupName;
	}

	public void setTeacherGroupName(String teachGroupName) {
		this.teacherGroupName = teachGroupName;
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

	public Set<String> getTeacherIdSet() {
		return teacherIdSet;
	}

	public void setTeacherIdSet(Set<String> teacherIdSet) {
		this.teacherIdSet = teacherIdSet;
	}

	public String getNoTimeStr() {
		return noTimeStr;
	}

	public void setNoTimeStr(String noTimeStr) {
		this.noTimeStr = noTimeStr;
	}

	public String getTeacherNameStr() {
		return teacherNameStr;
	}

	public void setTeacherNameStr(String teacherNameStr) {
		this.teacherNameStr = teacherNameStr;
	}

}
