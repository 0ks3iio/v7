package net.zdsoft.officework.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "office_classroom_relevance")
public class OfficeClassroomRelevance extends BaseEntity<String> {
	private static final long serialVersionUID = 1L;

	private String schoolId;
	private String placeId;
	private String classroomId;
	private String classRoomName;

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}

	public String getClassRoomName() {
		return classRoomName;
	}

	public void setClassRoomName(String classRoomName) {
		this.classRoomName = classRoomName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "officeClassroomRelevance";
	}

}
