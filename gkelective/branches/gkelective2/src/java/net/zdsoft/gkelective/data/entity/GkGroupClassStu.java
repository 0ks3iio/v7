package net.zdsoft.gkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 预排班级对应的学生
 */
@Entity
@Table(name="gkelective_group_class_stu")
public class GkGroupClassStu extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String groupClassId;
	private String studentId;
	
	public String getGroupClassId() {
		return groupClassId;
	}
	
	public void setGroupClassId(String groupClassId) {
		this.groupClassId = groupClassId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "gkGroupClassStu";
	}

}
