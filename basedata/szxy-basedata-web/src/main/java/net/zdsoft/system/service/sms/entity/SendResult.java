package net.zdsoft.system.service.sms.entity;

import java.io.Serializable;

public class SendResult extends Throwable implements Serializable {
    private static final long serialVersionUID = -2061041730144349117L;
    private String code;
    private String description;
    private String batchId;
    private String extendsCode; // 一些特殊的网关返回的代码，如玄武网关，发送完毕后，会将结果放到此处

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getExtendsCode() {
        return extendsCode;
    }

    public void setExtendsCode(String extendsCode) {
        this.extendsCode = extendsCode;
    }
}
