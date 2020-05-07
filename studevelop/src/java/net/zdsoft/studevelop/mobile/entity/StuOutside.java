package net.zdsoft.studevelop.mobile.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

/**
 * 校外表现
 * @author Administrator
 */
@Entity
@Table(name="studevelop_stu_outside")
public class StuOutside extends BaseEntity<String> {
	private static final long serialVersionUID = -3061052638955441674L;
	
	/**校外表现*/
	public static final int TYPE_1 = 1;
	/**假期表现*/
	public static final int TYPE_2 = 2;
	
	private String acadyear;
	private String semester;// 学期
	private String studentId;
	private Integer type;//1:校外表现，2:假期表现
	private String title;//主题
	private String content;//内容
	private Date creationTime;
	private Date modifyTime;
	
	//辅助字段
	@Transient
	private String imagePath;//第一张图片
	@Transient
	private int imageCount;//图片数量
	@Transient
	private List<StuAttachmentDto> images;
	@Transient
	private String delImgIds;
	
	
	public String getDelImgIds() {
		return delImgIds;
	}

	public void setDelImgIds(String delImgIds) {
		this.delImgIds = delImgIds;
	}

	public List<StuAttachmentDto> getImages() {
		return images;
	}

	public void setImages(List<StuAttachmentDto> images) {
		this.images = images;
	}

	public int getImageCount() {
		return imageCount;
	}

	public void setImageCount(int imageCount) {
		this.imageCount = imageCount;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	@Override
	public String fetchCacheEntitName() {
		return "stuOutside";
	}

	public String getActTheme(){
		return title;
	}
	
	public String getActRemark(){
		return content;
	}
}
