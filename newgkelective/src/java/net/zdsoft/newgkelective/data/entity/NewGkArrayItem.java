package net.zdsoft.newgkelective.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
import net.zdsoft.newgkelective.data.dto.NewGkItemDto;

/**
 * 排课基础数据
 */
@Entity
@Table(name = "newgkelective_array_item")
public class NewGkArrayItem extends BaseEntity<String> {

	private static final long serialVersionUID = 1L;

	private String divideId;
	private String divideType;
	private Integer times; // 次数
	private String itemName;
	private Date creationTime;
	private Date modifyTime;
	private Integer isDeleted;
	@Transient
	private int countPlace;
	@Transient
	private String galleryful;
	@Transient
	private NewGkItemDto newGkItemDto;
	@Transient
	private boolean ts;
	

	public boolean isTs() {
		return ts;
	}

	public void setTs(boolean ts) {
		this.ts = ts;
	}

	public NewGkItemDto getNewGkItemDto() {
		return newGkItemDto;
	}

	public void setNewGkItemDto(NewGkItemDto newGkItemDto) {
		this.newGkItemDto = newGkItemDto;
	}

	public String getGalleryful() {
		return galleryful;
	}

	public void setGalleryful(String galleryful) {
		this.galleryful = galleryful;
	}

	public int getCountPlace() {
		return countPlace;
	}

	public void setCountPlace(int countPlace) {
		this.countPlace = countPlace;
	}

	public String getDivideId() {
		return divideId;
	}

	public void setDivideId(String divideId) {
		this.divideId = divideId;
	}

	public String getDivideType() {
		return divideType;
	}

	public void setDivideType(String divideType) {
		this.divideType = divideType;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	public String fetchCacheEntitName() {
		return "newGkArrayItem";
	}

}
