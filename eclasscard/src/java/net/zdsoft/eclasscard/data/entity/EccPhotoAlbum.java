package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_photo_album")
public class EccPhotoAlbum  extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String objectId;
	private String pictrueDirpath;
	private String pictrueName;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Transient
	private int moreCount;//路径相同的文件条数
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardPhotoAlbum";
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getPictrueDirpath() {
		return pictrueDirpath;
	}

	public void setPictrueDirpath(String pictrueDirpath) {
		this.pictrueDirpath = pictrueDirpath;
	}

	public String getPictrueName() {
		return pictrueName;
	}

	public void setPictrueName(String pictrueName) {
		this.pictrueName = pictrueName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getMoreCount() {
		return moreCount;
	}

	public void setMoreCount(int moreCount) {
		this.moreCount = moreCount;
	}


}
