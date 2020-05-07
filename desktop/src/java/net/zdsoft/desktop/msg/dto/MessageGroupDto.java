package net.zdsoft.desktop.msg.dto;

import net.zdsoft.desktop.msg.entity.MessageGroup;

import java.util.List;

/**
 * Created by shenke on 2017/3/15.
 */
public class MessageGroupDto {

    private List<MessageDto> messageDtos;
    private MessageGroup messageGroup;

    public List<MessageDto> getMessageDtos() {
        return messageDtos;
    }

    public void setMessageDtos(List<MessageDto> messageDtos) {
        this.messageDtos = messageDtos;
    }

    public MessageGroup getMessageGroup() {
        return messageGroup;
    }

    public void setMessageGroup(MessageGroup messageGroup) {
        this.messageGroup = messageGroup;
    }
}
