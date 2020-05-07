package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name="eclasscard_bulletin_to")
public class EccBulletinTo  extends BaseEntity<String>{
	private static final long serialVersionUID = 1L;

	private String bulletinId;
	private String eccInfoId;
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardBulletinTo";
	}

	public String getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(String bulletinId) {
		this.bulletinId = bulletinId;
	}

	public String getEccInfoId() {
		return eccInfoId;
	}

	public void setEccInfoId(String eccInfoId) {
		this.eccInfoId = eccInfoId;
	}


}
