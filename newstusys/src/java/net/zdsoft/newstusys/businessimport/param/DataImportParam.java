/* 
 * @(#)DataImportViewParam.java    Created on Aug 4, 2010
 * Copyright (c) 2010 ZDSoft Networks, Inc. All rights reserved.
 * $Id$
 */
package net.zdsoft.newstusys.businessimport.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.dubbo.common.utils.Assert;

import net.zdsoft.newstusys.businessimport.core.StudentImportData;
import net.zdsoft.newstusys.businessimport.core.ImportObjectNode;

/**
 * 数据导入参数：与任务相关的，动态字段列表等
 * 
 * @author zhaosf
 * @version $Revision: 1.0 $, $Date: Aug 4, 2010 3:16:59 PM $
 */
public class DataImportParam {
    //----------------------成员------------------------
    private DataImportViewParam viewParam;
    private Map<String, String> customParamMap;// 自定义参数
    private StudentImportData importData;// 导入数据

    private String covered; // 是否覆盖
    private String replyId;// 消息反馈id
    private String objectName; // 对应xml文件中的object节点的name值
    private List<ImportObjectNode> dynamicFields;// 动态字段
    private Set<String> filterFields;// 过滤的中文字段列表
    private Map<String, Map<String,String>> constraintFields;// 限选字段

    public DataImportParam(DataImportViewParam viewParam, 
            List<String[]> customParams) {
        this.viewParam = viewParam;

        // 自定义参数信息
        this.customParamMap = new HashMap<String, String>();
        if (customParams != null) {
			for (String[] tmp : customParams) {
				this.customParamMap.put(tmp[0], tmp[1]);
			} 
		}
    }


    
    /**
     * 抽取paramMap，走任务时将这些参数保存到数据库中
     * @return
     */
    public Map<String, String> extractParamMap() {
        Map<String, String> paramMap = new HashMap<String, String>();

        //viewParam
        viewParam.fillParamMap(paramMap);

        // 登录用户信息
        // 自定义参数信息
        paramMap.putAll(customParamMap);

        // 自身
        if (null == this.covered)
            this.covered = "0";
        paramMap.put("covered", covered);

        return paramMap;
    }
    
    // -------------------customParam参数-----------------------

    /**
     * @return Returns the customParamMap.
     */
    public Map<String, String> getCustomParamMap() {
        return customParamMap;
    }

    // -------------------ViewParam参数-----------------------
    /**
     * @return Returns the viewParam.
     */
    public DataImportViewParam getViewParam() {
        return viewParam;
    }
    
    /**
     * @return Returns the importFile.
     */
    public String getImportFile() {
        return viewParam.getImportFile();
    }

    /**
     * @return Returns the hasSubtitle.
     */
    public boolean isHasSubtitle() {
        return viewParam.isHasSubtitle();
    }

    /**
     * @return Returns the fileType.
     */
    public String getFileType() {
        return viewParam.getFileType();
    }

    /**
     * @return Returns the batchImport.
     */
    public boolean isBatchImport() {
        return viewParam.isBatchImport();
    }

    /**
     * @return Returns the ignoreInvalidCol.
     */
    public boolean isIgnoreInvalidCol() {
        return viewParam.isIgnoreInvalidCol();
    }

    /**
     * @return Returns the onlyUpdate.
     */
    public boolean isOnlyUpdate() {
        return viewParam.isOnlyUpdate();
    }
    
    /**
     * @return Returns the zipExecPath.
     */
    public String getZipExecPath() {
        return viewParam.getZipExecPath();
    }

    /**
     * @return Returns the importFileVersion.
     */
    public String getImportFileVersion() {
        return viewParam.getImportFileVersion();
    }

    /**
     * @return Returns the importFilePwd.
     */
    public String getImportFilePwd() {
        return viewParam.getImportFilePwd();
    }

    /**
     * @return Returns the subtitle.
     */
    public String getSubtitle() {
        return viewParam.getSubtitle();
    }
    
    /**
     * @param subtitle The subtitle to set.
     */
    public void setSubtitle(String subtitle) {
        viewParam.setSubtitle(subtitle);
    }
    

	public boolean isHasTitle() {
		return viewParam.isHasTitle();
	}

    // -------------------importData自身参数-----------------------
    /**
     * @return Returns the importData.
     */
    public StudentImportData getImportData() {
        return importData;
    }


    /**
     * @param importData The importData to set.
     */
    public void setImportData(StudentImportData importData) {
        this.importData = importData;
    }
    
    // -------------------param自身参数-----------------------
    /**
     * @return Returns the replyId.
     */
    public String getReplyId() {
        return replyId;
    }

    /**
     * @param replyId The replyId to set.
     */
    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    /**
     * @return Returns the objectName.
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @param objectName The objectName to set.
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    /**
     * @return Returns the filterFields.
     */
    public Set<String> getFilterFields() {
        return filterFields;
    }

    /**
     * @param filterFields The filterFields to set.
     */
    public void setFilterFields(Set<String> filterFields) {
        this.filterFields = filterFields;
    }

    /**
     * @return Returns the dynamicFields.
     */
    public List<ImportObjectNode> getDynamicFields() {
        return dynamicFields;
    }

    /**
     * @param dynamicFields The dynamicFields to set.
     */
    public void setDynamicFields(List<ImportObjectNode> dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    /**
     * @return Returns the covered.
     */
    public String getCovered() {
        return covered;
    }

    /**
     * @param covered The covered to set.
     */
    public void setCovered(String covered) {
        this.covered = covered;
    }

	public Map<String, Map<String, String>> getConstraintFields() {
		return constraintFields;
	}

	public void setConstraintFields(
			Map<String, Map<String, String>> constraintFields) {
		this.constraintFields = constraintFields;
	}
}
