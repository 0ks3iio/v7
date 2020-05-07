package net.zdsoft.eclasscard.data.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.google.common.collect.Lists;

import net.zdsoft.basedata.entity.Attachment;
import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "eclasscard_attach_folder")
public class EccAttachFolder extends BaseEntity<String> {
	private static final long serialVersionUID = 6541114970222338272L;

	private String unitId;
	private String title;
	private int type;//1.相册  2.视频 3.PPT
	private int range;//1.我的班牌  2.校级班牌
	private boolean isShow;
	private int sendType;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;
	
	@Transient
	private String coverUrl;//封面图片url
	@Transient
	private Integer number;//相册内照片数量
	@Transient
	private List<Attachment> attachments = Lists.newArrayList();
	
	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardAttachFolder";
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}
}
