package net.zdsoft.dataimport.biz;

import com.alibaba.fastjson.JSON;

/**
 * @author shenke
 * @since 17-8-6 下午7:43
 */
public class JSONResponse{

    private String msg;
    private boolean success;
    private Object businessValue;
    private int importStateCode;

    public JSONResponse setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public JSONResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public JSONResponse setBusinessValue(Object value) {
        this.businessValue = value;
        return this;
    }

    public JSONResponse setImportStateCode(int importStateCode) {
        this.importStateCode = importStateCode;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getBusinessValue() {
        return businessValue;
    }

    public int getImportStateCode() {
        return importStateCode;
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }
}
