package net.zdsoft.teaeaxam.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.teaeaxam.dto.TeaexamTeacherDto;

@Entity
@Table(name="teaexam_subject_limit")
public class TeaexamSubjectLimit extends BaseEntity<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6544355894458511263L;
	
	private String examId;
	private String subjectInfoId;
	private String teacherIds;
	private Date creationTime;
	private Date modifyTime;
	@Transient
	private List<TeaexamTeacherDto> teacherDtoList;

	@Override
	public String fetchCacheEntitName() {
		return "teaexamSubjectLimit";
	}

	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}

	public String getSubjectInfoId() {
		return subjectInfoId;
	}

	public void setSubjectInfoId(String subjectInfoId) {
		this.subjectInfoId = subjectInfoId;
	}

	public String getTeacherIds() {
		return teacherIds;
	}

	public void setTeacherIds(String teacherIds) {
		this.teacherIds = teacherIds;
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

	public List<TeaexamTeacherDto> getTeacherDtoList() {
		return teacherDtoList;
	}

	public void setTeacherDtoList(List<TeaexamTeacherDto> teacherDtoList) {
		this.teacherDtoList = teacherDtoList;
	}

}
