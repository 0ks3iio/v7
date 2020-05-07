package net.zdsoft.diathesis.data.dto;

import net.zdsoft.framework.entity.Json;

import java.util.List;

/**
 * @Author: panlf
 * @Date: 2019/8/13 10:29
 */
public class DiathesisFieldDto {
    private String fieldId;
    private String fieldName;
    private String isUsing;
    private String value;
    private String fieldCode;

    /**
     *  0:no  1:yes
     */
    private String canModify;
    private String mustUsing;
    private List<Json> failedInfoList;

    public List<Json> getFailedInfoList() {
        return failedInfoList;
    }

    public void setFailedInfoList(List<Json> failedInfoList) {
        this.failedInfoList = failedInfoList;
    }

    public String getMustUsing() {
        return mustUsing;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setMustUsing(String mustUsing) {
        this.mustUsing = mustUsing;
    }

    public String getIsUsing() {
        return isUsing;
    }

    public void setIsUsing(String isUsing) {
        this.isUsing = isUsing;
    }

    public String getCanModify() {
        return canModify;
    }

    public void setCanModify(String canModify) {
        this.canModify = canModify;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
