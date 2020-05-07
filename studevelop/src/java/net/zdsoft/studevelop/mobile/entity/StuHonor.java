package net.zdsoft.studevelop.mobile.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 学生荣誉
 * @author Administrator
 */
@Entity
@Table(name="studevelop_stu_honor")
public class StuHonor extends BaseEntity<String> {
	private static final long serialVersionUID = -3061052638955441674L;
	
	private String acadyear;
	private String semester;// 学期
	private String studentId;
	private Date creationTime;
	private Date modifyTime;
	
	@Transient
	private List<StuAttachmentDto> images;
	

	public List<StuAttachmentDto> getImages() {
		return images;
	}

	public void setImages(List<StuAttachmentDto> images) {
		this.images = images;
	}

	public String getAcadyear() {
		return acadyear;
	}

	public void setAcadyear(String acadyear) {
		this.acadyear = acadyear;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	@Override
	public String fetchCacheEntitName() {
		return "StuHonor";
	}

}
