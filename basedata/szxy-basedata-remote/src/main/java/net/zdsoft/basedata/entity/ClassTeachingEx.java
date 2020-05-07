package net.zdsoft.basedata.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "base_class_teaching_ex")
public class ClassTeachingEx extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	private String classTeachingId;
	private String teacherId;

	public String getClassTeachingId() {
		return classTeachingId;
	}

	public void setClassTeachingId(String classTeachingId) {
		this.classTeachingId = classTeachingId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "classTeachingEx";
	}

}
