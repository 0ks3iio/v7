package net.zdsoft.eclasscard.data.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name="eclasscard_face_activate")
public class EccFaceActivate extends BaseEntity<String>{

	private static final long serialVersionUID = 1L;
	
	private String unitId;
	private String infoId;
	private Integer isLower;//1.下发中  0，下发完成
	private Integer currentFaceNum;//客户端响应的人脸数
	private Integer needLower;//是否要下发数据
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLowerTime;//服务端最后下发时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastClientTime;//客户端响应的最后下发时间
	
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

	public Integer getIsLower() {
		return isLower;
	}

	public void setIsLower(Integer isLower) {
		this.isLower = isLower;
	}

	public Date getLastLowerTime() {
		return lastLowerTime;
	}

	public void setLastLowerTime(Date lastLowerTime) {
		this.lastLowerTime = lastLowerTime;
	}

	public Integer getCurrentFaceNum() {
		return currentFaceNum;
	}

	public void setCurrentFaceNum(Integer currentFaceNum) {
		this.currentFaceNum = currentFaceNum;
	}

	public Integer getNeedLower() {
		return needLower;
	}

	public void setNeedLower(Integer needLower) {
		this.needLower = needLower;
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

	public Date getLastClientTime() {
		return lastClientTime;
	}

	public void setLastClientTime(Date lastClientTime) {
		this.lastClientTime = lastClientTime;
	}

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardFaceActivate";
	}

}
