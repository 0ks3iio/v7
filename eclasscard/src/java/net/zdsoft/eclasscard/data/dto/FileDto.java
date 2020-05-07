package net.zdsoft.eclasscard.data.dto;

import net.zdsoft.basedata.enums.StorageDirType;

public class FileDto {
	public static final int TASK_STATUS_NO_HAND = 0;// 等待执行
	public static final int TASK_STATUS_IN_HAND = 1;// 正在执行
	public static final int TASK_STATUS_SUCCESS = 2;// 执行成功 swf状态
	public static final int TASK_STATUS_ERROR = 3;// 执行失败
	public static final int TASK_STATUS_PRE_HAND = 4;// 预处理，和待执行和正在执行区分，以便于消息提示
	public static final int TASK_STATUS_WINDOW_HAND = 5;// 预处理，和待执行和正在执行区分，以便于消息提示
	public static final int TASK_STATUS_NOT_NEED_HAND = 9;// 不需要处理
	
	private String fileName;//附件名
	private String filePath;//临时文件路径
	private String objectType;//附件来源类型，按子系统或模块区分
	private String objectId;//附件
	private String objectUnitId;//单位id
	private int conStatus = TASK_STATUS_NOT_NEED_HAND;//转换状态

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

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectUnitId() {
		return objectUnitId;
	}

	public void setObjectUnitId(String objectUnitId) {
		this.objectUnitId = objectUnitId;
	}

	public int getConStatus() {
		return conStatus;
	}

	public void setConStatus(int conStatus) {
		this.conStatus = conStatus;
	}

	public StorageDirType getStorageDirType() {
		return StorageDirType.ATTACHMENT;
	}
}
