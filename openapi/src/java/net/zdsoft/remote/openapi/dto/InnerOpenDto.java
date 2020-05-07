package net.zdsoft.remote.openapi.dto;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

import net.zdsoft.remote.openapi.entity.Developer;

public class InnerOpenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean isPass = true;// 是否通过
    private int resultErrorCode;// false时返回的code
    private String errorMsg;// 错误信息
    private Developer developer;
    private JSONObject paramJsonObj;

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean isPass) {
        this.isPass = isPass;
    }

    public int getResultErrorCode() {
        return resultErrorCode;
    }

    public void setResultErrorCode(int resultErrorCode) {
        this.resultErrorCode = resultErrorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public JSONObject getParamJsonObj() {
        return paramJsonObj;
    }

    public void setParamJsonObj(JSONObject paramJsonObj) {
        this.paramJsonObj = paramJsonObj;
    }

}
