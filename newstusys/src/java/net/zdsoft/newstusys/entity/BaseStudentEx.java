package net.zdsoft.newstusys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 
 * @author weixh
 * @since 2018年3月2日 下午5:36:50
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "base_student_ex")
public class BaseStudentEx extends BaseEntity<String> {
	private String schoolId;
	private int isDeleted;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	@Override
	public String fetchCacheEntitName() {
		return "BaseStudentEx";
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}
