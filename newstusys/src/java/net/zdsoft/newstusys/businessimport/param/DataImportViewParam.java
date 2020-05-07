/* 
 * @(#)DataImportViewParam.java    Created on Aug 4, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.businessimport.param;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.dubbo.common.utils.Assert;

import net.zdsoft.newstusys.businessimport.common.DataImportConstants;

/**
 * 数据导入业务处理过程中的参数
 * 
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Aug 4, 2010 3:16:59 PM $
 */
public class DataImportViewParam {
    // ----------------------常量------------------------
    private static final String IS_UPDATE = "isUpdate";// 是否只更新
    private static final String HAS_SUBTITLE = "hasSubtitle";// 是否含有副标题
    private static final String HAS_TITLE = "hasTitle";// 是否含有标题
    private static final String FILE_TYPE = "file_type";// 文件类型
    private static final String IGNORE_INVALID_COL = "ignore_invalid_col";// 是否忽略文件中的无效列
    private static final String BATCH_IMPORT = "batch_import";// 分批导入

    // 以下特别用于重庆导入的压缩包解压
    private static final String ZIP_EXEC_PATH = "zip_exec_path";// 解压7zip文件的可执行文件路径
    private static final String IMPORT_FILE_VERSION = "import_file_version";// 导入文件版本
    private static final String IMPORT_FILE_PWD = "import_file_pwd";// 导入文件密码

    // ----------------------字段------------------------
    private String importFile;// xml导入文件，不用保存到数据库参数中
    private String objectName; // 对应xml文件中的object节点的name值

    private boolean hasTitle = true;//标题
    private boolean hasSubtitle;// 副标题
    private boolean hasMoreFileData;// 包含多文件数据，以zip包的形式下载模板，不用保存到数据库参数中
    private String fileType = DataImportConstants.FILE_TYPE_XLS;// 文件类型

    private boolean batchImport = false;// 是否分批导入，分批导入时错误文件以zip包导出
    private boolean ignoreInvalidCol = false;// 是否忽略文件中的无效列
    private boolean onlyUpdate;// 是否只更新，只更新时只须业务主键不能为空，其它字段可能为空

    private String zipExecPath;// 解压7zip文件的可执行文件路径
    private String importFileVersion;// 导入文件版本
    private String importFilePwd;// 导入文件密码

    private String subtitle;// 副标题，下载模板和生成错误数据

    public DataImportViewParam(String importFile, String objectName, String importDataServiceName) {
        Assert.notNull(importFile, "importFile must not be null and empty string");
        Assert.notNull(objectName, "objectName must not be null and empty string");

        this.importFile = importFile;
        this.objectName = objectName;
    }

    /**
     * 从数据库参数中抽取param对象
     * 
     * @param job 导入任务
     * @param paramMap
     * @return
     */
    public DataImportViewParam(Map<String, String> paramMap) {
        this.hasSubtitle = Boolean.parseBoolean(paramMap.remove(HAS_SUBTITLE));
        this.hasTitle = Boolean.parseBoolean(paramMap.remove(HAS_TITLE));
        this.fileType = paramMap.remove(FILE_TYPE);
        this.batchImport = Boolean.parseBoolean(paramMap.remove(BATCH_IMPORT));
        this.ignoreInvalidCol = Boolean.parseBoolean(paramMap.remove(IGNORE_INVALID_COL));
        this.onlyUpdate = Boolean.parseBoolean(paramMap.remove(IS_UPDATE));

        this.zipExecPath = paramMap.remove(ZIP_EXEC_PATH);
        this.importFileVersion = paramMap.remove(IMPORT_FILE_VERSION);
        this.importFilePwd = paramMap.remove(IMPORT_FILE_PWD);
    }

    /**
     * 填充paramMap，走任务时将这些参数保存到数据库中
     * 
     * @param paramMap
     */
    public void fillParamMap(Map<String, String> paramMap) {
        Assert.notNull(paramMap, "paramMap must not be null");

        paramMap.put(HAS_SUBTITLE, String.valueOf(hasSubtitle));
        paramMap.put(HAS_TITLE, String.valueOf(hasTitle));
        paramMap.put(FILE_TYPE, fileType);

        paramMap.put(BATCH_IMPORT, String.valueOf(batchImport));
        paramMap.put(IGNORE_INVALID_COL, String.valueOf(ignoreInvalidCol));
        paramMap.put(IS_UPDATE, String.valueOf(onlyUpdate));

        if (StringUtils.isNotBlank(zipExecPath)) {
            paramMap.put(ZIP_EXEC_PATH, zipExecPath);
            if (StringUtils.isNotBlank(importFileVersion))
                paramMap.put(IMPORT_FILE_VERSION, importFileVersion);
            if (StringUtils.isNotBlank(importFilePwd))
                paramMap.put(IMPORT_FILE_PWD, importFilePwd);
        }
    }

    /**
     * @param hasSubtitle The hasSubtitle to set.
     */
    public DataImportViewParam setHasSubtitle(boolean hasSubtitle) {
        this.hasSubtitle = hasSubtitle;
        return this;
    }

    /**
     * @param hasMoreFileData The hasMoreFileData to set.
     */
    public DataImportViewParam setHasMoreFileData(boolean hasMoreFileData) {
        this.hasMoreFileData = hasMoreFileData;
        return this;
    }

    /**
     * @param fileType The fileType to set.
     */
    public DataImportViewParam setFileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    /**
     * @param batchImport The batchImport to set.
     */
    public DataImportViewParam setBatchImport(boolean batchImport) {
        this.batchImport = batchImport;
        return this;
    }

    /**
     * @param ignoreInvalidCol The ignoreInvalidCol to set.
     */
    public DataImportViewParam setIgnoreInvalidCol(boolean ignoreInvalidCol) {
        this.ignoreInvalidCol = ignoreInvalidCol;
        return this;
    }

    /**
     * @param zipExecPath The zipExecPath to set.
     */
    public DataImportViewParam setZipExecPath(String zipExecPath) {
        this.zipExecPath = zipExecPath;
        return this;
    }

    /**
     * @param importFileVersion The importFileVersion to set.
     */
    public DataImportViewParam setImportFileVersion(String importFileVersion) {
        this.importFileVersion = importFileVersion;
        return this;
    }

    /**
     * @param importFilePwd The importFilePwd to set.
     */
    public DataImportViewParam setImportFilePwd(String importFilePwd) {
        this.importFilePwd = importFilePwd;
        return this;
    }

    /**
     * @return Returns the importFile.
     */
    public String getImportFile() {
        return importFile;
    }

    /**
     * @return Returns the hasSubtitle.
     */
    public boolean isHasSubtitle() {
        return hasSubtitle;
    }

    /**
     * @return Returns the hasMoreFileData.
     */
    public boolean isHasMoreFileData() {
        return hasMoreFileData;
    }

    /**
     * @return Returns the fileType.
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @return Returns the batchImport.
     */
    public boolean isBatchImport() {
        return batchImport;
    }

    /**
     * @return Returns the ignoreInvalidCol.
     */
    public boolean isIgnoreInvalidCol() {
        return ignoreInvalidCol;
    }

    /**
     * @return Returns the zipExecPath.
     */
    public String getZipExecPath() {
        return zipExecPath;
    }

    /**
     * @return Returns the importFileVersion.
     */
    public String getImportFileVersion() {
        return importFileVersion;
    }

    /**
     * @return Returns the importFilePwd.
     */
    public String getImportFilePwd() {
        return importFilePwd;
    }

    /**
     * @return Returns the onlyUpdate.
     */
    public boolean isOnlyUpdate() {
        return onlyUpdate;
    }

    /**
     * @param onlyUpdate The onlyUpdate to set.
     */
    public void setOnlyUpdate(boolean onlyUpdate) {
        this.onlyUpdate = onlyUpdate;
    }

    /**
     * @return Returns the objectName.
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @return Returns the subtitle.
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * @param subtitle The subtitle to set.
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

	public boolean isHasTitle() {
		return hasTitle;
	}

	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}

    
}
