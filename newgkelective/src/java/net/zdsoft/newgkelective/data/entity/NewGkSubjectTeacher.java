package net.zdsoft.newgkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 排课结果课程教师安排数据
 */
@Entity
@Table(name = "newgkelective_subject_teacher")
public class NewGkSubjectTeacher extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String arrayId;
	private String subjectId;
	private String teacherId;

	public String getArrayId() {
		return arrayId;
	}

	public void setArrayId(String arrayId) {
		this.arrayId = arrayId;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkSubjectTeacher";
	}

}
