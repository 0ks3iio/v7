package net.zdsoft.newgkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "newgkelective_teacher_group_ex")
public class NewGkTeacherGroupEx extends BaseEntity<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String teacherGroupId;// 教师组Id
	private String teacherId;// 老师Id
	
	@Override
	public String fetchCacheEntitName() {
		return "newGkTeacherGroupEx";
	}

	public String getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public String getTeacherGroupId() {
		return teacherGroupId;
	}



	public void setTeacherGroupId(String teacherGroupId) {
		this.teacherGroupId = teacherGroupId;
	}


}
