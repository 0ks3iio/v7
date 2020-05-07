package net.zdsoft.desktop.msg.dto;

import com.google.common.collect.Lists;
import net.zdsoft.basedata.dto.BaseDto;
import net.zdsoft.desktop.msg.entity.Message;
import net.zdsoft.desktop.msg.enums.MessageType;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shenke on 2017/3/14.
 */
public class MessageDto extends BaseDto{

    private MessageType messageType;
    private String messageTypeName;
    private String sendTime;
    private Message message;
    private List<Message> messages;

    //点击详细时参数
    private String url;
    private String modelId;
    private String modelName;
    private String serverName;
    private Integer subId;

    public MessageDto(MessageType messageType) {
        this.messageType = messageType;
        this.messages = Lists.newArrayList();
        this.messageTypeName = this.messageType.getName();
    }

    public void addMessage(Message message){
        this.messages.add(message);
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void sort(Comparator<Message> messageComparator){
        if(CollectionUtils.isNotEmpty(messages)){
            Collections.sort(messages,messageComparator);
        }
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getMessageTypeName() {
        return messageTypeName;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }
}
