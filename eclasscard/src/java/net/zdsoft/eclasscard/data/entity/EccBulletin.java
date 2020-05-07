package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="eclasscard_bulletin")
public class EccBulletin  extends EccTaskEntity{
	private static final long serialVersionUID = 1L;

	private String userId;
	private String unitId;
	private String title;
	private String content;
	private Integer sendType;
	private Integer type; //类型 1：普通 2：顶栏 3：全屏
	private Integer bulletinLevel; //级别 1：校级 2：班级
	private Integer templetType; //全屏公告类型 1：标准公告 2：喜报 3：欢迎致辞
	private String grounding;
	
	@Transient
	private String userName;
	@Transient
	private boolean canEdit;
	@Transient
	private String pictureUrl;
	@Transient
	private boolean lockScreen;
	@Transient
	private String fullObjAllId;
	
	@Override
	public String fetchCacheEntitName() {
		return "eclasscardBulletin";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public boolean isCanEdit() {
		return canEdit;
	}

	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getBulletinLevel() {
		return bulletinLevel;
	}

	public void setBulletinLevel(Integer bulletinLevel) {
		this.bulletinLevel = bulletinLevel;
	}

	public Integer getTempletType() {
		return templetType;
	}

	public void setTempletType(Integer templetType) {
		this.templetType = templetType;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public boolean isLockScreen() {
		return lockScreen;
	}

	public void setLockScreen(boolean lockScreen) {
		this.lockScreen = lockScreen;
	}

	public String getFullObjAllId() {
		return fullObjAllId;
	}

	public void setFullObjAllId(String fullObjAllId) {
		this.fullObjAllId = fullObjAllId;
	}

	public String getGrounding() {
		return grounding;
	}

	public void setGrounding(String grounding) {
		this.grounding = grounding;
	}
	
}
