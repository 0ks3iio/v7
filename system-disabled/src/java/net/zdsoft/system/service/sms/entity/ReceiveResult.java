package net.zdsoft.system.service.sms.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class ReceiveResult implements Serializable {
    private static final long serialVersionUID = 9056146429223187744L;
    private String batchCode;
    private String batchDescription;
    private int batchState;
    private Map<String, Integer> stateMap = new HashMap<String, Integer>();

    /**
     * 所有的手机号码
     * 
     * @return
     */
    public String[] getPhones() {
        return stateMap.keySet().toArray(new String[0]);
    }

    public void putState(String phone, int state) {
        stateMap.put(phone, state);
    }

    /**
     * 获取每个手机号码的状态
     * 
     * @param phone
     * @return
     */
    public int getState(String phone) {
        return stateMap.get(phone);
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public String getBatchDescription() {
        return batchDescription;
    }

    public void setBatchDescription(String batchDescription) {
        this.batchDescription = batchDescription;
    }

    public int getBatchState() {
        return batchState;
    }

    public void setBatchState(int batchState) {
        this.batchState = batchState;
    }
}
