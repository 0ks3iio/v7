package net.zdsoft.diathesis.data.dto;

import java.io.Serializable;

/**
 * @Author: panlf
 * @Date: 2019/9/2 17:23
 */
public class DiathesisRecordSetErrorDto implements Serializable {
    private Boolean seccess;
    private String message;
    private String projectId;
    private Integer errorPos;

    public DiathesisRecordSetErrorDto() {
    }

    public DiathesisRecordSetErrorDto(Boolean seccess, String message, String projectId, Integer errorPos) {
        this.seccess = seccess;
        this.message = message;
        this.projectId = projectId;
        this.errorPos = errorPos;
    }

    public Boolean getSeccess() {
        return seccess;
    }

    public void setSeccess(Boolean seccess) {
        this.seccess = seccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public Integer getErrorPos() {
        return errorPos;
    }

    public void setErrorPos(Integer errorPos) {
        this.errorPos = errorPos;
    }
}
