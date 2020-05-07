package net.zdsoft.career.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="career_resource_center")
public class CarResourceCenter extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;

	private Integer resourceType;	
	private String title;
	private String description;
	private Integer type;
	private Integer videoSource;
	private String linkUrl;
	private String content;
	private	Integer isDeleted;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	@Transient
	private String pictureUrl;
	@Transient
	private String videoUrl;
	@Transient
	private String videoName;
	
	public Integer getResourceType() {
		return resourceType;
	}



	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}



	public String getTitle() {
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public Integer getType() {
		return type;
	}



	public void setType(Integer type) {
		this.type = type;
	}



	public Integer getVideoSource() {
		return videoSource;
	}



	public void setVideoSource(Integer videoSource) {
		this.videoSource = videoSource;
	}



	public String getLinkUrl() {
		return linkUrl;
	}



	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public Integer getIsDeleted() {
		return isDeleted;
	}



	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
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



	public String getPictureUrl() {
		return pictureUrl;
	}



	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}



	public String getVideoUrl() {
		return videoUrl;
	}



	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}



	public String getVideoName() {
		return videoName;
	}



	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}



	@Override
	public String fetchCacheEntitName() {
		return "carResourceCenter";
	}
}
