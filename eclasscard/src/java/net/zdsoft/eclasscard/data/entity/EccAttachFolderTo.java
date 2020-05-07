package net.zdsoft.eclasscard.data.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "eclasscard_attach_folder_to")
public class EccAttachFolderTo extends BaseEntity<String> {
	private static final long serialVersionUID = -4167121872859854130L;

	private int type;
	private int range;
	private String folderId;
	private String sendObjectId;

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

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public String getSendObjectId() {
		return sendObjectId;
	}

	public void setSendObjectId(String sendObjectId) {
		this.sendObjectId = sendObjectId;
	}

	@Override
	public String fetchCacheEntitName() {
		return "eclasscardAttachFolderTo";
	}

}
