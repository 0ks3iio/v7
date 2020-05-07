package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="sys_version")
public class SysVersion extends BaseEntity<Integer>{
	
	private String name;
	@Column(name="productId")
	private String productId;
	@Column(name="createdate")
	private Date createDate;
	@Column(name="curVersion")
	private String curVersion;
	@Column(name="isUsing")
	private Integer isUsing;
	
	

	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getProductId() {
		return productId;
	}



	public void setProductId(String productId) {
		this.productId = productId;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public String getCurVersion() {
		return curVersion;
	}



	public void setCurVersion(String curVersion) {
		this.curVersion = curVersion;
	}



	public Integer getIsUsing() {
		return isUsing;
	}



	public void setIsUsing(Integer isUsing) {
		this.isUsing = isUsing;
	}



	@Override
	public String fetchCacheEntitName() {
		return null;
	}

}
