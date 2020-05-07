package net.zdsoft.bigdata.data.dto;

import java.io.Serializable;

/**
 * @author:zhujy
 * @since:2019/6/3 13:27
 */
public class LogDto implements Serializable {
    String bizCode;
    String description;
    Object newData;
    String bizName;
    Object oldData;

    public Object getOldData() {
        return oldData;
    }

    public void setOldData(Object oldData) {
        this.oldData = oldData;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getNewData() {
        return newData;
    }

    public void setNewData(Object newData) {
        this.newData = newData;
    }

    public String getBizName() {
        return bizName;
    }

    public void setBizName(String bizName) {
        this.bizName = bizName;
    }
}
