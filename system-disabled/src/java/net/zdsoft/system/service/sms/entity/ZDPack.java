package net.zdsoft.system.service.sms.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ZDPack implements Serializable {
    private static final long serialVersionUID = 3735402319237183239L;
    private String msg;
    private String sendTime;
    private String sendUserId;
    private String sendUsername;
    private String sendUnitId;
    private String sendUnitName;
    private String batchId;
    private List<Receiver> receiverList = new ArrayList<Receiver>();
    // 使用同步还是异步方式发送
    private String isSync;

    public String getIsSync() {
        return isSync;
    }

    public void setIsSync(String isSync) {
        this.isSync = isSync;
    }

    public void addReciever(Receiver receiver) {
        receiverList.add(receiver);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSendTime() {
        return sendTime;
    }

    /**
     * 格式要求：yyyyMMddHHmmss
     * 
     * @param sendTime
     */
    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUsername() {
        return sendUsername;
    }

    public void setSendUsername(String sendUsername) {
        this.sendUsername = sendUsername;
    }

    public String getSendUnitId() {
        return sendUnitId;
    }

    public void setSendUnitId(String sendUnitId) {
        this.sendUnitId = sendUnitId;
    }

    public String getSendUnitName() {
        return sendUnitName;
    }

    public void setSendUnitName(String sendUnitName) {
        this.sendUnitName = sendUnitName;
    }

    public List<Receiver> getReceiverList() {
        return receiverList;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

}
