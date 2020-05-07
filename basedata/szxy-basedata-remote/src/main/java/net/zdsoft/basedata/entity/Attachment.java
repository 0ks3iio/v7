package net.zdsoft.basedata.entity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.zdsoft.framework.entity.BaseEntity;

@Entity
@Table(name = "sys_attachment")
public class Attachment extends BaseEntity<String> {
	private static final long serialVersionUID = 3556139084494220378L;
	
	private String objecttype;
	private String filename;
	private long filesize;
	private String contenttype;
	private String description;
	private String objId;
	private String unitId;
	private String dirId;// 存储目录id
	private String filePath;// 文件相对路径
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime; // 创建时间戳
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	private String extName;
	private int status;

	/************* 辅助字段 ************/
	@Transient
	private String dirPath;//真实附件路径由dirPath+filePath组成
	
	public String getObjecttype() {
		return objecttype;
	}

	public void setObjecttype(String objecttype) {
		this.objecttype = objecttype;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public long getFilesize() {
		return filesize;
	}

	public void setFilesize(long filesize) {
		this.filesize = filesize;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getDirId() {
		return dirId;
	}

	public void setDirId(String dirId) {
		this.dirId = dirId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public String getExtName() {
		return extName;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDirPath() {
		return dirPath;
	}

	public void setDirPath(String dirPath) {
		this.dirPath = dirPath;
	}
	/**
	 * 图片附件可以用此方法展示，（如自定义后缀可以传入相应后缀的filePath）
	 * @return
	 */
	public String getShowPicUrl() {
		String showFilePath = "";
		try {
			showFilePath = URLEncoder.encode(filePath,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String showPicUrl = "/common/showpicture?filePath="
				+ showFilePath;
		return showPicUrl;
	} 
	
	@Override
	public String fetchCacheEntitName() {
		return "sysAttachment";
	}

}
