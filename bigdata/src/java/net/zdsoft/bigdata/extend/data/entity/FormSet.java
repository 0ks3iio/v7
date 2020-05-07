package net.zdsoft.bigdata.extend.data.entity;

import net.zdsoft.framework.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import java.io.File;
import java.util.Date;

@Entity
@Table(name = "bg_metadata_forms")
public class FormSet extends BaseEntity<String> {
	private static final long serialVersionUID = -6550533513110198693L;
	public static final String HTML_PATH = File.separator+"bigdata"+File.separator+"forms";
	private String name;
	private String mdId;// 元数据ID
	private String remark;

	private Integer orderId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;

	@Transient
	private String metaName;
	
	public String getName() {
		return name;
	}
	@Transient
	public String getHtmlPath() {
		return HTML_PATH+File.separator+this.getId()+".html";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMdId() {
		return mdId;
	}

	public void setMdId(String mdId) {
		this.mdId = mdId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public String getMetaName() {
		return metaName;
	}

	public void setMetaName(String metaName) {
		this.metaName = metaName;
	}

	@Override
	public String fetchCacheEntitName() {
		return "formSet";
	}
}
