package net.zdsoft.newgkelective.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 排课结果课程教师信息
 */
@Entity
@Table(name = "newgkelective_timetable_teach")
public class NewGkTimetableTeacher extends BaseEntity<String>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String timetableId;
	private String teacherId;
	
	public String getTimetableId() {
		return timetableId;
	}

	public void setTimetableId(String timetableId) {
		this.timetableId = timetableId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkTimetableTeacher";
	}
	
}
