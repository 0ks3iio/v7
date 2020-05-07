package net.zdsoft.basedata.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.zdsoft.framework.entity.BaseEntity;
@Entity
@Table(name = "base_import_entity")
public class ImportEntity extends BaseEntity<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4685642146944761959L;
	/**
	 * 离校
	 */
	public static final String IMPORT_TYPE_OUT = "0";
	/**
	 * 调入
	 */
	public static final String IMPORT_TYPE_IN = "1";

	public static final String IMPORT_TYPE_CLASSFLOW = "2";
	/**
	 * 导入等待
	 */
	public static final String IMPORT_STATUS_WAIT ="0";
	/**
	 * 导入开始
	 */
	public static final String IMPORT_STATUS_START ="1";
	/**
	 * 导入成功
	 */
	public static final String IMPORT_STATUS_SUCCESS ="2";
	/**
	 * 导入失败
	 */
	public static final String IMPORT_STATUS_ERROR ="3";
	
	/**
	 * 导入出错
	 */
	public static final String IMPORT_STATUS_WRONG ="4";
	
	@Override
	public String fetchCacheEntitName() {
		return this.getClass().getSimpleName();
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date modifyTime;
	
	private String unitId;
	
	private String status;
	
	private String fileName;
	
	private String filePath;
	
	private String fileType;
	
	private String importType;
	
	private String importUserId;

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

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getImportType() {
		return importType;
	}

	public void setImportType(String importType) {
		this.importType = importType;
	}

	public String getImportUserId() {
		return importUserId;
	}

	public void setImportUserId(String importUserId) {
		this.importUserId = importUserId;
	}
	
	
}
