package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 算法分堆
 */
@Entity
@Table(name = "newgkelective_course_heap")
public class NewGkCourseHeap extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;
	private String arrayId;
	private String subjectId;
	private String timetableId;
	private Integer heapNum;
	private Date creationTime;
	private Date modifyTime;

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

	public String getTimetableId() {
		return timetableId;
	}

	public void setTimetableId(String timetableId) {
		this.timetableId = timetableId;
	}

	public Integer getHeapNum() {
		return heapNum;
	}

	public void setHeapNum(Integer heapNum) {
		this.heapNum = heapNum;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkCourseHeap";
	}

}
