package net.zdsoft.studevelop.mobile.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;

/**
 * 自我介绍
 * @author Administrator
 */
@Entity
@Table(name="studevelop_stu_introduction")
public class StuIntroduction extends BaseEntity<String> {
	
	private static final long serialVersionUID = 4700730740192860522L;
	
	private String studentId;
	private String acadyear;
	private String semester;// 学期
	private String speciality;//爱好特长
	private String content;//自我介绍内容
	private Date creationTime;
	private Date modifyTime;
	private Integer hasRelease;
	
	@Transient
	private String imgPath;
	@Transient
	private String filePath;
	@Transient
	private String imgAttId;
	@Transient
	private StudevelopAttachment imgAtt;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
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

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Integer getHasRelease() {
		return hasRelease;
	}

	public void setHasRelease(Integer hasRelease) {
		this.hasRelease = hasRelease;
	}

	@Override
	public String fetchCacheEntitName() {
		return "StuIntroduction";
	}

	public String getImgAttId() {
		return imgAttId;
	}

	public void setImgAttId(String imgAttId) {
		this.imgAttId = imgAttId;
	}

	public StudevelopAttachment getImgAtt() {
		return imgAtt;
	}

	public void setImgAtt(StudevelopAttachment imgAtt) {
		this.imgAtt = imgAtt;
	}

}
