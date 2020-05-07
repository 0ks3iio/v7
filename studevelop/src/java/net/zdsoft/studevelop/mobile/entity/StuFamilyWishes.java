package net.zdsoft.studevelop.mobile.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.studevelop.data.entity.StudevelopAttachment;

/**
 * 家长寄语
 * @author Administrator
 */
@Entity
@Table(name="studevelop_family_wishes")
public class StuFamilyWishes extends BaseEntity<String> {
	
	private static final long serialVersionUID = 3628239601003818539L;
	
	private String acadyear;
	private String semester;// 学期
	private String studentId;
	private String parentContent;//家长寄语
	private String childContent;//学生寄语
	private Date creationTime;
	private Date modifyTime;
	
	@Transient
	private String imgPath;
	@Transient
	private String filePath;
	@Transient
	private boolean isExistsImgPath;
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

	public boolean isExistsImgPath() {
		return isExistsImgPath;
	}

	public void setExistsImgPath(boolean isExistsImgPath) {
		this.isExistsImgPath = isExistsImgPath;
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

	public String getParentContent() {
		return parentContent;
	}

	public void setParentContent(String parentContent) {
		this.parentContent = parentContent;
	}

	public String getChildContent() {
		return childContent;
	}

	public void setChildContent(String childContent) {
		this.childContent = childContent;
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
		return "StuFamilyWishes";
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
