package net.zdsoft.desktop.msg.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TODO
 * 此实体类 未于数据库做映射（有些字段待议，仅供测试）
 * Created by shenke on 2017/3/14.
 */
public class Message implements Serializable{

    private String id;

    //---显示部分
    private String title;
    private String titleString;
    private String content;
    private String imageUrl;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date sendTime;
    private Date invalidTime;
    @JSONField(name="isRead")
    private boolean read;
    private boolean top;
    private boolean star;

    @JSONField(name = "msgType")
    private Integer messageType; //
    private boolean readToDeleted;

    //功能部分字段
    private Integer modelId;
    private Integer subSystem;

    private Integer sendType;
    private String sendUserId;
    private String sendUnitId;

    private String receiverId;
    private Integer receiverType;
    private Integer receiverUnitType;
   
    
    public String getTitleString() {
		return titleString;
	}

	public void setTitleString(String titleString) {
		this.titleString = titleString;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public boolean isReadToDeleted() {
        return readToDeleted;
    }

    public void setReadToDeleted(boolean readToDeleted) {
        this.readToDeleted = readToDeleted;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public Integer getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(Integer subSystem) {
        this.subSystem = subSystem;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendUnitId() {
        return sendUnitId;
    }

    public void setSendUnitId(String sendUnitId) {
        this.sendUnitId = sendUnitId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(Integer receiverType) {
        this.receiverType = receiverType;
    }

    public Integer getReceiverUnitType() {
        return receiverUnitType;
    }

    public void setReceiverUnitType(Integer receiverUnitType) {
        this.receiverUnitType = receiverUnitType;
    }

    public static Message toMessage(String msg){
        return JSON.parseObject(msg,Message.class);
    }

    public static List<Message> toMessageList(String messages){
        return JSON.parseArray(messages,Message.class);
    }

    public int getSendTimeLong(){
        if(this.sendTime != null)
        return (int)this.sendTime.getTime();
        return Integer.MAX_VALUE;
    }
}
