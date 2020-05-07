package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "eclasscard_face_set")
public class EccFaceSet extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String infoId;
	private String setType;
	private String setId;
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;

	@Override
	public String fetchCacheEntitName() {
		return "eccFaceSet";
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getInfoId() {
		return infoId;
	}

	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}

	public String getSetType() {
		return setType;
	}

	public void setSetType(String setType) {
		this.setType = setType;
	}

	public String getSetId() {
		return setId;
	}

	public void setSetId(String setId) {
		this.setId = setId;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}


}
